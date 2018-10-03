package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.mongo;

import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.DateRange;
import com.pragmatists.opentrapp.timeregistration.domain.time.DisjointMonths;
import com.pragmatists.opentrapp.timeregistration.domain.time.Month;
import com.pragmatists.opentrapp.timeregistration.domain.time.Period;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class MongoWorkLogEntryRepository implements WorkLogEntryRepository {

    private final MongoTemplate mongo;

    public MongoWorkLogEntryRepository(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public void store(WorkLogEntry entry) throws WorkLogEntryAlreadyExists {

        assertEntryDoesNotExist(entry.id());
        mongo.insert(entry);
    }

    private void assertEntryDoesNotExist(EntryID id) {
        if (find(id) != null) {
            throw new WorkLogEntryAlreadyExists(id);
        }
    }

    @Override
    public WorkLogEntry load(EntryID entryID) throws WorkLogEntryDoesNotExists {
        WorkLogEntry found = find(entryID);
        if (found == null) {
            throw new WorkLogEntryDoesNotExists(entryID);
        }
        return found;
    }

    private WorkLogEntry find(EntryID entryID) {
        return mongo.findOne(withID(entryID), WorkLogEntry.class);
    }

    @Override
    public void delete(EntryID entryID) throws WorkLogEntryDoesNotExists {
        mongo.remove(load(entryID));
    }

    private Query withID(EntryID entryID) {
        return Query.query(Criteria.where("id").is(entryID));
    }

    @Override
    public WorkLog loadAll() {
        return new MongoWorkLog();
    }

    private final class MongoWorkLog implements WorkLog {

        private List<EmployeeID> employeeIDs = new ArrayList<>();

        private List<ProjectName> projectNames = new ArrayList<>();

        private List<DateRange> intervals = new ArrayList<>();

        @Override
        public Iterator<WorkLogEntry> iterator() {
            return mongo.find(buildQuery(), WorkLogEntry.class).iterator();
        }

        private Query buildQuery() {

            Criteria criteria = new Criteria();
            if (!employeeIDs.isEmpty()) {
                criteria.and("employeeID").in(employeeIDs);
            }
            if (!projectNames.isEmpty()) {
                criteria.and("projectNames").in(projectNames);
            }
            criteria.andOperator(getPeriodCriteria());

            return Query.query(criteria);
        }

        private Criteria getPeriodCriteria() {
            if (intervals.isEmpty()) {
                return new Criteria();
            }
            List<Criteria> dateCriteria = new ArrayList<Criteria>();
            for (DateRange interval : intervals) {
                Criteria criteria = Criteria.where("day.date");
                criteria.gte(interval.start.toString());
                criteria.lte(interval.end.toString());
                dateCriteria.add(criteria);
            }
            return new Criteria().orOperator(dateCriteria.toArray(new Criteria[0]));
        }

        @Override
        public WorkLog forProject(ProjectName projectName) {
            this.projectNames = newArrayList(projectName);
            return this;
        }

        @Override
        public WorkLog forProjects(List<ProjectName> projects) {
            this.projectNames = projects;
            return this;
        }

        @Override
        public WorkLog forEmployee(EmployeeID employeeID) {
            this.employeeIDs = newArrayList(employeeID);
            return this;
        }

        @Override
        public WorkLog forEmployees(List<EmployeeID> employeeIDs) {
            this.employeeIDs = employeeIDs;
            return this;

        }

        @Override
        public WorkLog in(Period period) {
            if (period instanceof Month) {
                Month month = (Month) period;
                intervals.add(new DateRange(month.firstDay(), month.lastDay()));
            } else if (period instanceof DisjointMonths) {
                DisjointMonths disjointMonths = (DisjointMonths) period;
                intervals.addAll(getIntervals(disjointMonths));
            } else {
                throw new IllegalArgumentException("Not implemented!");
            }
            return this;
        }

        @Override
        public WorkLog forDateRanges(List<DateRange> dateRanges) {
            this.intervals = dateRanges;
            return this;
        }

        @Override
        public WorkLog byQuery(WorkLogQuery query) {
            return query.applyOn(this);
        }
    }

    private ArrayList<DateRange> getIntervals(DisjointMonths disjointMonths) {
        ArrayList<DateRange> intervals = new ArrayList<>();
        for (Month month : disjointMonths.getMonths()) {
            intervals.add(new DateRange(month.firstDay(), month.lastDay()));
        }
        return intervals;
    }

}

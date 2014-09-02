package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import com.github.mpi.time_registration.domain.*;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.DisjointMonths;
import com.github.mpi.time_registration.domain.time.Month;
import com.github.mpi.time_registration.domain.time.Period;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        private EmployeeID employeeID;

        private ProjectName projectName;

        private List<Interval> intervals = new ArrayList<Interval>();

        @Override
        public Iterator<WorkLogEntry> iterator() {
            return mongo.find(buildQuery(), WorkLogEntry.class).iterator();
        }

        private Query buildQuery() {

            Criteria criteria = new Criteria();
            if (employeeID != null) {
                criteria.and("employeeID").is(employeeID);
            }
            if (projectName != null) {
                criteria.and("projectName").is(projectName);
            }
            criteria.andOperator(getPeriodCriteria());

            return Query.query(criteria);
        }

        private Criteria getPeriodCriteria() {
            if (intervals.isEmpty()) {
                return new Criteria();
            }
            List<Criteria> dateCriteria = new ArrayList<Criteria>();
            for (Interval interval : intervals) {
                Criteria criteria = Criteria.where("day.date");
                criteria.gte(interval.start.toString());
                criteria.lte(interval.end.toString());
                dateCriteria.add(criteria);
            }
            return new Criteria().orOperator(dateCriteria.toArray(new Criteria[0]));
        }

        @Override
        public WorkLog forProject(ProjectName projectName) {
            this.projectName = projectName;
            return this;
        }

        @Override
        public WorkLog forEmployee(EmployeeID employeeID) {
            this.employeeID = employeeID;
            return this;
        }

        @Override
        public WorkLog in(Period period) {
            if (period instanceof Month) {
                Month month = (Month) period;
                intervals.add(new Interval(month.firstDay(), month.lastDay()));
            } else if (period instanceof DisjointMonths) {
                DisjointMonths disjointMonths = (DisjointMonths) period;
                intervals.addAll(getIntervals(disjointMonths));
            } else {
                throw new IllegalArgumentException("Not implemented!");
            }
            return this;
        }
    }

    private ArrayList<Interval> getIntervals(DisjointMonths disjointMonths) {
        ArrayList<Interval> intervals = new ArrayList<Interval>();
        for (Month month : disjointMonths.getMonths()) {
            intervals.add(new Interval(month.firstDay(), month.lastDay()));
        }
        return intervals;
    }

}

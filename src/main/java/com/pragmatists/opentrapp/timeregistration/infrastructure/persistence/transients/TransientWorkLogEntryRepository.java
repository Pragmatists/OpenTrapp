package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.transients;

import static com.google.common.base.Predicates.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.DateRange;
import com.pragmatists.opentrapp.timeregistration.domain.time.Period;

public class TransientWorkLogEntryRepository implements WorkLogEntryRepository {

    private List<WorkLogEntry> store = new ArrayList<WorkLogEntry>();

    @Override
    public WorkLog loadAll() {
        return new TransientWorkLog();
    }

    @Override
    public void store(WorkLogEntry entry) {
        if (find(entry.id()) != null) {
            throw new WorkLogEntryAlreadyExists(entry.id());
        }
        store.add(entry);
    }

    @Override
    public WorkLogEntry load(EntryID entryID) {

        WorkLogEntry entry = find(entryID);
        if (entry != null) {
            return entry;
        }

        throw new WorkLogEntryDoesNotExists(entryID);
    }

    private WorkLogEntry find(EntryID entryID) {

        for (WorkLogEntry entry : store) {
            if (entryID.equals(entry.id())) {
                return entry;
            }
        }

        return null;
    }

    @Override
    public void delete(EntryID entryID) throws WorkLogEntryDoesNotExists {
        store.remove(load(entryID));
    }

    private final class TransientWorkLog implements WorkLog {

        private Predicate<WorkLogEntry> constraints = alwaysTrue();
        private Predicate<WorkLogEntry> employeeConstraint = alwaysTrue();
        private Predicate<WorkLogEntry> projectNameConstraint = alwaysTrue();
        private Predicate<WorkLogEntry> periodConstraint = alwaysTrue();

        private final class ExtractProjectNames implements Function<WorkLogEntry, Iterable<ProjectName>> {

            @Override
            public Iterable<ProjectName> apply(WorkLogEntry x) {
                return x.projectNames();
            }
        }

        private final class ExtractEmployeeID implements Function<WorkLogEntry, EmployeeID> {

            @Override
            public EmployeeID apply(WorkLogEntry x) {
                return x.employee();
            }
        }

        @Override
        public Iterator<WorkLogEntry> iterator() {
            addConstraint(employeeConstraint);
            addConstraint(projectNameConstraint);
            addConstraint(periodConstraint);
            return Iterators.filter(store.iterator(), constraints);
        }

        @Override
        public WorkLog forProject(final ProjectName projectName) {
            projectNameConstraint = compose(new Predicate<Iterable<ProjectName>>() {
                @Override
                public boolean apply(Iterable<ProjectName> input) {
                    return Iterables.contains(input, projectName);
                }
            }, new ExtractProjectNames());
            return this;
        }

        @Override
        public WorkLog forProjects(final List<ProjectName> projects) {
            projectNameConstraint = compose(new Predicate<Iterable<ProjectName>>() {
                @Override
                public boolean apply(Iterable<ProjectName> input) {
                    for (ProjectName projectName : input) {
                        if (projects.contains(projectName)) {
                            return true;
                        }
                    }
                    return false;
                }
            }, new ExtractProjectNames());
            return this;
        }

        @Override
        public WorkLog forEmployee(EmployeeID employeeID) {
            employeeConstraint = compose(equalTo(employeeID), new ExtractEmployeeID());
            return this;
        }

        @Override
        public WorkLog forEmployees(final List<EmployeeID> employees) {
            employeeConstraint = compose(new Predicate<EmployeeID>() {
                @Override
                public boolean apply(EmployeeID input) {
                    return employees.contains(input);
                }
            }, new ExtractEmployeeID());
            return this;
        }

        @Override
        public WorkLog in(final Period period) {
            periodConstraint = new Predicate<WorkLogEntry>() {
                @Override
                public boolean apply(WorkLogEntry entry) {
                    return period.contains(entry.day());
                }

            };
            return this;
        }

        @Override
        public WorkLog forDateRanges(final List<DateRange> dateRanges) {
            periodConstraint = new Predicate<WorkLogEntry>() {
                @Override
                public boolean apply(WorkLogEntry entry) {
                    for (DateRange dateRange : dateRanges) {
                        if (dateRange.contains(entry.day())) {
                            return true;
                        }
                    }
                    return false;
                }

            };
            return this;
        }

        @Override
        public WorkLog byQuery(WorkLogQuery query) {
            return query.applyOn(this);
        }


        private void addConstraint(Predicate<WorkLogEntry> constraint) {
            this.constraints = Predicates.and(constraints, constraint);
        }

    }
}

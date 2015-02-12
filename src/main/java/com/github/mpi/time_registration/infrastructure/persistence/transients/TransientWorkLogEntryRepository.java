package com.github.mpi.time_registration.infrastructure.persistence.transients;

import static com.google.common.base.Predicates.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.mpi.time_registration.domain.EmployeeID;
import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLog;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.time.Period;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

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

        private Predicate<WorkLogEntry> constraints = alwaysTrue();

        @Override
        public Iterator<WorkLogEntry> iterator() {
            return Iterators.filter(store.iterator(), constraints);
        }

        @Override
        public WorkLog forProject(final ProjectName projectName) {
            addConstraint(compose(new Predicate<Iterable<ProjectName>>() {
                @Override
                public boolean apply(Iterable<ProjectName> input) {
                    return Iterables.contains(input, projectName);
                }
            }, new ExtractProjectNames()));
            return this;
        }

        @Override
        public WorkLog forEmployee(EmployeeID employeeID) {
            addConstraint(compose(equalTo(employeeID), new ExtractEmployeeID()));
            return this;
        }

        @Override
        public WorkLog in(final Period period) {
            addConstraint(new Predicate<WorkLogEntry>() {
                @Override
                public boolean apply(WorkLogEntry entry) {
                    return period.contains(entry.day());
                }

            });
            return this;
        }



        private void addConstraint(Predicate<WorkLogEntry> constraint) {
            this.constraints = Predicates.and(constraints, constraint);
        }

    }
}

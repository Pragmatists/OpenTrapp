package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import java.util.Iterator;
import java.util.List;

import com.github.mpi.time_registration.domain.EmployeeID;
import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLog;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.WorkLogQuery;
import com.github.mpi.time_registration.domain.time.DateRange;
import com.github.mpi.time_registration.domain.time.Period;

public class UnitOfWorkAwareRepository implements WorkLogEntryRepository {

    private WorkLogEntryRepository repository;
    private UnitOfWork unitOfWork;

    public UnitOfWorkAwareRepository(WorkLogEntryRepository repository, UnitOfWork unitOfWork) {
        this.repository = repository;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void store(WorkLogEntry entry) throws WorkLogEntryAlreadyExists {
        repository.store(entry);
        unitOfWork.register(entry.id(), entry);
    }

    @Override
    public WorkLogEntry load(EntryID entryID) throws WorkLogEntryDoesNotExists {
        
        if(unitOfWork.contains(entryID)){
            return (WorkLogEntry) unitOfWork.get(entryID);
        }
        
        return registerInUoW(repository.load(entryID));
    }

    private WorkLogEntry registerInUoW(WorkLogEntry entry) {
        unitOfWork.register(entry.id(), entry);
        return entry;
    }

    @Override
    public void delete(EntryID entryID) throws WorkLogEntryDoesNotExists {
        repository.delete(entryID);
    }

    @Override
    public WorkLog loadAll() {
        
        return new UnitOfWorkWorkLog(repository.loadAll());
    }

    public class UnitOfWorkWorkLog implements WorkLog {

        private WorkLog worklog;

        public UnitOfWorkWorkLog(WorkLog worklog) {
            this.worklog = worklog;
        }

        @Override
        public Iterator<WorkLogEntry> iterator() {
            
            final Iterator<WorkLogEntry> iterator = worklog.iterator();
            
            return new Iterator<WorkLogEntry>() {

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public WorkLogEntry next() {
                    return registerInUoW(iterator.next());
                }

                @Override
                public void remove() {
                    iterator.remove();
                }
            };
        }

        @Override
        public WorkLog forProject(ProjectName projectName) {
            return worklog.forProject(projectName);
        }

        @Override
        public WorkLog forProjects(List<ProjectName> projects) {
            return worklog.forProjects(projects);
        }

        @Override
        public WorkLog forEmployee(EmployeeID employeeID) {
            return worklog.forEmployee(employeeID);
        }

        @Override
        public WorkLog forEmployees(List<EmployeeID> employees) {
            return worklog.forEmployees(employees);
        }

        @Override
        public WorkLog in(Period months) {
            return worklog.in(months);
        }

        @Override
        public WorkLog forDateRanges(List<DateRange> dateRanges) {
            return worklog.forDateRanges(dateRanges);
        }

        @Override
        public WorkLog byQuery(WorkLogQuery query) {
            return query.applyOn(this);
        }
    }

}

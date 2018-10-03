package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.mongo;

import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.DateRange;
import com.pragmatists.opentrapp.timeregistration.domain.time.Period;

import java.util.Iterator;
import java.util.List;


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
            return new UnitOfWorkWorkLog(worklog.forProject(projectName));
        }

        @Override
        public WorkLog forProjects(List<ProjectName> projects) {
            return new UnitOfWorkWorkLog(worklog.forProjects(projects));
        }

        @Override
        public WorkLog forEmployee(EmployeeID employeeID) {
            return new UnitOfWorkWorkLog(worklog.forEmployee(employeeID));
        }

        @Override
        public WorkLog forEmployees(List<EmployeeID> employees) {
            return new UnitOfWorkWorkLog(worklog.forEmployees(employees));
        }

        @Override
        public WorkLog in(Period months) {
            return new UnitOfWorkWorkLog(worklog.in(months));
        }

        @Override
        public WorkLog forDateRanges(List<DateRange> dateRanges) {
            return new UnitOfWorkWorkLog(worklog.forDateRanges(dateRanges));
        }

        @Override
        public WorkLog byQuery(WorkLogQuery query) {
            return new UnitOfWorkWorkLog(query.applyOn(worklog));
        }
    }

}

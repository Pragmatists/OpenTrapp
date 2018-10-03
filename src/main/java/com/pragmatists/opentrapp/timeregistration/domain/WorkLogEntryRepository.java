package com.pragmatists.opentrapp.timeregistration.domain;


import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;

public interface WorkLogEntryRepository {

    public void store(WorkLogEntry entry) 
            throws WorkLogEntryAlreadyExists;

    public WorkLogEntry load(EntryID entryID)
            throws WorkLogEntryDoesNotExists;

    public void delete(EntryID entryID)
            throws WorkLogEntryDoesNotExists;

    public WorkLog loadAll();

    public class WorkLogEntryDoesNotExists extends IllegalStateException{
        
        private static final long serialVersionUID = -7258507076537003524L;

        public WorkLogEntryDoesNotExists(EntryID entryID) {
            super(String.format("WorkLogEntry with id='%s' does not exists!", entryID));
        }
    }
    
    public class WorkLogEntryAlreadyExists extends IllegalStateException{

        private static final long serialVersionUID = -7635140701481625542L;
        
        public WorkLogEntryAlreadyExists(EntryID entryID) {
            super(String.format("WorkLogEntry with id='%s' already exists!", entryID));
        }
    }
}

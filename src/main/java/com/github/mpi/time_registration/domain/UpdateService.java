package com.github.mpi.time_registration.domain;

import java.util.Collection;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;

public class UpdateService {

    private final WorkLogEntryRepository repository;

    public UpdateService(WorkLogEntryRepository repository) {
        this.repository = repository;
    }

    public void updateWorkLogEntry(EntryID entryID, Workload workload, Collection<ProjectName> projectNames) {
        
        WorkLogEntry entry = repository.load(entryID);
        
        if(workload != null){
            entry.updateWorkload(workload);
        }
        if(projectNames != null){
            entry.changeProjectsTo(projectNames);
        }
        
    }

}

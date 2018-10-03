package com.pragmatists.opentrapp.timeregistration.domain;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;

import java.util.Collection;


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

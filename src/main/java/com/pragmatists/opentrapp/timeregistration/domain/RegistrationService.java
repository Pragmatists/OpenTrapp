package com.pragmatists.opentrapp.timeregistration.domain;

import java.util.Collection;

public class RegistrationService {

    private final WorkLogEntryFactory factory;
    private final WorkLogEntryRepository repository;

    public RegistrationService(WorkLogEntryFactory factory, WorkLogEntryRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    public void submit(String workload, Collection<String> projectNames, String day) {
        WorkLogEntry newEntry = factory.newEntry(workload, projectNames, day);
        repository.store(newEntry);
    }

}

package com.github.mpi.time_registration.domain;

public class RegistrationService {

    private final WorkLogEntryFactory factory;
    private final WorkLogEntryRepository repository;

    public RegistrationService(WorkLogEntryFactory factory, WorkLogEntryRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    public void submit(String workload, Iterable<String> projectNames, String day) {
        WorkLogEntry newEntry = factory.newEntry(workload, projectNames, day);
        repository.store(newEntry);
    }

}

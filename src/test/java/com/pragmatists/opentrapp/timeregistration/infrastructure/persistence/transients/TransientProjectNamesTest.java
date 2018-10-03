package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.transients;

import com.pragmatists.opentrapp.timeregistration.domain.ProjectNamesContractTest;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
import org.junit.Before;

public class TransientProjectNamesTest extends ProjectNamesContractTest {

    private WorkLogEntryRepository repository = new TransientWorkLogEntryRepository();
    
    @Before
    public void setUp() {

        projectNames = new TransientProjectNames(repository);
    }

    @Override
    protected void followingWorkLogEntriesExist(WorkLogEntry... entries) {
        for (WorkLogEntry entry : entries) {
            repository.store(entry);
        }
    }
    
}

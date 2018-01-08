package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import com.github.mpi.infrastructure.mongo.*;
import com.github.mpi.time_registration.domain.ProjectNamesContractTest;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
        MongoContext.class,
        MongoLabProfile.class,
        MongoDevelopmentProfile.class,
        MongoDevelopmentDatabase.class
})
@ActiveProfiles({"mongo", "mongo-dev"})
public class MongoProjectNamesTest extends ProjectNamesContractTest{

    private WorkLogEntryRepository repository;
    
    @Autowired
    private MongoTemplate mongo;
    
    @Before
    public void setUp() {
        repository = new MongoWorkLogEntryRepository(mongo);
        projectNames = new MongoProjectNames(mongo);
        mongo.dropCollection(WorkLogEntry.class);
        mongo.createCollection(WorkLogEntry.class);
    }

    @Override
    protected void followingWorkLogEntriesExist(WorkLogEntry... entries) {
        for (WorkLogEntry entry : entries) {
            repository.store(entry);
        }
    }
    
}

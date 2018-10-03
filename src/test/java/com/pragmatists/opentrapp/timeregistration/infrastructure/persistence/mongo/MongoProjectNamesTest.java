package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.mongo;

import com.pragmatists.opentrapp.infrastructure.mongo.MongoContext;
import com.pragmatists.opentrapp.infrastructure.mongo.MongoDevelopmentDatabase;
import com.pragmatists.opentrapp.infrastructure.mongo.MongoDevelopmentProfile;
import com.pragmatists.opentrapp.infrastructure.mongo.MongoLabProfile;
import com.pragmatists.opentrapp.timeregistration.domain.ProjectNamesContractTest;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
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
public class MongoProjectNamesTest extends ProjectNamesContractTest {

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

package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.mpi.infrastructure.mongo.MongoContext;
import com.github.mpi.infrastructure.mongo.MongoDevelopmentDatabase;
import com.github.mpi.infrastructure.mongo.MongoDevelopmentProfile;
import com.github.mpi.infrastructure.mongo.MongoLabProfile;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.infrastructure.persistence.transients.UnitOfWorkContractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={
        MongoContext.class, 
        MongoLabProfile.class, 
        MongoDevelopmentProfile.class, 
        MongoDevelopmentDatabase.class
})
@ActiveProfiles({"mongo", "mongo-dev"})
public class MongoWorkLogEntryRepository_UnitOfWork_Test extends UnitOfWorkContractTest{

    @Autowired
    private MongoTemplate mongo;
    
    private UnitOfWork unitOfWork;
    
    @Before
    public void setUp() {

        unitOfWork = new UnitOfWork(mongo);
        
        repository = new UnitOfWorkAwareRepository(new MongoWorkLogEntryRepository(mongo), unitOfWork);
        mongo.dropCollection(WorkLogEntry.class);
    }

    @Override
    protected void commitUnitOfWork() {
        unitOfWork.commit();
    }
}

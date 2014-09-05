package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.github.mpi.time_registration.domain.ProjectNames;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.infrastructure.persistence.PersistenceBoundedContext;

@Component
@Profile("mongo")
public class MongoPersistenceBoundedContext implements PersistenceBoundedContext{

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private UnitOfWork unitOfWork;

    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
    public UnitOfWork unitOfWork(){
        return new UnitOfWork(mongo);
    }
    
    @Bean
    @Scope(value="prototype", proxyMode=ScopedProxyMode.INTERFACES)
    public WorkLogEntryRepository repository(){
        return new UnitOfWorkAwareRepository(new MongoWorkLogEntryRepository(mongo), unitOfWork);
    }

    @Bean
    @Scope(value="prototype", proxyMode=ScopedProxyMode.INTERFACES)
    public ProjectNames projectNames(){
        return new MongoProjectNames(mongo);
    }
    
    @Override
    public void clear() {
        mongo.dropCollection(WorkLogEntry.class);
    }
}

package com.github.mpi.time_registration.infrastructure.persistence.transients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.github.mpi.time_registration.domain.ProjectNames;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.infrastructure.persistence.PersistenceBoundedContext;

@Component
@Profile("transients")
public class TransientPersistenceBoundedContext implements PersistenceBoundedContext {

    private TransientWorkLogEntryRepository repository = new TransientWorkLogEntryRepository();

    @Override
    @Bean
    @Scope(value="prototype", proxyMode=ScopedProxyMode.INTERFACES)
    public WorkLogEntryRepository repository(){
        return repository;
    }
    
    @Bean
    @Scope(value="prototype", proxyMode=ScopedProxyMode.INTERFACES)
    public ProjectNames projectNames(){
        return new TransientProjectNames(repository);
    }
    
    @Override
    public void clear(){
        repository = new TransientWorkLogEntryRepository();
    }
}

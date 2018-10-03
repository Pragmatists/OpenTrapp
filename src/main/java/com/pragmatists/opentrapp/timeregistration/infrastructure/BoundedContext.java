package com.pragmatists.opentrapp.timeregistration.infrastructure;

import com.pragmatists.opentrapp.timeregistration.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BoundedContext {

    @Autowired
    @Bean
    public RegistrationService registrationService(WorkLogEntryRepository repository, WorkLogEntryFactory factory){
        return new RegistrationService(factory, repository);
    }

    @Autowired
    @Bean
    public UpdateService updateService(WorkLogEntryRepository repository){
        return new UpdateService(repository);
    }
    
    @Autowired
    @Bean
    public WorkLogEntryFactory workLogEntryFactory(EntryIDSequence entryIDSequence, EmployeeContext employeeContext){
        return new WorkLogEntryFactory(entryIDSequence, employeeContext);
    }
    
    @Autowired
    @Bean
    public EntryIDSequence entryIdSequence(){

        return new EntryIDSequence() {


            @Override
            public WorkLogEntry.EntryID nextID() {
                return new WorkLogEntry.EntryID(String.format("WL.%s", UUID.randomUUID()));
            }
        };
    }
    
}

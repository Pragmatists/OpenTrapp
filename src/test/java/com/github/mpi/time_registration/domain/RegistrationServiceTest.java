package com.github.mpi.time_registration.domain;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {

    @Mock
    private WorkLogEntryRepository repository;
    @Mock
    private WorkLogEntryFactory factory;

    private RegistrationService service;


    @Before
    public void setUp() {

        service = new RegistrationService(factory, repository);
    }
    
    @Test
    public void shouldRegisterNewEntry() throws Exception {

        // given:
        WorkLogEntry newEntry = newEntryFor("1h", "stuff", "2014/01/01");
        
        // when:
        service.submit("1h", asList("stuff"), "2014/01/01");
        
        // then:
        verify(repository).store(newEntry);
    }

    // --
    
    private WorkLogEntry aWorkLogEntry() {
        return new WorkLogEntry(new EntryID("id"), Workload.of("1h"), asList(new ProjectName("stuff")), new EmployeeID("homer.simpson"),
                Day.of("2014/01/01"));
    }

    private WorkLogEntry newEntryFor(String workload, String projectName, String day) {
        WorkLogEntry newWorkLogEntry = aWorkLogEntry();
        when(factory.newEntry(workload, asList(projectName), day)).thenReturn(newWorkLogEntry);
        return newWorkLogEntry;
    }
}

package com.github.mpi.time_registration.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;

@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceTest {

    @Mock
    private WorkLogEntryRepository repository;
    
    private UpdateService service;

    @Before
    public void setUp() {
        service = new UpdateService(repository);
    }
    
    @Test
    public void shouldUpdateWorkloadOnly() throws Exception {

        // given:
        WorkLogEntry entry = existingEntryFor(new EntryID("entry-id"));
        
        // when:
        service.updateWorkLogEntry(new EntryID("entry-id"), Workload.of("8h"), null);
        
        // then:
        assertThat(entry.workload()).isEqualTo(Workload.of("8h"));
        assertThat(entry.projectName()).isNotNull();
    }

    @Test
    public void shouldChangeProjectOnly() throws Exception {
        
        // given:
        WorkLogEntry entry = existingEntryFor(new EntryID("entry-id"));
        
        // when:
        service.updateWorkLogEntry(new EntryID("entry-id"), null, new ProjectName("NewProject"));
        
        // then:
        assertThat(entry.projectName()).isEqualTo(new ProjectName("NewProject"));
        assertThat(entry.workload()).isNotNull();;
    }
    
    @Test
    public void shouldChangeBothProjectAndWorkloadOnly() throws Exception {
        
        // given:
        WorkLogEntry entry = existingEntryFor(new EntryID("entry-id"));
        
        // when:
        service.updateWorkLogEntry(new EntryID("entry-id"), Workload.of("8h"), new ProjectName("NewProject"));
        
        // then:
        assertThat(entry.workload()).isEqualTo(Workload.of("8h"));
        assertThat(entry.projectName()).isEqualTo(new ProjectName("NewProject"));
    }
    
    // --
    
    private WorkLogEntry existingEntryFor(EntryID entryID) {
        
        WorkLogEntry entry = new WorkLogEntry(entryID, Workload.of("1h"), new ProjectName("SomeProject"), new EmployeeID("homer.simpson"), Day.of("2014/01/01"),
                DateTime.now());
        when(repository.load(entryID)).thenReturn(entry);
        return entry;
    }
}

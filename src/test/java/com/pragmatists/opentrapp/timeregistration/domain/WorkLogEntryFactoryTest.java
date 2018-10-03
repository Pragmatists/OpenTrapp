package com.pragmatists.opentrapp.timeregistration.domain;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WorkLogEntryFactoryTest {
    
    @Mock
    private EntryIDSequence sequence;
    @Mock
    private EmployeeContext employeeContext;

    private WorkLogEntryFactory factory;

    @Before
    public void setUp() {
        factory = new WorkLogEntryFactory(sequence, employeeContext);
    }
    
    @Test
    public void shouldCreateWorkLogEntry() throws Exception {
        
        // given:
        // when:
        WorkLogEntry workLogEntry = factory.newEntry("2h 13m", "ProjectManhattan");
        
        // then:
        assertThat(workLogEntry.projectNames()).containsExactly(new ProjectName("ProjectManhattan"));
        assertThat(workLogEntry.workload()).isEqualTo(Workload.of("2h 13m"));
    }

    @Test
    public void shouldAssignNextIdFromSequence() throws Exception {
        
        // given:
        nextSequenceIDIs("nextId");
        
        // when:
        WorkLogEntry workLogEntry = factory.newEntry("2h 13m", "ProjectManhattan");
        
        // then:
        assertThat(workLogEntry.id()).isEqualTo(new EntryID("nextId"));
    }

    @Test
    public void shouldCreateEntryInContextOfEmployee() throws Exception {

        // given:
        currentEmployeeIs("Bart.Simpson");
        
        // when:
        WorkLogEntry entry = factory.newEntry("1h", "ProjectManhattan");
        
        // then:
        assertThat(entry.employee()).isEqualTo(new EmployeeID("Bart.Simpson"));
    }
    
    @Test
    public void shouldCreateEntryForGivenDay() throws Exception {
        
        // given:
        // when:
        WorkLogEntry entry = factory.newEntry("1h", asList("ProjectManhattan"), "2013/11/05");
        
        // then:
        assertThat(entry.day()).isEqualTo(Day.of("2013/11/05"));
    }
    
    // --
    
    private void currentEmployeeIs(String currentEmployee) {
        when(employeeContext.employeeID()).thenReturn(new EmployeeID(currentEmployee));
    }

    private void nextSequenceIDIs(String nextID) {
        when(sequence.nextID()).thenReturn(new EntryID(nextID));
    }
    
}

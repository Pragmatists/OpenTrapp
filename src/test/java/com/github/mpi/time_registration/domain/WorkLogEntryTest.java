package com.github.mpi.time_registration.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;

public class WorkLogEntryTest {

    @Test
    public void shouldUpdateWorkload() throws Exception {

        // given:
        WorkLogEntry entry = new WorkLogEntry(null, Workload.of("10h"), null, null, null, DateTime.now());
        
        // when:
        entry.updateWorkload(Workload.of("12h"));
        
        // then:
        assertThat(entry.workload()).isEqualTo(Workload.of("12h"));
    }
   
    @Test
    public void shouldChangeProject() throws Exception {
        
        // given:
        WorkLogEntry entry = new WorkLogEntry(null, null, new ProjectName("OldProject"), null, null, DateTime.now());
        
        // when:
        entry.changeProjectTo(new ProjectName("NewProject"));
        
        // then:
        assertThat(entry.projectName()).isEqualTo(new ProjectName("NewProject"));
    }
    
    @Test
    public void shouldNotBeEqualToSomethigDifferentThanWorkLogEntry() throws Exception {
        
        // given:
        WorkLogEntry some = entryOfID("entry-id");
        
        // when:
        boolean areEqual = some.equals("30m on #project");
        
        // then:
        assertThat(areEqual).isFalse();
    }
    
    @Test
    public void shouldBeEqualIfHaveEqualIDs() throws Exception {
        
        // given:
        WorkLogEntry some = entryOfID("some-id");
        WorkLogEntry same = entryOfID("some-id");
        
        // when:
        boolean areEqual = some.equals(same);
        
        // then:
        assertThat(areEqual).isTrue();
    }
    
    @Test
    public void shouldNotBeEqualIfHaveDifferentIDs() throws Exception {
        
        // given:
        WorkLogEntry some = entryOfID("some-id");
        WorkLogEntry other = entryOfID("other-id");
        
        // when:
        boolean areEqual = some.equals(other);
        
        // then:
        assertThat(areEqual).isFalse();
    }
    
    @Test
    public void shouldHaveEqualHashCodesIfHaveEqualIDs() throws Exception {
        
        // given:
        WorkLogEntry some = entryOfID("some-id");
        WorkLogEntry same = entryOfID("some-id");
        
        // when:
        boolean areEqual = some.hashCode() == same.hashCode();
        
        // then:
        assertThat(areEqual).isTrue();
    }

    @Test
    public void shouldNotHaveEqualHashCodesIfHaveDifferentIDs() throws Exception {
        
        // given:
        WorkLogEntry some = entryOfID("some-id");
        WorkLogEntry other = entryOfID("other-id");
        
        // when:
        boolean areEqual = some.hashCode() == other.hashCode();
        
        // then:
        assertThat(areEqual).isFalse();
    }
    
    // --
    
    private ProjectName project(String name) {
        return new ProjectName(name);
    }
    
    private WorkLogEntry entryOfID(String id) {
        return new WorkLogEntry(new EntryID(id), Workload.of("30m"), project("project-A"), new EmployeeID("homer.simpson"), Day.of("2014/01/01"),
                DateTime.now());
    }
}


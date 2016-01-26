package com.github.mpi.time_registration.domain;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;

public class WorkLogEntryTest {

    @Test
    public void shouldUpdateWorkload() throws Exception {

        // given:
        WorkLogEntry entry = anEntryWith(Workload.of("10h"));
        
        // when:
        entry.updateWorkload(Workload.of("12h"));
        
        // then:
        assertThat(entry.workload()).isEqualTo(Workload.of("12h"));
    }

    @Test
    public void shouldChangeProject() throws Exception {
        
        // given:
        WorkLogEntry entry = anEntryWith(new ProjectName("OldProject"));
        
        // when:
        entry.changeProjectsTo(asList(new ProjectName("NewProject")));
        
        // then:
        assertThat(entry.projectNames())
            .containsExactly(new ProjectName("NewProject"));
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
    
    private WorkLogEntry anEntryWith(Workload workload) {
        return new WorkLogEntry(new EntryID("WL.0001"), workload, Collections.<ProjectName>emptySet(), new EmployeeID("homer.simpson"), Day.of("2014/01/01"));
    }

    private WorkLogEntry anEntryWith(ProjectName project) {
        return new WorkLogEntry(new EntryID("WL.0001"), Workload.of("1h"), Arrays.asList(project), new EmployeeID("homer.simpson"), Day.of("2014/01/01"));
    }
       
    private ProjectName project(String name) {
        return new ProjectName(name);
    }
    
    private WorkLogEntry entryOfID(String id) {
        return new WorkLogEntry(new EntryID(id), Workload.of("30m"), asList(project("project-A")), new EmployeeID("homer.simpson"),
                Day.of("2014/01/01"));
    }
}


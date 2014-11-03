package com.github.mpi.time_registration.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;

public abstract class ProjectNamesContractTest {

    protected ProjectNames projectNames;
    
    @Test
    public void shouldBeEmptyIfNoData() throws Exception {

        // given:
        // when:
        // then:
        assertThat(projectNames).isEmpty();
    }

    @Test
    public void shouldListAvailableProjects() throws Exception {

        // given:
        followingWorkLogEntriesExist(worklogEntryFor("ApolloProgram"), worklogEntryFor("ManhattanProject"));
        // when:
        // then:
        assertThat(projectNames).containsOnly(new ProjectName("ApolloProgram"), new ProjectName("ManhattanProject"));
    }

    @Test
    public void shouldListDistinctProjects() throws Exception {
        
        // given:
        followingWorkLogEntriesExist(worklogEntryFor("ApolloProgram"), worklogEntryFor("ApolloProgram"));
        // when:
        // then:
        assertThat(projectNames).containsExactly(new ProjectName("ApolloProgram"));
    }
    
    @Test
    public void shouldListProjectLimitedByNumberOfMonths() throws Exception {
        
        // given:
        followingWorkLogEntriesExist(worklogEntryFor("OldProject", "2013/12/31"), worklogEntryFor("RecentProject", "2014/03/01"));
        // when:
        // then:
        assertThat(projectNames.after(Day.of("2014/01/01"))).containsExactly(new ProjectName("RecentProject"));
    }
    
    protected abstract void followingWorkLogEntriesExist(WorkLogEntry... entries);

    int id = 1;
    
    private WorkLogEntry worklogEntryFor(String name) {
        return worklogEntryFor(name, "2014/01/01");
    }

    private WorkLogEntry worklogEntryFor(String name, String day) {
        return new WorkLogEntry(new EntryID("WL." + id++), Workload.of("1h"), new ProjectName(name), new EmployeeID("homer.simpson"), Day.of(day),
                DateTime.now());
    }
}

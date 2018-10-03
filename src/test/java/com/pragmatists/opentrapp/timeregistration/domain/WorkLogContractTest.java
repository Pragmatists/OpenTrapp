package com.pragmatists.opentrapp.timeregistration.domain;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import com.pragmatists.opentrapp.timeregistration.domain.time.DisjointMonths;
import com.pragmatists.opentrapp.timeregistration.domain.time.Month;
import com.pragmatists.opentrapp.timeregistration.domain.time.Period;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class WorkLogContractTest {

    protected WorkLogEntryRepository repository;

    @Test
    public void shouldReturnEmptyWorkLog() throws Exception {
    
        // given:
        // when:
        WorkLog workLog = repository.loadAll();
        
        // then:
        assertThat(workLog).isEmpty();
    }

    @Test
    public void shouldReturnWorkLogWithMutipleEntries() throws Exception {
        
        WorkLogEntry entry1 = anEntry("1");
        WorkLogEntry entry2 = anEntry("2");
    
        // given:
        repositoryContainsFollowingEntries(entry1, entry2);
    
        // when:
        WorkLog workLog = repository.loadAll();
        
        // then:
        assertThat(workLog).containsExactly(entry1, entry2);
    }

    @Test
    public void shouldReturnWorkLogForGivenProjectOnly() throws Exception {
        
        WorkLogEntry relevantEntry = anEntryWithProject("1", "ManhattanProject");
        WorkLogEntry irrelevantEntry = anEntryWithProject("2", "ApolloProgram");
        
        // given:
        repositoryContainsFollowingEntries(relevantEntry, irrelevantEntry);
        
        // when:
        WorkLog workLog = repository.loadAll().forProject(new ProjectName("ManhattanProject"));
        
        // then:
        assertThat(workLog).containsExactly(relevantEntry);
    }

    @Test
    public void shouldReturnWorkLogForGivenEmployeeOnly() throws Exception {
        
        WorkLogEntry irrelevatntEntry = anEntryForEmployee("1", "Bart");
        WorkLogEntry relevantEntry = anEntryForEmployee("2", "Homer");
        
        // given:
        repositoryContainsFollowingEntries(irrelevatntEntry, relevantEntry);
        
        // when:
        WorkLog workLog = repository.loadAll().forEmployee(new EmployeeID("Homer"));
        
        // then:
        assertThat(workLog).containsExactly(relevantEntry);
    }

    @Test
    public void shouldReturnWorkLogForGivenEmployeeAndProjectOnly() throws Exception {
        
        WorkLogEntry irrelevatntEntry1 = anEntryForEmployeeAndProject("1", "Bart", "X");
        WorkLogEntry irrelevatntEntry2 = anEntryForEmployeeAndProject("2", "Homer", "X");
        WorkLogEntry irrelevatntEntry3 = anEntryForEmployeeAndProject("3", "Bart", "Y");
        WorkLogEntry relevantEntry     = anEntryForEmployeeAndProject("4", "Homer", "Y");

        // given:
        repositoryContainsFollowingEntries(irrelevatntEntry1, irrelevatntEntry2, irrelevatntEntry3, relevantEntry);
        
        // when:
        WorkLog workLog = repository.loadAll()
                .forEmployee(new EmployeeID("Homer"))
                .forProject(new ProjectName("Y"));
        
        // then:
        assertThat(workLog).containsExactly(relevantEntry);
    }



    @Test
    public void shouldRetunWorkLogForGivenMonth() throws Exception{
        WorkLogEntry beforeValentines = anEntryOnDay("Before Valentine's Day", Day.of("2014/01/01"));
        WorkLogEntry valentines = anEntryOnDay("On Valentine's Day", Day.of("2014/02/14"));
        WorkLogEntry afterValentines = anEntryOnDay("After Valentine's Day", Day.of("2014/03/24"));

        // given:
        repositoryContainsFollowingEntries(beforeValentines, valentines, afterValentines);
        // when:
        WorkLog log = repository.loadAll().in(Month.of("2014/02"));
        // then:
        assertThat(log).containsOnly(valentines);
    }
    @Test
    public void shouldRetunWorkLogForGivenMonths() throws Exception{
        WorkLogEntry beforeValentines = anEntryOnDay("Before Valentine's Day", Day.of("2014/01/01"));
        WorkLogEntry valentines = anEntryOnDay("On Valentine's Day", Day.of("2014/02/14"));
        WorkLogEntry christmasDay= anEntryOnDay("On Christmas Day", Day.of("2014/12/25"));
        WorkLogEntry afterValentines = anEntryOnDay("After Valentine's Day", Day.of("2014/03/24"));

        // given:
        repositoryContainsFollowingEntries(beforeValentines, valentines, afterValentines,christmasDay);

        // when:
        WorkLog log = repository.loadAll().in(periodContaining(Month.of("2014/02"),Month.of("2014/12")));
        // then:
        assertThat(log).containsOnly(valentines,christmasDay);
    }

    private Period periodContaining(Month... months) {
        DisjointMonths period = new DisjointMonths();
        for (Month month : months) {
            period.include(month);
        }
        return period;
    }

    // --
    
    private WorkLogEntry anEntryOnDay(String id, Day day) {
        return new WorkLogEntry(new EntryID(id), Workload.of("50m"), asList(new ProjectName("projectA")), new EmployeeID("Lovelas"), day);
        
    }

    private void repositoryContainsFollowingEntries(WorkLogEntry... entries) {
        for (WorkLogEntry entry : entries) {
            repository.store(entry);
        }
    }

    private WorkLogEntry anEntryForEmployee(String id, String employee) {
        return new WorkLogEntry(new EntryID(id), Workload.of("50m"), asList(new ProjectName("projectA")), new EmployeeID(employee),
                Day.of("2014/01/01"));
    }

    private WorkLogEntry anEntryForEmployeeAndProject(String id, String employee, String projectName) {
        return new WorkLogEntry(new EntryID(id), Workload.of("50m"), asList(new ProjectName(projectName)), new EmployeeID(employee),
                Day.of("2014/01/01"));
    }

    private WorkLogEntry anEntryWithProject(String id, String projectName) {
        WorkLogEntry entry = anEntry(id);
        entry.changeProjectsTo(asList(new ProjectName(projectName)));
        return entry;
    }

    private WorkLogEntry anEntry(String id) {
        return new WorkLogEntry(new EntryID(id), Workload.of("50m"), asList(new ProjectName("projectA")), new EmployeeID("homer.simpson"),
                Day.of("2014/01/01"));
    }

}

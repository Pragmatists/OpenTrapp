package com.github.mpi.time_registration.domain;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class WorkLogQueryContractTest {

    protected WorkLogEntryRepository repository;

    @Test
    public void shouldReturnEmptyWorkLog() throws Exception {

        // given:
        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery(""));
        
        // then:
        assertThat(workLog).isEmpty();
    }

    @Test
    public void shouldReturnWholeWorkLog() throws Exception {

        WorkLogEntry entry1 = anEntry("1");
        WorkLogEntry entry2 = anEntry("2");

        // given:
        repositoryContainsFollowingEntries(entry1, entry2);

        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery(""));

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
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery("#ManhattanProject"));
        
        // then:
        assertThat(workLog).containsExactly(relevantEntry);
    }

    @Test
    public void shouldReturnWorkLogForGivenProjects() throws Exception {

        WorkLogEntry relevantEntry1 = anEntryWithProject("1", "ManhattanProject", "FailedProject");
        WorkLogEntry relevantEntry2 = anEntryWithProject("3", "PrismProgram", "FailedProject");
        WorkLogEntry irrelevantEntry1 = anEntryWithProject("4", "MysteriesProgram");
        WorkLogEntry irrelevantEntry2 = anEntryWithProject("2", "ApolloProgram");

        // given:
        repositoryContainsFollowingEntries(relevantEntry1, relevantEntry2, irrelevantEntry1, irrelevantEntry2);

        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery("#ManhattanProject #FailedProject"));

        // then:
        assertThat(workLog).containsExactly(relevantEntry1, relevantEntry2);
    }

    @Test
    public void shouldReturnWorkLogForGivenEmployeeOnly() throws Exception {
        
        WorkLogEntry irrelevantEntry = anEntryForEmployee("1", "Bart");
        WorkLogEntry relevantEntry = anEntryForEmployee("2", "Homer");
        
        // given:
        repositoryContainsFollowingEntries(irrelevantEntry, relevantEntry);
        
        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery("*Homer"));
        
        // then:
        assertThat(workLog).containsExactly(relevantEntry);
    }

    @Test
    public void shouldReturnWorkLogForGivenEmployees() throws Exception {

        WorkLogEntry irrelevantEntry = anEntryForEmployee("1", "Bart");
        WorkLogEntry relevantEntry1 = anEntryForEmployee("2", "Homer");
        WorkLogEntry relevantEntry2 = anEntryForEmployee("3", "Marge");

        // given:
        repositoryContainsFollowingEntries(relevantEntry1, relevantEntry2, irrelevantEntry);

        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery("*Homer *Marge"));

        // then:
        assertThat(workLog).containsExactly(relevantEntry1, relevantEntry2);
    }

    @Test
    public void shouldReturnWorkLogForGivenEmployeeAndProjectOnly() throws Exception {
        
        WorkLogEntry irrelevantEntry1 = anEntryForEmployeeAndProject("1", "Bart", "X");
        WorkLogEntry irrelevantEntry2 = anEntryForEmployeeAndProject("2", "Homer", "X");
        WorkLogEntry irrelevantEntry3 = anEntryForEmployeeAndProject("3", "Bart", "Y");
        WorkLogEntry relevantEntry = anEntryForEmployeeAndProject("4", "Homer", "Y");

        // given:
        repositoryContainsFollowingEntries(irrelevantEntry1, irrelevantEntry2, irrelevantEntry3, relevantEntry);
        
        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery("*Homer #Y"));

        // then:
        assertThat(workLog).containsExactly(relevantEntry);
    }

    @Test
    public void shouldReturnWorkLogForGivenMonth() throws Exception {
        WorkLogEntry beforeValentines = anEntryOnDay("Before Valentine's Day", Day.of("2014/01/01"));
        WorkLogEntry valentines = anEntryOnDay("On Valentine's Day", Day.of("2014/02/14"));
        WorkLogEntry afterValentines = anEntryOnDay("After Valentine's Day", Day.of("2014/03/24"));

        // given:
        repositoryContainsFollowingEntries(beforeValentines, valentines, afterValentines);
        // when:
        WorkLog log = repository.loadAll().byQuery(new WorkLogQuery("@2014/02"));
        // then:
        assertThat(log).containsOnly(valentines);
    }

    @Test
    public void shouldReturnWorkLogForGivenDay() throws Exception {
        WorkLogEntry beforeValentines = anEntryOnDay("Before Valentine's Day", Day.of("2014/01/01"));
        WorkLogEntry valentines = anEntryOnDay("On Valentine's Day", Day.of("2014/02/14"));
        WorkLogEntry afterValentines = anEntryOnDay("After Valentine's Day", Day.of("2014/02/24"));

        // given:
        repositoryContainsFollowingEntries(beforeValentines, valentines, afterValentines);
        // when:
        WorkLog log = repository.loadAll().byQuery(new WorkLogQuery("@2014/02/14"));
        // then:
        assertThat(log).containsOnly(valentines);
    }

    @Test
    public void shouldReturnWorkLogForGivenEmployeeAndProjectsAndMonth() throws Exception {
        WorkLogEntry relevantEntry1 = anEntryForEmployeeAndProjectAndDay("1", "Homer", "X", Day.of("2014/02/12"));
        WorkLogEntry relevantEntry2 = anEntryForEmployeeAndProjectAndDay("2", "Homer", "Y", Day.of("2014/02/02"));
        WorkLogEntry irrelevantEntry1 = anEntryForEmployeeAndProjectAndDay("3", "Bart", "X", Day.of("2014/01/12"));
        WorkLogEntry irrelevantEntry2 = anEntryForEmployeeAndProjectAndDay("4", "Homer", "Y", Day.of("2014/03/23"));

        // given:
        repositoryContainsFollowingEntries(relevantEntry1, relevantEntry2, irrelevantEntry1, irrelevantEntry2);

        // when:
        WorkLog workLog = repository.loadAll().byQuery(new WorkLogQuery("*Homer #Y @2014/02 #X"));

        // then:
        assertThat(workLog).containsExactly(relevantEntry1, relevantEntry2);
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

    private WorkLogEntry anEntryWithProject(String id, String... names) {
        WorkLogEntry entry = anEntry(id);
        List<ProjectName> projectNames = newArrayList();
        for (String projectName : names) {
            projectNames.add(new ProjectName(projectName));
        }
        entry.changeProjectsTo(projectNames);
        return entry;
    }

    private WorkLogEntry anEntry(String id) {
        return new WorkLogEntry(new EntryID(id), Workload.of("50m"), asList(new ProjectName("projectA")), new EmployeeID("homer.simpson"),
            Day.of("2014/01/01"));
    }

    private WorkLogEntry anEntryForEmployeeAndProjectAndDay(String id, String employee, String project, Day day) {
        return new WorkLogEntry(new EntryID(id), Workload.of("50m"), asList(new ProjectName(project)), new EmployeeID(employee), day);
    }

}

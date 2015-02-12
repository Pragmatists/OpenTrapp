package com.github.mpi.time_registration.domain;

import static com.googlecode.catchexception.CatchException.*;
import static java.util.Arrays.*;
import static org.apache.commons.lang3.builder.EqualsBuilder.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository.WorkLogEntryAlreadyExists;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository.WorkLogEntryDoesNotExists;
import com.github.mpi.time_registration.domain.time.Day;

public abstract class WorkLogEntryRepositoryContractTest {

    protected WorkLogEntryRepository repository;

    @Test
    public void shouldBeEmptyOnStart() throws Exception {
    
        // given:
        // when:
        WorkLog log = repository.loadAll();
    
        // then:
        assertThat(log).isEmpty();
    }

    @Test
    public void shouldStoreAndLoadEntry() throws Exception {
    
        // given:
        EntryID id = entryID("entry-id");
        WorkLogEntry entry = new WorkLogEntry(id, Workload.of("25m"), asList(new ProjectName("Manhattan")),
                new EmployeeID("homer.simpson"), Day.of("2014/03/14"));
    
        // when:
        repository.store(entry);
    
        // then:
        assertThat(reflectionEquals(entry, repository.load(id))).isTrue();
    }

    @Test
    public void shouldLoadEntryByID() throws Exception {
    
        // given:
        entryWithGivenIdAlreadyExists(entryID("other"));
        WorkLogEntry expected = entryWithGivenIdAlreadyExists(entryID("expected"));
    
        // when:
        WorkLogEntry actual = repository.load(expected.id());
        
        // then:
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldFailMeaningfullyIfEntryNotFound() throws Exception {
    
        // given:
        // when:
        catchException(repository).load(entryID("404"));
    
        // then:
        assertThat(caughtException())
            .isInstanceOf(WorkLogEntryDoesNotExists.class)
            .hasMessage("WorkLogEntry with id='404' does not exists!");
    }

    @Test
    public void shouldFailMeaningfullyIfTryingToStoreEntryThatAlreadyExists() throws Exception {
    
        // given:
        entryWithGivenIdAlreadyExists(entryID("already-taken"));
    
        // when:
        WorkLogEntry entry = newEntryWithId(entryID("already-taken"));
        catchException(repository).store(entry);
    
        // then:
        assertThat(caughtException())
            .isInstanceOf(WorkLogEntryAlreadyExists.class)
            .hasMessage("WorkLogEntry with id='already-taken' already exists!");
    }
    
    @Test
    public void shouldDeleteEntry() throws Exception {

        // given:
        EntryID entryID = entryID("to-delete");
        WorkLogEntry entry = entryWithGivenIdAlreadyExists(entryID);
        
        // when:
        repository.delete(entryID);
        
        // then:
        assertThat(repository.loadAll().iterator()).doesNotContain(entry);
    }

    @Test
    public void shouldFailMeaningfullyIfDeletingEntryNotFound() throws Exception {

        // given:
        // when:
        catchException(repository).delete(entryID("404"));

        // then:
        assertThat(caughtException())
                .isInstanceOf(WorkLogEntryDoesNotExists.class)
                .hasMessage("WorkLogEntry with id='404' does not exists!");
    }

    // --
    
    private WorkLogEntry entryWithGivenIdAlreadyExists(EntryID id) {
        WorkLogEntry entry = newEntryWithId(id);
        repository.store(entry);
        return entry;
    }

    private WorkLogEntry newEntryWithId(EntryID id) {
        return new WorkLogEntry(id, Workload.of("66h"), asList(new ProjectName("doesn't matter")), new EmployeeID("homer.simpson"), null);
    }

    private EntryID entryID(String id) {
        return new EntryID(id);
    }

}
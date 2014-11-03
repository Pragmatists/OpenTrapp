package com.github.mpi.time_registration.infrastructure.persistence.transients;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

import com.github.mpi.time_registration.domain.EmployeeID;
import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.Workload;
import com.github.mpi.time_registration.domain.time.Day;

public abstract class UnitOfWorkContractTest {

    protected WorkLogEntryRepository repository;
    
    @Test
    public void shouldSupportIdentityMap() throws Exception {
    
        // given:
        repositoryContainsFollowingEntires(aWorkLogEntry(new EntryID("WL.001")));
        
        // when:
        WorkLogEntry entry = repository.load(new EntryID("WL.001"));
        WorkLogEntry sameEntry = repository.load(new EntryID("WL.001"));
        
        // then:
        assertThat(entry == sameEntry).isTrue();
        assertThat(entry).isSameAs(sameEntry);
    }

    @Test
    public void shouldSaveChangedWorklogWithoutExplicitStore_store() throws Exception {

        // given:
        WorkLogEntry entry = aWorkLogEntry(new EntryID("WL.002"));
        repository.store(entry);
        
        // when:
        entry.changeProjectTo(new ProjectName("nfon"));
        commitUnitOfWork();
        
        // then:
        assertThat(reload(new EntryID("WL.002")).projectName()).isEqualTo(new ProjectName("nfon"));
    }

    @Test
    public void shouldNotSaveChangedWorklogAfterUnitOfWorkHasBeenCommited() throws Exception {
        
        // given:
        WorkLogEntry entry = aWorkLogEntry(new EntryID("WL.002"));
        repository.store(entry);
        
        // when:
        commitUnitOfWork();
        entry.changeProjectTo(new ProjectName("nfon"));
        
        // then:
        assertThat(reload(new EntryID("WL.002")).projectName()).isNotEqualTo(new ProjectName("nfon"));
    }
    
    @Test
    public void shouldSaveChangedWorklogWithoutExplicitStore_load() throws Exception {
        
        // given:
        repositoryContainsFollowingEntires(aWorkLogEntry(new EntryID("WL.002")));
        
        // when:
        WorkLogEntry entry = reload(new EntryID("WL.002"));
        entry.changeProjectTo(new ProjectName("nfon"));
        commitUnitOfWork();

        // then:
        assertThat(reload(new EntryID("WL.002")).projectName()).isEqualTo(new ProjectName("nfon"));
    }
    
    @Test
    public void shouldSaveChangedWorklogWithoutExplicitStore_loadAll() throws Exception {
        
        // given:
        repositoryContainsFollowingEntires(aWorkLogEntry(new EntryID("WL.002")));
        
        // when:
        for(WorkLogEntry entry: repository.loadAll()){
            entry.changeProjectTo(new ProjectName("nfon"));
        }
        commitUnitOfWork();
        
        // then:
        assertThat(reload(new EntryID("WL.002")).projectName()).isEqualTo(new ProjectName("nfon"));
    }
    
    // --
    
    protected abstract void commitUnitOfWork();

    private void repositoryContainsFollowingEntires(WorkLogEntry entry) {
        repository.store(entry);
        commitUnitOfWork();
    }

    private WorkLogEntry aWorkLogEntry(EntryID entryID) {
        return new WorkLogEntry(entryID, Workload.of("10m"), new ProjectName("project"), new EmployeeID("homer.simpson"), Day.of("2014/01/01"),
                DateTime.now());
    }

    private WorkLogEntry reload(EntryID entryID) {
        return repository.load(entryID);
    }
}

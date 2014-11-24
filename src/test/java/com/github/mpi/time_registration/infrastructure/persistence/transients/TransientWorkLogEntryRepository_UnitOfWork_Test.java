package com.github.mpi.time_registration.infrastructure.persistence.transients;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TransientWorkLogEntryRepository_UnitOfWork_Test extends UnitOfWorkContractTest {

    @Before
    public void setUp() {

        repository = new TransientWorkLogEntryRepository();
    }

    @Test
    @Ignore
    @Override
    public void shouldNotSaveChangedWorklogAfterUnitOfWorkHasBeenCommited() throws Exception {
    }
    
    // -- 

    @Override
    protected void commitUnitOfWork() {
        // TransientRepository supports UnitOfWork by default
    }
    
}

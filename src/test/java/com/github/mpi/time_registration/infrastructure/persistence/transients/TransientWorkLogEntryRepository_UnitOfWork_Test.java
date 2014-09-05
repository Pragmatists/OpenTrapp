package com.github.mpi.time_registration.infrastructure.persistence.transients;

import org.junit.Before;

public class TransientWorkLogEntryRepository_UnitOfWork_Test extends UnitOfWorkContractTest {

    @Before
    public void setUp() {

        repository = new TransientWorkLogEntryRepository();
    }

    // -- 

    @Override
    protected void commitUnitOfWork() {
        // TransientRepository supports UnitOfWork by default
    }
    
}

package com.github.mpi.time_registration.infrastructure.persistence.transients;

import com.github.mpi.time_registration.domain.WorkLogQueryContractTest;
import org.junit.Before;

public class TransientWorkLogQueryTest extends WorkLogQueryContractTest {

    @Before
    public void setUp() {
        repository = new TransientWorkLogEntryRepository();
    }

}

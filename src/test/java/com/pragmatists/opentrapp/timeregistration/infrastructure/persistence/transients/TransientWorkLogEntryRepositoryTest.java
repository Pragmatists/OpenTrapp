package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.transients;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepositoryContractTest;
import org.junit.Before;

public class TransientWorkLogEntryRepositoryTest extends WorkLogEntryRepositoryContractTest {

    @Before
    public void setUp() {
        repository = new TransientWorkLogEntryRepository();
    }

}

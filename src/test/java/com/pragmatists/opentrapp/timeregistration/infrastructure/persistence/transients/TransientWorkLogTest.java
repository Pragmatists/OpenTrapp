package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.transients;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogContractTest;
import org.junit.Before;


public class TransientWorkLogTest extends WorkLogContractTest {

    @Before
    public void setUp() {
        repository = new TransientWorkLogEntryRepository();
    }
    
}

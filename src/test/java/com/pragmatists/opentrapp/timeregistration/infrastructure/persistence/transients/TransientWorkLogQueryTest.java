package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.transients;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogQueryContractTest;
import org.junit.Before;

public class TransientWorkLogQueryTest extends WorkLogQueryContractTest {

    @Before
    public void setUp() {
        repository = new TransientWorkLogEntryRepository();
    }

}

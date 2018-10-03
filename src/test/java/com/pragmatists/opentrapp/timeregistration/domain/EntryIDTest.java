package com.pragmatists.opentrapp.timeregistration.domain;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryIDTest extends ValueObjectContractTest{

    @Test
    public void shouldHaveDescriptiveToString() throws Exception {

        // given:
        // when:
        EntryID entryID = new EntryID("entry-id");
        // then:
        assertThat(entryID.toString()).isEqualTo("entry-id");
    }
    
    @Override
    protected EntryID aValue() {
        return new EntryID("E.0001");
    }

    @Override
    protected EntryID equalValue() {
        return new EntryID("E.0001");
    }

    @Override
    protected EntryID differentValue() {
        return new EntryID("E.9999");
    }
    
}

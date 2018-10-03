package com.pragmatists.opentrapp.timeregistration.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class EmployeeIDTest extends ValueObjectContractTest{

    @Override
    protected EmployeeID aValue() {
        return new EmployeeID("homer");
    }

    @Override
    protected EmployeeID equalValue() {
        return new EmployeeID("homer");
    }

    @Override
    protected EmployeeID differentValue() {
        return new EmployeeID("bart");
    }

    @Test
    public void shouldHaveDescriptiveToString() throws Exception {

        // given:
        // when:
        EmployeeID id = new EmployeeID("homer");
        // then:
        assertThat(id.toString()).contains("homer");
    }
    
}

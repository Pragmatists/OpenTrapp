package com.pragmatists.opentrapp.timeregistration.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public abstract class ValueObjectContractTest {

    protected abstract Object aValue();
    protected abstract Object equalValue();
    protected abstract Object differentValue();
    
    @Test
    public void shouldSameValuesBeEqual() throws Exception {
    
        // given:
        Object some = aValue();
        Object same = equalValue();
        
        // when:
        boolean areEqual = some.equals(same);
        
        // then:
        assertThat(areEqual).isTrue();
    }

    @Test
    public void shouldDifferentValuesNotBeEqual() throws Exception {
        
        // given:
        Object some = aValue();
        Object other = differentValue();
        
        // when:
        boolean areEqual = some.equals(other);
        
        // then:
        assertThat(areEqual).isFalse();
    }

    @Test
    public void shouldNotBeEqualToObjectOfSomeOtherClass() throws Exception {
        
        // given:
        Object some = aValue();
        
        // when:
        boolean areEqual = some.equals("differnt class");
        
        // then:
        assertThat(areEqual).isFalse();
    }

    @Test
    public void shouldSameValuesHaveSameHashCodes() throws Exception {
        
        // given:
        Object some = aValue();
        Object same = aValue();
        
        // when:
        boolean areEqual = some.hashCode() == same.hashCode();
        
        // then:
        assertThat(areEqual).isTrue();
    }

    @Test
    public void shouldDifferentValuesHaveDifferentHashCodes() throws Exception {
        
        // given:
        Object some = aValue();
        Object other = differentValue();
        
        // when:
        boolean areEqual = some.hashCode() == other.hashCode();
        
        // then:
        assertThat(areEqual).isFalse();
    }

}
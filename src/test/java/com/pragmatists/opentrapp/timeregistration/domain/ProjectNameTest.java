package com.pragmatists.opentrapp.timeregistration.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectNameTest extends ValueObjectContractTest {

    protected ProjectName aValue() {
        return new ProjectName("project");
    }

    protected ProjectName equalValue() {
        return new ProjectName("project");
    }
    
    protected ProjectName differentValue() {
        return new ProjectName("differentProject");
    }
    
    @Test
    public void shouldHaveDescriptiveToString() throws Exception {

        // given:
        ProjectName some = new ProjectName("projectManhattan");
        // when:
        String representation = some.toString();
        // then:
        assertThat(representation).isEqualTo("projectManhattan");
    }
}

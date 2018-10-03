package com.pragmatists.opentrapp.timeregistration.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class WorkloadTest {

    @Test
    public void shouldParseMinutes() throws Exception {

        // given:
        
        // when:
        Workload workload = Workload.of("30m");
        
        // then:
        assertThat(workload).isEqualTo(new Workload(30));
    }

    @Test
    public void shouldParseHours() throws Exception {

        // given:
        
        // when:
        Workload workload = Workload.of("1h");
        
        // then:
        assertThat(workload).isEqualTo(Workload.of("60m"));
    }

    @Test
    public void shouldParseDays() throws Exception {
        
        // given:
        
        // when:
        Workload workload = Workload.of("1d");
        
        // then:
        assertThat(workload).isEqualTo(Workload.of("8h"));
    }
    
    @Test
    public void shouldFailMeaninggullyIfInvalidExpression() throws Exception {
        
        // given:
        
        try{
            
            // when:
            Workload.of("invalid");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
            
        } catch(Exception e){

            // then:
            assertThat(e)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid workload expression 'invalid'. Valid pattern is 'Xd Xh Xm'");
        }
        
    }
    
    @Test
    public void shouldDisplayWorkloadConviniently() throws Exception {

        // given:
        // when:
        // then:
        assertThat(Workload.of("25m").toString()).isEqualTo("25m");
        assertThat(Workload.of("60m").toString()).isEqualTo("1h");
        assertThat(Workload.of("65m").toString()).isEqualTo("1h 5m");
        assertThat(Workload.of("9h 65m").toString()).isEqualTo("1d 2h 5m");
        assertThat(Workload.of("7h 65m").toString()).isEqualTo("1d 5m");
        assertThat(Workload.of("7h 60m").toString()).isEqualTo("1d");
    }
    
    @Test
    public void shouldSameWorkloadsBeEqual() throws Exception {

        // given:
        Workload some = Workload.of("30m");
        Workload same = Workload.of("30m");
        
        // when:
        boolean areEqual = some.equals(same);
        
        // then:
        assertThat(areEqual).isTrue();
    }

    @Test
    public void shouldDifferentWorkloadsNotBeEqual() throws Exception {
        
        // given:
        Workload some = Workload.of("30m");
        Workload other = Workload.of("29m");
        
        // when:
        boolean areEqual = some.equals(other);
        
        // then:
        assertThat(areEqual).isFalse();
    }

    @Test
    public void shouldNotBeEqualToSomethigThatIsNotWorkload() throws Exception {
        
        // given:
        Workload some = Workload.of("30m");
        
        // when:
        boolean areEqual = some.equals("30m");
        
        // then:
        assertThat(areEqual).isFalse();
    }
    
    @Test
    public void shouldSameWorkloadsHaveSameHashCode() throws Exception {
        
        // given:
        Workload some = Workload.of("30m");
        Workload same = Workload.of("30m");
        
        // when:
        boolean areEqual = some.hashCode() == same.hashCode();
        
        // then:
        assertThat(areEqual).isTrue();
    }
    
    @Test
    public void shouldOtherWorkloadsHaveDifferentHashCodes() throws Exception {
        
        // given:
        Workload some = Workload.of("30m");
        Workload other = Workload.of("29m");
        
        // when:
        boolean areEqual = some.hashCode() == other.hashCode();
        
        // then:
        assertThat(areEqual).isFalse();
    }
    
}

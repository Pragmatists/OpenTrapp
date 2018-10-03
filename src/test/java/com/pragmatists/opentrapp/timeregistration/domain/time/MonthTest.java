package com.pragmatists.opentrapp.timeregistration.domain.time;

import com.pragmatists.opentrapp.timeregistration.domain.ValueObjectContractTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class MonthTest extends ValueObjectContractTest {

    @Override
    protected Month aValue() {
        return Month.of("2013/12");
    }

    @Override
    protected Month equalValue() {
        return Month.of("2013/12");
    }

    @Override
    protected Month differentValue() {
        return Month.of("2013/02");
    }

    @Test
    public void shouldHaveDescriptiveRepresentation() throws Exception {

        // given:
        Month month = Month.of("2014/05");
        // when:
        String representation = month.toString();

        // then:
        assertThat(representation).isEqualTo("2014/05");
    }

    @Test
    public void shouldFailMeaningfullyOnInvalidDateFormat() throws Exception {

        // given:
        try {
            // when:
            Month.of("invalidMonth");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            // then:
            assertThat(e).isInstanceOf(IllegalArgumentException.class).hasMessage(
                    "Invalid month format: invalidMonth. Expected format is: yyyy/mm");
        }
    }

    @Test
    public void shouldFailMeaningfullyOnInvalidMonth() throws Exception {

        // given:
        try {
            // when:
            Month.of("2014/13");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            // then:
            assertThat(e).isInstanceOf(IllegalArgumentException.class).hasMessage("Invalid month: 2014/13");
        }
    }
    
    @Test
    public void shouldGetFirstDay() throws Exception {

        // given:
        Month month = Month.of("2014/03");
        
        // when:
        Day firstDay = month.firstDay();
        // then:
        assertThat(firstDay).isEqualTo(Day.of("2014/03/01"));
        
    }

    @Test
    public void shouldGetLastDay() throws Exception {
        
        // given:
        Month month = Month.of("2013/02");
        
        // when:
        Day firstDay = month.lastDay();
        
        // then:
        assertThat(firstDay).isEqualTo(Day.of("2013/02/28"));
    }
    
    @Test
    public void shouldGetLastDayOnALeapYear() throws Exception {
        
        // given:
        Month month = Month.of("2012/02");
        
        // when:
        Day firstDay = month.lastDay();
        
        // then:
        assertThat(firstDay).isEqualTo(Day.of("2012/02/29"));
    }

    @Test
    public void shouldCheckIfContainsDay() {
        Month month = Month.of("1997/03");
        Day day = Day.of("1997/03/13");

        assertThat(month.contains(day)).isTrue();
    }

    @Test
    public void shouldCheckIfDoesNotContainDay() {
        Month month = Month.of("1997/03");
        Day day = Day.of("1997/04/13");

        assertThat(month.contains(day)).isFalse();

    }
}

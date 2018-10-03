package com.pragmatists.opentrapp.timeregistration.domain.time;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DateRangeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCreateRangeForASpecificDay() throws Exception {
        DateRange fromString = DateRange.of("2014/11/12");

        Assertions.assertThat(fromString).isEqualTo(new DateRange(Day.of("2014/11/12"),Day.of("2014/11/12")));
    }

    @Test
    public void shouldCreateRangeForASpecificMonth() throws Exception {
        DateRange fromString = DateRange.of("2014/11");

        Assertions.assertThat(fromString).isEqualTo(new DateRange(Day.of("2014/11/01"),Day.of("2014/11/30")));
    }

    @Test
    public void shouldCreateRangeForASpecificYear() throws Exception {
        DateRange fromString = DateRange.of("2014");

        Assertions.assertThat(fromString).isEqualTo(new DateRange(Day.of("2014/01/01"),Day.of("2014/12/31")));
    }

    @Test
    public void shouldThrowExceptionWHenDateIsNotParsable() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Matchers.equalTo("Couldn't parse date '214'"));

        DateRange.of("214");
    }
}
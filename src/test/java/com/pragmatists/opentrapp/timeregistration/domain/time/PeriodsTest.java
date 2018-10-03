package com.pragmatists.opentrapp.timeregistration.domain.time;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PeriodsTest {

    @Test
    public void parseSingleMonth() {
        String input = "201406";
        Month expected = Month.of("2014/06");

        assertThat(Periods.parse(input)).isEqualTo(expected);
    }

    @Test
    public void parseMultipleMonts() {
        String input = "201406,201407,201209";
        DisjointMonths expected = new DisjointMonths().include(Month.of("2014/06")).include(Month.of("2014/07")).include(Month.of("2012/09"));

        assertThat(Periods.parse(input)).isEqualTo(expected);
    }
}

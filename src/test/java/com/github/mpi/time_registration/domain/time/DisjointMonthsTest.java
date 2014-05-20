package com.github.mpi.time_registration.domain.time;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DisjointMonthsTest {

    public final DisjointMonths period = new DisjointMonths();

    @Test
    public void shouldCheckIfDayIsIncludedInPeriod() {
        period.include(Month.of("2011/01"));

        assertThat( period.contains(Day.of("2011/01/31")) ).isTrue();
    }

    @Test
    public void ahouldCheckIfDayIsNotIncludedInPeriod() {
        period.include(Month.of("2011/05"));

        assertThat( period.contains(Day.of("2011/01/31")) ).isFalse();
    }

    @Test
    public void shouldCheckIdDayIsIncludedInMultipartPeriod() {
        period.include(Month.of("2011/05"));
        period.include(Month.of("2011/02"));

        assertThat( period.contains(Day.of("2011/02/12")) ).isTrue();
    }
}

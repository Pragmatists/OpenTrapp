package com.pragmatists.opentrapp.timeregistration.domain.time;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DateRange implements Period {

    public final Day start;
    public final Day end;

    public static DateRange of(String date) {

        if(Year.isValid(date)){
            Year start = Year.of(date);
            return new DateRange(start.firstDay(), start.lastDay());
        }
        if (Day.isValid(date)) {
            Day start = Day.of(date);
            return new DateRange(start, start);
        }
        if (Month.isValid(date)) {
            Month start = Month.of(date);
            return new DateRange(start.firstDay(), start.lastDay());
        }
        throw new IllegalArgumentException(String.format("Couldn't parse date '%s'", date));
    }

    public DateRange(Day start, Day end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(start).append(end).build();
    }

    @Override
    public boolean equals(Object x) {

        if (!(x instanceof DateRange)) {
            return false;
        }

        DateRange other = (DateRange) x;

        return start.equals(other.start) && end.equals(other.end);
    }

    @Override
    public String toString() {
        return String.format("[%s-%s]", start.toString(), end.toString());
    }

    @Override
    public boolean contains(Day day) {
        return day.after(start) && day.before(end);
    }
}

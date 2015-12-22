package com.github.mpi.time_registration.domain.time;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Year {

    private static Pattern YEAR_FORMAT = Pattern.compile("(\\d{4})");

    private final String value;

    public static Year of(String year) {
        validate(year);

        return new Year(year);
    }

    private static void validate(String year) {
        Matcher matcher = YEAR_FORMAT.matcher(year);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Invalid year format: %s. Expected format is: yyyy", year));
        }

        int yearValue = Integer.parseInt(matcher.group(1));

        if (!DateValidator.isValidYear(yearValue)) {
            throw new IllegalArgumentException(String.format("Invalid year: %s", year));
        }
    }

    private Year(String value) {
        this.value = value;
    }

    public Day firstDay() {
        return Day.of(String.format("%s/01/01", value));
    }

    public Day lastDay() {

        try {

            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            Date date = format.parse(firstDay().toString());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, 1);
            calendar.set(Calendar.MONTH, 0);
            calendar.add(Calendar.DAY_OF_MONTH, -1);

            return Day.of(format.format(calendar.getTime()));

        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return value;
    }

    public static boolean isValid(String date) {
        try {
            validate(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

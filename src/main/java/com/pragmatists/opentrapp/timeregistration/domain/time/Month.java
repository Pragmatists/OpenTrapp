package com.pragmatists.opentrapp.timeregistration.domain.time;

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

public class Month implements Period {

    private static Pattern DATE_FORMAT = Pattern.compile("(\\d{4})/(\\d{2})");

    private final String value;

    public static Month of(String month) {
        validate(month);

        return new Month(month);
    }

    private static void validate(String month) {
        Matcher matcher = DATE_FORMAT.matcher(month);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Invalid month format: %s. Expected format is: yyyy/mm", month));
        }

        int yearValue = Integer.parseInt(matcher.group(1));
        int monthValue = Integer.parseInt(matcher.group(2));

        if (!DateValidator.isValidMonth(yearValue, monthValue)) {
            throw new IllegalArgumentException(String.format("Invalid month: %s", month));
        }
    }

    private Month(String value) {
        this.value = value;
    }

    public Day firstDay() {
        return Day.of(String.format("%s/01", value));
    }

    public Day lastDay() {

        try {

            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            Date date = format.parse(firstDay().toString());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
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

    @Override
    public boolean contains(Day day) {
        return day.in(this);
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

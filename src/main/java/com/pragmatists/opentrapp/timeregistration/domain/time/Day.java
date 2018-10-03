package com.pragmatists.opentrapp.timeregistration.domain.time;

import java.text.ChoiceFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.getAvailableLocales;

public class Day {

    private static final SimpleDateFormat YEAR_MONTH_DAY_FORMATTER = new SimpleDateFormat("yyyy/MM/dd");
    private static Pattern DATE_FORMAT = Pattern.compile("(\\d{4})/(\\d{2})/(\\d{2})");

    private final String date;

    public static Day of(String date) {

        validate(date);

        return new Day(date);
    }

    private static void validate(String date) {
        if(date == null){
            throw new IllegalArgumentException("Invalid date: null");
        }

        Matcher matcher = DATE_FORMAT.matcher(date);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Invalid date format: %s. Expected format is: yyyy/mm/dd", date));
        }

        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day = Integer.parseInt(matcher.group(3));

        if (!DateValidator.isValidDay(year, month, day)) {
            throw new IllegalArgumentException(String.format("Invalid date: %s", date));
        }
    }

    private Day(String date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public boolean equals(Object x) {

        if (!(x instanceof Day)) {
            return false;
        }

        Day other = (Day) x;

        return date.equals(other.date);
    }

    @Override
    public String toString() {
        return date;
    }

    public boolean before(Day day) {
        return date.compareTo(day.date) <= 0;
    }

    public boolean after(Day day) {
        return date.compareTo(day.date) >= 0;
    }

    public boolean in(Month month) {
        return after(month.firstDay()) && before(month.lastDay());
    }

    public static boolean isValid(String date) {
        try {
            validate(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Day next() {
        try {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(YEAR_MONTH_DAY_FORMATTER.parse(date).getTime());
            calendar.add(DAY_OF_MONTH, 1);
            return Day.of(YEAR_MONTH_DAY_FORMATTER.format(calendar.getTime()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

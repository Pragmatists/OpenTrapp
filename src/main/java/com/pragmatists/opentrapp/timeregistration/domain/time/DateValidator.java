package com.pragmatists.opentrapp.timeregistration.domain.time;

import java.util.GregorianCalendar;

class DateValidator {

    public static boolean isValidDay(int year, int month, int day) {
    
        try {
    
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setLenient(false);
            calendar.set(year, month - 1, day);
            calendar.getTime();
    
            return true;
    
        } catch (Exception e) {
    
            return false;
        }
    }

    public static boolean isValidMonth(int yearValue, int monthValue) {
        return isValidDay(yearValue, monthValue, 1);
    }

    public static boolean isValidYear(int yearValue) {
        return isValidMonth(yearValue, 1);
    }
}

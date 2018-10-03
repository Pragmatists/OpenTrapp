package com.pragmatists.opentrapp.timeregistration.domain.time;

public class Periods {

    public static Period parse(String yearMonthList) {
        DisjointMonths months = new DisjointMonths();

        String[] splitted = yearMonthList.split(",");
        if (splitted.length == 1) {
            return Month.of(normalizeMonth(splitted[0]));
        }

        for (String month : splitted) {
            months.include(Month.of(normalizeMonth(month)));
        }
        return months;
    }

    private static String normalizeMonth(String yearMonth) {
        return yearMonth.substring(0, 4) + "/" + yearMonth.substring(4);
    }
}

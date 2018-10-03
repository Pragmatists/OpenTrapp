package com.pragmatists.opentrapp.timeregistration.domain;

import static java.lang.String.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Workload {

    private static Pattern WORKLOAD_EXPRESSION_PATTERN = Pattern.compile(format("%s\\s?%s\\s?%s", unit("d"), unit("h"), unit("m")));
    
    private final int minutes;

    protected Workload(int minutes) {
        this.minutes = minutes;
    }

    public static Workload of(String workloadExpression) {
        return new Workload(toMinutes(workloadExpression));
    }

    private static String unit(String type){
        return format("(?:([0-9]+)%s)?", type);
    }
    
    private static int toMinutes(String workloadExpression) {

        Matcher matcher = WORKLOAD_EXPRESSION_PATTERN.matcher(workloadExpression);
        
        if(!matcher.matches()){
            throw new IllegalArgumentException(String.format("Invalid workload expression '%s'. Valid pattern is 'Xd Xh Xm'", workloadExpression));
        }
        
        int days = parseGroup(matcher.group(1));
        int hours = parseGroup(matcher.group(2));
        int minutes = parseGroup(matcher.group(3));
        
        return days * 8 * 60 + hours * 60 + minutes;
    }

    private static Integer parseGroup(String group) {
        return group != null ? Integer.valueOf(group) : 0;
    }
    
    @Override
    public String toString() {
        
        String representation = "";
        
        int minutes = this.minutes; 
        if(minutes % 60 != 0){
            representation = String.format("%sm", minutes % 60);
            minutes -= minutes % 60;
        }

        int hours = minutes/60; 
        if(hours % 8 != 0){
            representation = String.format("%sh", hours % 8) + (representation.isEmpty() ? "" : " " + representation);
            hours -= hours % 8; 
        }

        int days = hours/8; 
        if(days != 0){
            representation = String.format("%sd", days) + (representation.isEmpty() ? "" : " " + representation);
        }
        
        return representation;
    }

    @Override
    public boolean equals(Object x) {
    
        if (!(x instanceof Workload)) {
            return false;
        }
        
        Workload other = (Workload) x;
        return this.minutes == other.minutes;
    }

    @Override
    public int hashCode() {
        return minutes;
    }
}

package com.pragmatists.opentrapp.timeregistration.application;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Controller
public class CalendarEndpoint {

    @RequestMapping(
            method   = GET, 
            value    = "/endpoints/v1/calendar/{year}/{month}")
    @ResponseStatus(OK)
    public @ResponseBody MonthJson getMonth(@PathVariable int year, @PathVariable int month){

        List<DayJson> days = new ArrayList<DayJson>();

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        while (calendar.get(Calendar.MONTH) == month - 1) {
            
            days.add(new DayJson(format.format(calendar.getTime()), isWeekend(calendar)));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return new MonthJson(year, month, days); 
    }

    private boolean isWeekend(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }
    
    @RequestMapping(
            method   = GET, 
            value    = "/endpoints/v1/calendar/{year}")
    @ResponseStatus(OK)
    public @ResponseBody YearJson getYear(@PathVariable int year){
        
        List<MonthJson> months = new ArrayList<MonthJson>();

        for (int month = 1; month <= 12; month++) {
            months.add(new MonthJson(year, month));
        }
        
        return new YearJson(year, months); 
    }

    private String link(String id) {
        return String.format("/endpoints/v1/calendar/%s", id);
    }

    @JsonAutoDetect(fieldVisibility= Visibility.ANY)
    class YearJson{
        
        String id, link;
        LinkJson next, prev;
        List<MonthJson> months;
        
        YearJson(int year, List<MonthJson> months) {
            this.id = String.valueOf(year);
            this.link = link(id);
            this.months = months;
            this.next = new LinkJson(String.valueOf(year+1));
            this.prev = new LinkJson(String.valueOf(year-1));
        }
    }
    
    @JsonAutoDetect(fieldVisibility= Visibility.ANY)
    @JsonInclude(Include.NON_NULL)
    class MonthJson{
        
        String id, link;
        LinkJson next, prev;
        List<DayJson> days;

        MonthJson(int year, int month){
            this(year, month, null);
        }
        MonthJson(int year, int month, List<DayJson> days) {
            this.id = String.format("%d/%02d", year, month);
            this.link = link(id);
            
            int nextYear = month == 12 ? year + 1 : year;
            int prevYear = month == 1 ? year - 1 : year;
            int nextMonth = month == 12 ? 1 : month + 1;
            int prevMonth = month == 1 ? 12 : month - 1;
            
            this.next = new LinkJson(String.format("%d/%02d", nextYear, nextMonth));
            this.prev = new LinkJson(String.format("%d/%02d", prevYear, prevMonth));
            this.days = days;
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    @JsonInclude(Include.NON_NULL)
    class DayJson{
        
        String link, id;
        boolean holiday;
        
        DayJson(String id, boolean holiday){
            this.id = id;
            this.link = String.format("/endpoints/v1/calendar/%s", id);
            this.holiday = holiday;
        }
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    class LinkJson{
        
        String link;

        LinkJson(String id) {
            this.link = link(id);
        }
    }
    
}

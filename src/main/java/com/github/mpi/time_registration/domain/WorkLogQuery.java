package com.github.mpi.time_registration.domain;

import com.github.mpi.time_registration.domain.time.DateRange;

import java.util.ArrayList;
import java.util.List;

public class WorkLogQuery {

    private final List<EmployeeID> employees = new ArrayList<>();
    private final List<ProjectName> projects = new ArrayList<>();
    private final List<DateRange> dateRanges = new ArrayList<>();

    public WorkLogQuery(String expression) {
        String[] expressionElements = expression.split(" ");
        for (String element : expressionElements) {
            if (element.startsWith("*")) {
                employees.add(new EmployeeID(element.substring(1)));
            } else if (element.startsWith("#")) {
                projects.add(new ProjectName(element.substring(1)));
            } else if (element.startsWith("@")) {
                dateRanges.add(DateRange.of(element.substring(1)));
            }
        }
    }

    public WorkLog applyOn(WorkLog worklog) {
        if (!employees.isEmpty())
            worklog = worklog.forEmployees(employees);
        if(!projects.isEmpty())
            worklog = worklog.forProjects(projects);
        if(!dateRanges.isEmpty()) {
            worklog = worklog.forDateRanges(dateRanges);
        }
        return worklog;
    }
}

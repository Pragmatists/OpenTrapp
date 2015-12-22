package com.github.mpi.time_registration.domain;

import com.github.mpi.time_registration.domain.time.DateRange;
import com.github.mpi.time_registration.domain.time.Period;

import java.util.List;

//TODO: change this to unmutable object
public interface WorkLog extends Iterable<WorkLogEntry> {

    @Deprecated
    WorkLog forProject(ProjectName projectName);
    WorkLog forProjects(List<ProjectName> projects);
    @Deprecated
    WorkLog forEmployee(EmployeeID employeeID);
    WorkLog forEmployees(List<EmployeeID> employees);
    @Deprecated
    WorkLog in(Period months);
    WorkLog forDateRanges(List<DateRange> dateRanges);


    WorkLog byQuery(WorkLogQuery query);

}

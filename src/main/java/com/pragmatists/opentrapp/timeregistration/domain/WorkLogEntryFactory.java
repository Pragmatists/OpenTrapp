package com.pragmatists.opentrapp.timeregistration.domain;

import static java.util.Arrays.*;

import java.util.Collection;

import com.pragmatists.opentrapp.timeregistration.domain.time.Day;

public class WorkLogEntryFactory {

    private final EntryIDSequence sequence;
    private final EmployeeContext employeeContext;

    public WorkLogEntryFactory(EntryIDSequence sequence, EmployeeContext employeeContext) {
        this.sequence = sequence;
        this.employeeContext = employeeContext;
    }

    public WorkLogEntry newEntry(String workload, String projectName) {
        return new WorkLogEntry(sequence.nextID(), Workload.of(workload), asList(new ProjectName(projectName)),
                employeeContext.employeeID(), null);

    }

    public WorkLogEntry newEntry(String workload, Collection<String> projectNames, String day) {
        return new WorkLogEntry(sequence.nextID(), Workload.of(workload), ProjectName.fromNamesAsStrings(projectNames), employeeContext.employeeID(), Day.of(day));
    }

}

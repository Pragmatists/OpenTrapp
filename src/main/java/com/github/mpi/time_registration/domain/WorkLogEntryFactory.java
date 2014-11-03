package com.github.mpi.time_registration.domain;

import org.joda.time.DateTime;

import com.github.mpi.time_registration.domain.time.Day;

public class WorkLogEntryFactory {

    private final EntryIDSequence sequence;
    private final EmployeeContext employeeContext;

    public WorkLogEntryFactory(EntryIDSequence sequence, EmployeeContext employeeContext) {
        this.sequence = sequence;
        this.employeeContext = employeeContext;
    }

    public WorkLogEntry newEntry(String workload, String projectName) {
        return new WorkLogEntry(sequence.nextID(), Workload.of(workload), new ProjectName(projectName), employeeContext.employeeID(), null,
                DateTime.now());
        
    }

    public WorkLogEntry newEntry(String workload, String projectName, String day) {
        return new WorkLogEntry(sequence.nextID(), Workload.of(workload), new ProjectName(projectName), employeeContext.employeeID(),
                Day.of(day), DateTime.now());
    }
    
}

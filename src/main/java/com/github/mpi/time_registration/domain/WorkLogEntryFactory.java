package com.github.mpi.time_registration.domain;

import static java.util.Arrays.*;

import com.github.mpi.time_registration.domain.time.Day;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.convert.Converter;

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

    public WorkLogEntry newEntry(String workload, Iterable<String> projectNames, String day) {
        return new WorkLogEntry(sequence.nextID(), Workload.of(workload), convert(projectNames), employeeContext.employeeID(), Day.of(day));
    }

    private Iterable<ProjectName> convert(Iterable<String> projectNames) {
        return Lambda.convert(projectNames, new Converter<String, ProjectName>() {
            @Override
            public ProjectName convert(String name) {
                return new ProjectName(name);
            }
        });
    }

}

package com.github.mpi.time_registration.infrastructure.persistence.transients;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.ProjectNames;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.time.Day;
import com.google.common.collect.Iterables;

public class TransientProjectNames implements ProjectNames {

    private final WorkLogEntryRepository repository;
    private Day after = Day.of("1900/01/01");

    public TransientProjectNames(WorkLogEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterator<ProjectName> iterator() {

        Set<ProjectName> projectNames = new HashSet<ProjectName>();
        for (WorkLogEntry w : repository.loadAll()) {
            if(w.day().after(after)){
                Iterables.addAll(projectNames, w.projectNames());
            }
        }
        return projectNames.iterator();
    }

    @Override
    public ProjectNames after(Day day) {
        this.after = day;
        return this;
    }

}

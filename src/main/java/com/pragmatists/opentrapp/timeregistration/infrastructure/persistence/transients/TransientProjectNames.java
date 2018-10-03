package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.transients;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.pragmatists.opentrapp.timeregistration.domain.ProjectName;
import com.pragmatists.opentrapp.timeregistration.domain.ProjectNames;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;

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

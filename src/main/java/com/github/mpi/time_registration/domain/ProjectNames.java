package com.github.mpi.time_registration.domain;

import com.github.mpi.time_registration.domain.time.Day;


public interface ProjectNames extends Iterable<ProjectName>{

    public ProjectNames after(Day day);
    
}
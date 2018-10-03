package com.pragmatists.opentrapp.timeregistration.domain;

import com.pragmatists.opentrapp.timeregistration.domain.time.Day;

import java.util.Collection;
import java.util.stream.Collectors;


public interface ProjectNames extends Iterable<ProjectName>{

    public ProjectNames after(Day day);
    
}
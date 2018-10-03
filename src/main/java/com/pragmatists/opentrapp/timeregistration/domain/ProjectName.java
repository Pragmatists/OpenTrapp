package com.pragmatists.opentrapp.timeregistration.domain;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectName {

    private final String name;

    public ProjectName(String name) {
        this.name = name;
    }

    public static Collection<ProjectName> fromNamesAsStrings(Collection<String> projectNames) {
        if (CollectionUtils.isEmpty(projectNames)) {
            return Collections.emptyList();
        }
        return projectNames.stream().map(ProjectName::new).collect(Collectors.toList());
    }

    public static Collection<ProjectName> fromNamesAsStrings(String[] projectNames) {
        return Stream.of(projectNames).map(ProjectName::new).collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object x) {
        
        if(!(x instanceof ProjectName)){
            return false;
        }
        
        ProjectName other = (ProjectName) x;
        return name.equals(other.name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}

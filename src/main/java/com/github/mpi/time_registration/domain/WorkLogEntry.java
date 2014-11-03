package com.github.mpi.time_registration.domain;

import org.joda.time.DateTime;

import com.github.mpi.time_registration.domain.time.Day;

public class WorkLogEntry {

    private final EntryID id;
    private final EmployeeID employeeID;
    private final Day day;
    
    private Workload workload;
    private ProjectName projectName;

    private DateTime createdAt;

    public WorkLogEntry(EntryID id, Workload workload, ProjectName projectName, EmployeeID employeeID, Day day, DateTime createdAt) {
        this.id = id;
        this.workload = workload;
        this.projectName = projectName;
        this.employeeID = employeeID;
        this.day = day;
        this.createdAt = createdAt;
    }

    public EntryID id() {
        return id;
    }

    public Workload workload() {
        return workload;
    }

    public ProjectName projectName() {
        return projectName;
    }

    public EmployeeID employee() {
        return employeeID;
    }

    public Day day() {
        return day;
    }
    
    public void updateWorkload(Workload newWorkload) {
        this.workload = newWorkload;
    }

    public void changeProjectTo(ProjectName newProject) {
        this.projectName = newProject;
    }

    @Override
    public boolean equals(Object x) {

        if (!(x instanceof WorkLogEntry)) {
            return false;
        }

        WorkLogEntry other = (WorkLogEntry) x;

        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public DateTime createdAt() {
        return createdAt;
    }

    public static class EntryID {

        private final String id;

        public EntryID(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object x) {

            if (!(x instanceof EntryID)) {
                return false;
            }

            EntryID other = (EntryID) x;
            return id.equals(other.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public String toString() {
            return String.format("%s", id);
        }
    }

}

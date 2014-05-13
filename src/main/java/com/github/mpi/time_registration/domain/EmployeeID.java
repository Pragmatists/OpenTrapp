package com.github.mpi.time_registration.domain;

public class EmployeeID {

    private final String id;

    public EmployeeID(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object x) {
        
        if(!(x instanceof EmployeeID)){
            return false;
        }
        
        EmployeeID other = (EmployeeID) x;
        
        return id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return id;
    }
}

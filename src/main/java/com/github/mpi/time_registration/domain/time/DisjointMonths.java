package com.github.mpi.time_registration.domain.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DisjointMonths implements Period {
    List<Month> months = new ArrayList<Month>();

    @Override
    public boolean contains(Day day) {
        for (Period period : months) {
            if(period.contains(day)){
                return true;
            }
        }
        return false;
    }

    public DisjointMonths include(Month period){
        months.add(period);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return months.containsAll(((DisjointMonths)o).months);
    }

    @Override
    public int hashCode() {
        return months != null ? months.hashCode() : 0;
    }

    public Collection<Month> getMonths() {
        return months;
    }
}

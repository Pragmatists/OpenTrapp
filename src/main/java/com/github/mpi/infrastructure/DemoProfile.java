package com.github.mpi.infrastructure;

import com.github.mpi.time_registration.domain.*;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.time.Day;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@Profile("demo")
public class DemoProfile {

    @Autowired
    private WorkLogEntryRepository repository;
    
    private Random random = new Random();

    @PostConstruct
    void init(){

        List<String> employees = Arrays.asList("homer.simpson", "bart.simpson", "marge.simpson", "principal.skinner", "mr.burns");
        List<String> workloads = Arrays.asList("8h", "8h", "8h", "4h 15m", "2h 30m", "1d 2h", "45m", "1h", "6h 40m", "7h");
        List<String> projects = Arrays.asList("ProjectManhattan", "ApolloProgram", "Internal", "Other");
        String workloadPattern = "WL.%s";
        String dayPattern =  new SimpleDateFormat("yyyy/MM").format(new Date()) + "/%02d";
        int worklogEntriesCount = 50;
        
        for(int i=0; i< worklogEntriesCount; i++){
            
            EntryID id = new EntryID(String.format(workloadPattern, i));
            Workload workload = Workload.of(random(workloads));
            ProjectName projectName = new ProjectName(random(projects));
            EmployeeID employeeID = new EmployeeID(random(employees));
            Day day = Day.of(String.format(dayPattern, random.nextInt(20) + 1));
            
            repository.store(new WorkLogEntry(id, workload, projectName, employeeID, day, DateTime.now()));
        }

    }

    private String random(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

}

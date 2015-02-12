package concordion.projects;

import static java.util.Arrays.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.mpi.time_registration.domain.EmployeeID;
import com.github.mpi.time_registration.domain.EntryIDSequence;
import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.Workload;
import com.github.mpi.time_registration.domain.time.Day;

import support.ApiFixture;

public class ProjectsFixture extends ApiFixture {

    @Autowired
    private WorkLogEntryRepository repository;

    @Autowired
    private EntryIDSequence sequence;
    
    public void workLogEntry(String projectName, String day){
        repository.store(new WorkLogEntry(sequence.nextID(), Workload.of("1h"), asList(new ProjectName(projectName)),
                new EmployeeID("homer.simpson"), Day.of(day)));
    }
    
}
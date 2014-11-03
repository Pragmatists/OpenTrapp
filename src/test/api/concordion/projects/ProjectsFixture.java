package concordion.projects;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import support.ApiFixture;

import com.github.mpi.time_registration.domain.EmployeeID;
import com.github.mpi.time_registration.domain.EntryIDSequence;
import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.Workload;
import com.github.mpi.time_registration.domain.time.Day;

public class ProjectsFixture extends ApiFixture {

    @Autowired
    private WorkLogEntryRepository repository;

    @Autowired
    private EntryIDSequence sequence;
    
    public void workLogEntry(String projectName, String day){
        repository.store(new WorkLogEntry(sequence.nextID(), Workload.of("1h"), new ProjectName(projectName), new EmployeeID("homer.simpson"), Day.of(day),
                DateTime.now()));
    }
    
}
package concordion.projects;

import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import org.springframework.beans.factory.annotation.Autowired;

import support.ApiFixture;

import static java.util.Arrays.asList;

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
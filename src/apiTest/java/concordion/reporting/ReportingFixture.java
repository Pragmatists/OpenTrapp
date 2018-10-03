package concordion.reporting;

import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import org.springframework.beans.factory.annotation.Autowired;

import support.ApiFixture;

import static java.util.Arrays.asList;

public class ReportingFixture extends ApiFixture {

    @Autowired
    private WorkLogEntryRepository repository;

    public void workLogEntry(String id, String workload, String projectName, String employee, String day) {
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of(workload), asList(new ProjectName(projectName)),
                new EmployeeID(employee), Day.of(day)));
    }

    public void workLogEntry(String id, String workload, String projectName, String employee) {
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of(workload), asList(new ProjectName(projectName)),
                new EmployeeID(employee), Day.of("2014/01/01")));
    }
    
}

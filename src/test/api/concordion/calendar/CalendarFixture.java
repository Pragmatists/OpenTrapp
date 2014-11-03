package concordion.calendar;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import support.ApiFixture;

import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.Workload;

public class CalendarFixture extends ApiFixture {

    @Autowired
    private WorkLogEntryRepository repository;

    public void successfulRequest(){
        workLogEntry("WL.0001", "1h", "SomeName");
        body("{\"projectName\":\"NewName\"}");
        request("POST", "/endpoints/v1/work-log/entries/WL.0001");
    }
    
    public void workLogEntry(String id, String workload, String projectName) {
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of(workload), new ProjectName(projectName), null, null, DateTime.now()));
    }

    public List<Entry> allWorkLogEntries() throws IllegalAccessException {

        List<Entry> entries = new ArrayList<Entry>();

        for (WorkLogEntry entry : repository.loadAll()) {
            String id = entry.id().toString();
            String workload = FieldUtils.readDeclaredField(entry, "workload", true).toString();
            String project = FieldUtils.readDeclaredField(entry, "projectName", true).toString();
            entries.add(new Entry(id, project, workload));
        }

        return entries;
    }

    public class Entry {

        public String id;
        public String projectName;
        public String workload;

        public Entry(String id, String project, String workload) {
            this.id = id;
            this.projectName = project;
            this.workload = workload;
        }
    }

}

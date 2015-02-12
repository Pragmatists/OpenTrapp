package concordion.updating;

import static com.google.inject.internal.Join.*;
import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.Workload;

import support.ApiFixture;

public class UpdatingFixture extends ApiFixture {

    @Autowired
    private WorkLogEntryRepository repository;

    public void successfulRequest(){
        workLogEntry("WL.0001", "1h", "SomeName");
        body("{\"projectNames\":[\"NewName\"]}");
        request("POST", "/endpoints/v1/work-log/entries/WL.0001");
    }
    
    public void workLogEntry(String id, String workload, String projectName) {
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of(workload), asList(new ProjectName(projectName)), null, null));
    }
    
    public void workLogEntry(String id) {
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of("5h"), asList(new ProjectName("some")), null, null));
    }
    
    public boolean entryExists(String id) {
        try {
            repository.load(new EntryID(id));
            return true;
        } catch (WorkLogEntryRepository.WorkLogEntryDoesNotExists e) {
            return false;
        }
    }

    public List<Entry> allWorkLogEntries() throws IllegalAccessException {

        List<Entry> entries = new ArrayList<Entry>();

        for (WorkLogEntry entry : repository.loadAll()) {
            String id = entry.id().toString();
            String workload = entry.workload().toString();
            String project = join(",", entry.projectNames());
            entries.add(new Entry(id, project, workload));
        }

        return entries;
    }

    public class Entry {

        public String id;
        public String projectNames;
        public String workload;

        public Entry(String id, String projects, String workload) {
            this.id = id;
            this.projectNames = projects;
            this.workload = workload;
        }
    }

}

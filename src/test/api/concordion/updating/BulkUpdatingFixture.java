package concordion.updating;

import static com.google.inject.internal.Join.join;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.Workload;

import support.ApiFixture;

public class BulkUpdatingFixture extends ApiFixture {

    @Autowired
    private WorkLogEntryRepository repository;

    public void workLogEntry(String id, String workload, String projectName) {
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of(workload), parseProjectNames(projectName), null, null));
    }

    private List<ProjectName> parseProjectNames(String projectNames) {
        
        List<ProjectName> projects = new ArrayList<>();

        for(String projectName: projectNames.split(", ")){
            projects.add(new ProjectName(projectName));
        }
        
        return projects;
    }
    
    public List<Entry> allWorkLogEntries() throws IllegalAccessException {

        List<Entry> entries = new ArrayList<Entry>();

        for (WorkLogEntry entry : repository.loadAll()) {
            String id = entry.id().toString();
            String workload = entry.workload().toString();
            String project = join(", ", entry.projectNames());
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

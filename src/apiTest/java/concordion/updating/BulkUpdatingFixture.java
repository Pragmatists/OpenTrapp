package concordion.updating;

import com.pragmatists.opentrapp.timeregistration.domain.ProjectName;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
import com.pragmatists.opentrapp.timeregistration.domain.Workload;
import org.springframework.beans.factory.annotation.Autowired;

import support.ApiFixture;
import java.util.ArrayList;
import java.util.List;

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
            String project = String.join(", ", entry.projectNamesAsStrings());
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

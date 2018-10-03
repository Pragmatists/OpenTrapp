package com.pragmatists.opentrapp.timeregistration.application;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import com.pragmatists.opentrapp.timeregistration.domain.time.Month;
import com.pragmatists.opentrapp.timeregistration.domain.time.Periods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(method = GET,
        value = "/endpoints/v1",
        produces = "application/json")
public class ReportingEndpoint {

    @Autowired
    private WorkLogEntryRepository repository;

    @RequestMapping("/projects/{projectName}/work-log/entries")
    public
    @ResponseBody
    WorkLogJson projectWorkLog(@PathVariable String projectName) {

        WorkLog workLog = repository.loadAll().forProject(new ProjectName(projectName));
        return jsonResponse(workLog);
    }

    @RequestMapping("/employee/{employeeID}/work-log/entries")
    public
    @ResponseBody
    WorkLogJson employeeWorkLog(@PathVariable String employeeID) {

        WorkLog workLog = repository.loadAll().forEmployee(new EmployeeID(employeeID));
        return jsonResponse(workLog);
    }

    @RequestMapping("/calendar/{year}/{month}/work-log/entries")
    public
    @ResponseBody
    WorkLogJson monthWorkLog(@PathVariable String year, @PathVariable String month) {

        Month m = Month.of(String.format("%s/%s", year, month));

        WorkLog workLog = repository.loadAll().in(m);

        return jsonResponse(workLog);
    }

    @RequestMapping("/calendar/{yearMonthList:(?:[\\d]{6})(?:\\,[\\d]{6})*}/work-log/entries")
    public
    @ResponseBody
    WorkLogJson monthsWorkLog(@PathVariable String yearMonthList) {
        WorkLog workLog = repository.loadAll().in(Periods.parse(yearMonthList));
        return jsonResponse(workLog);
    }

    private WorkLogJson jsonResponse(WorkLog workLog) {
        List<WorkLogEntryJson> items = new ArrayList<WorkLogEntryJson>();
        for (WorkLogEntry entry : workLog) {
            items.add(new WorkLogEntryJson(entry.id(), entry.workload(), entry.projectNames(), entry.employee(), entry.day()));
        }
        return new WorkLogJson(items);
    }

    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    class WorkLogJson {

        List<WorkLogEntryJson> items;

        WorkLogJson(List<WorkLogEntryJson> items) {
            this.items = items;
        }
    }

    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    class WorkLogEntryJson {

        String link, id, workload, employee, day;

        Iterable<String> projectNames;

        WorkLogEntryJson(EntryID id, Workload workload, Iterable<ProjectName> projectNames, EmployeeID employee, Day day) {
            this.id = id.toString();
            this.workload = workload.toString();
            this.projectNames = StreamSupport.stream(projectNames.spliterator(), false).map(Objects::toString).collect(Collectors.toList());
            this.employee = employee.toString();
            this.link = String.format("/endpoints/v1/work-log/entries/%s", id);
            this.day = day.toString();
        }
    }
}

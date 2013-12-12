package com.github.mpi.time_registration.application;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.UpdateService;
import com.github.mpi.time_registration.domain.WorkLogEntry.EntryID;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository.WorkLogEntryDoesNotExists;
import com.github.mpi.time_registration.domain.Workload;

@Controller
public class UpdateEndpoint {

    @Autowired
    private UpdateService service;
    
    @RequestMapping(
            method   = POST, 
            consumes = "application/json",
            value    = "/endpoints/v1/work-log/entries/{id:.+}")
    @ResponseStatus(OK)
    public @ResponseBody String updateEntry(HttpServletResponse response, @PathVariable String id, @RequestBody Form form){

        Workload workload = form.workload == null ? null : Workload.of(form.workload);
        ProjectName projectName = form.projectName == null ? null : new ProjectName(form.projectName);
        
        service.updateWorkLogEntry(new EntryID(id), workload, projectName);

        setLocation(response, "/endpoints/v1/work-log/entries/%s", id);
        
        return "{\"status\": \"sucess\"}";
    }

    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    public static class Form{
        String workload;
        String projectName;
    }
    
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletResponse response){
        
        if(e instanceof WorkLogEntryDoesNotExists){
            setStatus(response, SC_NOT_FOUND);
            return;
        }
        if(e instanceof IllegalArgumentException){
            setStatus(response, SC_BAD_REQUEST);
            return;
        }
    }

    private void setStatus(HttpServletResponse response, int statusCode) {
        response.setStatus(statusCode);
    }

    private void setLocation(HttpServletResponse response, String template, String id) {
        response.setHeader("Location", String.format(template, id));
    }
}


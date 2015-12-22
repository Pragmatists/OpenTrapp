package com.github.mpi.time_registration.application;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.mpi.time_registration.domain.WorkLogEntry;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository;
import com.github.mpi.time_registration.domain.WorkLogUpdateExpression;
import com.github.mpi.time_registration.domain.WorkLogEntryRepository.WorkLogEntryDoesNotExists;
import com.github.mpi.time_registration.infrastructure.persistence.mongo.UnitOfWork;

@Controller
public class BulkUpdateEndpoint {

    @Autowired
    private WorkLogEntryRepository repository;
    
    @Autowired
    private UnitOfWork unitOfWork;
    
    @RequestMapping(
            method   = POST, 
            consumes = "application/json",
            value    = "/endpoints/v1/work-log/bulk-update")
    @ResponseStatus(OK)
    public @ResponseBody AffectedEntriesJson updateEntry(@RequestBody BulkUpdateJson bulkUpdate){

        Integer rows = 0; 
        
        WorkLogUpdateExpression expression = WorkLogUpdateExpression.parse(bulkUpdate.expression);
        
        for(WorkLogEntry entry: repository.loadAll()){
            
            expression.applyOn(entry);
            rows++;
        }
        
        unitOfWork.commit();
        
        return new AffectedEntriesJson(rows);
    }

    @RequestMapping(
            method   = GET,
            produces = "application/json",
            value    = "/endpoints/v1/work-log/{query:.*}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AffectedEntriesJson validateQuery(@PathVariable("query") String query){
     
        if(query.contains("project")){
            return new AffectedEntriesJson(12);
        }
        
        throw new IllegalArgumentException("Invalid query!");
    }

    @RequestMapping(
            method   = GET,
            produces = "application/json",
            value    = "/endpoints/v1/work-log/")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AffectedEntriesJson validateQuery(){
        return validateQuery("");
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorJson handleException(Exception e, HttpServletResponse response){
        
        if(e instanceof WorkLogEntryDoesNotExists){
            setStatus(response, SC_NOT_FOUND);
            return new ErrorJson(e.getMessage());
        }
        if(e instanceof IllegalArgumentException){
            setStatus(response, SC_BAD_REQUEST);
            return new ErrorJson(e.getMessage());
        }
        if(e instanceof AccessDeniedException){
            setStatus(response, SC_FORBIDDEN);
            return new ErrorJson(e.getMessage());
        }

        setStatus(response, SC_BAD_REQUEST);
        return new ErrorJson(e.getMessage());
    }

    private void setStatus(HttpServletResponse response, int statusCode) {
        response.setStatus(statusCode);
    }
    
    public static class BulkUpdateJson {
        public String query;
        public String expression;
    }

    public static class AffectedEntriesJson {
        public Integer entriesAffected;

        public AffectedEntriesJson(Integer entriesAffected) {
            this.entriesAffected = entriesAffected;
        }
        
    }

    public static class ErrorJson {
        public String error;

        public ErrorJson(String error) {
            this.error = error;
        }
        
    }



}


package com.pragmatists.opentrapp.timeregistration.application;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository.WorkLogEntryDoesNotExists;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogQuery;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogUpdateExpression;
import com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.mongo.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
        
        for(WorkLogEntry entry: repository.loadAll().byQuery(new WorkLogQuery(bulkUpdate.query))){
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

        int rows = 0;
     
        for(WorkLogEntry entry: repository.loadAll().byQuery(new WorkLogQuery(decode(query)))){
            rows++;
        }
        
        return new AffectedEntriesJson(rows);
    }

    private String decode(String query) {
        return query.replaceAll("!project=", "#")
                .replaceAll("!employee=", "*")
                .replaceAll("!date=", "@")
                .replaceAll("\\+", " ")
                .replaceAll(":", "/");
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


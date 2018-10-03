package com.pragmatists.opentrapp.timeregistration.application;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.pragmatists.opentrapp.timeregistration.domain.EmployeeID;
import com.pragmatists.opentrapp.timeregistration.domain.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegistrationEndpoint {

    @Autowired
    private RegistrationService service;

    @Autowired
    private ManualEmployeeContext context;

    @RequestMapping(
              method = POST,
            consumes = "application/json",
               value = "/endpoints/v1/employee/{employeeID}/work-log/entries")
    @ResponseStatus(CREATED)
    @PreAuthorize("@permissions.canCreate(#employeeID)")
    public void submitEntry(@PathVariable String employeeID, @RequestBody Form form) {

        try{

            context.enter(new EmployeeID(employeeID));

            service.submit(form.workload, form.projectNames == null ? null : asList(form.projectNames), form.day);

        } catch (IllegalArgumentException e) {

            throw new InvalidExpressionException(e);

        } finally{

            context.leave();
        }
    }

    @JsonAutoDetect(fieldVisibility= Visibility.ANY)
    public static class Form{

        String[] projectNames;
        String workload, day;
    }

    @ResponseStatus(BAD_REQUEST)
    public class InvalidExpressionException extends IllegalArgumentException {

        private static final long serialVersionUID = -981128848209981239L;

        public InvalidExpressionException(Throwable cause) {
            super(cause);
        }
    }
}
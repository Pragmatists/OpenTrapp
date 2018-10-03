package com.pragmatists.opentrapp.usersandaccess.infrastructure.spring;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
import com.pragmatists.opentrapp.usersandaccess.application.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("permissions")
@Profile("spring-security")
public class OwnerPermissions implements Permissions {

    @Autowired
    private WorkLogEntryRepository repository;
    
    @Override
    public boolean canDelete(String id) {
        return employeeOf(id).equals(username());
    }

    @Override
    public boolean canModify(String id) {
        return employeeOf(id).equals(username());
    }

    @Override
    public boolean canCreate(String user) {
        return user.equals(username());
    }

    private String employeeOf(String id) {
        return repository.load(new EntryID(id)).employee().toString();
    }
    
    private String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

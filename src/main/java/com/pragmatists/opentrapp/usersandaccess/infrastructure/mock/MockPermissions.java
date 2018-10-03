package com.pragmatists.opentrapp.usersandaccess.infrastructure.mock;

import com.pragmatists.opentrapp.usersandaccess.application.Permissions;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

public class MockPermissions implements Permissions {

    @Override
    public boolean canDelete(String id){
        return true;
    }
    
    @Override
    public boolean canModify(String id){
        return true;
    }
    
    @Override
    public boolean canCreate(String user){
        return true;
    }
    
}

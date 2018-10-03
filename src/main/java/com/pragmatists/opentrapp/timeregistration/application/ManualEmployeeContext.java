package com.pragmatists.opentrapp.timeregistration.application;

import com.pragmatists.opentrapp.timeregistration.domain.EmployeeContext;
import com.pragmatists.opentrapp.timeregistration.domain.EmployeeID;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ManualEmployeeContext implements EmployeeContext {

    private EmployeeID employeeID = null;
    
    @Override
    public EmployeeID employeeID(){
        return employeeID;
    }
    
    public void enter(EmployeeID employeeID){
        this.employeeID = employeeID;
    }
    
    public void leave(){
        this.employeeID = null;
    }
}

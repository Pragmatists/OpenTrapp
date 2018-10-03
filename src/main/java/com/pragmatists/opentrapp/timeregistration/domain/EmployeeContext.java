package com.pragmatists.opentrapp.timeregistration.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;


public interface EmployeeContext {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public abstract EmployeeID employeeID();

}
package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

public interface PersistenceBoundedContext {

    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
    public abstract WorkLogEntryRepository repository();

    public abstract void clear();

}
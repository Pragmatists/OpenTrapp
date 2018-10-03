package com.pragmatists.opentrapp.usersandaccess.infrastructure.mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pragmatists.opentrapp.usersandaccess.application.Permissions;
import com.pragmatists.opentrapp.usersandaccess.infrastructure.spring.OwnerPermissions;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Profile("mock-security")
public class MockSecurityContext {

    @Autowired
    private Filter springSecurityFilterChain;
    
    private boolean mockModeEnabled = true;
    
    public void disableAtAll(){
        mockModeEnabled = false;
    }
    
    public void enableMockMode(){
        mockModeEnabled = true;
    }
    
    @Bean(name="permissions", autowire=Autowire.BY_TYPE)
    @Scope("prototype")
    public Permissions permissions(){
        if(mockModeEnabled){
            return new OwnerPermissions();
        } else{
            return new MockPermissions();
        }
    }
    
    @Bean(name="securityFilterChain")
    public Filter securityFilterChain(){
        
        return new OncePerRequestFilter() {
            
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                
                if(mockModeEnabled){
                    springSecurityFilterChain.doFilter(request, response, filterChain);
                } else{

                    try{
                        SecurityContext context = SecurityContextHolder.getContext();
                        List<SimpleGrantedAuthority> employeeRole = Arrays.asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                        context.setAuthentication(new AnonymousAuthenticationToken("User", new User("User", "", employeeRole), employeeRole));
                        filterChain.doFilter(request, response);
                    } finally{
                        SecurityContextHolder.clearContext();
                    }
                }
            }
        };
    }
}

package com.github.mpi.users_and_access.application;

import com.github.mpi.users_and_access.domain.SecurityContext;
import com.github.mpi.users_and_access.domain.User;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class AuthenticationEndpoint {

    @Autowired
    private SecurityContext securityContext;
    
    @Autowired
    private HttpServletRequest request;
    
    @RequestMapping(
            method   = GET,           
            value    = "/endpoints/v1/authentication/status")
    @ResponseStatus(OK)
    public @ResponseBody AuthenticationStatus status(){
        
        User authenticatedUser = securityContext.authenticatedUser();
        
        boolean isAuthenticated = !User.ANONYMOUS.equals(authenticatedUser);
        
        return new AuthenticationStatus(authenticatedUser.username(), authenticatedUser.displayName(), isAuthenticated);
    }
    
    private String host(){
        
        String fullPath = request.getRequestURL().toString();
        return fullPath.replace(request.getRequestURI(), "");
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    class AuthenticationStatus {

        String username, displayName;
        boolean authenticated;
        String loginUrl =  host() + "/endpoints/v1/authentication/login"; 
        String logoutUrl = host() + "/endpoints/v1/authentication/logout"; 

        public AuthenticationStatus(String username, String displayName, boolean authenticated) {
            this.username = username;
            this.displayName = displayName;
            this.authenticated = authenticated;
        }
    }
}

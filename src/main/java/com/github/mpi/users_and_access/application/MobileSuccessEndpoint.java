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
public class MobileSuccessEndpoint {

    @Autowired
    private SecurityContext securityContext;
    @Autowired
    private HttpServletRequest request;
    
    @RequestMapping(
            method   = GET,           
            value    = "/endpoints/v1/authentication/mobile-success")
    @ResponseStatus(OK)
    public @ResponseBody
    MobileStatus status(){
        
        User authenticatedUser = securityContext.authenticatedUser();
        
        boolean isAuthenticated = !User.ANONYMOUS.equals(authenticatedUser);

        return new MobileStatus(authenticatedUser.username(), authenticatedUser.displayName(), isAuthenticated, request.getSession().getId());
    }
    
    @JsonAutoDetect(fieldVisibility=Visibility.ANY)
    class MobileStatus {

        String username, displayName;
        boolean authenticated;
        String sessionId;

        public MobileStatus(String username, String displayName, boolean authenticated, String sessionId) {
            this.username = username;
            this.displayName = displayName;
            this.authenticated = authenticated;
            this.sessionId = sessionId;
        }
    }
}

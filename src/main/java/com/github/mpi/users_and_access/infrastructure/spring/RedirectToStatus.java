package com.github.mpi.users_and_access.infrastructure.spring;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectToStatus implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        
        response.sendRedirect(computeRedirectionUrl(request) + "#/authFailed");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        response.sendRedirect(computeRedirectionUrl(request) + "#/authToken/" + request.getSession().getId());
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        response.sendRedirect(computeRedirectionUrl(request));
    }
    
    private String computeRedirectionUrl(HttpServletRequest request) {

        String redirectTo = "/endpoints/v1/authentication/status/";

        if(System.getenv("REDIRECT_AFTER_LOGIN_URL") != null){
            redirectTo = System.getenv("REDIRECT_AFTER_LOGIN_URL");
        }
        return redirectTo;
    }


}

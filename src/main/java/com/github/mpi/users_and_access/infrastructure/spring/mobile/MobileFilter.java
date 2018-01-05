package com.github.mpi.users_and_access.infrastructure.spring.mobile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class MobileFilter extends AbstractAuthenticationProcessingFilter {
    Logger log = LoggerFactory.getLogger(MobileFilter.class);

    public MobileFilter(MobileAuthenticationManager mobileAuthenticationManager) {
        super("/endpoints/v1/authentication/mobile");
        setAuthenticationManager(mobileAuthenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.warn("------------- MobileFilter");
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            return null;
        }
        final String idTokenAsString = StringUtils.remove(authorizationHeader, "Bearer ");
        Authentication token = new AbstractAuthenticationToken(Collections.<GrantedAuthority>emptyList()) {
            @Override
            public Object getCredentials() {
                return idTokenAsString;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }
        };
        return getAuthenticationManager().authenticate(token);
    }

}

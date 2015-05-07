package com.github.mpi.users_and_access.infrastructure.spring.oauth2;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class OpenIDConnectAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private OAuth2RestOperations restTemplate;
    
    protected OpenIDConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
                
        final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.getForEntity("https://www.googleapis.com/oauth2/v2/userinfo", UserInfo.class);
        Authentication preAuthenticationToken = new PreAuthenticatedAuthenticationToken(userInfoResponseEntity.getBody(), "", NO_AUTHORITIES);
        
        return getAuthenticationManager().authenticate(preAuthenticationToken);
    }
}
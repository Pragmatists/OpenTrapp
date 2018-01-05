package com.github.mpi.users_and_access.infrastructure.spring.mobile;

import com.github.mpi.users_and_access.domain.User;
import com.github.mpi.users_and_access.infrastructure.spring.OpenIDUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import static java.util.Collections.singletonList;

@Component
public class MobileAuthenticationManager implements AuthenticationManager {
    Logger log = LoggerFactory.getLogger(MobileAuthenticationManager.class);

    @Autowired
    private TokenVerifier tokenVerifier;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String idTokenAsString = (String) authentication.getCredentials();

        final GoogleIdToken payload = tokenVerifier.verify(idTokenAsString);
        if (payload == null) {
            log.warn("Token is not properly verified");
            throw new AuthenticationException("Not verified") {};
        }
        log.debug("Token verified");

        return new MyAbstractAuthenticationToken(new OpenIDUserService.OpenIDUserAdapter(payload.getPayload().getEmail(), ""));
    }

    private static class MyAbstractAuthenticationToken extends AbstractAuthenticationToken {
        private final User user;

        public MyAbstractAuthenticationToken(User user) {
            super(singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
            this.user = user;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return user;
        }

        @Override
        public boolean isAuthenticated() {
            return true;
        }
    }
}

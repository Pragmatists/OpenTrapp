package com.github.mpi.users_and_access.infrastructure.spring.mobile;

import com.github.mpi.users_and_access.domain.User;
import com.github.mpi.users_and_access.infrastructure.spring.OpenIDUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Collections.singletonList;

public class MobileAuthenticationManager implements AuthenticationManager {
    Logger log = LoggerFactory.getLogger(MobileAuthenticationManager.class);
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.warn("--------- Hello from authentication manager");
        String idTokenAsString = (String) authentication.getCredentials();
        log.warn("--------- " + idTokenAsString);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new JacksonFactory())
                // android expo OpenTrappDev
                .build();

        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idTokenAsString);
        } catch (GeneralSecurityException | IOException e) {
            log.warn("{} during calling verify", e);
            throw new AuthenticationException("Exception during verify") {};
        }

        if (googleIdToken == null) {
            log.warn("Token is not properly verified");
            throw new AuthenticationException("Not verified") {};
        }

        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        return new MyAbstractAuthenticationToken(new OpenIDUserService.OpenIDUserAdapter(payload.getEmail(), ""));
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

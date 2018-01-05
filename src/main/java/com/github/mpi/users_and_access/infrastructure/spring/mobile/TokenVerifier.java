package com.github.mpi.users_and_access.infrastructure.spring.mobile;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static java.util.Arrays.asList;

@Component
class TokenVerifier {
    private Logger log = LoggerFactory.getLogger(TokenVerifier.class);

    @Value("#{environment.OAUTH2_ANDROID_CLIENT_ID}")
    private String androidClientId;

    @Value("#{environment.OAUTH2_IPHONE_CLIENT_ID}")
    private String iphoneClientId;

    GoogleIdToken verify(String idToken) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new JacksonFactory())
                .setAudience(asList(androidClientId, iphoneClientId))
                .build();

        try {
            return verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException e) {
            log.warn("Got {} during token verification");
            return null;
        }
    }
}

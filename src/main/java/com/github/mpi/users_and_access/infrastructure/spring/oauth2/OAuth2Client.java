package com.github.mpi.users_and_access.infrastructure.spring.oauth2;

import java.util.Arrays;

import javax.annotation.Resource;

import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
@Profile("google-security")
public class OAuth2Client {
    
    @Value("#{environment.OAUTH2_CLIENT_ID}")
    private String clientId;

    @Value("#{environment.OAUTH2_CLIENT_SECRET}")
    private String clientSecret;

    @Bean
    // TODO retrieve from https://accounts.google.com/.well-known/openid-configuration ?
    public OAuth2ProtectedResourceDetails googleOAuth2Details() {
        AuthorizationCodeResourceDetails googleOAuth2Details = new AuthorizationCodeResourceDetails();
        googleOAuth2Details.setAuthenticationScheme(form);
        googleOAuth2Details.setClientAuthenticationScheme(form);
        googleOAuth2Details.setClientId(clientId);
        googleOAuth2Details.setClientSecret(clientSecret);
        googleOAuth2Details.setUserAuthorizationUri("https://accounts.google.com/o/oauth2/auth");
        googleOAuth2Details.setAccessTokenUri("https://www.googleapis.com/oauth2/v3/token");
        googleOAuth2Details.setScope(Arrays.asList("openid", "email"));
        return googleOAuth2Details;
    }

    @Resource
    private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestOperations googleOAuth2RestTemplate() {
        return new OAuth2RestTemplate(googleOAuth2Details(), oAuth2ClientContext);
    }
}
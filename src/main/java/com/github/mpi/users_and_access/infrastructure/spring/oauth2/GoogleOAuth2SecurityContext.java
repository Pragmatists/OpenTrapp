package com.github.mpi.users_and_access.infrastructure.spring.oauth2;

import com.github.mpi.users_and_access.infrastructure.spring.RedirectToStatus;
import com.github.mpi.users_and_access.infrastructure.spring.mobile.MobileAuthenticationManager;
import com.github.mpi.users_and_access.infrastructure.spring.mobile.MobileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("google-security")
public class GoogleOAuth2SecurityContext {
    Logger log = LoggerFactory.getLogger(GoogleOAuth2SecurityContext.class);

    private final String LOGIN_URL = "/endpoints/v1/authentication/login";

    @Autowired
    private Filter springSecurityFilterChain;

    public GoogleOAuth2SecurityContext() {

    }

    @Bean(name="securityFilterChain")
    public Filter securityFilterChain(){
        return springSecurityFilterChain;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(LOGIN_URL);
    }

    private AuthenticationManager authenticationManager(){
        PreAuthenticatedAuthenticationProvider basicProvider = new PreAuthenticatedAuthenticationProvider();
        basicProvider.setPreAuthenticatedUserDetailsService(detailsService());
        List<AuthenticationProvider> singleProvider = Arrays.<AuthenticationProvider>asList(basicProvider);
        return new ProviderManager(singleProvider);
    }

    private GoogleOpenIDConnectUserService detailsService(){
        GoogleOpenIDConnectUserService detailsService = new GoogleOpenIDConnectUserService();
        detailsService.setAllowedUserEmails(Arrays.asList("^.*@pragmatists.pl$"));
        return detailsService;
    }

    @Bean
    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
        OpenIDConnectAuthenticationFilter filter = new OpenIDConnectAuthenticationFilter(LOGIN_URL);
        filter.setAuthenticationFailureHandler(new RedirectToStatus());
        filter.setAuthenticationSuccessHandler(new RedirectToStatus());
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public OAuth2ClientContextFilter oAuth2ClientContextFilter() {
        return new OAuth2ClientContextFilter();
    }

    @Bean(name = "mobileFilter")
    public MobileFilter mobileFilter(MobileAuthenticationManager mobileAuthenticationManager) {
        MobileFilter mobileFilter = new MobileFilter(mobileAuthenticationManager);
        mobileFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/endpoints/v1/authentication/mobile-success"));
        return mobileFilter;
    }

}

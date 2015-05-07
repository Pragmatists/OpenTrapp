package com.github.mpi.users_and_access.infrastructure.spring.oauth2;

import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import com.github.mpi.users_and_access.infrastructure.spring.RedirectToStatus;

@Configuration
@EnableWebSecurity
@Profile("google-security")
public class GoogleOAuth2SecurityContext extends WebSecurityConfigurerAdapter{

    private final String LOGIN_URL = "/endpoints/v1/authentication/login";

    @Autowired
    private Filter springSecurityFilterChain;

    @Bean(name="securityFilterChain")
    public Filter securityFilterChain(){
        return springSecurityFilterChain;
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(LOGIN_URL);
    }

    public AuthenticationManager authenticationManager(){
        PreAuthenticatedAuthenticationProvider basicProvider = new PreAuthenticatedAuthenticationProvider();
        basicProvider.setPreAuthenticatedUserDetailsService(detailsService());
        List<AuthenticationProvider> singleProvider = Arrays.<AuthenticationProvider>asList(basicProvider);
        return new ProviderManager(singleProvider); 
    }
    
    public GoogleOpenIDConnectUserService detailsService(){
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
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .logout()
                    .logoutSuccessHandler(new RedirectToStatus())
                    .logoutUrl("/endpoints/v1/authentication/logout")
                .and()
                .authorizeRequests()
                    .antMatchers("/endpoints/v1/authentication/**").permitAll()
                    .antMatchers("/**").authenticated();
    }
    
}

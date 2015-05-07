package com.github.mpi.users_and_access.infrastructure.spring.oauth2;

public class GoogleOAuth2SecurityContext {

//    private final String LOGIN_URL = "/login";
//
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return new LoginUrlAuthenticationEntryPoint(LOGIN_URL);
//    }
//
//    @Bean
//    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
//        return new OpenIDConnectAuthenticationFilter(LOGIN_URL);
//    }
//
//    @Bean
//    public OAuth2ClientContextFilter oAuth2ClientContextFilter() {
//        return new OAuth2ClientContextFilter();
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
//                .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
//                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
//                .and().authorizeRequests()
//                .antMatchers(GET, "/").permitAll()
//                .antMatchers(GET, "/test").authenticated();
//    }
    
}

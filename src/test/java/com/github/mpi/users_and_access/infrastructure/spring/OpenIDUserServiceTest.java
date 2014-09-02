package com.github.mpi.users_and_access.infrastructure.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

public class OpenIDUserServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private OpenIDUserService service;
    
    @Before
    public void setUp() {
        service = new OpenIDUserService();
    }
    
    @Test
    public void shouldRejectAllEmailsIfAllowedNotSpecified() throws Exception {

        // expect:
        thrown.expect(UsernameNotFoundException.class);
        
        // when:
        detailsOfUserAuthenticatedAs("homer.simpson@springfield.com");
    }
    
    @Test
    public void shouldAcceptUserIfSpecifiedExplicitly() throws Exception {

        // given:
        service.setAllowedUserEmails(Arrays.asList("homer.simpson@springfield.com"));
        
        // when:
        UserDetails userDetails = detailsOfUserAuthenticatedAs("homer.simpson@springfield.com");
        
        // then:
        assertThat(userDetails.getUsername()).isEqualTo("homer.simpson");
    }
    
    @Test
    public void shouldAcceptUserIfSpecifiedByRegexp() throws Exception {
        
        // given:
        service.setAllowedUserEmails(Arrays.asList("^.*@springfield.com$"));
        
        // when:
        UserDetails userDetails = detailsOfUserAuthenticatedAs("homer.simpson@springfield.com");
        
        // then:
        assertThat(userDetails.getUsername()).isEqualTo("homer.simpson");
    }

    // --
    
    public UserDetails detailsOfUserAuthenticatedAs(String email) {
        List<OpenIDAttribute> attributes = Arrays.asList(new OpenIDAttribute("Email", "Email", Arrays.asList(email)));
        return service.loadUserDetails(new OpenIDAuthenticationToken(null,  Collections.<GrantedAuthority>emptyList(), null, attributes));
    }
    
}

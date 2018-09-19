package com.github.mpi.users_and_access.infrastructure.spring.oauth2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.github.mpi.users_and_access.domain.User;

public class GoogleOpenIDConnectUserService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private List<String> allowedUserEmailsRegexes = Collections.emptyList();

    public void setAllowedUserEmails(List<String> allowedUserEmails) {
        this.allowedUserEmailsRegexes = allowedUserEmails;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {

        Object principal = token.getPrincipal();
        if(!(principal instanceof UserInfo)){
            throw new IllegalArgumentException("Only UserInfo class is supported!");
        }
        
        UserInfo userInfo = (UserInfo) principal;
        
        String email = userInfo.getEmail();
        if (isNotAllowed(email)) {
            throw new UsernameNotFoundException(email);
        }

        return new OpenIDUserAdapter(userNameFrom(email), userNameFrom(email));
    }

    private String userNameFrom(String email) {
        return email.split("@")[0];
    }

    private boolean isNotAllowed(String email) {
        return !isAllowed(email);
    }

    private boolean isAllowed(String email) {
        for (String emailRegex : allowedUserEmailsRegexes) {
            if (Pattern.compile(emailRegex).matcher(email).matches())
                return true;
        }
        return false;
    }

    private String readAttribute(List<OpenIDAttribute> attributes, String name) {
        for (OpenIDAttribute attribute : attributes) {
            if (name.equals(attribute.getName())) {
                return attribute.getValues().get(0);
            }
        }
        return null;
    }

    public static final class OpenIDUserAdapter extends User implements UserDetails {
        
        private static final long serialVersionUID = -6345826126136996788L;

        public OpenIDUserAdapter(String email, String fullName) {
            super(email, fullName);
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public String getUsername() {
            return super.username();
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }
    }
}
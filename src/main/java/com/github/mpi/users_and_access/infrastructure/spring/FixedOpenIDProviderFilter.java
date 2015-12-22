package com.github.mpi.users_and_access.infrastructure.spring;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.openid.OpenIDAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class FixedOpenIDProviderFilter implements Filter{
    
    private String providerUrl;

    @Autowired
    private OpenIDAuthenticationFilter configuredFilter;
    
    @PostConstruct
    void configure(){
        configuredFilter.setReturnToUrlParameters(Sets.newHashSet("redirect_to"));
    }
    
    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        chain.doFilter(new FixedOpenIDProviderRequest(request), response);
    }

    private final class FixedOpenIDProviderRequest extends HttpServletRequestWrapper {


        public FixedOpenIDProviderRequest(ServletRequest request) {
            super((HttpServletRequest) request);
        }

        @Override
        public String getParameter(String name) {
            
            if(OpenIDAuthenticationFilter.DEFAULT_CLAIMED_IDENTITY_FIELD.equals(name)){
                return providerUrl;
            }
            
            return super.getParameter(name);
        }
    }
}

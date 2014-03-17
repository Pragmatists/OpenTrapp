package com.github.mpi.users_and_access.infrastructure.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.message.AuthSuccess;
import org.openid4java.message.DirectError;
import org.openid4java.message.Message;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.server.ServerManager;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * Based on: https://code.google.com/p/openid4java/wiki/SampleServer
 */
@Profile("mock-security")
@Component
public class MockOpenIDServer {

    public ServerManager manager = new ServerManager();

    private String username = "homer.simpson";
    private String firstName = "Homer";
    private String lastName = "Simpson";
    private boolean authenticated = true;
    
    public MockOpenIDServer() {
        manager.setOPEndpointUrl("http://localhost:8080/MockOpenID/authenticate");
    }

    public String processRequest(HttpServletRequest httpReq, HttpServletResponse httpResp) throws Exception {

        ParameterList request = new ParameterList(httpReq.getParameterMap());

        String mode = request.hasParameter("openid.mode") ? request.getParameterValue("openid.mode") : null;

        Message response;
        String responseText;

        if ("associate".equals(mode)) {
            response = manager.associationResponse(request);
            responseText = response.keyValueFormEncoding();
        } else if ("checkid_setup".equals(mode) || "checkid_immediate".equals(mode)) {
            
            String userSelectedId = "http://localhost:8080/MockOpenID/id/" + username;
            String userSelectedClaimedId = userSelectedId;
            Boolean authenticatedAndApproved = authenticated;

            response = manager.authResponse(request, userSelectedId, userSelectedClaimedId, authenticatedAndApproved.booleanValue(), false);

            if(response instanceof AuthSuccess){

                Map<String, String> map = new HashMap<>();
                map.put("ns.ext1", "http://openid.net/srv/ax/1.0");
                map.put("mode", "fetch_response");
                map.put("type.Email", "http://schema.openid.net/contact/email");
                map.put("value.Email", username);
                map.put("type.FirstName", "http://axschema.org/namePerson/first");
                map.put("value.FirstName", firstName);
                map.put("type.LastName", "http://axschema.org/namePerson/last");
                map.put("value.LastName", lastName);
                ParameterList params = new ParameterList(map);
                response.addExtension(FetchResponse.createFetchResponse(params));

                manager.sign((AuthSuccess) response);
                
            } else{
                throw new BadCredentialsException("OpenID authentication failed!");
            }

            if (response instanceof DirectError)
                return directResponse(httpResp, response.keyValueFormEncoding());
            else {
                return response.getDestinationUrl(true);
            }
            
        } else if ("check_authentication".equals(mode)) {
            response = manager.verify(request);
            responseText = response.keyValueFormEncoding();
        } else {
            response = DirectError.createDirectError("Unknown request");
            responseText = response.keyValueFormEncoding();
        }

        return responseText;
    }

    private String directResponse(HttpServletResponse httpResp, String response) throws IOException {
        ServletOutputStream os = httpResp.getOutputStream();
        os.write(response.getBytes());
        os.close();
        return null;
    }

    public void setAuthenticatedAs(String username, String firstName, String lastName){
        this.authenticated = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public void setUnauthenticated() {
        this.authenticated = false;
        this.firstName = "";
        this.lastName = "";
        this.username = "";
    }
}
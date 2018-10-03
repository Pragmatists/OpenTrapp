package concordion.authentication;

//import com.github.mpi.users_and_access.infrastructure.mock.MockOpenIDServer;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import support.ApiFixture;

public class AuthenticationFixture extends ApiFixture {

//    @Autowired
//    private MockOpenIDServer openID;
    
    private SessionFilter filter = new SessionFilter();

    private String eventuallyRedirectedPage = "no-redirection";
    
    @Override
    public void clear() {
        super.clear();
//        securityContext.enableMockMode();
    }
    
    @Override
    public void get(String location) {
        response = request.filter(filter).when().get(location);
        response.prettyPrint();
    }
    
    public void loggedInAs(final String displayName, final String username) throws UnsupportedEncodingException{
        String firstName = displayName.split(" ")[0];
        String lastName = displayName.split(" ")[1];
//        openID.setAuthenticatedAsPrivileged(username, firstName, lastName);
        
        login();
    }

    public String redirectedPage(){
        return eventuallyRedirectedPage.replaceAll("http://localhost:8080", "").replaceAll(sessionId(), "{sessionId}");
    }
    
    public void login(String url) {
        
        int i = 1;
        
        while(url != null){
            
            response = 
                 RestAssured
                    .given()
                        .filter(filter)
                        .redirects()
                        .follow(false)
                    .when()
                        .get(url);
            
            url = response.getHeader("Location");
            
            if(url != null){
                url = decode(url);
                eventuallyRedirectedPage = url;
            }
            
            System.err.println("redirect to(" + i++ + "): " + url);
            System.err.println("session set to: " + filter.getSessionId());
        }
    }

    private String decode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }    
    
    private void login() {
        login("/endpoints/v1/authentication/login");
    }

    public void loginSuccessfully(String url) {
//        openID.setAuthenticatedAsPrivileged("homer.simpson", "Homer", "Simpson");
        login(url);
    }

    public void loginUnsuccessfully(String url) {
//        openID.setAuthenticatedAsUnprivileged("homer.simpson", "Homer", "Simpson");
        login(url);
    }
    
    public void unauthenticated(){

//        openID.setUnauthenticated();
        login();
    }

    public void authenticated() throws UnsupportedEncodingException{
        loggedInAs("Homer Simpson", "homer.simpson");
    }
    
    public String sessionId(){
        return filter.getSessionId();
    }
}

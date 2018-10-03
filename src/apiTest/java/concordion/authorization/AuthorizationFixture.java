package concordion.authorization;

import com.pragmatists.opentrapp.timeregistration.domain.*;
import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import support.ApiFixture;

import static java.util.Arrays.asList;

public class AuthorizationFixture extends ApiFixture {

//    @Autowired
//    private MockOpenIDServer openID;
    
    @Autowired
    private WorkLogEntryRepository repository;
    
    private SessionFilter filter = new SessionFilter();

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

    @Override
    public void post(String location) {
        response = request.filter(filter).when().post(location);
        response.prettyPrint();
    }
    
    @Override
    public void delete(String location) {
        response = request.filter(filter).when().delete(location);
        response.prettyPrint();
    }
    
    public void workLogEntryFor(String id, String employee){
        repository.store(new WorkLogEntry(new EntryID(id), Workload.of("12m"), asList(new ProjectName("p")), new EmployeeID(employee),
                Day.of("2014/02/12")));
    }
    
    public void loggedInAs(String username) throws UnsupportedEncodingException{
//        openID.setAuthenticatedAsPrivileged(username, username, username);
        login();
    }

    private void login() {
        login("/endpoints/v1/authentication/login");
        response = null;
    }

    public void unauthenticated(){

//        openID.setUnauthenticated();
        login();
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
    
    public void authenticated() throws UnsupportedEncodingException{
        loggedInAs("Homer");
    }
    
    public void validRegistration(String location){
        body("{\"workload\": \"1h 30m\",\"projectNames\": [\"ApiDesign\"],\"day\": \"2014/01/01\"}");
        post(location);
    }
    
    public void validUpdate(String location){
        body("{\"projectNames\": [\"NewProject\"]}");
        post(location);
    }
}

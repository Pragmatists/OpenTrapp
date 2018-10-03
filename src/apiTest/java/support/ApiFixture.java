package support;

import com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.PersistenceBoundedContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.concordion.api.extension.Extensions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;


@RunWith(ApiAcceptanceTestRunner.class)
@Extensions(Template.class)
public class ApiFixture {

    @Autowired
    private PersistenceBoundedContext persistenceContext;

//    @Autowired
//    protected MockSecurityContext securityContext;
    
    protected RequestSpecification request;
    protected Response response;

    public void clear() {

//        securityContext.disableAtAll();
        persistenceContext.clear();

        request = given()
                .log().all()
                .contentType(ContentType.JSON);
        response = null;
    }

    public String status() {
        return response.statusLine();
    }

    public boolean isSuccessful() {
        return response.statusCode() >= 200 && response.statusCode() < 300;
    }

    public void body(String body) {
        request.body(body);
    }

    public void contentType(String contentType) {
        request.contentType(contentType);
    }

    public void request(String method, String location) {

        if ("POST".equals(method)) {
            post(location);
        }

        if ("GET".equals(method)) {
            get(location);
        }

        if ("PUT".equals(method)) {
            put(location);
        }

        if ("DELETE".equals(method)) {
            delete(location);
        }
    }

    public void get(String location) {
        response = request.when().get(location);
        response.prettyPrint();
    }

    public void post(String location) {
        response = request.when().post(location);
        response.prettyPrint();
    }

    public void put(String location) {
        response = request.when().put(location);
        response.prettyPrint();
    }

    public void delete(String location) {
        response = request.when().delete(location);
        response.prettyPrint();
    }

    public String response() {
        return response.asString().replaceAll("localhost:8080", "{host}");
    }

    public String headerContent(String header) {

        String headerContent = response.header(header);

        if (headerContent != null)
            return header + ": " + headerContent;

        return "(no header)";
    }

}
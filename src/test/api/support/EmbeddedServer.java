package support;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class EmbeddedServer {
    
    Server server;

    public EmbeddedServer(Integer port) {
        server = new Server(port);
        WebAppContext context = new WebAppContext();
        context.setResourceBase("./src/main/webapp");
        context.setDescriptor("./WEB-INF/web.xml");
        server.setHandler(context);
    }

    public void start() {
        
        enableProfiles();
        
        System.err.println("-------------- Starting Embedded Server for Integration Tests...");
        
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void enableProfiles() {
        
//        System.setProperty("spring.profiles.active","mock-security,transients");            // aceptance-tests
//        System.setProperty("spring.profiles.active","mongo,mongo-lab,spring-security");   // test
        System.setProperty("spring.profiles.active","mongo,mongo-dev,spring-security");   // test
//        System.setProperty("spring.profiles.active","spring-security,transients");        // dev

        System.err.println("-------------------------------");
        System.err.println(String.format("    Using profiles: %s ", System.getProperty("spring.profiles.active")));
        System.err.println("-------------------------------");

    }

    void awaitShutdown() {
        try {
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {

        System.err.println("-------------- Stopping Embedded Server for Integration Tests...");
        
        try {
            server.stop();
            awaitShutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
        
        EmbeddedServer server = new EmbeddedServer(Integer.valueOf(envOr("PORT", "8080")));
        
        server.start();
        server.awaitShutdown();
    }
    
    private static String envOr(String port, String defaultValue) {
        String variable = System.getenv(port);
        if (variable == null) {
            return defaultValue;
        }
        return variable;
    }
}
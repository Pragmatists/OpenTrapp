package support;

import com.pragmatists.opentrapp.OpenTrappApplication;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import concordion.ConcordionFixture;
import org.springframework.context.ConfigurableApplicationContext;

@RunWith(Suite.class)
@SuiteClasses(ConcordionFixture.class)
public class ApiTestSuite {
    private static ConfigurableApplicationContext applicationContext;

    @BeforeClass
    public static void setUp() {
        SpringApplication springApplication = new SpringApplication(OpenTrappApplication.class);
        springApplication.setAdditionalProfiles("transients", "demo", "mock-security");
        applicationContext = springApplication.run();
    }

    @AfterClass
    public static void tearDown() {
        SpringApplication.exit(applicationContext);
    }

    public static ApplicationContext context() {
        return applicationContext;
    }
}
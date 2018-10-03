package support;

import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import support.ApiTestSuite;

public class ApiAcceptanceTestRunner extends ConcordionRunner {

    public ApiAcceptanceTestRunner(Class<?> fixtureClass) throws InitializationError {
        super(fixtureClass);
    }

    @Override
    protected Object createTest() throws Exception {
        
        Object test = super.createTest();

        injectDependencies(test);
        
        return test;
    }
    
    private void injectDependencies(Object test) {

        AutowireCapableBeanFactory injector = adHocInjector();
        injector.autowireBeanProperties(test, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }

    private AutowireCapableBeanFactory adHocInjector() {
        AnnotationConfigApplicationContext adHocAutowiringContext 
            = new AnnotationConfigApplicationContext(AutowiredAnnotationBeanPostProcessor.class);
        adHocAutowiringContext.setParent(ApiTestSuite.context());
        return adHocAutowiringContext.getAutowireCapableBeanFactory();
    }
    
}

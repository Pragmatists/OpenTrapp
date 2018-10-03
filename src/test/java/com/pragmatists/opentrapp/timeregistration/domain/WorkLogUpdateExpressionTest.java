package com.pragmatists.opentrapp.timeregistration.domain;

import com.pragmatists.opentrapp.timeregistration.domain.WorkLogEntry.EntryID;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class WorkLogUpdateExpressionTest {

    @Test
    public void shouldValidateExpression() throws Exception {
        
        // when:
        try {
            WorkLogUpdateExpression.parse("+#NewProject 123sa");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch(Exception e){
            // then:
            assertThat(e)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid WorkLog update expression!");
        }
    }
    
    @Test
    public void shouldAllowToAddProject() throws Exception {

        // given:
        WorkLogUpdateExpression expression = new WorkLogUpdateExpression("+#NewProject");
        
        // when:
        WorkLogEntry worklog = anWorkloadWithProjects(new ProjectName("OldProject"));
        expression.applyOn(worklog);
        
        // then:
        assertThat(worklog.projectNames())
            .containsOnly(
                new ProjectName("OldProject"), 
                new ProjectName("NewProject")
            );
    }

    @Test
    public void shouldAllowToAddMultipleProjects() throws Exception {

        // given:
        WorkLogUpdateExpression expression = new WorkLogUpdateExpression("+#NewProject +#SecondProject");
        
        // when:
        WorkLogEntry worklog = anWorkloadWithProjects(new ProjectName("OldProject"));
        expression.applyOn(worklog);
        
        // then:
        assertThat(worklog.projectNames())
            .containsOnly(
                new ProjectName("OldProject"), 
                new ProjectName("NewProject"),
                new ProjectName("SecondProject")
            );
    }
    
    @Test
    public void shouldAllowToRemoveProject() throws Exception {

        // given:
        WorkLogUpdateExpression expression = new WorkLogUpdateExpression("-#OldProject");
        
        // when:
        WorkLogEntry worklog = anWorkloadWithProjects(new ProjectName("OldProject"), new ProjectName("SecondProject"));
        expression.applyOn(worklog);
        
        // then:
        assertThat(worklog.projectNames())
            .containsOnly(
                new ProjectName("SecondProject")
            );
    }

    @Test
    public void shouldAllowToRemoveMultipleProject() throws Exception {

        // given:
        WorkLogUpdateExpression expression = new WorkLogUpdateExpression("-#OldProject -#SecondProject");
        
        // when:
        WorkLogEntry worklog = anWorkloadWithProjects(new ProjectName("OldProject"), new ProjectName("SecondProject"));
        expression.applyOn(worklog);
        
        // then:
        assertThat(worklog.projectNames()).isEmpty();
    }
    

    private WorkLogEntry anWorkloadWithProjects(ProjectName... projects) {
        return new WorkLogEntry(new EntryID("123"), Workload.of("14m"), asList(projects), new EmployeeID("homer.simpson"), Day.of("2014/01/01"));
    }
    
}

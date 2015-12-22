package com.github.mpi.time_registration.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WorkLogUpdateExpression {

    private static final Pattern REMOVE_PROJECT_PATTERN = Pattern.compile("\\-#([^\\s]*)");
    private static final Pattern ADD_PROJECT_PATTERN = Pattern.compile("\\+#([^\\s]*)");
    
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(String.format("^(%s|%s)(%s|%s|\\s)*$", "\\+#([^\\s]*)", "\\-#([^\\s]*)", "\\+#([^\\s]*)", "\\-#([^\\s]*)"));
    
    private String expression;

    public WorkLogUpdateExpression(String expression) {

        if(!EXPRESSION_PATTERN.matcher(expression).matches()){
            throw new IllegalArgumentException("Invalid WorkLog update expression!");
        }
        this.expression = expression;
    }

    public static WorkLogUpdateExpression parse(String expression) {
        return new WorkLogUpdateExpression(expression);
    }

    public void applyOn(WorkLogEntry entry) {
        Matcher addMatcher = ADD_PROJECT_PATTERN.matcher(expression);
        while(addMatcher.find()){
            String newProject = addMatcher.group(1);
            entry.addProject(new ProjectName(newProject));
        }
        Matcher removeMatcher = REMOVE_PROJECT_PATTERN.matcher(expression);
        while(removeMatcher.find()){
            String projectToRemove = removeMatcher.group(1);
            entry.removeProject(new ProjectName(projectToRemove));
        }
    }

}

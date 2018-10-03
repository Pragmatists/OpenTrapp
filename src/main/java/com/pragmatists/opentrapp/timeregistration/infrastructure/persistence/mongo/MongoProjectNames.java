package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.mongo;

import com.pragmatists.opentrapp.timeregistration.domain.ProjectName;
import com.pragmatists.opentrapp.timeregistration.domain.ProjectNames;
import com.pragmatists.opentrapp.timeregistration.domain.time.Day;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MongoProjectNames implements ProjectNames {

    private final MongoTemplate mongo;
    
    private Day after = Day.of("1900/01/01");

    public MongoProjectNames(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    @Override
    public Iterator<ProjectName> iterator() {

        String mapJS =
                "function () {"+
                "   if(this.day.date >= '" + after.toString() + "') {" +
                "       this.projectNames.forEach(function(projectName) {" +
                "           emit(projectName.name, 1);" +
                "       });" +
                "   }" +
                "}";
     
        String reduceJS = 
                "function (key, values) { "+
                "    return Array.sum(values); " +
                "}";

        MapReduceResults<Document> results = mongo.mapReduce("workLogEntry", mapJS, reduceJS, Document.class);
        
        List<ProjectName> projectNames = new ArrayList<ProjectName>();
        
        for (Document o : results) {
           projectNames.add(new ProjectName(o.get("_id").toString()));
        }
        return projectNames.iterator();
    }

    @Override
    public ProjectNames after(Day after) {
        this.after = after;
        return this;
    }
}

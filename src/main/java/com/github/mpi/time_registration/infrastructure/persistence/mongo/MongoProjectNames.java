package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

import com.github.mpi.time_registration.domain.ProjectName;
import com.github.mpi.time_registration.domain.ProjectNames;
import com.github.mpi.time_registration.domain.time.Day;
import com.mongodb.DBObject;

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

        MapReduceResults<DBObject> results = mongo.mapReduce("workLogEntry", mapJS, reduceJS, DBObject.class);
        
        List<ProjectName> projectNames = new ArrayList<ProjectName>();
        
        for (DBObject o : results) {
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

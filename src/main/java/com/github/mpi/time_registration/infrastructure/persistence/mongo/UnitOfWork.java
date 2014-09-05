package com.github.mpi.time_registration.infrastructure.persistence.mongo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;

public class UnitOfWork {

    private Map<Object, Object> identityMap = new HashMap<Object, Object>();
    
    private MongoTemplate mongo;
    
    public UnitOfWork(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public void commit() {
        
        System.err.println("-- commiting unit of work: " + identityMap);
        
        for (Object entry : identityMap.values()) {
            synchronize(entry);
        }
        
        identityMap.clear();
    }

    private void synchronize(Object entry) {
        mongo.save(entry);
    }

    public void register(Object id, Object entry) {
        identityMap.put(id, entry);
    }

    public boolean contains(Object id) {
        return identityMap.containsKey(id);
    }

    public Object get(Object id) {
        return identityMap.get(id);
    }

}

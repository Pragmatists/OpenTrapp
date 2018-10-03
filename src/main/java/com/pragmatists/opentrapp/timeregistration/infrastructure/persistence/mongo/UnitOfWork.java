package com.pragmatists.opentrapp.timeregistration.infrastructure.persistence.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.mongodb.core.MongoTemplate;

public class UnitOfWork {

    private Map<Object, Object> identityMap = new HashMap<Object, Object>();
    private Map<Object, Integer> clonesMap = new HashMap<Object, Integer>();
    
    private MongoTemplate mongo;
    
    protected UnitOfWork() {
    }
    
    public UnitOfWork(MongoTemplate mongo) {
        this();
        this.mongo = mongo;
    }

    public void commit() {
        
        System.err.println("-- commiting unit of work: " + identityMap);
        
        for (Entry<Object, Object> entry : identityMap.entrySet()) {
            synchronize(entry.getKey(), entry.getValue());
        }
        identityMap.clear();
        clonesMap.clear();
    }

    private void synchronize(Object id, Object entry) {
        if(isDirty(id, entry)){
            mongo.save(entry);
        }
    }

    private boolean isDirty(Object id, Object entry) {
        return hashOf(entry) != clonesMap.get(id);
    }

    private int hashOf(Object entry) {
        return mongo.getConverter().convertToMongoType(entry).hashCode();
    }

    public void register(Object id, Object entry) {
        if(identityMap.containsKey(id)){
            throw new IllegalArgumentException("Unit of Work already contains entry with id: " + id);
        }
        identityMap.put(id, entry);
        clonesMap.put(id, hashOf(entry));
    }

    public boolean contains(Object id) {
        return identityMap.containsKey(id);
    }

    public Object get(Object id) {
        return identityMap.get(id);
    }
}

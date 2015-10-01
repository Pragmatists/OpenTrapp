package com.github.mpi.infrastructure.mongo;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.List;

@Configuration
@Profile("mongo")
public class MongoContext {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) throws Exception {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    protected MongoDbFactory mongoDbFactory(
            @Value("${mongo.db.url}") String host, 
            @Value("${mongo.db.port}") int port,
            @Value("${mongo.db.name}") String databaseName, 
            @Value("${mongo.db.user}") String username, 
            @Value("${mongo.db.pass}") String password) throws Exception {
        
        return new SimpleMongoDbFactory(new MongoClient(new ServerAddress(host, port), getCredentials(username, databaseName, password)), databaseName);
    }

    private List<MongoCredential> getCredentials(String user, String databaseName, String password) {
        if(user.length() == 0) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(MongoCredential.createCredential(user, databaseName, password.toCharArray()));
    }
}

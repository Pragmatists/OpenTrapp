package com.pragmatists.opentrapp.infrastructure.mongo;

import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@Profile("mongo")
public class MongoContext {

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) throws Exception {
        return new MongoTemplate(mongoDbFactory);
    }

    @Bean
    protected MongoDbFactory mongoDbFactory(MongoClientURI mongoDbURI) throws Exception {
        return new SimpleMongoDbFactory(mongoDbURI);
    }

}

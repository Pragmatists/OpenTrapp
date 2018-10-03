package com.pragmatists.opentrapp.infrastructure.mongo;

import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mongo-dev")
public class MongoDevelopmentProfile {

    @Bean
    public MongoClientURI mongoDbURI() {
        return new MongoClientURI("mongodb://localhost:27017/open-trapp");
    }

}

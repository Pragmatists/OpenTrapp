package com.pragmatists.opentrapp.infrastructure.mongo;

import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mongo-lab")
public class MongoLabProfile {

    @Bean
    public MongoClientURI mongoDbURI() {
        String mongoUri = System.getenv("MONGOLAB_CONNECTION_URI");
        return new MongoClientURI(mongoUri);
    }

}

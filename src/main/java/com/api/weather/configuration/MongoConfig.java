package com.api.weather.configuration;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("test")
public class MongoConfig {

    @Bean
    CommandLineRunner cleanDatabase(MongoTemplate mongoTemplate) {
        return args -> mongoTemplate.getDb().drop();
    }

}

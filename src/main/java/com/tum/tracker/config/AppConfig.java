package com.tum.tracker.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AppConfig {

    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://sysadmin:Destroyer22@cluster0.j1yj9.mongodb.net/test");
    }

    public @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "trackerDB");
    }
}

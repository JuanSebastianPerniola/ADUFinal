package com.example.demo.Mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        // Cambia la URI si tu MongoDB está en otro host/puerto
        return MongoClients.create("mongodb://localhost:27017/adufinal");
    }
}

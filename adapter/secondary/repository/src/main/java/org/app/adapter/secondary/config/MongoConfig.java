package org.app.adapter.secondary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("org.app.adapter.secondary")
public class MongoConfig {

    //fix for Mongo 2.0.2 - bean not found parent-child
}

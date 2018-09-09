package org.app.integration.service.starter;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"org.app.adapter", "org.app.usecase"})
@EnableScheduling
@EnableKafka
public class KafcamApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafcamApplication.class, args);
    }

}

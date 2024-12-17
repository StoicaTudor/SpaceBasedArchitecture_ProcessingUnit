package com.sba.processingunit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProcessingUnitApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcessingUnitApplication.class, args);
    }


//    @Bean
//    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
//        return args -> kafkaTemplate.send("TestTopic", new User(UUID.randomUUID().toString(), "name", 39));
//    }

//    @Bean
//    CommandLineRunner commandLineRunner(KafkaTemplate<String, UserFireAndForgetCommand> kafkaTemplate) {
//        String id = UUID.randomUUID().toString();
//        return args -> kafkaTemplate.send("TestTopic", id, new User(id, "name", 39));
//    }
}
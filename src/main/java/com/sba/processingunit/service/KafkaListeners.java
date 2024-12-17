package com.sba.processingunit.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @KafkaListener(topics = "DataWriterReadTopic")
    void listener(String data) {
        System.out.println("Listener received: " + data);
    }
}

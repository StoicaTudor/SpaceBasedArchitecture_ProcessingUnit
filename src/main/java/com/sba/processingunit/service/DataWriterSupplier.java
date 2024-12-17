package com.sba.processingunit.service;

import com.sba.processingunit.egress_communication.fire_and_forget.UserFireAndForgetCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataWriterSupplier {
    private final KafkaTemplate<String, UserFireAndForgetCommand> kafkaTemplate;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String boostrapServers;

    @Value(value = "${data.writer.topic.name}")
    private String topicName;

    public DataWriterSupplier(KafkaTemplate<String, UserFireAndForgetCommand> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCommand(UserFireAndForgetCommand userFireAndForgetCommand) {
        kafkaTemplate.send(topicName, userFireAndForgetCommand.getUser().id(), userFireAndForgetCommand);
    }
}

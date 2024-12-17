package com.sba.processingunit.service;

import com.sba.processingunit.egress_communication.read.UserFetchCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataReaderSupplier {
    private final KafkaTemplate<String, UserFetchCommand> kafkaTemplate;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String boostrapServers;

    @Value(value = "DataReaderWriteTopic")
    private String topicName;

    public DataReaderSupplier(KafkaTemplate<String, UserFetchCommand> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCommand(UserFetchCommand userFetchCommand) {
        kafkaTemplate.send(topicName, userFetchCommand.getCommandKey(), userFetchCommand);
    }
}

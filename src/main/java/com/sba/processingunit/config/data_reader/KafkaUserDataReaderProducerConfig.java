package com.sba.processingunit.config.data_reader;

import com.sba.processingunit.egress_communication.read.UserFetchCommand;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;


@Configuration
public class KafkaUserDataReaderProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String boostrapServers;

    private Map<String, Object> producerConfigUser() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    @Bean
    public ProducerFactory<String, UserFetchCommand> userDataReaderProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigUser());
    }

    @Bean
    public KafkaTemplate<String, UserFetchCommand> userDataReaderProducerTemplate(ProducerFactory<String, UserFetchCommand> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
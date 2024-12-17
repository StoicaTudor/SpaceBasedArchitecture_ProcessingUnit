package com.sba.processingunit.config.data_writer;

import com.sba.processingunit.egress_communication.fire_and_forget.UserFireAndForgetCommand;
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
public class KafkaUserDataWriterProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String boostrapServers;

    public Map<String, Object> producerConfig() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
    }

    public Map<String, Object> producerConfigUser() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    @Bean
    public ProducerFactory<String, UserFireAndForgetCommand> userDataWriterProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigUser());
    }

    @Bean
    public KafkaTemplate<String, UserFireAndForgetCommand> kafkaTemplateUser(ProducerFactory<String, UserFireAndForgetCommand> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
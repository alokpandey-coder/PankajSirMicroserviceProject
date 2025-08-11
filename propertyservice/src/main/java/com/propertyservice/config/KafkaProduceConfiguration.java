package com.propertyservice.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.propertyservice.contants.AppConstants;
import com.propertyservice.dto.EmailRequest;

@Configuration
public class KafkaProduceConfiguration {

    @Bean
    public ProducerFactory<String, EmailRequest> producerFactory() {
        Map<String, Object> kafkaProps = new HashMap<>();

        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.KAFKA_HOST);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Recommended Kafka producer configs for reliability and performance
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "all");           // Ensure full commit
        kafkaProps.put(ProducerConfig.RETRIES_CONFIG, 3);           // Retry on transient failures
        kafkaProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);         // Small delay to batch requests
        kafkaProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Ensure exactly-once delivery (if needed)

        return new DefaultKafkaProducerFactory<>(kafkaProps);
    }

    @Bean
    public KafkaTemplate<String, EmailRequest> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}

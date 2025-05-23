package com.bustanil.warehouse.infrastructure;

import com.bustanil.warehouse.domain.MeasurementReceived;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaConfiguration {
    
    @Bean
    public ReactiveKafkaProducerTemplate<String, MeasurementReceived> reactiveKafkaProducerTemplate(
            KafkaProperties properties) {
        
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }
}
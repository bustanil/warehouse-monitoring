package com.bustanil.monitoring.infrastructure;

import com.bustanil.shared.domain.MeasurementReceived;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Bean
    public ReactiveKafkaConsumerTemplate<String, MeasurementReceived> reactiveKafkaConsumerTemplate(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "central-monitoring");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        ReceiverOptions<String, MeasurementReceived> receiverOptions = ReceiverOptions
                .<String, MeasurementReceived>create(props)
                .subscription(List.of("sensor-measurements"));

        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);

    }

}

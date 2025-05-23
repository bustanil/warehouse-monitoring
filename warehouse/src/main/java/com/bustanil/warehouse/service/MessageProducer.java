package com.bustanil.warehouse.service;

import com.bustanil.shared.domain.MeasurementReceived;
import com.bustanil.shared.domain.SensorMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    private static final String TOPIC = "sensor-measurements";
    
    private final ReactiveKafkaProducerTemplate<String, MeasurementReceived> kafkaTemplate;
    
    public MessageProducer(ReactiveKafkaProducerTemplate<String, MeasurementReceived> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public Mono<Void> sendMeasurement(String warehouseId, SensorMeasurement sensorMeasurement) {
        MeasurementReceived event = new MeasurementReceived(warehouseId, sensorMeasurement);
        return kafkaTemplate
                .send(TOPIC, warehouseId, event)
                .doOnNext(result -> logger.debug("Sent measurement to Kafka: {}", event))
                .doOnError(error -> logger.error("Failed to send measurement to Kafka: {}", event, error))
                .then();
    }
}
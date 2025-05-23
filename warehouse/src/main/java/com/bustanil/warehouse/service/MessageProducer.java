package com.bustanil.warehouse.service;

import com.bustanil.shared.domain.MeasurementReceived;
import com.bustanil.shared.domain.SensorMeasurement;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

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
        RecordHeaders headers = new RecordHeaders();
        headers.add("trace-id", UUID.randomUUID().toString().getBytes());
        ProducerRecord<String, MeasurementReceived> record = new ProducerRecord<>(TOPIC, null, warehouseId, event, headers);
        return kafkaTemplate
                .send(record)
                .doOnNext(result -> logger.debug("Sent measurement to Kafka: {}", event))
                .doOnError(error -> logger.error("Failed to send measurement to Kafka: {}", event, error))
                .then();
    }
}
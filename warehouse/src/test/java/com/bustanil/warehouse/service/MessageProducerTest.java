package com.bustanil.warehouse.service;

import com.bustanil.shared.domain.MeasurementReceived;
import com.bustanil.shared.domain.SensorMeasurement;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageProducerTest {

    @Mock
    private ReactiveKafkaProducerTemplate<String, MeasurementReceived> kafkaTemplate;

    @Mock
    private SenderResult<Void> senderResult;

    @InjectMocks
    private MessageProducer messageProducer;

    @Test
    void sendMeasurement_Success_CompletesSuccessfully() {
        // Given
        var sensorMeasurement = new SensorMeasurement("t1", 25.5, SensorMeasurement.SensorType.TEMPERATURE);
        RecordHeaders headers = new RecordHeaders();
        headers.add("trace-id", "test".getBytes());
        when(kafkaTemplate.send((ProducerRecord<String, MeasurementReceived>) any()))
                .thenReturn(Mono.just(senderResult));

        // When
        Mono<Void> result = messageProducer.sendMeasurement("test-warehouse", sensorMeasurement);

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    void sendMeasurement_KafkaError_PropagatesError() {
        // Given
        var sensorMeasurement = new SensorMeasurement("t1", 25.5, SensorMeasurement.SensorType.TEMPERATURE);
        RuntimeException kafkaError = new RuntimeException("Kafka connection failed");

        when(kafkaTemplate.send((ProducerRecord<String, MeasurementReceived>) any()))
                .thenReturn(Mono.error(kafkaError));

        // When
        Mono<Void> result = messageProducer.sendMeasurement("test-warehouse", sensorMeasurement);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
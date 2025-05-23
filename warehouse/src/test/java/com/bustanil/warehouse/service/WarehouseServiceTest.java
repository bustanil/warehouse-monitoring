package com.bustanil.warehouse.service;

import com.bustanil.shared.domain.SensorMeasurement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    SensorParser sensorParser;
    @Mock
    MessageProducer messageProducer;
    @InjectMocks
    WarehouseService warehouseService;



    @Test
    public void shouldHandleMeasurementData() {
        // Given
        when(sensorParser.parse(eq("sensor_id=t1; value=40"), eq(SensorMeasurement.SensorType.TEMPERATURE)))
                .thenReturn(new SensorMeasurement("t1", 40, SensorMeasurement.SensorType.TEMPERATURE));
        when(messageProducer.sendMeasurement(any(), any())).thenReturn(Mono.empty());

        // When
        Function<String, Publisher<?>> tempFunction = warehouseService.collectMeasurement(SensorMeasurement.SensorType.TEMPERATURE);
        Publisher<?> result = tempFunction.apply("sensor_id=t1; value=40");

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void shouldHandleMeasurementError() {
        // Given
        when(sensorParser.parse(eq("test"), eq(SensorMeasurement.SensorType.TEMPERATURE)))
                .thenReturn(new SensorMeasurement("t1", 40, SensorMeasurement.SensorType.TEMPERATURE));
        when(messageProducer.sendMeasurement(any(), any())).thenReturn(Mono.error(new RuntimeException()));

        // When
        Function<String, Publisher<?>> tempFunction = warehouseService.collectMeasurement(SensorMeasurement.SensorType.TEMPERATURE);
        Publisher<?> result = tempFunction.apply("test");

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

}
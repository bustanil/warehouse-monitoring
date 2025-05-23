package com.bustanil.monitoring.service;

import com.bustanil.shared.domain.MeasurementReceived;
import com.bustanil.shared.domain.SensorMeasurement.SensorType;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;


@Service
public class CentralMonitoringService {

    @Value("${monitoring.sensors.temperature.threshold}")
    private Double temperatureThreshold;
    @Value("${monitoring.sensors.humidity.threshold}")
    private Double humidityThreshold;
    final ReactiveKafkaConsumerTemplate<String, MeasurementReceived> kafkaConsumerTemplate;

    public static final Logger logger = LoggerFactory.getLogger(CentralMonitoringService.class);

    public CentralMonitoringService(ReactiveKafkaConsumerTemplate<String, MeasurementReceived> kafkaConsumerTemplate) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
    }

    @PostConstruct
    public void startMonitoring() {
        kafkaConsumerTemplate
                .receiveAutoAck()
                .map(ConsumerRecord::value)
                .subscribe(this::processMeasurement,
                        t -> logger.error("Error occurred while monitoring", t)
                );
    }

    private void processMeasurement(MeasurementReceived measurementReceived) {
        logger.debug("Received measurement received: {}", measurementReceived);
        SensorType sensorType = measurementReceived.sensorMeasurement().getSensorType();
        switch (sensorType) {
            case HUMIDITY:
                checkThresholdAndAlarm(measurementReceived, humidityThreshold);
                break;
            case TEMPERATURE:
                checkThresholdAndAlarm(measurementReceived, temperatureThreshold);
                break;
            default:
                logger.error("Unknown sensor type: {}", sensorType);
                break;
        }
    }

    private void checkThresholdAndAlarm(MeasurementReceived measurementReceived, Double temperatureThreshold) {
        if (measurementReceived.sensorMeasurement().getValue() > temperatureThreshold) {
            logger.warn("⚠\uFE0F {} threshold exceeded at warehouse {}, {} > {}",
                    measurementReceived.sensorMeasurement().getSensorType().toString(),
                    measurementReceived.warehouseId(),
                    measurementReceived.sensorMeasurement().getValue(),
                    temperatureThreshold);
        }
    }
}

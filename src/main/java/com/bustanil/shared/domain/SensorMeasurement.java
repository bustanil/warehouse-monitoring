package com.bustanil.shared.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class SensorMeasurement {
    private final String sensorId;
    private final double value;
    private final SensorType sensorType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private final Instant timestamp;

    public SensorMeasurement(String sensorId, double value, SensorType sensorType) {
        this.sensorId = sensorId;
        this.value = value;
        this.sensorType = sensorType;
        this.timestamp = Instant.now();
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getValue() {
        return value;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public enum SensorType {HUMIDITY, TEMPERATURE }
}

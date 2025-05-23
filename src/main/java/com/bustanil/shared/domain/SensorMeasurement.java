package com.bustanil.shared.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class SensorMeasurement {
    private final String sensorId;
    private final double value;
    private final SensorType sensorType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private final Instant timestamp;

    public SensorMeasurement(String sensorId, double value, SensorType sensorType) {
        this(sensorId, value, sensorType, Instant.now());
    }

    @JsonCreator
    public SensorMeasurement(
            @JsonProperty("sensorId") String sensorId,
            @JsonProperty("value") double value,
            @JsonProperty("type") SensorType sensorType,
            @JsonProperty("timestamp") Instant timestamp) {
        this.sensorId = sensorId;
        this.value = value;
        this.sensorType = sensorType;
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "SensorMeasurement[" +
                "sensorId='" + sensorId + '\'' +
                ", value=" + value +
                ", sensorType=" + sensorType +
                ", timestamp=" + timestamp +
                ']';
    }

    public enum SensorType {HUMIDITY, TEMPERATURE }
}

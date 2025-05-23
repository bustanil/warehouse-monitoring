package com.bustanil.warehouse.model;

public class SensorMeasurement {
    private final String sensorId;
    private final double value;
    private final Type sensorType;

    public SensorMeasurement(String sensorId, double value, Type type) {
        this.sensorId = sensorId;
        this.value = value;
        this.sensorType = type;
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getValue() {
        return value;
    }

    public Type getSensorType() {
        return sensorType;
    }

    public enum Type { TEMPERATURE }
}

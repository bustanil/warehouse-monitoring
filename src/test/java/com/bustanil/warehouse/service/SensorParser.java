package com.bustanil.warehouse.service;

import com.bustanil.warehouse.model.SensorMeasurement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensorParser {

    private static final Pattern SENSOR_DATA_PATTERN = Pattern.compile("sensor_id=([^;]+);\\s*value=([\\d.]+)");

    public SensorMeasurement parse(String data, SensorMeasurement.Type type) {
        Matcher matcher = SENSOR_DATA_PATTERN.matcher(data);

        if (matcher.find()) {
            String sensorId = matcher.group(1);
            double value = Double.parseDouble(matcher.group(2));
            return new SensorMeasurement(sensorId, value, type);
        } else {
            throw new IllegalArgumentException("Sensor data is invalid");
        }
    }
}

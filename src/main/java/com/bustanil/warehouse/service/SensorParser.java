package com.bustanil.warehouse.service;

import com.bustanil.warehouse.domain.SensorMeasurement;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SensorParser {

    private static final Pattern SENSOR_DATA_PATTERN = Pattern.compile("sensor_id=([^;]+);\\s*value=([\\d.]+)");

    public SensorMeasurement parse(String data, SensorMeasurement.SensorType sensorType) {
        Matcher matcher = SENSOR_DATA_PATTERN.matcher(data);

        if (matcher.find()) {
            String sensorId = matcher.group(1);
            double value = Double.parseDouble(matcher.group(2));
            return new SensorMeasurement(sensorId, value, sensorType);
        } else {
            throw new IllegalArgumentException("Sensor data is invalid");
        }
    }
}

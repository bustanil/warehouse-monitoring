package com.bustanil.warehouse.service;

import com.bustanil.warehouse.domain.SensorMeasurement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

public class SensorParserTest {

    @Test
    public void shouldParseTemperatureData(){
        var parser = new SensorParser();
        var temperatureMeasurement = parser.parse("sensor_id=t1; value=30", SensorMeasurement.SensorType.TEMPERATURE);
        assertThat(temperatureMeasurement).isNotNull();
        assertThat(temperatureMeasurement.getSensorId()).isEqualTo("t1");
        assertThat(temperatureMeasurement.getValue()).isEqualTo(30.0);
        assertThat(temperatureMeasurement.getSensorType()).isEqualTo(SensorMeasurement.SensorType.TEMPERATURE);
    }

    @Test
    public void shouldParseHumidityData(){
        var parser = new SensorParser();
        var temperatureMeasurement = parser.parse("sensor_id=h1; value=50", SensorMeasurement.SensorType.HUMIDITY);
        assertThat(temperatureMeasurement).isNotNull();
        assertThat(temperatureMeasurement.getSensorId()).isEqualTo("h1");
        assertThat(temperatureMeasurement.getValue()).isEqualTo(50.0);
        assertThat(temperatureMeasurement.getSensorType()).isEqualTo(SensorMeasurement.SensorType.HUMIDITY);
    }

    @Test
    public void shouldThrowExceptionWhenDataIsInvalid(){
        var parser = new SensorParser();
        assertThatException().isThrownBy(() -> {
            parser.parse("sensor_id=t1;",  SensorMeasurement.SensorType.TEMPERATURE);
        });
    }

}

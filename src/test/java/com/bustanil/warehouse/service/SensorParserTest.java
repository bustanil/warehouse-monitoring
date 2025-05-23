package com.bustanil.warehouse.service;

import com.bustanil.warehouse.model.SensorMeasurement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

public class SensorParserTest {

    @Test
    public void shouldParseTemperatureData(){
        var parser = new SensorParser();
        var temperatureMeasurement = parser.parse("sensor_id=t1; value=30", SensorMeasurement.Type.TEMPERATURE);
        assertThat(temperatureMeasurement).isNotNull();
        assertThat(temperatureMeasurement.getSensorId()).isEqualTo("t1");
        assertThat(temperatureMeasurement.getValue()).isEqualTo(30.0);
        assertThat(temperatureMeasurement.getSensorType()).isEqualTo(SensorMeasurement.Type.TEMPERATURE);
    }

    @Test
    public void shouldThrowExceptionWhenDataIsInvalid(){
        var parser = new SensorParser();
        assertThatException().isThrownBy(() -> {
            parser.parse("sensor_id=t1;",  SensorMeasurement.Type.TEMPERATURE);
        });
    }

}

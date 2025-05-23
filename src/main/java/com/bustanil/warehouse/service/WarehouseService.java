package com.bustanil.warehouse.service;

import com.bustanil.warehouse.domain.SensorMeasurement;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WarehouseService {

    @Value("${warehouse.id}")
    private String warehouseId;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private SensorParser sensorParser;

    public static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    public Function<String, Publisher<?>> collectMeasurement(SensorMeasurement.SensorType sensorType){
        return (String data) -> {
            logger.debug("Collecting measurement for sensor type {}, data {}", sensorType, data);
            SensorMeasurement sensorMeasurement = sensorParser.parse(data, sensorType);
            return messageProducer.sendMeasurement(warehouseId, sensorMeasurement);
        };
    }

}

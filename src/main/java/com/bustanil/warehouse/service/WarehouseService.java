package com.bustanil.warehouse.service;

import com.bustanil.warehouse.domain.SensorMeasurement;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class WarehouseService {

    @Value("${warehouse.sensors.temperature.port}")
    private int temperaturePort;
    @Value("${warehouse.sensors.humidity.port}")
    private int humidityPort;
    @Value("${warehouse.id}")
    private String warehouseId;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private SensorParser sensorParser;

    private final List<Disposable> connections = new ArrayList<>();
    public static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    @PostConstruct
    public void start() {
        logger.info("Starting warehouse service");
        var tempConn = new UdpListener().listen(temperaturePort, collectMeasurement(SensorMeasurement.SensorType.TEMPERATURE));
        connections.add(tempConn);
        var humidityConn = new UdpListener().listen(humidityPort, collectMeasurement(SensorMeasurement.SensorType.HUMIDITY));
        connections.add(humidityConn);
    }

    public Function<String, Publisher<?>> collectMeasurement(SensorMeasurement.SensorType sensorType){
        return (String data) -> {
            logger.debug("Collecting measurement for sensor type {}, data {}", sensorType, data);
            SensorMeasurement sensorMeasurement = sensorParser.parse(data, sensorType);
            return messageProducer.sendMeasurement(warehouseId, sensorMeasurement);
        };
    }

    @PreDestroy
    public void stop() {
        logger.info("Stopping UDP listeners");
        for (Disposable connection : connections) {
           connection.dispose();
        }
    }

}

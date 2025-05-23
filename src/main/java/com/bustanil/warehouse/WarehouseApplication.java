package com.bustanil.warehouse;

import com.bustanil.shared.domain.SensorMeasurement;
import com.bustanil.warehouse.service.UdpListener;
import com.bustanil.warehouse.service.WarehouseService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class WarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);

        // Keep the application running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

@Component
class UDPServerStarter implements CommandLineRunner {

    @Value("${warehouse.sensors.temperature.port}")
    private int temperaturePort;
    @Value("${warehouse.sensors.humidity.port}")
    private int humidityPort;

    private final WarehouseService warehouseService;

    private final List<Disposable> connections = new ArrayList<>();

    public UDPServerStarter(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }


    @Override
    public void run(String... args) {
        var tempConn = new UdpListener().listen(temperaturePort, warehouseService.collectMeasurement(SensorMeasurement.SensorType.TEMPERATURE));
        connections.add(tempConn);
        var humidityConn = new UdpListener().listen(humidityPort, warehouseService.collectMeasurement(SensorMeasurement.SensorType.HUMIDITY));
        connections.add(humidityConn);
    }

    @PreDestroy
    public void cleanup() {
        for (var disposable : connections) {
            disposable.dispose();
        }
    }

}

package com.bustanil.warehouse;

import com.bustanil.warehouse.service.SensorParser;
import com.bustanil.warehouse.service.UdpListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import reactor.core.publisher.Flux;
import reactor.netty.udp.UdpServer;

import java.nio.charset.StandardCharsets;

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

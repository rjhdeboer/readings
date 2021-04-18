package org.example.readings.iotdevices;

import org.example.readings.iotdevices.domain.Car;
import org.example.readings.iotdevices.domain.HeartRateMonitor;
import org.example.readings.iotdevices.domain.IotDevice;
import org.example.readings.iotdevices.domain.Thermostat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class Application {

    private final List<IotDevice> devices = new ArrayList<>();

    @Value("${iot-gateway.endpoint.url}")
    private String iotGatewayEndpointUrl;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void registerIotDevices() {
        devices.add(new Car("Mom", "Suzuki"));
        devices.add(new Car("Dad", "Volkswagen"));
        devices.add(new Car("Brother", "Audi"));
        devices.add(new Thermostat());
        devices.add(new HeartRateMonitor());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner onStartup(RestTemplate restTemplate) throws Exception {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        return args -> {
            devices.forEach(device -> device.startEmittingReadings(scheduler, restTemplate, iotGatewayEndpointUrl));
        };
    }
}

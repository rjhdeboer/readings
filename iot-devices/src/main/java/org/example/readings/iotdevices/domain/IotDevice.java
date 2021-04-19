package org.example.readings.iotdevices.domain;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class IotDevice {

    private final String sensorId;

    IotDevice(String sensorId) {
        this.sensorId = sensorId;
    }

    abstract Reading takeReading();

    public void startEmittingReadings(ScheduledExecutorService scheduler,
                                      RestTemplate restTemplate,
                                      String iotGatewayEndpointUrl) {
        scheduler.scheduleAtFixedRate(() -> {
            restTemplate.postForObject(iotGatewayEndpointUrl, takeReading(), String.class);
        }, 0, 1, TimeUnit.SECONDS);
    }

    public String getSensorId() {
        return sensorId;
    }
}

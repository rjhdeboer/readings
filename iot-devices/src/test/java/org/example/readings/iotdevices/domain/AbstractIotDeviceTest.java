package org.example.readings.iotdevices.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

public abstract class AbstractIotDeviceTest {

    private static final String IOT_GATEWAY_ENDPOINT_URL = "http://iot-gateway:8080/readings";

    private IotDevice device;
    @Mock
    private ScheduledExecutorService scheduler;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        device = createIotDevice();
        MockitoAnnotations.openMocks(this);
    }

    abstract IotDevice createIotDevice();

    @Test
    public void shouldStartEmittingReadings() {
        device.startEmittingReadings(scheduler, restTemplate, IOT_GATEWAY_ENDPOINT_URL);

        ArgumentCaptor<Runnable> commandCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).scheduleAtFixedRate(commandCaptor.capture(), eq(0L), eq(1L), eq(TimeUnit.SECONDS));
        Runnable command = commandCaptor.getValue();
        command.run();
        verify(restTemplate).postForObject(eq(IOT_GATEWAY_ENDPOINT_URL), isA(Reading.class), eq(String.class));
    }
}

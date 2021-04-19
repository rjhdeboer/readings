package org.example.readings.iotdevices.domain;

public class HeartRateMonitorTest extends AbstractIotDeviceTest {

    @Override
    IotDevice createIotDevice() {
        return new HeartRateMonitor("heart-rate_1");
    }
}

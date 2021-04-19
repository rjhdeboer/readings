package org.example.readings.iotdevices.domain;

public class ThermostatTest extends AbstractIotDeviceTest {

    @Override
    IotDevice createIotDevice() {
        return new Thermostat("thermostat_1");
    }
}

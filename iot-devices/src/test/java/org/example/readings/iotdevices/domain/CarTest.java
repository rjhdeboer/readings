package org.example.readings.iotdevices.domain;

public class CarTest extends AbstractIotDeviceTest {

    @Override
    IotDevice createIotDevice() {
        return new Car("car_mom", "Mom", "Suzuki");
    }
}

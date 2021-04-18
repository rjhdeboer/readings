package org.example.readings.iotgateway.domain.processor;

public interface CommandProcessor<T> {

    void process(T command);
}

package org.example.readings.statistics.domain.processor;

public interface QueryProcessor<Q, R> {

    R handle(Q query);
}

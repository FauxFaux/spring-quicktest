package com.goeswhere.quicktest.one.peek;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.camel.Exchange;

import java.util.Map;

public class Peeker {
    static final String PEEKER_PROPERTY = "Peeker";

    private final PeekGenerator generator;
    private final Map<String, String> values;

    Peeker(PeekGenerator generator, Map<String, String> values) {
        this.generator = generator;
        this.values = Maps.newHashMap(values);
    }

    public Peeker plus(String key, Object value) {
        return new Peeker(generator, ImmutableMap.<String, String>builder()
                .putAll(values)
                .put(key, String.valueOf(value))
                .build());
    }

    public void saveAll(Map<String, String> values) {
        this.values.putAll(values);
    }

    public void emit(String key) {
        generator.emit(key, plus("event_id", key).plus("time", System.currentTimeMillis()).toMap());
    }

    private Map<String, String> toMap() {
        return ImmutableMap.copyOf(values);
    }

    public static Peeker fromExchange(Exchange exchange) {
        return Preconditions.checkNotNull(exchange.getProperty(PEEKER_PROPERTY, Peeker.class));
    }
}

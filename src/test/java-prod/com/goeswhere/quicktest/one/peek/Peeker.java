package com.goeswhere.quicktest.one.peek;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import java.util.Map;

public class Peeker {
    static final String PEEKER_PROPERTY = "Peeker";

    private final ProducerTemplate template;
    private final Map<String, String> values;

    Peeker(ProducerTemplate template, Map<String, String> values) {
        this.template = template;
        this.values = Maps.newHashMap(values);
    }

    public Peeker plus(String key, Object value) {
        return new Peeker(template, ImmutableMap.<String, String>builder()
                .putAll(values)
                .put(key, String.valueOf(value))
                .build());
    }

    public void saveAll(Map<String, String> values) {
        values.putAll(values);
    }

    public void emit(String key) {
        template.sendBody(plus("event_id", key).plus("time", System.currentTimeMillis()));
    }

    public static Peeker fromExchange(Exchange exchange) {
        return Preconditions.checkNotNull(exchange.getProperty(PEEKER_PROPERTY, Peeker.class));
    }
}

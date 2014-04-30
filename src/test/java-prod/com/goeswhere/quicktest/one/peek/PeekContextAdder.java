package com.goeswhere.quicktest.one.peek;

import com.google.common.collect.ImmutableMap;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.UUID;

public class PeekContextAdder implements Processor {
    private final PeekGenerator peekGenerator;

    public PeekContextAdder(PeekGenerator peekGenerator) {
        this.peekGenerator = peekGenerator;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.setProperty(Peeker.PEEKER_PROPERTY,
                new Peeker(peekGenerator, ImmutableMap.of("exchange_id", UUID.randomUUID().toString())));
    }
}

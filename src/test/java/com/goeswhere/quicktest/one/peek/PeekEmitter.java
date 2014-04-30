package com.goeswhere.quicktest.one.peek;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PeekEmitter implements Processor {
    private final String key;

    public PeekEmitter(String key) {
        this.key = key;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Peeker.fromExchange(exchange).emit(key);
    }
}

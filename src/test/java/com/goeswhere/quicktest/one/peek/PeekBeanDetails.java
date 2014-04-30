package com.goeswhere.quicktest.one.peek;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PeekBeanDetails<T extends Peekabean> implements Processor {
    private final Class<T> clazz;

    public PeekBeanDetails(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Peeker.fromExchange(exchange).saveAll(exchange.getIn().getBody(clazz).toPeekMap());
    }
}

package com.goeswhere.quicktest.one.peek;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PeekGenerator {
    private final ProducerTemplate template;

    @Autowired
    public PeekGenerator(@Value("${endpoint_peek}") String peek, ProducerTemplate template, CamelContext camelContext) {
        template.setDefaultEndpoint(camelContext.getEndpoint(peek));
        this.template = template;
    }

    public Peeker generate() {
        return new Peeker(template, Collections.<String, String>emptyMap());
    }
}

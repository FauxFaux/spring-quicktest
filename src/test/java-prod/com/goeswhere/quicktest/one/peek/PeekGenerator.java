package com.goeswhere.quicktest.one.peek;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PeekGenerator {
    private final String peek;
    private final ProducerTemplate template;

    @Autowired
    public PeekGenerator(@Value("${endpoint_peek}") String peek, ProducerTemplate template) {
        this.peek = peek;
        this.template = template;
    }

    public void emit(String key, Map<String, String> values) {
        template.sendBody(peek, values);
    }
}

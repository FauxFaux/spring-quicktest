package com.goeswhere.quicktest.one.service;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

@Component
public class ThereIsSomethingToIngest implements Predicate {
    @Override
    public boolean matches(Exchange exchange) {
        return false;
    }
}

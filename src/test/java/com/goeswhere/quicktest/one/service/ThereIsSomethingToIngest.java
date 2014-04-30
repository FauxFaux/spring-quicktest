package com.goeswhere.quicktest.one.service;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

public class ThereIsSomethingToIngest implements Predicate {
    @Override
    public boolean matches(Exchange exchange) {
        return false;
    }
}

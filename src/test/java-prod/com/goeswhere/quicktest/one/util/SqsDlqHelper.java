package com.goeswhere.quicktest.one.util;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.component.aws.sqs.SqsConstants;

import java.util.Collections;
import java.util.Map;

public class SqsDlqHelper implements Predicate {
    private final int rollbacksBeforeDlq;

    public SqsDlqHelper(int rollbacksBeforeDlq) {
        this.rollbacksBeforeDlq = rollbacksBeforeDlq;
    }

    @Override
    public boolean matches(Exchange exchange) {
        final Map<?, ?> attributes = exchange.getIn().getHeader(SqsConstants.ATTRIBUTES, Collections.emptyMap(), Map.class);
        final Object attr = attributes.get("ApproximateReceiveCount");
        return null != attr && Integer.parseInt(String.valueOf(attr)) > rollbacksBeforeDlq;
    }
}

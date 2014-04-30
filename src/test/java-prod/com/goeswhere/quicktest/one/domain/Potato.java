package com.goeswhere.quicktest.one.domain;

import com.goeswhere.quicktest.one.peek.Peekabean;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Potato extends Peekabean {
    public String colour;

    @Override
    public Map<String, String> toPeekMap() {
        return ImmutableMap.<String, String>builder()
                .put("colour", this.colour)
                .build();
    }
}

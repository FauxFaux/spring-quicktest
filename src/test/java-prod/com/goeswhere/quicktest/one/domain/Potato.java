package com.goeswhere.quicktest.one.domain;

import com.goeswhere.quicktest.one.peek.Peekabean;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

public class Potato extends Peekabean {
    public String colour;

    @Override
    public Map<String, String> toPeekMap() {
        return ImmutableMap.<String, String>builder()
                .put("colour", this.colour)
                .build();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

}

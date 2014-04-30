package com.goeswhere.quicktest.one.service;

import com.goeswhere.quicktest.one.domain.Potato;
import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
public class PrepareForChipper {
    @Handler
    public void handle(@Body Potato potato) {
    }
}

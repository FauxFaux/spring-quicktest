package com.goeswhere.quicktest.one.route;

import com.goeswhere.quicktest.one.domain.Potato;
import com.goeswhere.quicktest.one.peek.*;
import com.goeswhere.quicktest.one.service.EvaluateTastiness;
import com.goeswhere.quicktest.one.service.FetchGenus;
import com.goeswhere.quicktest.one.service.PrepareForChipper;
import com.goeswhere.quicktest.one.service.ThereIsSomethingToIngest;
import com.goeswhere.quicktest.one.util.SqsDlqHelper;
import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChippingRoute extends RouteBuilder {
    private final Endpoint input;
    private final Endpoint inputDlq;
    private final Endpoint chipper;
    private final PeekGenerator peek;
    private final int rollbacksBeforeDlq;
    private final FetchGenus fetchGenus;
    private final EvaluateTastiness evaluateTastiness;
    private final PrepareForChipper prepareForChipper;

    @Autowired
    public ChippingRoute(@Value("${endpoint_one}") Endpoint input,
                         @Value("${endpoint_one_dlq}") Endpoint inputDlq,
                         @Value("${endpoint_chipper}") Endpoint chipper,
                         PeekGenerator peek,
                         @Value("${rollbacks}") int rollbacksBeforeDlq,
                         FetchGenus fetchGenus,
                         EvaluateTastiness evaluateTastiness,
                         PrepareForChipper prepareForChipper) {
        this.input = input;
        this.inputDlq = inputDlq;
        this.chipper = chipper;
        this.peek = peek;
        this.rollbacksBeforeDlq = rollbacksBeforeDlq;
        this.fetchGenus = fetchGenus;
        this.evaluateTastiness = evaluateTastiness;
        this.prepareForChipper = prepareForChipper;
    }

    @Override
    public void configure() throws Exception {
        onException(Throwable.class)
            .handled(false)
            .process(new PeekException("one.chipping.exception"));

        from(this.input)
            .process(new PeekContextAdder(this.peek))
            .choice().when(new SqsDlqHelper(this.rollbacksBeforeDlq)).to(this.inputDlq).stop().end()
            .unmarshal().json(JsonLibrary.Gson)
            .process(new PeekBeanDetails<>(Potato.class))
            .process(new PeekEmitter("one.chipping.message.received"))
            .bean(this.fetchGenus)
            .bean(this.evaluateTastiness)
            .choice()
                .when(new ThereIsSomethingToIngest())
                    .process(new PeekEmitter("one.chipping.ingest.required"))
                    .bean(this.prepareForChipper)
                    .to(this.chipper)
                    .process(new PeekEmitter("one.chipping.ingest.request.sent"))
                .otherwise()
                    .process(new PeekEmitter("one.chipping.ingest.nothing.to.do"))
            .end();
    }

}

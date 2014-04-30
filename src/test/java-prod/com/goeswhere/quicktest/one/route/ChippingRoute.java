package com.goeswhere.quicktest.one.route;

import com.goeswhere.quicktest.one.domain.Potato;
import com.goeswhere.quicktest.one.peek.*;
import com.goeswhere.quicktest.one.service.EvaluateTastiness;
import com.goeswhere.quicktest.one.service.FetchGenus;
import com.goeswhere.quicktest.one.service.PrepareForChipper;
import com.goeswhere.quicktest.one.service.ThereIsSomethingToIngest;
import com.goeswhere.quicktest.one.util.SqsDlqHelper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChippingRoute extends RouteBuilder {
    private final String endpointInput;
    private final String endpointInputDlq;
    private final String endpointChipper;
    private final PeekGenerator peek;
    private final int rollbacksBeforeDlq;
    private final FetchGenus fetchGenus;
    private final EvaluateTastiness evaluateTastiness;
    private final PrepareForChipper prepareForChipper;
    private final ThereIsSomethingToIngest thereIsSomethingToIngest;

    @Autowired
    public ChippingRoute(@Value("${endpoint_one}") String input,
                         @Value("${endpoint_one_dlq}") String inputDlq,
                         @Value("${endpoint_chipper}") String chipper,
                         PeekGenerator peek,
                         @Value("${rollbacks}") int rollbacksBeforeDlq,
                         FetchGenus fetchGenus,
                         EvaluateTastiness evaluateTastiness,
                         PrepareForChipper prepareForChipper,
                         ThereIsSomethingToIngest thereIsSomethingToIngest) {
        this.endpointInput = input;
        this.endpointInputDlq = inputDlq;
        this.endpointChipper = chipper;
        this.peek = peek;
        this.rollbacksBeforeDlq = rollbacksBeforeDlq;
        this.fetchGenus = fetchGenus;
        this.evaluateTastiness = evaluateTastiness;
        this.prepareForChipper = prepareForChipper;
        this.thereIsSomethingToIngest = thereIsSomethingToIngest;
    }

    @Override
    public void configure() throws Exception {
        onException(Throwable.class)
            .handled(false)
            .process(new PeekException("one.chipping.exception"));

        from(this.endpointInput)
            .process(new PeekContextAdder(this.peek))
            .choice().when(new SqsDlqHelper(this.rollbacksBeforeDlq)).to(this.endpointInputDlq).stop().end()
            .unmarshal(new GsonDataFormat(Potato.class))
            .process(new PeekBeanDetails<>(Potato.class))
            .process(new PeekEmitter("one.chipping.message.received"))
            .bean(this.fetchGenus)
            .bean(this.evaluateTastiness)
            .choice()
                .when(thereIsSomethingToIngest)
                    .process(new PeekEmitter("one.chipping.ingest.required"))
                    .bean(this.prepareForChipper)
                    .to(this.endpointChipper)
                    .process(new PeekEmitter("one.chipping.ingest.request.sent"))
                .otherwise()
                    .process(new PeekEmitter("one.chipping.ingest.nothing.to.do"))
            .end();
    }
}

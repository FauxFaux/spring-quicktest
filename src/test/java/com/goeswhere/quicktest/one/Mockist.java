package com.goeswhere.quicktest.one;

import com.goeswhere.quicktest.one.peek.PeekGenerator;
import com.goeswhere.quicktest.one.route.ChippingRoute;
import com.goeswhere.quicktest.one.service.EvaluateTastiness;
import com.goeswhere.quicktest.one.service.FetchGenus;
import com.goeswhere.quicktest.one.service.PrepareForChipper;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class Mockist extends CamelTestSupport {
    @Produce(uri = "direct:input") private ProducerTemplate producer;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PeekGenerator peek;
    @Mock private FetchGenus fetchGenus;
    @Mock private EvaluateTastiness evaluateTastiness;
    @Mock private PrepareForChipper prepareForChipper;

    @Override
    public RouteBuilder createRouteBuilder() {
        return new ChippingRoute(
                "direct:input",
                "mock:input-dlq",
                "mock:chipper",
                peek,
                3,
                fetchGenus,
                evaluateTastiness,
                prepareForChipper);
    }

    @Test
    public void test() {
        producer.sendBody("{\"colour\": \"red\"}");
        verifyNoMoreInteractions(peek);
    }
}

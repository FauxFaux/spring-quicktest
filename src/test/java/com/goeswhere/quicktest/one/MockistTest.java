package com.goeswhere.quicktest.one;

import com.goeswhere.quicktest.one.domain.Potato;
import com.goeswhere.quicktest.one.peek.PeekGenerator;
import com.goeswhere.quicktest.one.route.ChippingRoute;
import com.goeswhere.quicktest.one.service.EvaluateTastiness;
import com.goeswhere.quicktest.one.service.FetchGenus;
import com.goeswhere.quicktest.one.service.PrepareForChipper;
import com.goeswhere.quicktest.one.service.ThereIsSomethingToIngest;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockistTest extends CamelTestSupport {
    @Produce(uri = "direct:input") private ProducerTemplate producer;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private PeekGenerator peek;
    @Mock private FetchGenus fetchGenus;
    @Mock private EvaluateTastiness evaluateTastiness;
    @Mock private PrepareForChipper prepareForChipper;
    @Mock private ThereIsSomethingToIngest thereIsSomethingToIngest;

    @EndpointInject(uri = "mock:chipper") private MockEndpoint chipper;

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
                prepareForChipper,
                thereIsSomethingToIngest);
    }

    @Test
    public void happy() {
        Given:
        when(thereIsSomethingToIngest.matches(any(Exchange.class))).thenReturn(true);

        When:
        producer.sendBody("{\"colour\": \"red\"}");

        final InOrder o = inOrder(peek, fetchGenus, evaluateTastiness, thereIsSomethingToIngest, prepareForChipper);
        Then:
        o.verify(peek).emit(eq("one.chipping.message.received"), anyMapOf(String.class, String.class));
        o.verify(fetchGenus).handle(eq(redPotato()), any(Exchange.class));
        o.verify(evaluateTastiness).handle(eq(redPotato()), any(Exchange.class));
        o.verify(thereIsSomethingToIngest).matches(any(Exchange.class));
        o.verify(peek).emit(eq("one.chipping.ingest.required"), anyMapOf(String.class, String.class));
        o.verify(peek).emit(eq("one.chipping.ingest.request.sent"), anyMapOf(String.class, String.class));
        o.verifyNoMoreInteractions();
        chipper.expectedMessageCount(1);
    }

    private Potato redPotato() {
        final Potato potato = new Potato();
        potato.colour = "red";
        return potato;
    }
}

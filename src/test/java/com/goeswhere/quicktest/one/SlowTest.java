package com.goeswhere.quicktest.one;

import com.goeswhere.quicktest.one.route.ChippingRoute;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/config/one.xml")
public class SlowTest {
    @Autowired
    ChippingRoute route;

    @Test
    public void testStarts() {
        assertNotNull(route);
    }

}

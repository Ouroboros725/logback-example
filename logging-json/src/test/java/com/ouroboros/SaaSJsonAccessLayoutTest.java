package com.ouroboros;

import ch.qos.logback.access.spi.IAccessEvent;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Created by zhanxies on 7/5/2017.
 *
 */
public class SaaSJsonAccessLayoutTest {

    @Test
    public void test() {
        SaaSJsonAccessLayout layout = new SaaSJsonAccessLayout();
        layout.setServiceName("345");

        Map map = layout.toJsonMap(Mockito.mock(IAccessEvent.class));

        Assert.assertEquals("345", layout.getServiceName());
        Assert.assertEquals("345", map.get("service"));
    }
}

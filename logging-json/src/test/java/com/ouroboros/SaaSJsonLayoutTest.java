package com.ouroboros;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanxies on 7/5/2017.
 *
 */
public class SaaSJsonLayoutTest {

    @Test
    public void test() {
        SaaSJsonLayout layout = new SaaSJsonLayout();
        layout.setLoggerType("123");
        layout.setServiceName("345");

        Map<String, Object> map = new HashMap<>();
        layout.addCustomDataToJsonMap(map, null);

        Assert.assertEquals("345", layout.getServiceName());
        Assert.assertEquals("123", layout.getLoggerType());

        Assert.assertEquals("345", map.get("service"));
        Assert.assertEquals("123", map.get("loggerType"));
    }

    @Test
    public void test_addMap() {
        SaaSJsonLayout layout = new SaaSJsonLayout();

        Map<String, Object> map = new HashMap<>();
        map.put("123", "123");
        map.put("234", "234");

        Map<String, Object> aMap = new HashMap<>();

        layout.addMap("", true, map, aMap);

        Assert.assertTrue(aMap.containsKey("123") && "123".equals(aMap.get("123")));
        Assert.assertTrue(aMap.containsKey("234") && "234".equals(aMap.get("234")));
    }
}

package org.ei.telemedicine.test.domain;

import junit.framework.Assert;

import org.ei.telemedicine.domain.ServiceProvided;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 1/12/16.
 */
public class ServiceProvidedTest {
    ServiceProvided serviceProvided;
    Map<String, String> map;
    //map.put("sd","sdf");

    @Before
    public void setUp(){
        map = new HashMap<String, String>();
        serviceProvided = new ServiceProvided("145", "anm", "11/01/2016", map);
    }
    @Test
    public void entityIdTest(){
        String s = serviceProvided.entityId();
        Assert.assertEquals(s, "145");
    }
    @Test
    public void nameTest(){
        String s = serviceProvided.name();
        Assert.assertEquals(s, "anm");
    }
    @Test
    public void dateTest(){
        String s = serviceProvided.date();
        Assert.assertEquals(s, "11/01/2016");
    }

}

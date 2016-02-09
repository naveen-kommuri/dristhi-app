package org.ei.telemedicine.test.domain;

/**
 * Created by administrator on 1/5/16.
 */

import junit.framework.Assert;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.domain.Child;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ChildTest {
    Child child;
    Map<String, String> map;
    private boolean closed;
    //HashCodeBuilder hashCodeBuilder;
    @Before
    public void setUp(){
        map = new HashMap<String, String>();
        map.put(AllConstants.ChildRegistrationFields.IS_CHILD_HIGH_RISK,AllConstants.BOOLEAN_TRUE);
        map.put(AllConstants.ChildRegistrationFields.HIGH_RISK_REASON,"SampleRisk");
        child = new Child("856", "145", "123", "18/07/2015", "gender", map);
                //hashCodeBuilder = new HashCodeBuilder();
     }
/*

    @Test
    public void stingTest() {
        map.put(AllConstants.ChildRegistrationFields.IS_CHILD_HIGH_RISK,AllConstants.BOOLEAN_TRUE);
        map.put(AllConstants.ChildRegistrationFields.HIGH_RISK_REASON,"Sample Risk");
        child = new Child("caseId", "motherCaseId", "thayiCardNumber", "dateOfBirth", "gender", map);
        assertEquals(AllConstants.BOOLEAN_TRUE,child.isHighRisk());
    }
*/

    @Test
    public void caseIdTest(){
        String s = child.caseId();
        Assert.assertEquals("856", s);
    }

    @Test
    public void caseIdTesst(){
        String s = "sdf";
        Assert.assertEquals("SampleRisk", child.highRiskReason());
    }


    /*@Test
    public void isClosed(){
        boolean b = child.isClosed();
    }*/

    @Test
    public void compsletionDateTest(){
        closed = child.isClosed();
        boolean s3 = closed;
        Assert.assertEquals(s3, closed);
    }
}

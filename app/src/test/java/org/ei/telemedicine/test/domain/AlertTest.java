package org.ei.telemedicine.test.domain;

import org.ei.telemedicine.domain.Alert;
//import org.ei.telemedicine.view.contract.AlertStatus;
import org.ei.telemedicine.dto.AlertStatus;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by naveen on 12/24/15.
 */
public class AlertTest {
    Alert alert;
    AlertStatus alertStatus;
    private boolean complete;

    @Before
    public void setUp(){
        alert = new Alert("123", "anm1", "454", alertStatus, "12/08/2015", "18/09/2015");
    }

    @Test
    public void caseIdTest(){
        String s = alert.caseId();
        assertEquals("123",s);
    }


    @Test
    public void expiryDateTest(){
        String s1 = alert.expiryDate();
        assertEquals("18/09/2015",s1);
    }
    @Test
    public void scheduleDateTest(){
        String s2 = alert.scheduleName();
        assertEquals("anm1",s2);
    }
    @Test
    public void completionDateTest(){
        Alert s3 = alert.withCompletionDate("25/10/2016");
        String str=s3.completionDate();
        assertEquals("25/10/2016",str);
    }


    @Test
    public void compsletionDateTest(){
        complete = alert.isComplete();
        boolean s3 = complete;
        assertEquals(s3,complete);
    }
}

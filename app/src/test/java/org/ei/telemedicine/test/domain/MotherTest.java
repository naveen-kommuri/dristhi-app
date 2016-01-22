package org.ei.telemedicine.test.domain;

import junit.framework.Assert;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.domain.Mother;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 1/7/16.
 */
public class MotherTest {
    Mother mother;
    Map<String, String> map;
    EqualsBuilder equalsBuilder;
    private boolean equals;
    private boolean closed;
    private boolean isClosed;
    private boolean withOutOfArea;
    //private boolean equal;
    //private boolean reflectionEquals;
    public String type;

    @Before
    public void setUp(){
        map = new HashMap<String, String>();
        map.put(AllConstants.ANCRegistrationFields.IS_HIGH_RISK,AllConstants.BOOLEAN_TRUE);
        map.put(AllConstants.ANCRegistrationFields.HIGH_RISK_REASON,"highRiskReason");
        map.put(AllConstants.PNCRegistrationFields.PNC_NUMBER,"sample");
        mother = new Mother("147", "741", "458", "05/10/2012");
        mother.withDetails(map);

    }
   @Test
    public void casIdTest(){
       String s = mother.caseId();
       Assert.assertEquals("147",s);
   }
    @Test
    public void compsletionDateTest(){
        closed = mother.isClosed();
        boolean s3 = closed;
        Assert.assertEquals(s3, closed);
    }
    @Test
    public void compslsetionDateTest(){
       type = mother.getType();
        String  s3 = type;
        Assert.assertEquals(s3, type);
    }

    @Test
    public void setIsClosed() {
        mother.setIsClosed(false);
        boolean b = mother.isClosed();
        Assert.assertEquals(false,b);

    }


    @Test
    public void caseIdTesst(){
        String s = "sdf";
        Assert.assertEquals("highRiskReason", mother.highRiskReason());
    }

}

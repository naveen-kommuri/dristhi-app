package org.ei.telemedicine.test.domain;

import junit.framework.Assert;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.domain.EligibleCouple;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EligibleCoupleTest {
    //EqualsBuilder equalsBuilder;
    EligibleCouple eligibleCouple;
    Map<String, String> map;
    EqualsBuilder equalsBuilder;

    private boolean equals;
    private boolean closed;
    private boolean isClosed;
    private boolean withOutOfArea;
    //private boolean equal;
    //private boolean reflectionEquals;


    @Before
    public void setUp()
    {
        map = new HashMap<String, String>();
//        map.put(AllConstants.SPACE,AllConstants.SPACE);
        map.put(AllConstants.ECRegistrationFields.HIGH_PRIORITY_REASON,"sampletest");
        map.put(AllConstants.ECRegistrationFields.IS_HIGH_PRIORITY,AllConstants.BOOLEAN_TRUE);
        eligibleCouple = new EligibleCouple("145", "sharvani", "sudheer", "1", "Yerragondapalem", "Mrkp", map);

            }

    @Test
    public void caseIdTest(){
        String s = eligibleCouple.caseId();
        Assert.assertEquals("145", s);
    }

    @Test
    public void compsletionDateTest(){
        closed = eligibleCouple.isClosed();
        boolean s3 = closed;
        Assert.assertEquals(s3, closed);
    }
    @Test
    public void setIsClosed() {
        eligibleCouple.setIsClosed(false);
        boolean b = eligibleCouple.isClosed();
        Assert.assertEquals(false,b);

    }

    @Test
    public void caseIdTesst(){
        String s = "sdf";
        Assert.assertEquals("sampletest", eligibleCouple.highPriorityReason());
    }


}

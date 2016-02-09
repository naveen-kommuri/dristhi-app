package org.ei.telemedicine.test.domain;

import junit.framework.Assert;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.ei.telemedicine.domain.TestDomain;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 1/8/16.
 */
public class TestDomainTest {
    TestDomain testDomain;
    //EqualsBuilder equalsBuilder;
    Map<String, String> map;
    Map<String, String> details;
    EqualsBuilder equalsBuilder;

    private boolean equals;
    private boolean closed;
    private boolean isClosed;
    private boolean withOutOfArea;
    public String setCaseID;
    public String caseId;
    public String testName;
    //private boolean equal;
    //private boolean reflectionEquals;
    @Before
    public void setUp(){
        map = new HashMap<String, String>();
//        map.put(AllConstants.SPACE,AllConstants.SPACE);
        //map.put(AllConstants.ECRegistrationFields.HIGH_PRIORITY_REASON,"sampletest");
        //map.put(AllConstants.ECRegistrationFields.IS_HIGH_PRIORITY,AllConstants.BOOLEAN_TRUE);

        testDomain = new TestDomain("caseId", "testName", "testAge",map);
        map.put("sd","sdf");
        testDomain.setDetails(map);
        testDomain.getDetails();
        testDomain.setCaseId("caeID");

    }
    @Test
    public void caseIdTest(){
        String s = testDomain.getCaseId();

            }
    @Test
    public void setIsClosed() {
        testDomain.setCaseId("stg");
        String s = testDomain.getCaseId();
        Assert.assertEquals(s,"stg");

    }

    @Test
    public void setIsClossed() {
        testDomain.setTestName("stg");
        String s = testDomain.getTestName();
        Assert.assertEquals(s,"stg");

    }

    @Test
    public void setIssClossed() {
        testDomain.setTestAge("24");
        String s = testDomain.getTestAge();
        Assert.assertEquals(s,"24");
    }


    @Test
    public void setIssCslossed() {
        //testDomain.getDetail("sudheer");
        String s = testDomain.getDetail("sd");
        Assert.assertEquals(s,"sdf");
    }

//    @Test
//    public void setIsssClossed() {
//        testDomain.setDetails(map);
//        String s = testDomain.getDetail("s");
//        Assert.assertEquals(s,"s");
//    }
}

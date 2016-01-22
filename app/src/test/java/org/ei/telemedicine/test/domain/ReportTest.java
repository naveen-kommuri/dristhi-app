package org.ei.telemedicine.test.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.ei.telemedicine.domain.Report;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by administrator on 1/11/16.
 */
public class ReportTest {
Report report;
    private boolean equals;
    EqualsBuilder equalsBuilder;
    HashCodeBuilder hashCodeBuilder;
    ToStringBuilder toStringBuilder;
    @Before
    public void setUp(){
        report = new Report("sdh", "annualTarget", "monthlySummaries");
    }
    @Test
    public void indicatorTest(){
        String s = report.indicator();
        assertEquals(s,"sdh");
            }
    @Test
    public void monthlySummariesTest(){
        String s = report.monthlySummariesJSON();
        assertEquals(s, "monthlySummaries");

    }
}

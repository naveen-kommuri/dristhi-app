package org.ei.telemedicine.test.domain;

import org.ei.telemedicine.domain.ReportIndicator;
import org.ei.telemedicine.view.controller.ProfileNavigationController;
import org.junit.Test;
//import android.content.Context;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.ei.telemedicine.domain.ReportIndicator.*;
import static org.ei.telemedicine.domain.ReportIndicator.parseToReportIndicator;


public class ReportIndicatorTest {
    ReportIndicator reportIndicator;
    List<String> stringList;
    String caseID;
    ProfileNavigationController navigationController;
    private ReportIndicatorTest startCaseDetailActivity;
    private ReportIndicatorTest navigateToECProfile;
    @Test
    public void shouldParseStringToReportIndicator() throws Exception {
        assertEquals(CONDOM, parseToReportIndicator("condom"));
    }

    @Test
    public void shouldParseStringToReportIudTest() throws Exception {
        assertEquals(IUD, parseToReportIndicator("iud"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenValueIsNotAValidReportIndicator() throws Exception {
        parseToReportIndicator("invalid value");
    }
}

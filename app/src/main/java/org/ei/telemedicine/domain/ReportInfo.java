package org.ei.telemedicine.domain;

/**
 * Created by naveen on 1/6/16.
 */
public class ReportInfo {
    private final String indicator;
    private final String annualTarget;
    private final String monthlySummaries;
    private final String totalTarget;

    public ReportInfo(String indicator, String annualTarget, String monthlySummaries, String totalTarget) {
        this.indicator = indicator;
        this.annualTarget = annualTarget;
        this.monthlySummaries = monthlySummaries;
        this.totalTarget = totalTarget;
    }


    public String getIndicator() {
        return indicator;
    }

    public String getAnnualTarget() {
        return annualTarget;
    }

    public String getMonthlySummaries() {
        return monthlySummaries;
    }

    public String getTotalTarget() {
        return totalTarget;
    }
}

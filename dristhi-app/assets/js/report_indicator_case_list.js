function ReportIndicatorCaseList(reportIndicatorCaseListBridge, cssIdOf) {
    return {
        populateInto: function () {
            $(cssIdOf.rootElement).html(Handlebars.templates.report_indicator_case_list(reportIndicatorCaseListBridge.getReportIndicatorCaseList()));
        },
        bindEveryItemToIndicatorCaseDetailView: function () {
            $(cssIdOf.indicator).click(function () {
                reportIndicatorCaseListBridge.delegateToReportIndicatorCaseDetail($(this).data("caseId"));
            });
        }
    };
}

function ReportIndicatorCaseListBridge() {
    var reportIndicatorListContext = window.context;
    if (typeof reportIndicatorListContext === "undefined" && typeof ReportIndicatorListContext !== "undefined") {
        reportIndicatorListContext = new ReportIndicatorListContext();
    }

    return {
        getReportIndicatorCaseList: function () {
            return JSON.parse(reportIndicatorListContext.get());
        },
        delegateToReportIndicatorCaseList: function (caseId) {
            return reportIndicatorListContext.startReportIndicatorCaseDetail(caseId);
        }
    };
}

function ReportIndicatorListContext() {
    return {
        get: function () {
            return JSON.stringify({
                    month: "5",
                    beneficiaries: [
                        {
                            caseId: "12345",
                            womanName: "Wife 1",
                            husbandName: "Husband 1",
                            thaayiCardNumber: "TC Number 1",
                            ecNumber: "EC 1",
                            villageName: "chikkabheriya",
                            isHighRisk: true
                        },
                        {
                            caseId: "11111",
                            womanName: "Wife 2",
                            husbandName: "Husband 2",
                            thaayiCardNumber: "TC Number 2",
                            ecNumber: "EC 2",
                            villageName: "munjanahalli",
                            isHighRisk: true
                        }
                    ]
                }
            );
        },
        startReportIndicatorCaseDetail: function (caseId) {
            window.location.href = "report_indicator_case_detail.html";
        }
    }
}
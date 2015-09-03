package org.ei.telemedicine.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;
import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.adapter.OverviewTimelineAdapter;
import org.ei.telemedicine.domain.EligibleCouple;
import org.ei.telemedicine.domain.Mother;
import org.ei.telemedicine.domain.TimelineEvent;
import org.ei.telemedicine.domain.form.FieldOverrides;
import org.ei.telemedicine.dto.TimelineEventDTO;
import org.ei.telemedicine.repository.AllBeneficiaries;
import org.ei.telemedicine.repository.AllEligibleCouples;
import org.ei.telemedicine.repository.AllTimelineEvents;
import org.ei.telemedicine.util.Log;
import org.ei.telemedicine.view.controller.ANCDetailController;
import org.ei.telemedicine.view.controller.ChildDetailController;
import org.ei.telemedicine.view.controller.EligibleCoupleDetailController;
import org.ei.telemedicine.view.controller.PNCDetailController;
import org.ei.telemedicine.view.customControls.CustomFontTextView;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static org.ei.telemedicine.AllConstants.FormNames.*;
import static org.ei.telemedicine.AllConstants.VISIT_TYPE;
import static org.ei.telemedicine.util.DateUtil.formatDate;

public class NativeOverviewActivity extends SecuredActivity implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {
    ImageButton ib_overview_options, ib_profile_pic;
    ImageView iv_home;
    CustomFontTextView tv_wife_name, tv_woman_name, tv_priority, tv_summary_priority, tv_husband_name, tv_id_no, tv_village_name, tv_fp_value, tv_register_type;
    CustomFontTextView anc_summary_priority, anc_pregnency_time, anc_edd_summary_date;
    CustomFontTextView pnc_summary_priority, pnc_summary_risks, pnc_postpartum_days, pnc_delivery_date;
    CustomFontTextView tv_child_name, child_summary_priority, child_age, child_date_of_birth;
    ListView lv_timeline_events;
    List<org.ei.telemedicine.domain.TimelineEvent> timelineEvents;
    String caseId, visitType;
    View ecSummaryLayout, ancSummaryLayout, pncSummaryLayout, childSummaryLayout;
    Context context;
    LinearLayout ec_summary, anc_summary, pnc_summary, child_summary;
    static Boolean isANCOA = false, isFromEC = false;


    private void setupViews() {

        tv_priority = (CustomFontTextView) findViewById(R.id.tv_priority);
        tv_woman_name = (CustomFontTextView) findViewById(R.id.tv_woman_name);
        tv_wife_name = (CustomFontTextView) findViewById(R.id.tv_wife_name);
        tv_husband_name = (CustomFontTextView) findViewById(R.id.tv_husband_name);
        tv_village_name = (CustomFontTextView) findViewById(R.id.tv_village_name);
        tv_id_no = (CustomFontTextView) findViewById(R.id.tv_id_no);
        tv_register_type = (CustomFontTextView) findViewById(R.id.tv_register_type);

        ib_overview_options = (ImageButton) findViewById(R.id.ib_overview_options);
        lv_timeline_events = (ListView) findViewById(R.id.lv_timeline_events);
        iv_home = (ImageView) findViewById(R.id.iv_home);

        ecSummaryLayout = findViewById(R.id.ec_summary_layout);
        ancSummaryLayout = findViewById(R.id.anc_summary_layout);
        pncSummaryLayout = findViewById(R.id.pnc_summary_layout);
        childSummaryLayout = findViewById(R.id.child_summary_layout);

        ec_summary = (LinearLayout) findViewById(R.id.ec_summary);
        anc_summary = (LinearLayout) findViewById(R.id.anc_summary);
        pnc_summary = (LinearLayout) findViewById(R.id.pnc_summary);
        child_summary = (LinearLayout) findViewById(R.id.child_summary);

        tv_summary_priority = (CustomFontTextView) ecSummaryLayout.findViewById(R.id.summary_priority);
        tv_fp_value = (CustomFontTextView) ecSummaryLayout.findViewById(R.id.tv_fp_value);


        anc_summary_priority = (CustomFontTextView) ancSummaryLayout.findViewById(R.id.anc_summary_priority);
        anc_pregnency_time = (CustomFontTextView) ancSummaryLayout.findViewById(R.id.anc_pregnency_time);
        anc_edd_summary_date = (CustomFontTextView) ancSummaryLayout.findViewById(R.id.anc_edd_summary_date);

        pnc_summary_priority = (CustomFontTextView) pncSummaryLayout.findViewById(R.id.pnc_summary_priority);
        pnc_summary_risks = (CustomFontTextView) pncSummaryLayout.findViewById(R.id.pnc_summary_risks);
        pnc_postpartum_days = (CustomFontTextView) pncSummaryLayout.findViewById(R.id.pnc_postpartum_days);
        pnc_delivery_date = (CustomFontTextView) pncSummaryLayout.findViewById(R.id.pnc_delivery_date);

        tv_child_name = (CustomFontTextView) findViewById(R.id.tv_child_name);
        child_summary_priority = (CustomFontTextView) childSummaryLayout.findViewById(R.id.child_summary_priority);
        child_age = (CustomFontTextView) childSummaryLayout.findViewById(R.id.child_age);
        child_date_of_birth = (CustomFontTextView) childSummaryLayout.findViewById(R.id.child_date_of_birth);
        ib_profile_pic = (ImageButton) findViewById(R.id.ib_profile_pic);
        ib_overview_options.setOnClickListener(this);
        iv_home.setOnClickListener(this);
    }

    @Override
    protected void onCreation() {
        {
            setContentView(R.layout.overview_layout);
            if (getIntent().getExtras() != null) {
                caseId = getIntent().getExtras().getString("caseId");
                visitType = getIntent().getExtras().getString(VISIT_TYPE);
                context = Context.getInstance();
                setupViews();
                timelineEvents = context.allTimelineEvents().forCase(caseId);
                android.util.Log.e("CaseId", caseId);

                switch (visitType.toLowerCase()) {
                    case "ec":
                        ec_summary.setVisibility(View.VISIBLE);
                        isFromEC = true;

                        String ecData = new EligibleCoupleDetailController(this, caseId, context.allEligibleCouples(), context.allTimelineEvents()).get();
                        String coupleDetails = getDataFromJson(ecData, "coupleDetails");
                        String details = getDataFromJson(ecData, "details");

                        android.util.Log.e("ecData", ecData);
                        tv_register_type.setText(visitType.toUpperCase());
                        tv_woman_name.setText(WordUtils.capitalize(getDataFromJson(coupleDetails, "wifeName")));
                        tv_wife_name.setText(WordUtils.capitalize(getDataFromJson(coupleDetails, "wifeName")));
                        tv_husband_name.setText(WordUtils.capitalize(getDataFromJson(coupleDetails, "husbandName")));
                        tv_id_no.setText("No: " + WordUtils.capitalize(getDataFromJson(coupleDetails, "ecNumber")));
                        tv_village_name.setText(WordUtils.capitalize(getDataFromJson(ecData, "village")));

                        tv_priority.setText(getDataFromJson(details, "isHighPriority").equals("yes") ? "High Priority" : "Normal Priority");
                        tv_priority.setTextColor(getDataFromJson(details, "isHighPriority").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));

                        tv_summary_priority.setText(getDataFromJson(details, "isHighPriority").equals("yes") ? "High Priority" : "Normal Priority");
                        tv_summary_priority.setTextColor(getDataFromJson(details, "isHighPriority").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));
                        tv_fp_value.setText(WordUtils.capitalize(!getDataFromJson(details, "currentMethod").equals("") ? getDataFromJson(details, "currentMethod") : "none"));
                        break;
                    case "anc":
                        anc_summary.setVisibility(View.VISIBLE);

                        Mother mother = context.allBeneficiaries().findMother(caseId);
                        if (mother != null) {
                            String pocInfo = mother.getDetail("docPocInfo") != null ? mother.getDetail("docPocInfo") : "";

                            if (!pocInfo.equals("")) {
                                try {
                                    JSONArray pocJsonArray = new JSONArray(pocInfo);
                                    int val = pocJsonArray.length();
                                    android.util.Log.e("size", val + "");
                                    for (int i = 0; i < pocJsonArray.length(); i++) {
                                        String pocData = getDataFromJson(pocJsonArray.getJSONObject(i).toString(), "poc");
                                        String title = "Plan of care for " + getDataFromJson(pocData, "visitType") + "Visit - " + getDataFromJson(pocData, "visitNumber");
                                        timelineEvents.add(new TimelineEvent(caseId, "Plan Of Care ", LocalDate.parse(changeDateFormat(getDataFromJson(pocData, "planofCareDate"))), title, "", ""));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        String ancData = new ANCDetailController(this, caseId, context.allEligibleCouples(), context.allBeneficiaries(), context.allTimelineEvents()).get();
                        String anccoupleDetails = getDataFromJson(ancData, "coupleDetails");
                        String ancDetails = getDataFromJson(ancData, "details");
                        String locationDetails = getDataFromJson(ancData, "location");
                        String pregnancyDetails = getDataFromJson(ancData, "pregnancyDetails");

                        android.util.Log.e("anc Data", ancData);
                        tv_register_type.setText(visitType.toUpperCase());
                        tv_woman_name.setText(WordUtils.capitalize(getDataFromJson(anccoupleDetails, "wifeName")));
                        tv_wife_name.setText(WordUtils.capitalize(getDataFromJson(anccoupleDetails, "wifeName")));
                        tv_husband_name.setText(WordUtils.capitalize(getDataFromJson(anccoupleDetails, "husbandName")));
                        isANCOA = getDataFromJson(ancDetails, "ancNumber").toLowerCase().contains("oa");
                        tv_id_no.setText("ANC No: " + WordUtils.capitalize(getDataFromJson(ancDetails, "ancNumber")));
                        tv_village_name.setText(WordUtils.capitalize(getDataFromJson(locationDetails, "villageName")));

                        tv_priority.setText(getDataFromJson(ancDetails, "isHighRisk").equals("yes") ? "High Risk" : "Normal Risk");
                        tv_priority.setTextColor(getDataFromJson(ancDetails, "isHighRisk").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));

                        anc_summary_priority.setText(getDataFromJson(ancDetails, "isHighRisk").equals("yes") ? "High Risk" : "Normal Risk");
                        anc_summary_priority.setTextColor(getDataFromJson(ancDetails, "isHighRisk").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));
                        anc_pregnency_time.setText(getDataFromJson(pregnancyDetails, "monthsPregnant"));
                        anc_edd_summary_date.setText(getDataFromJson(ancDetails, "edd").replace(" 00:00:00 GMT", ""));

                        break;
                    case "pnc":
                        pnc_summary.setVisibility(View.VISIBLE);
                        Mother pncMother = context.allBeneficiaries().findMother(caseId);
                        if (pncMother != null) {
                            String pocInfo = pncMother.getDetail("docPocInfo") != null ? pncMother.getDetail("docPocInfo") : "";
                            if (!pocInfo.equals("")) {
                                try {
                                    JSONArray pocJsonArray = new JSONArray(pocInfo);
                                    for (int i = 0; i < pocJsonArray.length(); i++) {
                                        String pocData = getDataFromJson(pocJsonArray.getJSONObject(i).toString(), "poc");
                                        String title = "Plan of care for " + getDataFromJson(pocData, "visitType");
                                        timelineEvents.add(new TimelineEvent(caseId, "Plan Of Care", LocalDate.parse(changeDateFormat(getDataFromJson(pocData, "planofCareDate"))), title, "", ""));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        String pncData = new PNCDetailController(this, caseId, context.allEligibleCouples(), context.allBeneficiaries(), context.allTimelineEvents()).get();
                        String pnccoupleDetails = getDataFromJson(pncData, "coupleDetails");
                        String pncDetails = getDataFromJson(pncData, "details");
                        String pncLocationDetails = getDataFromJson(pncData, "location");
                        String pncDelieveryDetails = getDataFromJson(pncData, "pncDetails");

                        android.util.Log.e("pncData", pncData);

                        tv_register_type.setText(visitType.toUpperCase());
                        tv_woman_name.setText(WordUtils.capitalize(getDataFromJson(pnccoupleDetails, "wifeName")));
                        tv_wife_name.setText(WordUtils.capitalize(getDataFromJson(pnccoupleDetails, "wifeName")));
                        tv_husband_name.setText(WordUtils.capitalize(getDataFromJson(pnccoupleDetails, "husbandName")));
                        tv_id_no.setText("ANC No: " + WordUtils.capitalize(getDataFromJson(pncDetails, "ancNumber")));
                        tv_village_name.setText(WordUtils.capitalize(getDataFromJson(pncLocationDetails, "villageName")));

                        tv_priority.setText(getDataFromJson(pncDetails, "isHighRisk").equals("yes") ? "High Risk" : "Normal Risk");
                        tv_priority.setTextColor(getDataFromJson(pncDetails, "isHighRisk").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));
                        pnc_summary_priority.setText(getDataFromJson(pncDetails, "isHighRisk").equals("yes") ? "High Risk" : "Normal Risk");
                        pnc_summary_priority.setTextColor(getDataFromJson(pncDetails, "isHighRisk").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));

                        pnc_summary_risks.setText(getDataFromJson(pncDetails, "complications"));
                        pnc_postpartum_days.setText(getDataFromJson(pncDelieveryDetails, "daysPostpartum"));
                        pnc_delivery_date.setText(getDataFromJson(pncDelieveryDetails, "dateOfDelivery"));

                        break;
                    case "child":
                        child_summary.setVisibility(View.VISIBLE);
                        tv_child_name.setVisibility(View.VISIBLE);
                        String childData = new ChildDetailController(this, caseId, context.allEligibleCouples(), context.allBeneficiaries(), context.allTimelineEvents()).get();
                        android.util.Log.e("childData", childData);

                        String childCoupleDetails = getDataFromJson(childData, "coupleDetails");
                        String childDetails = getDataFromJson(childData, "details");
                        String childLocationDetails = getDataFromJson(childData, "location");
                        String childInfoDetails = getDataFromJson(childData, "childDetails");

                        tv_register_type.setText(visitType.toUpperCase());

                        tv_child_name.setText(getDataFromJson(childDetails, "name"));
                        tv_woman_name.setText(WordUtils.capitalize(getDataFromJson(childCoupleDetails, "wifeName")));
                        tv_wife_name.setText(WordUtils.capitalize(getDataFromJson(childCoupleDetails, "wifeName")));
                        tv_husband_name.setText(WordUtils.capitalize(getDataFromJson(childCoupleDetails, "husbandName")));
                        tv_id_no.setText("EC No: " + WordUtils.capitalize(getDataFromJson(childCoupleDetails, "ecNumber")));
                        tv_village_name.setText(WordUtils.capitalize(getDataFromJson(childLocationDetails, "villageName")));

                        tv_priority.setText(getDataFromJson(childDetails, "childReferral").equals("yes") ? "High Risk" : "Normal Risk");
                        tv_priority.setTextColor(getDataFromJson(childDetails, "childReferral").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));
                        child_summary_priority.setText(getDataFromJson(childDetails, "childReferral").equals("yes") ? "High Risk" : "Normal Risk");
                        child_summary_priority.setTextColor(getDataFromJson(childDetails, "childReferral").equals("yes") ? getResources().getColor(android.R.color.holo_red_dark) : getResources().getColor(android.R.color.holo_blue_dark));
                        ib_profile_pic.setImageResource(getDataFromJson(childInfoDetails, "gender").equalsIgnoreCase("female") ? R.drawable.child_girl_infant : R.drawable.child_boy_infant);
                        child_age.setText(getDataFromJson(childInfoDetails, "age"));
                        child_date_of_birth.setText(getDataFromJson(childInfoDetails, "dateOfBirth"));
                        break;
                }

                lv_timeline_events.setAdapter(new OverviewTimelineAdapter(this, timelineEvents));
                lv_timeline_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View view, int position, long id) {
                        switch (timelineEvents.get(position).type()) {
                            case "ECREGISTERED":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "PREGNANCY":
                                if (isANCOA)
                                    startFormActivity(VIEW_ANC_REGISTRATION, caseId, null, true);
                                else {
                                    if (!isFromEC) {
                                        startFormActivity(AllConstants.FormNames.VIEW_ANC_REGISTRATION_EC, context.allBeneficiaries().findMother(caseId).ecCaseId(), null, true);
                                    } else
                                        startFormActivity(AllConstants.FormNames.VIEW_ANC_REGISTRATION_EC, caseId, null, true);
                                }
                                break;
                            case "CHILD-BIRTH":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "CHILD-DELIVERYPLAN":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "FPCHANGE":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "ANCVISIT":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "IFAPROVIDED":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "TTSHOTPROVIDED":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "PNCVISIT":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "IMMUNIZATIONSGIVEN":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            case "FPRENEW":
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                            default:
                                startFormActivity(AllConstants.FormNames.VIEW_EC_REGISTRATION, caseId, null, true);
                                break;
                        }
                    }
                });
            }
        }
    }

    public String changeDateFormat(String date) {
        String vdate[] = date.split("-");
        return vdate[2] + "-" + vdate[1] + "-" + vdate[0];
    }

    @Override
    protected void onResumption() {
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            //Overview Ec Menu
            case R.id.register_anc_through_ec:
                startFormActivity(ANC_REGISTRATION, caseId, null);
                return true;
            case R.id.register_fp_through_ec:
                startFormActivity(FP_CHANGE, caseId, null);
                return true;
            case R.id.register_child_through_ec:
                startFormActivity(CHILD_REGISTRATION_EC, caseId, null);
                return true;
            case R.id.edit_ec:
                startFormActivity(EC_EDIT, caseId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
                return true;
            case R.id.view_ec:
                startFormActivity(VIEW_EC_REGISTRATION, caseId, null);
                return true;
            case R.id.close_ec:
                startFormActivity(EC_CLOSE, caseId, null);
                return true;

            //Overview ANC Menu
            case R.id.anc_visit:
                startFormActivity(ANC_VISIT, caseId, null);
                return true;
            case R.id.anc_visit_edit:
                startFormActivity(ANC_VISIT_EDIT, caseId, null);
                return true;
            case R.id.hb_test:
                startFormActivity(HB_TEST, caseId, null);
                return true;
            case R.id.view_poc_anc:
                viewPOCActivity(AllConstants.VisitTypes.ANC_VISIT, caseId);
                return true;
            case R.id.ifa:
                startFormActivity(IFA, caseId, null);
                return true;
            case R.id.tt:
                startFormActivity(TT, caseId, null);
                return true;
            case R.id.delivery_plan:
                startFormActivity(DELIVERY_PLAN, caseId, null);
                return true;
            case R.id.pnc_registration:
                startFormActivity(DELIVERY_OUTCOME, caseId, null);
                return true;
            case R.id.anc_investigations:
                startFormActivity(ANC_INVESTIGATIONS, caseId, null);
                return true;
            case R.id.anc_close:
                startFormActivity(ANC_CLOSE, caseId, null);
                return true;

            //Overview PNC Menu
            case R.id.pnc_visit:
                startFormActivity(PNC_VISIT, caseId, null);
                return true;
            case R.id.view_poc_pnc:
                viewPOCActivity(AllConstants.VisitTypes.PNC_VISIT, caseId);
                return true;

            case R.id.postpartum_family_planning:
                startFormActivity(PNC_POSTPARTUM_FAMILY_PLANNING, caseId, null);
                return true;
            case R.id.pnc_close:
                startFormActivity(PNC_CLOSE, caseId, null);
                return true;

            //Overview CHILD Menu
            case R.id.child_immunizations:
                startFormActivity(CHILD_IMMUNIZATIONS, caseId, null);
                return true;
            case R.id.view_poc_child:
                viewPOCActivity(AllConstants.VisitTypes.CHILD_VISIT, caseId);
                return true;
            case R.id.child_illness:
                startFormActivity(CHILD_ILLNESS, caseId, null);
                return true;
            case R.id.vitamin_a:
                startFormActivity(VITAMIN_A, caseId, null);
                return true;
            case R.id.child_close:
                startFormActivity(CHILD_CLOSE, caseId, null);
                return true;

            default:
                return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_overview_options:
                PopupMenu popup = new PopupMenu(this, v);
                MenuInflater inflater = popup.getMenuInflater();
                switch (visitType.toLowerCase()) {
                    case "ec":
                        inflater.inflate(R.menu.overview_ec_menu, popup.getMenu());
                        break;
                    case "anc":
                        inflater.inflate(R.menu.overview_anc_menu, popup.getMenu());
                        break;
                    case "pnc":
                        inflater.inflate(R.menu.overview_pnc_menu, popup.getMenu());
                        break;
                    case "child":
                        inflater.inflate(R.menu.overview_child_menu, popup.getMenu());
                        break;
                }
                popup.show();
                popup.setOnMenuItemClickListener(this);
                break;
            case R.id.iv_home:
                this.finish();
                break;
        }
    }

    public String getDataFromJson(String jsonData, String keyValue) {
        if (jsonData != null && !jsonData.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return jsonObject.has(keyValue) && jsonObject.getString(keyValue) != null ? jsonObject.getString(keyValue) : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return "";
    }

}

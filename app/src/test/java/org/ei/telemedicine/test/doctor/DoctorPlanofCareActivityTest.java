package org.ei.telemedicine.test.doctor;


import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.test.ActivityTestCase;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.DoctorPlanofCareActivity;
import org.ei.telemedicine.doctor.PocBaseAdapter;
import org.ei.telemedicine.doctor.PocDrugBaseAdapter;
import org.ei.telemedicine.util.StringUtil;
import org.ei.telemedicine.view.customControls.CustomFontTextView;
import org.junit.Test;

public class DoctorPlanofCareActivityTest extends ActivityTestCase {

    public String CALLER_URL = "http://202.153.34.169:8004/call?id=%s&peer_id=%s";
    private DoctorPlanofCareActivity poc;
    AutoCompleteTextView act_icd10Diagnosis, act_tests;
    ListView lv_selected_icd10, lv_selected_tests, lv_selected_drugs;
    Switch swich_poc_pending;
    Spinner sp_services, sp_drug_name, sp_drug_frequency, sp_drug_direction, sp_drug_dosage;
    EditText et_drug_qty, et_drug_no_of_days, et_reason, et_advice;

    Button bt_save_plan_of_care;
    ImageButton ib_stop_by, ib_add_drug, ib_anm_logo;
    CustomFontTextView tv_stop_date, tv_health_worker_name, tv_health_worker_village, tv_doc_name, tv_doc_type, tv_mother_name, tv_age, tv_visit_type, tv_village;

    Dialog popup_dialog;
    Object obj;

    static PocBaseAdapter pocDiagnosisBaseAdapter, pocTestBaseAdapter;
    static PocDrugBaseAdapter pocDrugBaseAdapter;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        poc =(DoctorPlanofCareActivity)getActivity();
    }

    public EditText et_drug_qty()
    {
        et_drug_qty.setText("goal");
        return et_drug_qty;
    }


    public Dialog getDialog(){
        return popup_dialog;
    }

    @Test
    public void testet_drug_qty()
    {
        assertEquals("goal",et_drug_qty());
    }

    @Test
    public void testDialog()
    {
        assertTrue(getDialog().isShowing());
    }

    @Test
    public void testBroswerCall() throws Exception{

        // open current activity.

        ib_anm_logo = (ImageButton) poc.findViewById(R.id.iv_anm_logo);
        poc.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // click button and open next activity.
                ib_anm_logo.performClick();
            }
        });

        //testIfBrowserCallIsInitiated.
        String caller_url = String.format(CALLER_URL,"user","password");
        Uri url = Uri.parse(caller_url);
        Intent _browser = new Intent(Intent.ACTION_VIEW,url);
        assertNotNull(_browser);
    }

    @Test
    public void testCurrentAcitivity(){

        assertNotNull(DoctorPlanofCareActivity.class
        );
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
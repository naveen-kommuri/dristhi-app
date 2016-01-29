package org.ei.telemedicine.test.doctor;


import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.DoctorANCScreenActivity;
import org.ei.telemedicine.doctor.DoctorFormDataConstants;
import org.ei.telemedicine.doctor.DoctorPlanofCareActivity;
import org.ei.telemedicine.view.customControls.CustomFontTextView;
import org.json.JSONObject;

public class DoctorANCScreenActivityTest extends ActivityUnitTestCase<DoctorANCScreenActivity> {

    DoctorANCScreenActivity doctorANCScreenActivity;
    EditText et_anc_num, et_woman_name, et_anc_visit_date, et_bp_sys, et_bp_dia, et_temp, et_bloodGlucose, et_fetal;
    CustomFontTextView tv_risks;

    String et_anc_num_string = "et_anc_num_string";
    String et_woman_name_string = "et_woman_name_string";
    String et_anc_visit_date_string = "et_anc_visit_date_string";
    String et_bp_sys__string = "et_bp_sys__string";
    String et_temp_string = "et_temp";
    String bp_dia_string = "bp_dia";
    String et_bloodGlucose_string = "et_bloodGlucose";
    String et_fetal_string = "et_fetal";
    String risk_symptoms_string = "risk_symptoms";


    Intent mLaunchIntent;
    public DoctorANCScreenActivityTest(){
        super(DoctorANCScreenActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }


    @MediumTest
    public void testDataSetToViews(){
        assertEquals(et_anc_num.getText().toString(), "Visit No " + et_anc_num_string);
        assertEquals(et_woman_name.getText().toString(),et_woman_name_string);
        assertEquals(et_anc_visit_date.getText().toString(),et_anc_visit_date_string);
        assertEquals(et_bp_sys.getText().toString(),et_bp_sys__string);
        assertEquals(et_bp_dia.getText().toString(),bp_dia_string);
        assertEquals(et_temp.getText().toString(),et_temp_string);
        assertEquals(et_bloodGlucose.getText().toString(),et_bloodGlucose_string);
        assertEquals(et_fetal.getText().toString(),et_fetal_string);
        assertEquals(tv_risks.getText().toString(),risk_symptoms_string);
    }

    public void testCurrentAcitivity() {
        assertNull("Check acitivity", DoctorANCScreenActivity.class);
    }



    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

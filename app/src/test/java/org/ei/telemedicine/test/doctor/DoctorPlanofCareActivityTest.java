package org.ei.telemedicine.test.doctor;


import android.content.Intent;
import android.graphics.Typeface;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.DoctorFormDataConstants;
import org.ei.telemedicine.doctor.DoctorPlanofCareActivity;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.view.customControls.CustomFontTextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;
import org.robolectric.util.ActivityController;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 19, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@Implements(CustomFontTextView.class)
@RunWith(RobolectricTestRunner.class)
public class DoctorPlanofCareActivityTest {

    CustomFontTextView customFontTextView1;
    DoctorPlanofCareActivity doctorPlanofCareActivity;
    AutoCompleteTextView autoCompleteTextView1;
    @Mock
    private Context context;
    @Mock
    private Cache<Typeface> cache;

    @Mock
    private AllDoctorRepository allDoctorRepository;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    ActivityController activityController;

    @Before
    public void setup() {
        initMocks(this);
    }


    @Test
    public void testingonCreate() {
        Context.setInstance(context);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String drugs = "{\"drug_data\": [{\"direction\": \"Take with Food\", \"frequency\": \"Every 12 Hours\", \"name\": \"Albuterol\", \"dosage\": \"200 mg\"}, {\"direction\": \"To be chewed\", \"frequency\": \"3 times a day\", \"name\": \"corex\", \"dosage\": \"200 mg\"}, {\"direction\": \"For every 1 hour\", \"frequency\": \"2 times a week\", \"name\": \"diclomole\", \"dosage\": \"200 mg\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 8 Hours\", \"name\": \"Valamycin \", \"dosage\": \"5 ml\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 8 Hours\", \"name\": \"Chloroquine\", \"dosage\": \"35 ml\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 24 Hours\", \"name\": \"Tixlix\", \"dosage\": \"35 ml\"}], \"diagnosis_data\": [{\"ICD10_Chapter\": \"XI - Diseases of the digestive system (K00-K93)\", \"ICD10_Code\": \"O00.9\", \"ICD10_Name\": \"Ectopic pregnancy, unspecified\"}, {\"ICD10_Chapter\": \"XVII - Congenital malformations, deformations and chromosomal abnormalities (Q00-Q99)\", \"ICD10_Code\": \"A01\", \"ICD10_Name\": \"Typhoid and paratyphoid \"}, {\"ICD10_Chapter\": \"II - Neoplasms (C00-D48)\", \"ICD10_Code\": \"O07.2\", \"ICD10_Name\": \"Embolism following failed attempted termination of pregnancy\"}, {\"ICD10_Chapter\": \"III - Diseases of the blood and blood-forming organs and certain disorders involving the immune mechanism (D50-D89)\", \"ICD10_Code\": \"O01.0\", \"ICD10_Name\": \"Classical hydatidiform mo\"}], \"investigation_data\": [{\"service_group_name\": \"procedures\", \"investigation_name\": \"major dressing\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Pregnancy-associated plasma protein screening (PAPP-A)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Human chorionic gonadotropin (hCG)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"MSAFP (maternal serum AFP)\"}, {\"service_group_name\": \"radiology\", \"investigation_name\": \"Ultrasound test for fetal nuchal translucency (NT)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Human immunodeficiency virus (HIV) antibody\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Pregnancy test -(Blood Sample)\"}]}";
        String formData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"ANC\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        intent.putExtra(AllConstants.DRUG_INFO_RESULT, drugs);
        intent.putExtra(DoctorFormDataConstants.documentId, "e5a5213e292944ae0ca87a314bd397ee");
        intent.putExtra(DoctorFormDataConstants.formData, formData);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        when(allDoctorRepository.getPocInfoCaseId("")).thenReturn("ad");
        System.out.println("---------------------------------++");
//        when(Context.getInstance().allDoctorRepository()).thenReturn(allDoctorRepository);
        System.out.println("---------------------------------");
        doctorPlanofCareActivity = Robolectric.buildActivity(DoctorPlanofCareActivity.class).withIntent(intent).create().get();
        ((ImageButton) doctorPlanofCareActivity.findViewById(R.id.ib_stop_by_date)).performClick();

//        autoCompleteTextView1 = (AutoCompleteTextView) doctorPlanofCareActivity.findViewById(R.id.act_tests);
//        autoCompleteTextView1.performItemClick();
//        customFontTextView1 = (CustomFontTextView) (LayoutInflater.from(doctorPlanofCareActivity)).inflate(R.layout.doc_plan_of_care, null);
//        customFontTextView1.setText("Hello");
//
//
//        assertEquals(customFontTextView1.getText().toString(), "sasm");
//        assertNull(allDoctorRepository);
//        when(doctorRepository.getPocInfo("sdf")).thenReturn("");
//        assertNotNull(context);
//        assertNull(context.allDoctorRepository());
//        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
//        assertNotNull(context.allDoctorRepository());
//        when(allDoctorRepository.getPocInfoCaseId("sadf")).thenReturn("a");
//        activityController.create();
    }
//
//    @Test
//    public void testListeners() {
//        doctorPlanofCareActivity = Robolectric.buildActivity(DoctorPlanofCareActivity.class).get();
//        ((ImageButton) doctorPlanofCareActivity.findViewById(R.id.ib_stop_by_date)).performClick();
//    }

}

package org.ei.telemedicine.test.doctor;


import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.DristhiConfiguration;
import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.DiagnosisArrayAdapter;
import org.ei.telemedicine.doctor.DoctorFormDataConstants;
import org.ei.telemedicine.doctor.DoctorPlanofCareActivity;
import org.ei.telemedicine.doctor.PocBaseAdapter;
import org.ei.telemedicine.doctor.PocDiagnosis;
import org.ei.telemedicine.doctor.PocDrugBaseAdapter;
import org.ei.telemedicine.doctor.PocDrugData;
import org.ei.telemedicine.doctor.PocInvestigation;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.UserService;
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

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@Implements(CustomFontTextView.class)
@RunWith(RobolectricTestRunner.class)
public class DoctorPlanofCareActivityTest {

    CustomFontTextView customFontTextView1;

    AutoCompleteTextView autoCompleteTextView1;
    @Mock
    private Context context;
    @Mock
    private Cache<Typeface> cache;

    @Mock
    private AllDoctorRepository allDoctorRepository;
    @Mock
    private DristhiConfiguration configuration;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    ActivityController activityController;
    DoctorPlanofCareActivity doctorPlanofCareActivityActivityController;
    @Mock
    private UserService userService;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        when(allDoctorRepository.getPocInfoCaseId("e5a5213e292944ae0ca87a314bd397ee")).thenReturn("{\"investigations\":[\"laboratory-Blood Test\"],\"diagnosis\":[\"Typhoid fever\"],\"planofCareDate\":\"03-07-2015\",\"doctorName\":\"demodoc1\",\"advice\":\"\",\"drugs\":[{\"dosage\":\"Liquid Solution222\",\"frequency\":\"11\",\"drugNoOfDays\":\"4\",\"drugQty\":\"4\",\"direction\":\"afterbreakfast\",\"drugName\":\"Macrobid, Macrodantin (nitrofurantoin) Rx\",\"drugStopDate\":\"01-01-0000\"},{\"dosage\":\"Liquid Solution222\",\"frequency\":\"10\",\"drugNoOfDays\":\"4\",\"drugQty\":\"5\",\"direction\":\"30 mins after food\",\"drugName\":\"corex\",\"drugStopDate\":\"01-01-0000\"}],\"visitType\":\"ANC\"}");
        String drugs = "{\"drug_data\": [{\"direction\": \"Take with Food\", \"frequency\": \"Every 12 Hours\", \"name\": \"Albuterol\", \"dosage\": \"200 mg\"}, {\"direction\": \"To be chewed\", \"frequency\": \"3 times a day\", \"name\": \"corex\", \"dosage\": \"200 mg\"}, {\"direction\": \"For every 1 hour\", \"frequency\": \"2 times a week\", \"name\": \"diclomole\", \"dosage\": \"200 mg\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 8 Hours\", \"name\": \"Valamycin \", \"dosage\": \"5 ml\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 8 Hours\", \"name\": \"Chloroquine\", \"dosage\": \"35 ml\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 24 Hours\", \"name\": \"Tixlix\", \"dosage\": \"35 ml\"}], \"diagnosis_data\": [{\"ICD10_Chapter\": \"XI - Diseases of the digestive system (K00-K93)\", \"ICD10_Code\": \"O00.9\", \"ICD10_Name\": \"Ectopic pregnancy, unspecified\"}, {\"ICD10_Chapter\": \"XVII - Congenital malformations, deformations and chromosomal abnormalities (Q00-Q99)\", \"ICD10_Code\": \"A01\", \"ICD10_Name\": \"Typhoid and paratyphoid \"}, {\"ICD10_Chapter\": \"II - Neoplasms (C00-D48)\", \"ICD10_Code\": \"O07.2\", \"ICD10_Name\": \"Embolism following failed attempted termination of pregnancy\"}, {\"ICD10_Chapter\": \"III - Diseases of the blood and blood-forming organs and certain disorders involving the immune mechanism (D50-D89)\", \"ICD10_Code\": \"O01.0\", \"ICD10_Name\": \"Classical hydatidiform mo\"}], \"investigation_data\": [{\"service_group_name\": \"procedures\", \"investigation_name\": \"major dressing\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Pregnancy-associated plasma protein screening (PAPP-A)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Human chorionic gonadotropin (hCG)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"MSAFP (maternal serum AFP)\"}, {\"service_group_name\": \"radiology\", \"investigation_name\": \"Ultrasound test for fetal nuchal translucency (NT)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Human immunodeficiency virus (HIV) antibody\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Pregnancy test -(Blood Sample)\"}]}";
        String formData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"ANC\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(AllConstants.DRUG_INFO_RESULT, drugs);
        intent.putExtra(DoctorFormDataConstants.documentId, "e5a5213e292944ae0ca87a314bd397ee");
        intent.putExtra(DoctorFormDataConstants.formData, formData);
        doctorPlanofCareActivityActivityController = Robolectric.buildActivity(DoctorPlanofCareActivity.class).withIntent(intent).create().get();

    }

    @Test
    public void testListView() {
        assertNotNull(doctorPlanofCareActivityActivityController);
        assertNotNull(context);
        PocDrugData pocDrugData = new PocDrugData();
        pocDrugData.setDirection("direction1");
        pocDrugData.setDosage("dosage1");
        pocDrugData.setDrugName("drugName1");
        pocDrugData.setDrugNoofDays("drugDays1");
        pocDrugData.setDrugQty("drugqty1");
        pocDrugData.setDrugStopByDate("drugDate1");
        pocDrugData.setIsDrugDuplicate(false);
        ArrayList<PocDrugData> pocDrugDatas = new ArrayList<PocDrugData>();
        pocDrugDatas.add(pocDrugData);
        PocDrugBaseAdapter pocDrugBaseAdapter = new PocDrugBaseAdapter(doctorPlanofCareActivityActivityController, pocDrugDatas);
        Object item = pocDrugBaseAdapter.getItem(0);
        assertEquals("drugName1", ((PocDrugData) item).getDrugName());
        View view = pocDrugBaseAdapter.getView(0, null, null);
        assertNotNull(view);
        view.findViewById(R.id.ib_remove).performClick();
        assertEquals(0, pocDrugBaseAdapter.getItemId(0));

    }

    @Test
    public void testInvestigationAdapter() {
        assertNotNull(doctorPlanofCareActivityActivityController);
        ArrayList<String> sampleData = new ArrayList<String>();
        sampleData.add("investigation1");
        sampleData.add("investigation2");
        PocBaseAdapter pocBaseAdapter = new PocBaseAdapter(doctorPlanofCareActivityActivityController, sampleData);
        assertEquals("investigation2", ((String) pocBaseAdapter.getItem(1)));
        assertEquals(1, pocBaseAdapter.getItemId(1));
        View view = pocBaseAdapter.getView(1, null, null);
        assertNotNull(view);
        view.findViewById(R.id.ib_remove).performClick();
    }

    @Test
    public void testDiagnosisAdapter() {
        ArrayList<PocDiagnosis> diagnosis = new ArrayList<PocDiagnosis>();
        PocDiagnosis pocDiagnosis = new PocDiagnosis();
        pocDiagnosis.setIcd10_chapter("chapter1");
        pocDiagnosis.setIcd10_code("code1");
        pocDiagnosis.setIcd10_name("Name1");
        diagnosis.add(pocDiagnosis);
        DiagnosisArrayAdapter diagnosisArrayAdapter = new DiagnosisArrayAdapter(doctorPlanofCareActivityActivityController, R.layout.diagnosis_list_item, diagnosis);
        diagnosisArrayAdapter.getView(0, null, null);
        assertEquals("Name1", ((PocDiagnosis) diagnosisArrayAdapter.getItem(0)).getIcd10_name());

    }

    @Test
    public void testImageBT() {
        assertNotNull(doctorPlanofCareActivityActivityController);
        ImageButton ib_anm_logo = (ImageButton) doctorPlanofCareActivityActivityController.findViewById(R.id.iv_anm_logo);
        assertNotNull(ib_anm_logo);
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(configuration.drishtiVideoURL()).thenReturn("http://video");
        ib_anm_logo.performClick();

        Button bt_save_plan_of_care = (Button) doctorPlanofCareActivityActivityController.findViewById(R.id.bt_save_plan_of_care);
        when(context.userService()).thenReturn(userService);
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.POC_DATA_SAVE_URL_PATH + "visitid=6b901662-4a44-48c6-804c-0682322ce90d")).thenReturn("success");
        bt_save_plan_of_care.performClick();
        ImageButton ib_stop_by = (ImageButton) doctorPlanofCareActivityActivityController.findViewById(R.id.ib_stop_by_date);
        ib_stop_by.performClick();
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        PocInvestigation pocInvestigation = new PocInvestigation();
        pocInvestigation.setInvestigation_name("Investigation1");
        pocInvestigation.setService_group_name("Service1");
        assertEquals(pocInvestigation.getInvestigation_name(), "Investigation1");
        assertEquals(pocInvestigation.getService_group_name(), "Service1");

        allDoctorRepository.updatePocInLocal("e5a5213e292944ae0ca87a314bd397ee", "poc", "");
        verify(allDoctorRepository).updatePocInLocal("e5a5213e292944ae0ca87a314bd397ee", "poc", "");
        doctorPlanofCareActivityActivityController.saveDatainLocal("e5a5213e292944ae0ca87a314bd397ee", "poc", "");
        ImageButton ib_add_drug = (ImageButton) doctorPlanofCareActivityActivityController.findViewById(R.id.ib_add_drug);
        ib_add_drug.performClick();
    }


}

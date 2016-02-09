package org.ei.telemedicine.test.doctor;


import android.content.Intent;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.ImageButton;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.DristhiConfiguration;
import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.DoctorANCScreenActivity;
import org.ei.telemedicine.doctor.DoctorFormDataConstants;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.UserService;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.view.activity.DrishtiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class DoctorANCScreenActivityTest {

    @Mock
    Context context;
    ActivityController activityController;
    @Mock
    DrishtiApplication drishtiApplication;
    @Mock
    private Cache<Typeface> cache;
    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private DristhiConfiguration configuration;
    @Mock
    private UserService userService;
    @Mock
    private AllDoctorRepository allDoctorRepository;
    DoctorANCScreenActivity doctorANCScreenActivity;
    Button bt_poc;
    ImageButton ib_bp_graph;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);

        String dummyData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"30\",\"bloodGlucoseData\":\"30\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"50\",\"visit_type\":\"ANC\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"23-c\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"23\",\"childDob\":\"2013-01-07\",\"bpSys\":\"10\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
//        Intent intent = new Intent(Intent.ACTION_VIEW);
        Intent intent = new Intent(RuntimeEnvironment.application, DoctorANCScreenActivity.class);
        intent.putExtra(DoctorFormDataConstants.formData, dummyData);
        doctorANCScreenActivity = Robolectric.buildActivity(DoctorANCScreenActivity.class).withIntent(intent).create().resume().get();
        bt_poc = (Button) doctorANCScreenActivity.findViewById(R.id.bt_plan_of_care);
        ib_bp_graph = (ImageButton) doctorANCScreenActivity.findViewById(R.id.ib_bp_graph);
    }

    //
    @Test
    public void testingonCreate() {
        assertNotNull(doctorANCScreenActivity);
        assertEquals("1", "1");
        String drugs = "{\"drug_data\": [{\"direction\": \"Take with Food\", \"frequency\": \"Every 12 Hours\", \"name\": \"Albuterol\", \"dosage\": \"200 mg\"}, {\"direction\": \"To be chewed\", \"frequency\": \"3 times a day\", \"name\": \"corex\", \"dosage\": \"200 mg\"}, {\"direction\": \"For every 1 hour\", \"frequency\": \"2 times a week\", \"name\": \"diclomole\", \"dosage\": \"200 mg\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 8 Hours\", \"name\": \"Valamycin \", \"dosage\": \"5 ml\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 8 Hours\", \"name\": \"Chloroquine\", \"dosage\": \"35 ml\"}, {\"direction\": \"Take Orally \", \"frequency\": \"Every 24 Hours\", \"name\": \"Tixlix\", \"dosage\": \"35 ml\"}], \"diagnosis_data\": [{\"ICD10_Chapter\": \"XI - Diseases of the digestive system (K00-K93)\", \"ICD10_Code\": \"O00.9\", \"ICD10_Name\": \"Ectopic pregnancy, unspecified\"}, {\"ICD10_Chapter\": \"XVII - Congenital malformations, deformations and chromosomal abnormalities (Q00-Q99)\", \"ICD10_Code\": \"A01\", \"ICD10_Name\": \"Typhoid and paratyphoid \"}, {\"ICD10_Chapter\": \"II - Neoplasms (C00-D48)\", \"ICD10_Code\": \"O07.2\", \"ICD10_Name\": \"Embolism following failed attempted termination of pregnancy\"}, {\"ICD10_Chapter\": \"III - Diseases of the blood and blood-forming organs and certain disorders involving the immune mechanism (D50-D89)\", \"ICD10_Code\": \"O01.0\", \"ICD10_Name\": \"Classical hydatidiform mo\"}], \"investigation_data\": [{\"service_group_name\": \"procedures\", \"investigation_name\": \"major dressing\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Pregnancy-associated plasma protein screening (PAPP-A)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Human chorionic gonadotropin (hCG)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"MSAFP (maternal serum AFP)\"}, {\"service_group_name\": \"radiology\", \"investigation_name\": \"Ultrasound test for fetal nuchal translucency (NT)\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Blood Test for Human immunodeficiency virus (HIV) antibody\"}, {\"service_group_name\": \"laboratory\", \"investigation_name\": \"Pregnancy test -(Blood Sample)\"}]}";
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(context.userService()).thenReturn(userService);
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.DRUG_INFO_URL_PATH)).thenReturn(drugs);
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        allDoctorRepository.updatePocInLocal("e5a5213e292944ae0ca87a314bd397ee", "", "");
        verify(allDoctorRepository).updatePocInLocal("e5a5213e292944ae0ca87a314bd397ee", "", "");
        assertNotNull(bt_poc);
        assertEquals("1", "1");
        bt_poc.performClick();
    }

    @Test
    public void testingRefer() {
        assertNotNull(doctorANCScreenActivity);
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(context.userService()).thenReturn(userService);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("doc1");
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.DOCTOR_REFER_URL_PATH + "doc1&visitid=6b901662-4a44-48c6-804c-0682322ce90d&entityid=15f46603-9f36-41d8-a8ba-b7e89e1adc84&patientname=demores")).thenReturn("result");
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        allDoctorRepository.deleteUseCaseId("6b901662-4a44-48c6-804c-0682322ce90d");
        verify(allDoctorRepository).deleteUseCaseId("6b901662-4a44-48c6-804c-0682322ce90d");
        Button bt_refer = (Button) doctorANCScreenActivity.findViewById(R.id.bt_refer);
        assertNotNull(bt_refer);
        boolean b = bt_refer.performClick();
        assertEquals(b, true);
    }


    @Test
    public void testingGraph() {
        assertNotNull(doctorANCScreenActivity);
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(context.userService()).thenReturn(userService);
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.VITALS_INFO_URL_PATH + "6b901662-4a44-48c6-804c-0682322ce90d")).thenReturn("result");
        assertNotNull(ib_bp_graph);
        boolean b = ib_bp_graph.performClick();
        assertEquals(b, true);
        ImageButton ib_temp_graph = (ImageButton) doctorANCScreenActivity.findViewById(R.id.ib_temp_graph);
        ib_temp_graph.performClick();
        ImageButton ib_bgm_graph = (ImageButton) doctorANCScreenActivity.findViewById(R.id.ib_bgm_graph);
        ib_bgm_graph.performClick();
        ImageButton ib_fetal_graph = (ImageButton) doctorANCScreenActivity.findViewById(R.id.ib_fetal_graph);
        ib_fetal_graph.performClick();
        ImageButton ib_pause_stehoscope = (ImageButton) doctorANCScreenActivity.findViewById(R.id.ib_pause_stehoscope);
        ib_pause_stehoscope.performClick();
    }

    @Test
    public void testingSam() {
        assertEquals("1", "1");
    }

}
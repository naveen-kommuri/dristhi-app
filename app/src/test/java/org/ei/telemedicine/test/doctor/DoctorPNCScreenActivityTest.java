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
import org.ei.telemedicine.doctor.DoctorFormDataConstants;
import org.ei.telemedicine.doctor.DoctorPNCScreenActivity;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.UserService;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.view.activity.DrishtiApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class DoctorPNCScreenActivityTest {

    @Mock
    Context context;
    ActivityController activityController;
    @Mock
    DrishtiApplication drishtiApplication;
    @Mock
    private Cache<Typeface> cache;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    ActivityController<DoctorPNCScreenActivity> doctorPNCScreenActivityController;
    DoctorPNCScreenActivity doctorPNCScreenActivity;
    @Mock
    private DristhiConfiguration configuration;
    @Mock
    private UserService userService;
    @Mock
    private AllDoctorRepository allDoctorRepository;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);

        String dummyData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"ANC\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"/Downloads/opensrp/anm111/audios/0d4ed20b-ed83-4c2f-9d05-7a5fc0aec3ee.wav\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(DoctorFormDataConstants.formData, dummyData);
        doctorPNCScreenActivityController = Robolectric.buildActivity(DoctorPNCScreenActivity.class).withIntent(intent).create();
        doctorPNCScreenActivity = doctorPNCScreenActivityController.get();
    }

    @Test
    public void testingonCreate() {
        assertNotNull(doctorPNCScreenActivity);
        ImageButton ib_pause_stehoscope = (ImageButton) doctorPNCScreenActivity.findViewById(R.id.ib_pause_stehoscope);
        ib_pause_stehoscope.performClick();
    }

    @Test
    public void testingGraph() {
        assertNotNull(doctorPNCScreenActivity);
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(context.userService()).thenReturn(userService);
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.VITALS_INFO_URL_PATH + "6b901662-4a44-48c6-804c-0682322ce90d")).thenReturn("result");
        ImageButton ib_bp_graph = (ImageButton) doctorPNCScreenActivity.findViewById(R.id.ib_bp_graph);
        assertNotNull(ib_bp_graph);
        boolean b = ib_bp_graph.performClick();
        assertEquals(b, true);
        ImageButton ib_temp_graph = (ImageButton) doctorPNCScreenActivity.findViewById(R.id.ib_temp_graph);
        ib_temp_graph.performClick();
        ImageButton ib_bgm_graph = (ImageButton) doctorPNCScreenActivity.findViewById(R.id.ib_bgm_graph);
        ib_bgm_graph.performClick();
        ImageButton ib_pause_stehoscope = (ImageButton) doctorPNCScreenActivity.findViewById(R.id.ib_pause_stehoscope);
        ib_pause_stehoscope.performClick();
    }

    @Test
    public void testingRefer() {
        assertNotNull(doctorPNCScreenActivity);
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(context.userService()).thenReturn(userService);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("doc1");
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.DOCTOR_REFER_URL_PATH + "doc1&visitid=6b901662-4a44-48c6-804c-0682322ce90d&entityid=15f46603-9f36-41d8-a8ba-b7e89e1adc84&patientname=demores")).thenReturn("result");
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        allDoctorRepository.deleteUseCaseId("6b901662-4a44-48c6-804c-0682322ce90d");
        verify(allDoctorRepository).deleteUseCaseId("6b901662-4a44-48c6-804c-0682322ce90d");
        Button bt_refer = (Button) doctorPNCScreenActivity.findViewById(R.id.bt_refer);
        assertNotNull(bt_refer);
        boolean b = bt_refer.performClick();
        assertEquals(b, true);
    }

    @After
    public void destroy() {
        doctorPNCScreenActivity.pausePlay();
        doctorPNCScreenActivityController.stop();
    }

}
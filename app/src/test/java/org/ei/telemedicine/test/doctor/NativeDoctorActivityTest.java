package org.ei.telemedicine.test.doctor;


import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.DristhiConfiguration;
import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.DoctorData;
import org.ei.telemedicine.doctor.DoctorFormDataConstants;
import org.ei.telemedicine.doctor.NativeDoctorActivity;
import org.ei.telemedicine.doctor.PendingConsultantBaseAdapter;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.ActionService;
import org.ei.telemedicine.service.FormSubmissionSyncService;
import org.ei.telemedicine.service.UserService;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.view.activity.DrishtiApplication;
import org.ei.telemedicine.view.controller.ANMController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.ei.telemedicine.domain.FetchStatus.fetched;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class NativeDoctorActivityTest {

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
    private AllDoctorRepository allDoctorRepository;
    @Mock
    private ActionService actionService;
    @Mock
    private FormSubmissionSyncService formSubmissionSyncService;
    NativeDoctorActivity nativeDoctorActivity;
    @Mock
    private DristhiConfiguration configuration;
    @Mock
    private UserService userService;
    @Mock
    private ANMController anmController;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        when(context.actionService()).thenReturn(actionService);
        when(context.formSubmissionSyncService()).thenReturn(formSubmissionSyncService);
        when(context.userService()).thenReturn(userService);
        context.allSharedPreferences().saveVillageName("village1");
        verify(context.allSharedPreferences()).saveVillageName("village1");
        when(allSharedPreferences.getUserRole()).thenReturn("DOC");
        when(formSubmissionSyncService.sync("village1")).thenReturn(fetched);
        nativeDoctorActivity = Robolectric.buildActivity(NativeDoctorActivity.class).create().resume().get();
    }

    @Test
    public void testingANCAdapter() {
        assertNotNull(nativeDoctorActivity);
        String dummyANCData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"ANC\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        DoctorData doctorData = new DoctorData();
        doctorData.setAnmId("banm1");
        doctorData.setCaseId("case1");
        doctorData.setFormInformation(dummyANCData);
        doctorData.setFormTime("tim1");
        doctorData.setImgUrl("");
        doctorData.setPOCInformation("");
        doctorData.setPocStatus("");
        doctorData.setPocTime("");
        ArrayList<DoctorData> doctorDatas = new ArrayList<DoctorData>();
        doctorDatas.add(doctorData);
        PendingConsultantBaseAdapter pendingAdapter = new PendingConsultantBaseAdapter(nativeDoctorActivity, doctorDatas, nativeDoctorActivity);
        assertEquals("banm1", (pendingAdapter.getItem(0).getAnmId()));
        View view = pendingAdapter.getView(0, null, null);
        assertNotNull(view);
        assertEquals(0, pendingAdapter.getItemId(0));
        pendingAdapter.sort(DoctorFormDataConstants.sorting_name, doctorDatas);
        View viewById = view.findViewById(R.id.btn_edit);
        assertNotNull(viewById);
        viewById.performClick();
    }

    @Test
    public void testingPNCAdapter() {
        assertNotNull(nativeDoctorActivity);
        String dummyPNCData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"PNC\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        DoctorData doctorData = new DoctorData();
        doctorData.setAnmId("banm1");
        doctorData.setCaseId("case1");
        doctorData.setFormInformation(dummyPNCData);
        doctorData.setFormTime("tim1");
        doctorData.setImgUrl("");
        doctorData.setPOCInformation("");
        doctorData.setPocStatus("");
        doctorData.setPocTime("");
        ArrayList<DoctorData> doctorDatas = new ArrayList<DoctorData>();
        doctorDatas.add(doctorData);
        PendingConsultantBaseAdapter pendingAdapter = new PendingConsultantBaseAdapter(nativeDoctorActivity, doctorDatas, nativeDoctorActivity);
        assertEquals("banm1", (pendingAdapter.getItem(0).getAnmId()));
        View view = pendingAdapter.getView(0, null, null);
        assertNotNull(view);
        assertEquals(0, pendingAdapter.getItemId(0));
        pendingAdapter.sort(DoctorFormDataConstants.sorting_hr, doctorDatas);
        View viewById = view.findViewById(R.id.btn_edit);
        assertNotNull(viewById);
        viewById.performClick();
    }

    @Test
    public void testingChildAdapter() {
        assertNotNull(nativeDoctorActivity);
        String dummyChildData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"CHILD\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        DoctorData doctorData = new DoctorData();
        doctorData.setAnmId("banm1");
        doctorData.setCaseId("case1");
        doctorData.setFormInformation(dummyChildData);
        doctorData.setFormTime("tim1");
        doctorData.setImgUrl("imag1");
        doctorData.setPOCInformation("poc");
        doctorData.setPocStatus("pocStatus");
        doctorData.setPocTime("time");
        assertEquals("banm1", doctorData.getAnmId());
        assertEquals("case1", doctorData.getCaseId());
        assertEquals(dummyChildData, doctorData.getFormInformation());
        assertEquals("tim1", doctorData.getFormTime());
        assertEquals("imag1", doctorData.getImgUrl());
        assertEquals("poc", doctorData.getPOCInformation());
        assertEquals("pocStatus", doctorData.getPocStatus());
        assertEquals("time", doctorData.getPocTime());

        ArrayList<DoctorData> doctorDatas = new ArrayList<DoctorData>();
        doctorDatas.add(doctorData);
        PendingConsultantBaseAdapter pendingAdapter = new PendingConsultantBaseAdapter(nativeDoctorActivity, doctorDatas, nativeDoctorActivity);
        assertEquals("banm1", (pendingAdapter.getItem(0).getAnmId()));
        View view = pendingAdapter.getView(0, null, null);
        assertNotNull(view);
        assertEquals(0, pendingAdapter.getItemId(0));

        View viewById = view.findViewById(R.id.btn_edit);
        assertNotNull(viewById);
        viewById.performClick();
        View profileView = view.findViewById(R.id.profile_layout);
        when(context.configuration()).thenReturn(configuration);
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
        when(context.userService()).thenReturn(userService);
        when(userService.gettingFromRemoteURL("http://django" + AllConstants.DOCTOR_OVERVIEW_URL_PATH + "visitid=6b901662-4a44-48c6-804c-0682322ce90d&entityid=15f46603-9f36-41d8-a8ba-b7e89e1adc84")).thenReturn("");
        when(context.anmController()).thenReturn(anmController);
        profileView.performClick();
        pendingAdapter.filter("a", doctorDatas);
        pendingAdapter.villagesFilter("village1", doctorDatas);
    }

    @Test
    public void testViewsWithListeners() {
        assertNotNull(nativeDoctorActivity);
        ImageButton ib_clear_search = (ImageButton) nativeDoctorActivity.findViewById(R.id.ib_doc_search_cancel);
        ib_clear_search.performClick();
        ImageButton ib_logout = (ImageButton) nativeDoctorActivity.findViewById(R.id.ib_logout);
        when(context.userService()).thenReturn(userService);
        userService.logout();
        verify(userService).logout();
        nativeDoctorActivity.logoutUser();
        ib_logout.performClick();
        EditText edt_search = (EditText) nativeDoctorActivity.findViewById(R.id.edt_doc_search);
        edt_search.setText("ad");
    }

    @Test
    public void testCouts() {
        assertNotNull(nativeDoctorActivity);
        ListView lv_pending_consultants = (ListView) nativeDoctorActivity.findViewById(R.id.consultants_list);
        ArrayList<DoctorData> doctorDatas = new ArrayList<DoctorData>();
        String dummyChildData = "{\"childInfo\":\"\",\"childReportdiseasePlace\":\"phc\",\"childAge\":\"\",\"bpDia\":\"0\",\"bloodGlucoseData\":\"0\",\"childGender\":\"\",\"childImmediateReferralReason\":\"null\",\"bloodInStool\":\"null\",\"phoneNumber\":\"2548121337675\",\"sickVisitDate\":\"null\",\"age\":\"\",\"fetal\":\"0\",\"visit_type\":\"CHILD\",\"riskObservedDuringANC\":\"none\",\"wifeName\":\"demores\",\"idNo\":\"OA24554\",\"isHighRisk\":\"null\",\"daysOfDiarrhea\":\"null\",\"childName\":\"\",\"husbandName\":\"null\",\"temp\":\"0\",\"childImmediateReferral\":\"null\",\"anmPoc\":\"null\",\"childNoOfOsrs\":\"6\",\"villageName\":\"Chemoinoi\",\"ancNumber\":\"OA24554\",\"weight\":\"null\",\"childReportChildDisease\":\"diarrhea_dehydration\",\"childReferral\":\"null\",\"childReportdiseaseOther\":\"null\",\"entityId\":\"15f46603-9f36-41d8-a8ba-b7e89e1adc84\",\"edd\":\"14-Jul-2016\",\"visitId\":\"6b901662-4a44-48c6-804c-0682322ce90d\",\"childSigns\":\"null\",\"vommitEveryThing\":\"null\",\"pulseRate\":\"null\",\"childDob\":\"2013-01-07\",\"bpSys\":\"0\",\"ancVisitDate\":\"2015-11-26\",\"lmp\":\"08-Oct-2015\",\"ancVisitNumber\":\"1\",\"pstechoscopeData\":\"0\",\"childReportdiseaseDate\":\"2015-11-07\",\"anmId\":\"demo2\",\"numberOfDaysCough\":\"null\",\"childSubmissionDate\":\"2016-01-07\",\"daysOfFever\":\"null\",\"childSignsOther\":\"null\",\"breathsPerMinute\":\"null\",\"documentId\":\"e5a5213e292944ae0ca87a314bd397ee\",\"pocPending\":\" \"}";
        DoctorData doctorData = new DoctorData();
        doctorData.setAnmId("banm1");
        doctorData.setCaseId("case1");
        doctorData.setFormInformation(dummyChildData);
        doctorData.setFormTime("tim1");
        doctorData.setImgUrl("imag1");
        doctorData.setPOCInformation("poc");
        doctorData.setPocStatus("pocStatus");
        doctorData.setPocTime("time");
        doctorDatas.add(doctorData);
        PendingConsultantBaseAdapter pendingConsultantBaseAdapter = new PendingConsultantBaseAdapter(nativeDoctorActivity, doctorDatas, nativeDoctorActivity);
        lv_pending_consultants.setAdapter(pendingConsultantBaseAdapter);
        ListAdapter adapter = lv_pending_consultants.getAdapter();
        assertEquals(pendingConsultantBaseAdapter, (PendingConsultantBaseAdapter) adapter);


    }
}
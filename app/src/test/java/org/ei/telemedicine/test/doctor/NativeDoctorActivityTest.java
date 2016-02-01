package org.ei.telemedicine.test.doctor;


import android.graphics.Typeface;
import android.view.View;

import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
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
    private UserService userService;
    @Mock
    private FormSubmissionSyncService formSubmissionSyncService;
    NativeDoctorActivity nativeDoctorActivity;
//    @Mock
//    private DoctorData doctorData, doctorData1;

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
        nativeDoctorActivity = Robolectric.buildActivity(NativeDoctorActivity.class).create().resume().destroy().get();
    }

    @Test
    public void testingAdapter() {
        assertEquals("1", "1");
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

        DoctorData doctorData1 = new DoctorData();
        doctorData1.setAnmId("anm1");
        doctorData1.setCaseId("case2");
        doctorData1.setFormInformation(dummyANCData);
        doctorData1.setFormTime("tim2");
        doctorData1.setImgUrl("");
        doctorData1.setPOCInformation("");
        doctorData1.setPocStatus("");
        doctorData1.setPocTime("");

        ArrayList<DoctorData> doctorDatas = new ArrayList<DoctorData>();
        doctorDatas.add(doctorData);
        doctorDatas.add(doctorData1);

        PendingConsultantBaseAdapter pendingAdapter = new PendingConsultantBaseAdapter(nativeDoctorActivity, doctorDatas, nativeDoctorActivity);
        assertEquals("banm1", (pendingAdapter.getItem(0).getAnmId()));
        View view = pendingAdapter.getView(0, null, null);
        assertNotNull(view);
        assertEquals(0, pendingAdapter.getItemId(0));
//
        pendingAdapter.sort(DoctorFormDataConstants.sorting_name, doctorDatas);

    }


}
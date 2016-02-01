package org.ei.telemedicine.test.doctor;


import android.content.Intent;
import android.graphics.Typeface;

import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.doctor.ViewPreVisitScreenActivity;
import org.ei.telemedicine.repository.AllSharedPreferences;
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

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class ViewPreVisitScreenActivityTest {

    @Mock
    Context context;
    ActivityController activityController;
    @Mock
    DrishtiApplication drishtiApplication;
    @Mock
    private Cache<Typeface> cache;
    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testingonCreate() {
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);

        String dummyData = "{\"isHighRisk\":\"\",\"ancNumber\":\"OA1325409058749\",\"weight\":null,\"riskObserved\":\"jaundice convulsions\",\"docPocInfo\":\"[{\\\"server_version\\\": 1453706974528, \\\"poc\\\": \\\"{\\\\\\\"investigations\\\\\\\":[\\\\\\\"laboratory-Blood Test for Human chorionic gonadotropin (hCG)\\\\\\\"],\\\\\\\"planofCareDate\\\\\\\":\\\\\\\"25-01-2015\\\\\\\",\\\\\\\"drugs\\\\\\\":[],\\\\\\\"diagnosis\\\\\\\":[\\\\\\\"O01 - Hydatidiform mole\\\\\\\"],\\\\\\\"visitNumber\\\\\\\":\\\\\\\"2\\\\\\\",\\\\\\\"doctorName\\\\\\\":\\\\\\\"doc111\\\\\\\",\\\\\\\"advice\\\\\\\":\\\\\\\"kckfkf\\\\\\\",\\\\\\\"visitType\\\\\\\":\\\\\\\"ANC\\\\\\\"}\\\", \\\"pending\\\": \\\"\\\"}]\",\"visitNumber\":\"2\",\"fetalData\":\"122\",\"visitDate\":\"2016-01-15\",\"bpDiastolic\":\"61\",\"bloodGlucoseData\":\"5.3\",\"bpSystolic\":\"103\",\"temperature\":\"36.8-C\",\"pulseRate\":\"88\",\"server_version\":1453706974528,\"visitPerson\":\"lhv\",\"visit_type\":\"ANC\",\"anmPoc\":\"\",\"documentId\":\"38d7b454ed2a0f8a5b2f534ae65e580f\"}";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("formInfo", dummyData);
        ActivityController activityController = Robolectric.buildActivity(ViewPreVisitScreenActivity.class).withIntent(intent).create();
    }


}
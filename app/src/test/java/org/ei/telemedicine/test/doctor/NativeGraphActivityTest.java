package org.ei.telemedicine.test.doctor;


import android.content.Intent;
import android.graphics.Typeface;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.doctor.NativeGraphActivity;
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
public class NativeGraphActivityTest {

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

        String dummyData = "[{\"bpSystolic\": \"0\", \"visit_number\": \"1\", \"temperature\": \"0\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"0\", \"visitDate\": \"2012-01-01\", \"bloodGlucoseData\": \"0\", \"fetalData\": \"0\"}, {\"bpSystolic\": \"103\", \"visit_number\": \"2\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"61\", \"visitDate\": \"2016-01-15\", \"bloodGlucoseData\": \"5.3\", \"fetalData\": \"122\"}, {\"bpSystolic\": \"\", \"visit_number\": \"4\", \"temperature\": \"33-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"\", \"visitDate\": \"2015-01-25\", \"bloodGlucoseData\": \"\", \"fetalData\": \"\"}]";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(AllConstants.VITALS_INFO_RESULT, dummyData);
        intent.putExtra(AllConstants.VITAL_TYPE, AllConstants.GraphFields.TEMPERATURE);
        ActivityController activityController = Robolectric.buildActivity(NativeGraphActivity.class).withIntent(intent).create();
    }

    @Test
    public void testingBPChart() {
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);

        String dummyData = "[{\"bpSystolic\": \"130\", \"visit_number\": \"1\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"86\", \"visitDate\": \"2016-01-22\", \"bloodGlucoseData\": \"5.3\", \"fetalData\": \"113\"}, {\"bpSystolic\": \"139\", \"visit_number\": \"1\", \"temperature\": \"36.5-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"91\", \"visitDate\": \"2016-01-07\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"127\"}, {\"bpSystolic\": \"120\", \"visit_number\": \"2\", \"temperature\": \"36.1-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"60\", \"visitDate\": \"2016-01-21\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"122\"}, {\"bpSystolic\": \"120\", \"visit_number\": \"2\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"67\", \"visitDate\": \"2016-01-21\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"120\"}]";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(AllConstants.VITALS_INFO_RESULT, dummyData);
        intent.putExtra(AllConstants.VITAL_TYPE, AllConstants.GraphFields.BP);
        ActivityController activityController = Robolectric.buildActivity(NativeGraphActivity.class).withIntent(intent).create();

    }

    @Test
    public void testingBloodChart() {
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);

        String dummyData = "[{\"bpSystolic\": \"130\", \"visit_number\": \"1\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"86\", \"visitDate\": \"2016-01-22\", \"bloodGlucoseData\": \"5.3\", \"fetalData\": \"113\"}, {\"bpSystolic\": \"139\", \"visit_number\": \"1\", \"temperature\": \"36.5-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"91\", \"visitDate\": \"2016-01-07\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"127\"}, {\"bpSystolic\": \"120\", \"visit_number\": \"2\", \"temperature\": \"36.1-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"60\", \"visitDate\": \"2016-01-21\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"122\"}, {\"bpSystolic\": \"120\", \"visit_number\": \"2\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"67\", \"visitDate\": \"2016-01-21\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"120\"}]";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(AllConstants.VITALS_INFO_RESULT, dummyData);
        intent.putExtra(AllConstants.VITAL_TYPE, AllConstants.GraphFields.BLOODGLUCOSEDATA);
        ActivityController activityController = Robolectric.buildActivity(NativeGraphActivity.class).withIntent(intent).create();

    }

    @Test
    public void testingfetalChart() {
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);

        String dummyData = "[{\"bpSystolic\": \"130\", \"visit_number\": \"1\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"86\", \"visitDate\": \"2016-01-22\", \"bloodGlucoseData\": \"5.3\", \"fetalData\": \"113\"}, {\"bpSystolic\": \"139\", \"visit_number\": \"1\", \"temperature\": \"36.5-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"91\", \"visitDate\": \"2016-01-07\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"127\"}, {\"bpSystolic\": \"120\", \"visit_number\": \"2\", \"temperature\": \"36.1-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"60\", \"visitDate\": \"2016-01-21\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"122\"}, {\"bpSystolic\": \"120\", \"visit_number\": \"2\", \"temperature\": \"36.8-C\", \"visit_type\": \"ANC\", \"bpDiastolic\": \"67\", \"visitDate\": \"2016-01-21\", \"bloodGlucoseData\": \"4.8\", \"fetalData\": \"120\"}]";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(AllConstants.VITALS_INFO_RESULT, dummyData);
        intent.putExtra(AllConstants.VITAL_TYPE, AllConstants.GraphFields.FETALDATA);
        ActivityController activityController = Robolectric.buildActivity(NativeGraphActivity.class).withIntent(intent).create();

    }


}
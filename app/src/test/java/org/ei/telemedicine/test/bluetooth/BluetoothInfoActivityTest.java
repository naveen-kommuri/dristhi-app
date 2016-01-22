package org.ei.telemedicine.test.bluetooth;

import android.content.Intent;

import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.bluetooth.BlueToothInfoActivity;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSettings;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 19, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class BluetoothInfoActivityTest {
    @Mock
    Cache cache;
    @Mock
    private Context context;
    @Mock
    private DrishtiApplication drishtiApplication;
    @Mock
    private AllDoctorRepository allDoctorRepository;
    @Mock
    private AllSettings allSettings;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
//        DrishtiApplication application = mock(DrishtiApplication.class);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("entityId", "sdsd");
        intent.putExtra("instanceId", "sdsd");
        intent.putExtra("formName", "sdsd");
        intent.putExtra("subFormCount", "sdsd");
        intent.putExtra("pncRisks", "sdsd");
        intent.putExtra("childSigns", "sdsd");
        intent.putExtra("riskObservedDuringANC", "sdsd");
//        when(Context.getInstance()).thenReturn(context);
//        when(Context.getInstance().updateApplicationContext(application.getApplicationContext())).thenReturn(context);
        when(context.typefaceCache()).thenReturn(cache);
        when(context.allSettings()).thenReturn(allSettings);
        when(allSettings.fetchANMConfiguration("temperature")).thenReturn("c");
        when(context.IsUserLoggedOut()).thenReturn(false);
        ActivityController activityController = Robolectric.buildActivity(BlueToothInfoActivity.class).withIntent(intent).create();
    }


    @Test
    public void testSample() {
        assertEquals("2", 1 + 1 + "");
    }
}
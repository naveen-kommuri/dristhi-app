package org.ei.telemedicine.test.view.activity;

import android.content.Intent;

import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.view.activity.NativeReportsActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class NativeReportsActivityTest {
    @Before
    public void setup() {
        initMocks(this);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ActivityController<NativeReportsActivity> activityController = Robolectric.buildActivity(NativeReportsActivity.class).withIntent(intent).create();
    }

    @Test
    public void testingancPoc() {
        assertEquals("sd", "sd");
    }

}
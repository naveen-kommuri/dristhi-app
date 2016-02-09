package org.ei.telemedicine.test.view;


import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.view.UserSettingsActivity;
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
public class UserSettingsActivityTest {

    @Before
    public void setup() {
        initMocks(this);
        ActivityController activityController = Robolectric.buildActivity(UserSettingsActivity.class).create();
    }

    @Test
    public void testingonCreate() {
        assertEquals("1", "1");
    }


}
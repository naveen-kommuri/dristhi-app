package org.ei.telemedicine.test.view.activity;

import android.graphics.Typeface;

import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.repository.AllSettings;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.UserService;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.util.Session;
import org.ei.telemedicine.view.activity.NativeHomeActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class NativeHomeActivityTest {

    private ActivityController<NativeHomeActivity> homeActivity;
    @Mock
    Context context;
    @Mock
    UserService userService;
    @Mock
    Session session;
    @Mock
    private Cache<Typeface> cache;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    @Mock
    private AllSettings allSettings;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);
        when(context.session()).thenReturn(session);
        when(session.hasExpired()).thenReturn(false);
        when(context.userService()).thenReturn(userService);
        when(userService.hasSessionExpired()).thenReturn(false);
        when(context.IsUserLoggedOut()).thenReturn(false);
        when(allSharedPreferences.fetchIsFirstLogin()).thenReturn(true);
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("anm1");
        when(context.allSettings()).thenReturn(allSettings);
        when(allSettings.fetchANMLocation()).thenReturn("{\"phcName\":\"phc\",\"subCenter\":\"sub1\"}");
        homeActivity = Robolectric.buildActivity(NativeHomeActivity.class).create().resume();
    }

    @Test
    public void tesVal() {
        assertEquals("1", "1");
    }
//
//    @Test
//    public void shouldLaunchEcRegisterOnPressingEcRegisterButton() {
//        verifyLaunchOfActivityOnPressingButton(R.id.btn_ec_register, NativeECSmartRegisterActivity.class);
//    }
//
//    @Test
//    public void shouldLaunchAncRegisterOnPressingAncRegisterButton() {
//        verifyLaunchOfActivityOnPressingButton(R.id.btn_anc_register, NativeANCSmartRegisterActivity.class);
//    }
//
//    @Test
//    public void shouldLaunchPncRegisterOnPressingPncRegisterButton() {
//        verifyLaunchOfActivityOnPressingButton(R.id.btn_pnc_register, NativePNCSmartRegisterActivity.class);
//    }
//
//    @Test
//    public void shouldLaunchFpRegisterOnPressingFpRegisterButton() {
//        verifyLaunchOfActivityOnPressingButton(R.id.btn_fp_register, NativeFPSmartRegisterActivity.class);
//    }
//
//    @Test
//    public void shouldLaunchChildRegisterOnPressingChildRegisterButton() {
//        verifyLaunchOfActivityOnPressingButton(R.id.btn_child_register, NativeChildSmartRegisterActivity.class);
//
//    }
//
//
//    public <T> void verifyLaunchOfActivityOnPressingButton(int buttonId, Class<T> clazz) {
//        ShadowActivity shadowHome = shadowOf(homeActivity);
//        homeActivity.findViewById(buttonId).performClick();
//
//        assertEquals(clazz.getName(),
//                shadowHome.getNextStartedActivity().getComponent().getClassName());
//    }
//

}
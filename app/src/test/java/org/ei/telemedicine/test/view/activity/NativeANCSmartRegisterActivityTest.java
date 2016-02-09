package org.ei.telemedicine.test.view.activity;


import android.graphics.Typeface;

import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.repository.AllBeneficiaries;
import org.ei.telemedicine.repository.AllEligibleCouples;
import org.ei.telemedicine.repository.AllSettings;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.ActionService;
import org.ei.telemedicine.service.AlertService;
import org.ei.telemedicine.service.FormSubmissionSyncService;
import org.ei.telemedicine.service.ServiceProvidedService;
import org.ei.telemedicine.service.UserService;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.view.activity.DrishtiApplication;
import org.ei.telemedicine.view.activity.NativeANCSmartRegisterActivity;
import org.ei.telemedicine.view.contract.ANCClients;
import org.ei.telemedicine.view.contract.Villages;
import org.ei.telemedicine.view.controller.ANMLocationController;
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
public class NativeANCSmartRegisterActivityTest {

    @Mock
    Context context;

    @Mock
    DrishtiApplication drishtiApplication;
    @Mock
    private Cache<Typeface> cache;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    @Mock
    private AllEligibleCouples allEligibleCouples;
    @Mock
    private ActionService actionService;
    @Mock
    private UserService userService;
    @Mock
    private FormSubmissionSyncService formSubmissionSyncService;

    @Mock
    private AllBeneficiaries allBeneficiaries;
    @Mock
    private Cache<String> listcache;
    @Mock
    private Cache<ANCClients> ancClientsCache;
    @Mock
    private Cache<Villages> villagesCache;
    @Mock
    private ANMLocationController anmLocationController;
    @Mock
    private AllSettings allSettings;
    @Mock
    private AlertService alertService;
    @Mock
    private ServiceProvidedService serviceProvidedService;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        when(context.serviceProvidedService()).thenReturn(serviceProvidedService);
        when(context.alertService()).thenReturn(alertService);
        when(context.allBeneficiaries()).thenReturn(allBeneficiaries);
        when(context.listCache()).thenReturn(listcache);
        when(context.ancClientsCache()).thenReturn(ancClientsCache);
        when(context.allEligibleCouples()).thenReturn(allEligibleCouples);
        when(context.villagesCache()).thenReturn(villagesCache);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(context.typefaceCache()).thenReturn(cache);
        ActivityController<NativeANCSmartRegisterActivity> nativeANCSmartRegisterActivityActivityController = Robolectric.buildActivity(NativeANCSmartRegisterActivity.class).create().resume().destroy();
    }

    @Test
    public void testVal() {
        assertEquals("1", "1");
    }

}
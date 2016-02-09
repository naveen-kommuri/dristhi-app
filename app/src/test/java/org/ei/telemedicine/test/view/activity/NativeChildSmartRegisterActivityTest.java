//package org.ei.telemedicine.test.view.activity;
//
//
//import android.graphics.Typeface;
//
//import org.ei.telemedicine.BuildConfig;
//import org.ei.telemedicine.Context;
//import org.ei.telemedicine.repository.AllBeneficiaries;
//import org.ei.telemedicine.repository.AllEligibleCouples;
//import org.ei.telemedicine.repository.AllSettings;
//import org.ei.telemedicine.repository.AllSharedPreferences;
//import org.ei.telemedicine.service.ActionService;
//import org.ei.telemedicine.service.AlertService;
//import org.ei.telemedicine.service.FormSubmissionSyncService;
//import org.ei.telemedicine.service.ServiceProvidedService;
//import org.ei.telemedicine.service.UserService;
//import org.ei.telemedicine.util.Cache;
//import org.ei.telemedicine.view.activity.DrishtiApplication;
//import org.ei.telemedicine.view.activity.NativeChildSmartRegisterActivity;
//import org.ei.telemedicine.view.contract.ECClients;
//import org.ei.telemedicine.view.contract.SmartRegisterClients;
//import org.ei.telemedicine.view.contract.Villages;
//import org.ei.telemedicine.view.controller.ANMLocationController;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//import org.robolectric.util.ActivityController;
//
//import static junit.framework.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
//@RunWith(RobolectricTestRunner.class)
//public class NativeChildSmartRegisterActivityTest {
//
//    @Mock
//    Context context;
//
//    @Mock
//    DrishtiApplication drishtiApplication;
//    @Mock
//    private Cache<Typeface> cache;
//    @Mock
//    private AllSharedPreferences allSharedPreferences;
//    @Mock
//    private AllEligibleCouples allEligibleCouples;
//    @Mock
//    private ActionService actionService;
//    @Mock
//    private UserService userService;
//    @Mock
//    private FormSubmissionSyncService formSubmissionSyncService;
//    ActivityController<NativeChildSmartRegisterActivity> nativeChildSmartRegisterActivity;
//    @Mock
//    private AllBeneficiaries allBeneficiaries;
//    @Mock
//    private Cache<String> listcache;
//    @Mock
//    private Cache<ECClients> ecClientsCache;
//    @Mock
//    private Cache<Villages> villagesCache;
//    @Mock
//    private ANMLocationController anmLocationController;
//    @Mock
//    private AllSettings allSettings;
//    @Mock
//    private Cache<SmartRegisterClients> smartRegisterClientsCache;
//    @Mock
//    private AlertService alertService;
//    @Mock
//    private ServiceProvidedService serviceProvidedService;
//
//    @Before
//    public void setup() {
//        initMocks(this);
//        Context.setInstance(context);
//        when(context.serviceProvidedService()).thenReturn(serviceProvidedService);
//        when(context.alertService()).thenReturn(alertService);
//        when(context.smartRegisterClientsCache()).thenReturn(smartRegisterClientsCache);
//        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
//        when(context.typefaceCache()).thenReturn(cache);
//        when(context.allBeneficiaries()).thenReturn(allBeneficiaries);
//        when(context.listCache()).thenReturn(listcache);
//        when(context.villagesCache()).thenReturn(villagesCache);
//        when(context.anmLocationController()).thenReturn(anmLocationController);
//        when(context.allSettings()).thenReturn(allSettings);
//        when(context.allEligibleCouples()).thenReturn(allEligibleCouples);
//
//        nativeChildSmartRegisterActivity = Robolectric.buildActivity(NativeChildSmartRegisterActivity.class).create().resume();
//    }
//
//    @Test
//    public void testVal() {
//        assertEquals("1", "1");
//    }
//}
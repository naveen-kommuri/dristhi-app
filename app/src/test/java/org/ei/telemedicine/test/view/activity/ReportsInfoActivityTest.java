//package org.ei.telemedicine.test.view.activity;
//
//import android.content.Intent;
//import android.graphics.Typeface;
//
//import org.ei.telemedicine.AllConstants;
//import org.ei.telemedicine.BuildConfig;
//import org.ei.telemedicine.Context;
//import org.ei.telemedicine.DristhiConfiguration;
//import org.ei.telemedicine.repository.AllSharedPreferences;
//import org.ei.telemedicine.service.UserService;
//import org.ei.telemedicine.util.Cache;
//import org.ei.telemedicine.view.activity.ReportsInfoActivity;
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
///**
// * Created by naveen on 2/1/16.
// */
//@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
//@RunWith(RobolectricTestRunner.class)
//public class ReportsInfoActivityTest {
//    @Mock
//    private Context context;
//    @Mock
//    private DristhiConfiguration configuration;
//    @Mock
//    private UserService userService;
//    @Mock
//    private AllSharedPreferences allSharedPreferences;
//    @Mock
//    private Cache<Typeface> cache;
//
//    @Before
//    public void setup() {
//        initMocks(this);
//        Context.setInstance(context);
//        when(context.typefaceCache()).thenReturn(cache);
//        when(context.configuration()).thenReturn(configuration);
//        when(context.userService()).thenReturn(userService);
//        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django");
//        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
//        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("anm333");
//        when(userService.gettingFromRemoteURL("http://django/reporting/?activity=fp&anmid=anm333")).thenReturn("[{\"percentage\": 0, \"year\": 0, \"name\": \"Condom usage\", \"annual_target\": 0, \"month\": 0}, {\"percentage\": 0, \"year\": 0, \"name\": \"Condom pieces\", \"annual_target\": 0, \"month\": 0}, {\"percentage\": 0, \"year\": 0, \"name\": \"IUD Adoption\", \"annual_target\": 0, \"month\": 0}, {\"percentage\": 0, \"year\": 0, \"name\": \"Oral Pills\", \"annual_target\": 0, \"month\": 0}]");
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.putExtra("service", AllConstants.Reports.FP_SERVICES);
//        ActivityController<ReportsInfoActivity> activityController = Robolectric.buildActivity(ReportsInfoActivity.class).withIntent(intent).create();
//    }
//
//    @Test
//    public void testingOnCreate() {
//        assertEquals("sd", "sd");
//    }
//}

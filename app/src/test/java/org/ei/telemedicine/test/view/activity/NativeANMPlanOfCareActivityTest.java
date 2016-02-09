//package org.ei.telemedicine.test.view.activity;
//
//import android.content.Intent;
//
//import org.ei.telemedicine.BuildConfig;
//import org.ei.telemedicine.Context;
//import org.ei.telemedicine.repository.AllSettings;
//import org.ei.telemedicine.repository.AllSharedPreferences;
//import org.ei.telemedicine.util.Cache;
//import org.ei.telemedicine.view.activity.NativeANMPlanofCareActivity;
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
//public class NativeANMPlanOfCareActivityTest {
//
//    @Mock
//    Context context;
//    @Mock
//    private AllSettings allSettings;
//    @Mock
//    private AllSharedPreferences allSharedPreferences;
//    @Mock
//    Cache cache;
//
//    @Before
//    public void setup() {
//        initMocks(this);
//        Context.setInstance(context);
//
////        String drugsData = "{\"Pallor\": [\"Dexorange \"], \"Heavy Bleeding per vaginum\": [\"Tranexemic\"], \"Difficult Breathing\": [\"Albuterol\"], \"Breast Hardness\": [\"Ampicillin\"], \"Abdominal Pain\": [\"Spasmopriv\"], \"Diarrhea\": [\"Valamycin \"], \"Mealses\": [\"Paracetomal Syrup\"], \"Severe Acute Mal Nutrition\": [\"Protein and Fatty Acid Supplement\"], \"Convulsions\": [\"Eption Tablets\", \"Epitoine\"], \"Jaundice\": [\"Hepamers\"], \"Blurred Vision\": [\"Terbinafine\"], \"Infected perineum suture\": [\"Metrogyl\"], \"Fever\": [\"Calpol\"], \"Malaria\": [\"Chloroquine\"], \"Bleeding\": [\"Tranexemic\"], \"Swelling\": [\"Chymerolforte\"], \"Nipple Hardness\": [\"Ampicillin\"], \"Burning sensation when urinating\": [\"Cefixime\"], \"Vomiting\": [\"Vomikind\"], \"Diarrhea and dehydration\": [\"Valamycin \"], \"Bad Headache\": [\"Acetaminophen\"], \"Cough\": [\"Tixlix\"]}";
//        String drugsData = "{Pallor: [Dexorange ]";
////        String drugsData = "";
//        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
//        when(context.typefaceCache()).thenReturn(cache);
//        when(context.allSettings()).thenReturn(allSettings);
//        when(allSettings.fetchDrugs()).thenReturn(drugsData);
////        when(context.getColorResource(R.color.light_blue)).thenReturn(000000);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
////        intent.putExtra(AllConstants.ANCVisitFields.RISKS, "Pallor");
//
//        ActivityController<NativeANMPlanofCareActivity> activityController = Robolectric.buildActivity(NativeANMPlanofCareActivity.class).withIntent(intent).create();
//
//    }
//
//    @Test
//    public void testingancPoc() {
//        assertEquals("ad", "ad");
//    }
//
//}
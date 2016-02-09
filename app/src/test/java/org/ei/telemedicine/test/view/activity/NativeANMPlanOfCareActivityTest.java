package org.ei.telemedicine.test.view.activity;

import android.content.Intent;
import android.graphics.Typeface;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.domain.EligibleCouple;
import org.ei.telemedicine.domain.Mother;
import org.ei.telemedicine.repository.AllBeneficiaries;
import org.ei.telemedicine.repository.AllEligibleCouples;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.ChildService;
import org.ei.telemedicine.view.activity.ViewPlanOfCareActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(sdk = 21, manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class NativeANMPlanOfCareActivityTest {

    @Mock
    Context context;
    @Mock
    private ChildService childService;
    @Mock
    private AllEligibleCouples allEligibleCouples;
    @Mock
    private AllBeneficiaries allBeneficiaries;
    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private org.ei.telemedicine.util.Cache<Typeface> cache;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
    }

    @Test
    public void testingancPoc() {

        String pocData = "[{\"pending\": \"\", \"poc\": \"{\\\"investigations\\\":[\\\"laboratory-Complete blood count\\\",\\\"procedures-minilaparotomy\\\"],\\\"planofCareDate\\\":\\\"13-01-2016\\\",\\\"drugs\\\":[{\\\"dosage\\\":\\\"5 ml\\\",\\\"frequency\\\":\\\"Every 24 Hours\\\",\\\"drugNoOfDays\\\":\\\"5\\\",\\\"drugQty\\\":\\\"5\\\",\\\"direction\\\":\\\"before break fast\\\",\\\"drugName\\\":\\\"Metrogyl\\\"},{\\\"dosage\\\":\\\"100 mg\\\",\\\"frequency\\\":\\\"Every 24 Hours\\\",\\\"drugNoOfDays\\\":\\\"7\\\",\\\"drugQty\\\":\\\"7\\\",\\\"direction\\\":\\\"To be chewed\\\",\\\"drugName\\\":\\\"Epitoine\\\"}],\\\"diagnosis\\\":[\\\"O08.1 - Delayed or excessive hemorrhage following ectopic and molar pregnancy\\\\t\\\",\\\"O11 - Pre-existing hypertensive disorder with superimposed proteinuria\\\"],\\\"visitNumber\\\":\\\"1\\\",\\\"doctorName\\\":\\\"doc111\\\",\\\"advice\\\":\\\"\\\",\\\"visitType\\\":\\\"ANC\\\"}\", \"server_version\": 1452674086358}, {\"pending\": \"still need to check\", \"poc\": \"{\\\"investigations\\\":[\\\"laboratory-Blood Test for Human chorionic gonadotropin (hCG)\\\"],\\\"planofCareDate\\\":\\\"13-01-2016\\\",\\\"drugs\\\":[{\\\"dosage\\\":\\\"35 ml\\\",\\\"frequency\\\":\\\"Every 24 Hours\\\",\\\"drugNoOfDays\\\":\\\"5\\\",\\\"drugQty\\\":\\\"5\\\",\\\"direction\\\":\\\"Take Orally\\\",\\\"drugName\\\":\\\"Tixlix\\\"}],\\\"diagnosis\\\":[\\\"O08.5 - Metabolic disorders following an ectopic and molar pregnancy\\\\t\\\"],\\\"visitNumber\\\":\\\"1\\\",\\\"doctorName\\\":\\\"doc111\\\",\\\"advice\\\":\\\"\\\",\\\"visitType\\\":\\\"ANC\\\"}\", \"server_version\": 1452674163745}, {\"pending\": \"\", \"poc\": \"{\\\"investigations\\\":[\\\"laboratory-Complete blood count\\\"],\\\"planofCareDate\\\":\\\"13-01-2016\\\",\\\"drugs\\\":[{\\\"dosage\\\":\\\"100 mg\\\",\\\"frequency\\\":\\\"3 times a day\\\",\\\"drugNoOfDays\\\":\\\"5\\\",\\\"drugQty\\\":\\\"5\\\",\\\"direction\\\":\\\"For every 1 hour\\\",\\\"drugName\\\":\\\"Tranexemic\\\"}],\\\"diagnosis\\\":[\\\"O08.1 - Delayed or excessive hemorrhage following ectopic and molar pregnancy\\\\t\\\"],\\\"visitNumber\\\":\\\"1\\\",\\\"doctorName\\\":\\\"doc111\\\",\\\"advice\\\":\\\"Take rest\\\",\\\"visitType\\\":\\\"ANC\\\"}\", \"server_version\": 1452674606786}, {\"pending\": \"\", \"poc\": \"{\\\"investigations\\\":[\\\"laboratory-Blood Test for Human chorionic gonadotropin (hCG)\\\"],\\\"planofCareDate\\\":\\\"13-01-2016\\\",\\\"drugs\\\":[{\\\"dosage\\\":\\\"200 mg\\\",\\\"frequency\\\":\\\"Every 12 Hours\\\",\\\"drugNoOfDays\\\":\\\"10\\\",\\\"drugQty\\\":\\\"1\\\",\\\"direction\\\":\\\"Take with Food\\\",\\\"drugName\\\":\\\"Albuterol\\\"}],\\\"diagnosis\\\":[\\\"O08.1 - Delayed or excessive hemorrhage following ectopic and molar pregnancy\\\\t\\\"],\\\"visitNumber\\\":\\\"2\\\",\\\"doctorName\\\":\\\"doc111\\\",\\\"advice\\\":\\\"\\\",\\\"visitType\\\":\\\"ANC\\\"}\", \"server_version\": 1452692692176}, {\"server_version\": 1453459750416, \"poc\": \"{\\\"investigations\\\":[\\\"laboratory-Blood Test for Human chorionic gonadotropin (hCG)\\\"],\\\"planofCareDate\\\":\\\"22-01-2016\\\",\\\"drugs\\\":[{\\\"dosage\\\":\\\"35 ml\\\",\\\"frequency\\\":\\\"Every 12 Hours\\\",\\\"drugNoOfDays\\\":\\\"3\\\",\\\"drugQty\\\":\\\"3\\\",\\\"direction\\\":\\\"before break fast\\\",\\\"drugName\\\":\\\"Paracetomal Syrup\\\"}],\\\"diagnosis\\\":[\\\"O08.5 - Metabolic disorders following an ectopic and molar pregnancy\\\\t\\\"],\\\"visitNumber\\\":\\\"3\\\",\\\"doctorName\\\":\\\"doc111\\\",\\\"advice\\\":\\\"take rest\\\",\\\"visitType\\\":\\\"ANC\\\"}\", \"pending\": \"\"}]";
//        String pocData = "";
        when(context.typefaceCache()).thenReturn(cache);
        when(context.childService()).thenReturn(childService);
        when(context.allEligibleCouples()).thenReturn(allEligibleCouples);
        when(context.allBeneficiaries()).thenReturn(allBeneficiaries);
        Map<String, String> dummyData = new HashMap<String, String>();
        dummyData.put("docPocInfo", pocData);
        dummyData.put("ancNumber", "anc1");
        when(childService.getMotherUseEntityId("entity1")).thenReturn(new Mother("case1", "ecCase1", "", "00-00-0000").withDetails(dummyData));
        EligibleCouple eligibleCouple = new EligibleCouple("case1", "wife1", "husband1", "123", "village1", "sub1", dummyData);
        when(allEligibleCouples.findByCaseID("ecCase1")).thenReturn(eligibleCouple);
//        when(mother.getDetail("docPocInfo")).thenReturn(pocData);
//        when(eligibleCouple.wifeName()).thenReturn("Wife1");
//        when(mother.getDetail("ancNumber")).thenReturn("anc1");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(AllConstants.ENTITY_ID, "entity1");
        intent.putExtra(AllConstants.VISIT_TYPE, "ANC");
        ActivityController<ViewPlanOfCareActivity> activityController = Robolectric.buildActivity(ViewPlanOfCareActivity.class).withIntent(intent).create().resume();
    }

}
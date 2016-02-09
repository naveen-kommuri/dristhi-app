package org.ei.telemedicine.test.bluetooth;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.BuildConfig;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.bluetooth.BlueToothInfoActivity;
import org.ei.telemedicine.bluetooth.Constants;
import org.ei.telemedicine.domain.SyncStatus;
import org.ei.telemedicine.domain.form.FormSubmission;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSettings;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.repository.FormDataRepository;
import org.ei.telemedicine.service.UserService;
import org.ei.telemedicine.util.Cache;
import org.ei.telemedicine.view.activity.DrishtiApplication;
import org.ei.telemedicine.view.controller.ANMLocationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    BlueToothInfoActivity blueToothInfoActivity;
    ActivityController activityController;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    @Mock
    private FormDataRepository formDataRepository;
    @Mock
    private UserService userService;
    @Mock
    private ANMLocationController anmLocationController;

    @Before
    public void setup() {
        initMocks(this);
        Context.setInstance(context);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("entityId", "entity1");
        intent.putExtra("instanceId", "instance1");
        intent.putExtra("formName", "pnc_visit");
        intent.putExtra("subFormCount", 3);
        intent.putExtra("pncRisks", "pncrisk1");
        intent.putExtra("childSigns", "");
        intent.putExtra("riskObservedDuringANC", "");
        when(context.typefaceCache()).thenReturn(cache);
        when(context.allSettings()).thenReturn(allSettings);
        when(context.allSharedPreferences()).thenReturn(allSharedPreferences);
        when(allSettings.fetchANMConfiguration("temperature")).thenReturn("c");
        when(context.IsUserLoggedOut()).thenReturn(false);
        activityController = Robolectric.buildActivity(BlueToothInfoActivity.class).withIntent(intent).create().start().resume();
        blueToothInfoActivity = (BlueToothInfoActivity) activityController.get();
    }

    @Test
    public void testChildForm() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("entityId", "entity2");
        intent.putExtra("instanceId", "instance2");
        intent.putExtra("formName", AllConstants.FormNames.CHILD_ILLNESS);
        intent.putExtra("subFormCount", 3);
        intent.putExtra("pncRisks", "");
        intent.putExtra("childSigns", "childsign1");
        intent.putExtra("riskObservedDuringANC", "");
        ActivityController activityController = Robolectric.buildActivity(BlueToothInfoActivity.class).withIntent(intent).create().start().resume();
    }

    @Test
    public void testANCForm() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("entityId", "entity3");
        intent.putExtra("instanceId", "instance3");
        intent.putExtra("formName", AllConstants.FormNames.ANC_VISIT);
        intent.putExtra("subFormCount", 0);
        intent.putExtra("pncRisks", "");
        intent.putExtra("childSigns", "");
        intent.putExtra("riskObservedDuringANC", "ancRisk1");
        BlueToothInfoActivity activityController = Robolectric.buildActivity(BlueToothInfoActivity.class).withIntent(intent).create().start().resume().get();
        when(context.anmLocationController()).thenReturn(anmLocationController);
        when(anmLocationController.getFormInfoJSON()).thenReturn("{\"field1\":\"value1\"}");
        activityController.onBackPressed();
    }


    @Test
    public void testSample() {
        assertEquals("2", 1 + 1 + "");
        assertNotNull(blueToothInfoActivity);
        ImageView iv_bgm = (ImageView) blueToothInfoActivity.findViewById(R.id.iv_bgm);
        assertNotNull(iv_bgm);
        iv_bgm.performClick();
        ImageView iv_eet = (ImageView) blueToothInfoActivity.findViewById(R.id.iv_eet);
        iv_eet.performClick();
        ImageView iv_fetal = (ImageView) blueToothInfoActivity.findViewById(R.id.iv_fetal);
        iv_fetal.performClick();
        ImageView iv_bp = (ImageView) blueToothInfoActivity.findViewById(R.id.iv_bp);
        iv_bp.performClick();
        ImageView iv_steh = (ImageView) blueToothInfoActivity.findViewById(R.id.iv_steh);
        iv_steh.performClick();
        ImageView iv_poc = (ImageView) blueToothInfoActivity.findViewById(R.id.iv_poc);
        iv_poc.performClick();
        ImageButton ib_record = (ImageButton) blueToothInfoActivity.findViewById(R.id.ib_record);
        ib_record.performClick();
        EditText et_bgm = (EditText) blueToothInfoActivity.findViewById(R.id.et_bgm_mmoi);
        et_bgm.setText("10");
    }

    @Test
    public void testSavePoc() {
        EditText et_bp_sys, et_bp_dia, et_bgm, et_bgm_mg, et_fetal, et_eet, et_bp_heart;
        et_bp_heart = (EditText) blueToothInfoActivity.findViewById(R.id.et_bp_heart);
        et_bgm = (EditText) blueToothInfoActivity.findViewById(R.id.et_bgm_mmoi);
        et_bgm_mg = (EditText) blueToothInfoActivity.findViewById(R.id.et_bgm_mg);
        et_bp_sys = (EditText) blueToothInfoActivity.findViewById(R.id.et_bp_sys);
        et_bp_dia = (EditText) blueToothInfoActivity.findViewById(R.id.et_bp_dia);
        et_fetal = (EditText) blueToothInfoActivity.findViewById(R.id.et_fetal);
        et_eet = (EditText) blueToothInfoActivity.findViewById(R.id.et_eet);

        et_bp_dia.setText("");
        et_bp_heart.setText("");
        et_bgm.setText("");
        et_bgm_mg.setText("");
        et_fetal.setText("");
        et_eet.setText("a");
        when(context.formDataRepository()).thenReturn(formDataRepository);
        String instance = "{\"form_data_definition_version\":\"1\",\"form\":{\"bind_type\":\"mother\",\"default_bind_path\":\"/model/instance/PNC_Visit_EngKan/\",\"fields\":[{\"name\":\"id\",\"value\":\"3ed96ef1-2445-4215-ad3a-8d58ea20078c\",\"source\":\"mother.id\"},{\"name\":\"pncNumber\",\"value\":\"123\",\"source\":\"mother.eligible_couple.pncNumber\"},{\"name\":\"ecId\",\"value\":\"9e80a8dc-769c-4b19-86c2-bc7638d6ed0d\",\"source\":\"mother.eligible_couple.id\"},{\"name\":\"wifeName\",\"value\":\"murage\",\"source\":\"mother.eligible_couple.wifeName\"},{\"name\":\"deliveryOutcome\",\"value\":\"live_birth\",\"source\":\"mother.deliveryOutcome\"},{\"name\":\"didWomanSurvive\",\"value\":\"yes\",\"source\":\"mother.didWomanSurvive\"},{\"name\":\"didMotherSurvive\",\"source\":\"mother.didMotherSurvive\"},{\"name\":\"deliveryPlace\",\"value\":\"phc\",\"source\":\"mother.deliveryPlace\"},{\"name\":\"dischargeDate\",\"source\":\"mother.dischargeDate\"},{\"name\":\"deliveryType\",\"source\":\"mother.deliveryType\"},{\"name\":\"referenceDate\",\"value\":\"2015-06-11\",\"source\":\"mother.referenceDate\"},{\"name\":\"dischargeDate\",\"value\":\"2015-06-05\",\"source\":\"mother.dischargeDate\"},{\"name\":\"pncVisitDate\",\"value\":\"2015-06-17\",\"source\":\"mother.pncVisitDate\"},{\"name\":\"pncVisitDay\",\"value\":\"6\",\"source\":\"mother.pncVisitDay\"},{\"name\":\"pncVisitPlace\",\"value\":\"home\",\"source\":\"mother.pncVisitPlace\"},{\"name\":\"pncVisitPerson\",\"value\":\"anm\",\"source\":\"mother.pncVisitPerson\"},{\"name\":\"difficulties1\",\"value\":\"bad_headache\",\"source\":\"mother.difficulties1\"},{\"name\":\"abdominalProblems\",\"value\":\"abdominal_pain\",\"source\":\"mother.abdominalProblems\"},{\"name\":\"vaginalProblems\",\"value\":\"no_problems\",\"source\":\"mother.vaginalProblems\"},{\"name\":\"difficulties2\",\"value\":\"no_problems\",\"source\":\"mother.difficulties2\"},{\"name\":\"breastProblems\",\"value\":\"no_problems\",\"source\":\"mother.breastProblems\"},{\"name\":\"cesareanIncisionArea\",\"source\":\"mother.cesareanIncisionArea\"},{\"name\":\"hasFeverSymptoms\",\"value\":\"no\",\"source\":\"mother.hasFeverSymptoms\"},{\"name\":\"temperature\",\"value\":\"28.0\",\"source\":\"mother.temperature\"},{\"name\":\"hasFever\",\"source\":\"mother.hasFever\"},{\"name\":\"pulseRate\",\"source\":\"mother.pulseRate\"},{\"name\":\"bpSystolic\",\"value\":\"120\",\"source\":\"mother.bpSystolic\"},{\"name\":\"temperature\",\"value\":\"28.0\",\"source\":\"mother.temperature\"},{\"name\":\"fetalData\",\"value\":\"0\",\"source\":\"mother.fetalData\"},{\"name\":\"bloodGlucoseData\",\"value\":\"0\",\"source\":\"mother.bloodGlucoseData\"},{\"name\":\"bpDiastolic\",\"value\":\"90\",\"source\":\"mother.bpDiastolic\"},{\"name\":\"hbLevel\",\"value\":\"2\",\"source\":\"mother.hbLevel\"},{\"name\":\"numberOfIFATabletsGiven\",\"value\":\"2\",\"source\":\"mother.numberOfIFATabletsGiven\"},{\"name\":\"immediateReferral\",\"value\":\"yes\",\"source\":\"mother.immediateReferral\"},{\"name\":\"immediateReferralReason\",\"value\":\"bad_headache abdominal_pain       Very_low_blood_pressure\",\"source\":\"mother.immediateReferralReason\"},{\"name\":\"anaemicStatus\",\"source\":\"mother.anaemicStatus\"},{\"name\":\"anaemicStatus\",\"value\":\"Severe_Anaemia\",\"source\":\"mother.anaemicStatus\"},{\"name\":\"pih\",\"source\":\"mother.pih\"},{\"name\":\"pih\",\"source\":\"mother.pih\"},{\"name\":\"preEclampsia\",\"source\":\"mother.preEclampsia\"},{\"name\":\"preEclampsia\",\"source\":\"mother.preEclampsia\"},{\"name\":\"isHighRiskTillPNCClose\",\"source\":\"mother.isHighRiskTillPNCClose\"},{\"name\":\"highRiskTillPNCCloseReason\",\"source\":\"mother.highRiskTillPNCCloseReason\"},{\"name\":\"highRiskReason\",\"value\":\"Severe_Anaemia   \",\"source\":\"mother.highRiskReason\"},{\"name\":\"is_high_risk\",\"value\":\"yes\",\"source\":\"mother.is_high_risk\"},{\"name\":\"submissionDate\",\"value\":\"2015-06-17\",\"source\":\"mother.submissionDate\"},{\"name\":\"isConsultDoctor\",\"value\":\"yes\",\"source\":\"mother.isConsultDoctor\"}],\"sub_forms\":[{\"name\":\"child_pnc_visit\",\"bind_type\":\"child\",\"default_bind_path\":\"/model/instance/PNC_Visit_EngKan/pnc_child_repeat/\",\"fields\":[{\"name\":\"id\",\"source\":\"child.id\"},{\"name\":\"gender\",\"source\":\"child.gender\"},{\"name\":\"weight\",\"source\":\"child.weight\"},{\"name\":\"weight\",\"source\":\"child.weight\"},{\"name\":\"immunizationsGiven\",\"source\":\"child.immunizationsGiven\"},{\"name\":\"urineStoolProblems\",\"source\":\"child.urineStoolProblems\"},{\"name\":\"daysOfDiarrhea\",\"source\":\"child.daysOfDiarrhea\"},{\"name\":\"bloodInStool\",\"source\":\"child.bloodInStool\"},{\"name\":\"activityProblems\",\"source\":\"child.activityProblems\"},{\"name\":\"breathingProblems\",\"source\":\"child.breathingProblems\"},{\"name\":\"respirationRate\",\"source\":\"child.respirationRate\"},{\"name\":\"skinProblems\",\"source\":\"child.skinProblems\"},{\"name\":\"hasFeverSymptoms\",\"source\":\"child.hasFeverSymptoms\"},{\"name\":\"temperature\",\"source\":\"child.temperature\"},{\"name\":\"immediateReferral\",\"source\":\"child.immediateReferral\"},{\"name\":\"immediateReferralReason\",\"source\":\"child.immediateReferralReason\"},{\"name\":\"childExclusiveBF\",\"source\":\"child.childExclusiveBF\"},{\"name\":\"isChildHighRisk\",\"source\":\"child.isChildHighRisk\"}],\"instances\":[{\"immediateReferral\":\"yes\",\"weight\":\"5\",\"daysOfDiarrhea\":\"\",\"isChildHighRisk\":\"no\",\"childExclusiveBF\":\"\",\"immunizationsGiven\":\"opv_0\",\"hasFeverSymptoms\":\"no\",\"urineStoolProblems\":\"vomiting\",\"temperature\":\"\",\"id\":\"0825b686-d27b-4f39-bb3d-05aa2b1fe4f6\",\"breathingProblems\":\"no_problems\",\"bloodInStool\":\"\",\"gender\":\"male\",\"skinProblems\":\"no_problems\",\"activityProblems\":\"no_problems\",\"immediateReferralReason\":\"vomiting    \",\"respirationRate\":\"50\"},{\"immediateReferral\":\"no\",\"weight\":\"4\",\"daysOfDiarrhea\":\"\",\"isChildHighRisk\":\"no\",\"childExclusiveBF\":\"\",\"immunizationsGiven\":\"opv_0\",\"hasFeverSymptoms\":\"no\",\"urineStoolProblems\":\"no_problems\",\"temperature\":\"\",\"id\":\"d43f2b9d-add3-4e63-af04-bb77bec4d1e7\",\"breathingProblems\":\"no_problems\",\"bloodInStool\":\"\",\"gender\":\"female\",\"skinProblems\":\"\",\"activityProblems\":\"\",\"immediateReferralReason\":\"\",\"respirationRate\":\"\"}]}]}}";
        when(formDataRepository.fetchFromSubmissionUseEntity("entity1")).thenReturn(new FormSubmission("instance1", "entity1", "pnc_visit", instance, "1", SyncStatus.PENDING, "5"));
        when(context.userService()).thenReturn(userService);
        when(userService.getUserRole()).thenReturn("ANM");

        org.ei.telemedicine.view.customControls.CustomFontTextView bt_save = (org.ei.telemedicine.view.customControls.CustomFontTextView) blueToothInfoActivity.findViewById(R.id.bt_info_save);
        assertNotNull(bt_save);
        bt_save.performClick();

    }

    @Test
    public void testDestroy() {
        assertEquals(Constants.PULSE_DEVICE, "Sp");
        blueToothInfoActivity.onResult(new byte[]{}, 101);
        blueToothInfoActivity.onBackPressed();
        activityController.destroy();
    }
}
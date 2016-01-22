package org.ei.telemedicine.test.service;

import com.google.gson.Gson;

import org.ei.telemedicine.Context;
import org.ei.telemedicine.domain.form.FormSubmission;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSettings;
import org.ei.telemedicine.repository.FormDataRepository;
import org.ei.telemedicine.service.FormSubmissionService;
import org.ei.telemedicine.service.ZiggyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.ei.telemedicine.domain.SyncStatus.SYNCED;
import static org.ei.telemedicine.util.EasyMap.create;
import static org.ei.telemedicine.util.FormSubmissionBuilder.create;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FormSubmissionServiceTest {
    @Mock
    private ZiggyService ziggyService;
    @Mock
    private Context context;
    @Mock
    private AllDoctorRepository allDoctorRepository;
    @Mock
    private AllSettings allSettings;
    @Mock
    private FormDataRepository formDataRepository;

    private FormSubmissionService service;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new FormSubmissionService(ziggyService, formDataRepository, allSettings);
    }

    @Test
    public void testShouldDelegateProcessingToZiggyServiceAndMarkAsSynced() throws Exception {
        List<FormSubmission> submissions = asList(create().withInstanceId("instance id 1").withVersion("122").build(),
                create().withInstanceId("instance id 2").withVersion("123").build());

        service.processSubmissions(submissions, "");

        String paramsForFirstSubmission = new Gson().toJson(
                create("instanceId", "instance id 1")
                        .put("entityId", "entity id 1")
                        .put("formName", "form name 1")
                        .put("version", "122")
                        .put("sync_status", SYNCED.value())
                        .map());
        String paramsForSecondSubmission = new Gson().toJson(
                create("instanceId", "instance id 2")
                        .put("entityId", "entity id 1")
                        .put("formName", "form name 1")
                        .put("sync_status", SYNCED.value())
                        .put("version", "123")
                        .map());
        InOrder inOrder = inOrder(ziggyService, allSettings, formDataRepository);
        inOrder.verify(ziggyService).saveForm(paramsForFirstSubmission, "{}");
        inOrder.verify(formDataRepository).updateServerVersion("instance id 1", "0", "");
        inOrder.verify(allSettings).savePreviousFormSyncIndex("0");
        inOrder.verify(ziggyService).saveForm(paramsForSecondSubmission, "{}");
        inOrder.verify(formDataRepository).updateServerVersion("instance id 2", "0", "");
        inOrder.verify(allSettings).savePreviousFormSyncIndex("0");
    }

    @Test
    public void testShouldNotDelegateProcessingToZiggyServiceForProcessedSubmissions() throws Exception {
        FormSubmission firstFormSubmission = create().withInstanceId("instance id 1").withVersion("122").build();
        FormSubmission secondFormSubmission = create().withInstanceId("instance id 2").withVersion("123").withServerVersion("1").build();
        List<FormSubmission> submissions = asList(firstFormSubmission, secondFormSubmission);
        when(formDataRepository.submissionExists("instance id 1")).thenReturn(true);
        when(formDataRepository.submissionExists("instance id 2")).thenReturn(false);

        service.processSubmissions(submissions, "");

        String paramsForFirstSubmission = new Gson().toJson(
                create("instanceId", "instance id 1")
                        .put("entityId", "entity id 1")
                        .put("formName", "form name 1")
                        .put("version", "122")
                        .put("sync_status", SYNCED.value())
                        .map());
        String paramsForSecondSubmission = new Gson().toJson(
                create("instanceId", "instance id 2")
                        .put("entityId", "entity id 1")
                        .put("formName", "form name 1")
                        .put("version", "123")
                        .put("sync_status", SYNCED.value())
                        .map());
        InOrder inOrder = inOrder(ziggyService, allSettings, formDataRepository);
        inOrder.verify(ziggyService, times(1)).saveForm(paramsForFirstSubmission, "{}");
        inOrder.verify(formDataRepository).updateServerVersion("instance id 1", "0", "");
        inOrder.verify(allSettings).savePreviousFormSyncIndex("0");
        inOrder.verify(ziggyService).saveForm(paramsForSecondSubmission, "{}");
        inOrder.verify(formDataRepository).updateServerVersion("instance id 2", "1", "");
        inOrder.verify(allSettings).savePreviousFormSyncIndex("1");
    }

    @Test
    public void testDoctorData() throws Exception {
        String dummyData = "[{\"anmId\": \"demo2\", \"riskinfo\": [{\"visit_type\": \"CHILD\", \"sickVisitDate\": \"\", \"daysOfDiarrhea\": \"\", \"breathsPerMinute\": \"\", \"id\": \"8e691e6973617e14dd98631d4f634de4\", \"edd\": \"14-Jul-2016\", \"childSigns\": \"\", \"vommitEveryThing\": \"\", \"childReferral\": \"\", \"dateOfBirth\": \"2016-01-12\", \"immediateReferralReason\": \"\", \"isHighRisk\": \"\", \"reportChildDiseasePlace\": \"\", \"reportChildDiseaseOther\": \"\", \"numberOfORSGiven\": \"\", \"lmp\": \"08-Oct-2015\", \"numberOfDaysCough\": \"\", \"reportChildDisease\": \"measles\", \"immediateReferral\": \"\", \"childTemperature\": \"222-F\", \"bloodInStool\": \"\", \"daysOfFever\": \"\", \"submissionDate\": \"2016-01-12\", \"reportChildDiseaseDate\": \"2016-01-12\", \"childSignsOther\": \"\", \"entityid\": \"0135fde6-6a1d-4dfc-816a-5aec1e329738\", \"anmPoc\": \"\"}], \"district\": \"\", \"entityidec\": \"15f46603-9f36-41d8-a8ba-b7e89e1adc84\", \"phoneNumber\": \"2548121337675\", \"wifeName\": \"demores\", \"village\": \"Chemoinoi\", \"husbandName\": \"\", \"pending\": \" \", \"wifeAge\": \"\"}]";
        Context.setInstance(context);
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        service.processDoctorRecords(dummyData);

    }
}

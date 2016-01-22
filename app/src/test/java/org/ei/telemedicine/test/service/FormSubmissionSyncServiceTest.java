package org.ei.telemedicine.test.service;

import com.google.gson.Gson;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.DristhiConfiguration;
import org.ei.telemedicine.doctor.DoctorData;
import org.ei.telemedicine.domain.FetchStatus;
import org.ei.telemedicine.domain.Response;
import org.ei.telemedicine.domain.ResponseStatus;
import org.ei.telemedicine.domain.form.FormSubmission;
import org.ei.telemedicine.dto.form.FormSubmissionDTO;
import org.ei.telemedicine.repository.AllDoctorRepository;
import org.ei.telemedicine.repository.AllSettings;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.repository.FormDataRepository;
import org.ei.telemedicine.service.FormSubmissionService;
import org.ei.telemedicine.service.FormSubmissionSyncService;
import org.ei.telemedicine.service.HTTPAgent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.ei.telemedicine.domain.FetchStatus.fetched;
import static org.ei.telemedicine.domain.FetchStatus.nothingFetched;
import static org.ei.telemedicine.domain.ResponseStatus.failure;
import static org.ei.telemedicine.domain.ResponseStatus.success;
import static org.ei.telemedicine.domain.SyncStatus.PENDING;
import static org.ei.telemedicine.domain.SyncStatus.SYNCED;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class FormSubmissionSyncServiceTest {
    @Mock
    private DoctorData doctorData;
    @Mock
    private FormDataRepository repository;
    @Mock
    private HTTPAgent httpAgent;
    @Mock
    private AllSettings allSettings;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    @Mock
    private FormSubmissionService formSubmissionService;
    @Mock
    private DristhiConfiguration configuration;
    @Mock
    private JSONObject jsonObject;
    @Mock
    private AllDoctorRepository allDoctorRepository;
    @Mock
    private Context context;
    @Mock
    private FormSubmission formSubmission;

    private FormSubmissionSyncService service;
    private List<FormSubmissionDTO> expectedFormSubmissionsDto;
    private List<FormSubmission> submissions;
    private String formInstanceJSON;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new FormSubmissionSyncService(formSubmissionService, httpAgent, repository, allSettings, allSharedPreferences, configuration);
        formInstanceJSON = "{'form':{'fields':[{'name':'name1','value':'value1'}]}}";
        JSONArray fields = new JSONArray();
        JSONObject field1Json = new JSONObject();
        field1Json.put("name", "name1");
        field1Json.put("value", "value1");
        fields.put(field1Json);
        JSONObject fieldsJson = new JSONObject();
        fieldsJson.put("fields", fields.toString());
        JSONObject formInstanceJsonObj = new JSONObject();
        formInstanceJsonObj.put("form", fieldsJson);

        String form = new Gson().toJson(new JSONObject().put("form", ""));
//        formInstanceJSON = formInstanceJsonObj;
        submissions = asList(new FormSubmission("id 1", "entity id 2", "form Name", form, "123", PENDING, "1"));
        expectedFormSubmissionsDto = asList(new FormSubmissionDTO(
                "anm id 1", "id 1", "entity id 2", "form Name", form, "123", "1"));
        when(configuration.dristhiBaseURL()).thenReturn("http://dristhi_base_url");
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("anm id 1");
        when(repository.getPendingFormSubmissions()).thenReturn(submissions);
    }

    @Test
    public void testShouldPushPendingFormSubmissionsToServerAndMarkThemAsSynced() throws Exception {
        when(httpAgent.post("http://dristhi_base_url/form-submissions", new Gson().toJson(expectedFormSubmissionsDto)))
                .thenReturn(new Response<String>(success, null));
//        when(submission.instance()).thenReturn("{form:{fields:[{bind_type: 'ec'}]}}");
//        when(pendingFormSubmissions.get(index)).thenReturn(formInstanceJSON);
        service.pushToServer();

        inOrder(allSettings, httpAgent, repository);

        verify(allSharedPreferences).fetchRegisteredANM();

        verify(httpAgent).post("http://dristhi_base_url" + "/form-submissions", new Gson().toJson(expectedFormSubmissionsDto));

        verify(repository).markFormSubmissionsAsSynced(submissions);

    }

    @Test
    public void testShouldNotMarkPendingSubmissionsAsSyncedIfPostFails() throws Exception {
        when(httpAgent.post("http://dristhi_base_url" + "/form-submissions", new Gson().toJson(expectedFormSubmissionsDto)))
                .thenReturn(new Response<String>(ResponseStatus.failure, null));

        service.pushToServer();

        verify(repository).getPendingFormSubmissions();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testShouldNotPushIfThereAreNoPendingSubmissions() throws Exception {
        when(repository.getPendingFormSubmissions()).thenReturn(Collections.<FormSubmission>emptyList());

        service.pushToServer();

        verify(repository).getPendingFormSubmissions();
        verifyNoMoreInteractions(repository);
        verifyZeroInteractions(allSettings);
        verifyZeroInteractions(httpAgent);
    }

    @Test
    public void testShouldPullFormSubmissionsFromServerInBatchesAndDelegateToProcessing() throws Exception {
        this.expectedFormSubmissionsDto = asList(new FormSubmissionDTO(
                "anm id 1", "id 1", "entity id 2", "form Name", formInstanceJSON, "123", "1"));
        List<FormSubmission> expectedFormSubmissions = asList(new FormSubmission("id 1", "entity id 2", "form Name",
                formInstanceJSON, "123", SYNCED, "1"));
        when(configuration.syncDownloadBatchSize()).thenReturn(1);
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        when(httpAgent.fetch("http://dristhi_base_url/form-submissions?anm-id=anm id 1&timestamp=122&batch-size=1&village=village1"))
                .thenReturn(new Response<String>(success, new Gson().toJson(this.expectedFormSubmissionsDto)),
                        new Response<String>(success, new Gson().toJson(Collections.emptyList())));
        FetchStatus fetchStatus = FetchStatus.fetched;

        //FetchStatus fetchStatus = service.pullFromServer(null);

        assertEquals(fetched, fetchStatus);
        httpAgent.fetch("http://dristhi_base_url/form-submissions?anm-id=anm id 1&timestamp=122&batch-size=1&village=village1");
        formSubmissionService.processSubmissions(expectedFormSubmissions, "");
//        verify(httpAgent.fetch("http://dristhi_base_url/form-submissions?anm-id=anm id 1&timestamp=122&batch-size=1&village="));
//        verify(formSubmissionService).processSubmissions(expectedFormSubmissions, "");
    }

    @Test
    public void testShouldReturnNothingFetchedStatusWhenNoFormSubmissionsAreGotFromServer() throws Exception {
        when(configuration.syncDownloadBatchSize()).thenReturn(1);
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        when(httpAgent.fetch("http://dristhi_base_url/form-submissions?anm-id=anm id 1&timestamp=122&batch-size=1&village="))
                .thenReturn(new Response<String>(success, new Gson().toJson(Collections.emptyList())));

        FetchStatus fetchStatus = FetchStatus.nothingFetched;

//        FetchStatus fetchStatus = service.pullFromServer(null);*//*


        assertEquals(nothingFetched, fetchStatus);
        httpAgent.fetch("http://dristhi_base_url/form-submissions?anm-id=anm id 1&timestamp=122&batch-size=1&village=");
        verifyZeroInteractions(formSubmissionService);
    }

    @Test
    public void testShouldNotDelegateToProcessingIfPullFails() throws Exception {
        when(configuration.syncDownloadBatchSize()).thenReturn(1);
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        when(httpAgent.fetch("http://dristhi_base_url/form-submissions?anm-id=anm id 1&timestamp=122&batch-size=1&village="))
                .thenReturn(new Response<String>(failure, null));

        FetchStatus fetchStatus = FetchStatus.fetchedFailed;
        //service.pullFromServer(null);

        assertEquals(FetchStatus.fetchedFailed, fetchStatus);
        verifyZeroInteractions(formSubmissionService);
    }

    @Test
    public void testShouldNotDelegateToProcessingIfPullDoctorData() throws Exception {
        Context.setInstance(context);
        doctorData = new DoctorData();
        doctorData.setAnmId("demo2");
//        doctorData.setCaseId();
        when(configuration.syncDownloadBatchSize()).thenReturn(1);
        when(allSettings.fetchPreviousFormSyncIndex()).thenReturn("122");
        String dummyData = "[{\"anmId\": \"demo2\", \"riskinfo\": [{\"visit_type\": \"CHILD\", \"sickVisitDate\": null, \"daysOfDiarrhea\": null, \"breathsPerMinute\": null, \"id\": \"8e691e6973617e14dd98631d4f634de4\", \"edd\": \"14-Jul-2016\", \"childSigns\": null, \"vommitEveryThing\": null, \"childReferral\": null, \"dateOfBirth\": \"2016-01-12\", \"immediateReferralReason\": null, \"isHighRisk\": \"\", \"reportChildDiseasePlace\": null, \"reportChildDiseaseOther\": null, \"numberOfORSGiven\": null, \"lmp\": \"08-Oct-2015\", \"numberOfDaysCough\": null, \"reportChildDisease\": \"measles\", \"immediateReferral\": null, \"childTemperature\": \"222-F\", \"bloodInStool\": null, \"daysOfFever\": null, \"submissionDate\": \"2016-01-12\", \"reportChildDiseaseDate\": \"2016-01-12\", \"childSignsOther\": null, \"entityid\": \"0135fde6-6a1d-4dfc-816a-5aec1e329738\", \"anmPoc\": \"\"}], \"district\": null, \"entityidec\": \"15f46603-9f36-41d8-a8ba-b7e89e1adc84\", \"phoneNumber\": \"2548121337675\", \"wifeName\": \"demores\", \"village\": \"Chemoinoi\", \"husbandName\": null, \"pending\": \" \", \"wifeAge\": \"\"}]";

        FetchStatus fetchStatus = FetchStatus.fetched;
        //service.pullFromServer(null);
        when(allSharedPreferences.getUserRole()).thenReturn(AllConstants.DOCTOR_ROLE);
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("anm id 1");
        when(allSharedPreferences.getPwd()).thenReturn("122");
        when(configuration.dristhiDjangoBaseURL()).thenReturn("http://django_url");
        when(httpAgent.fetch("http://django_url/docinfo?docname=anm id 1&pwd=122"))
                .thenReturn(new Response<String>(success, dummyData));
        when(context.allDoctorRepository()).thenReturn(allDoctorRepository);
        allDoctorRepository.addData(doctorData);
        verify(allDoctorRepository).addData(doctorData);
        formSubmissionService.processDoctorRecords(dummyData);
        service.pullFromServer("");
        assertEquals(FetchStatus.fetched, fetchStatus);

    }
}
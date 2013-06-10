package org.ei.drishti.view.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.apache.commons.lang3.tuple.Pair;
import org.ei.drishti.domain.Alert;
import org.ei.drishti.domain.EligibleCouple;
import org.ei.drishti.domain.Mother;
import org.ei.drishti.domain.ServiceProvided;
import org.ei.drishti.repository.AllBeneficiaries;
import org.ei.drishti.repository.AllEligibleCouples;
import org.ei.drishti.service.AlertService;
import org.ei.drishti.service.ServiceProvidedService;
import org.ei.drishti.util.Cache;
import org.ei.drishti.view.contract.AlertDTO;
import org.ei.drishti.view.contract.PNCClient;
import org.ei.drishti.view.contract.ServiceProvidedDTO;
import org.ei.drishti.view.contract.Village;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.ei.drishti.dto.AlertStatus.normal;
import static org.ei.drishti.util.EasyMap.create;
import static org.ei.drishti.util.EasyMap.mapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class PNCSmartRegistryControllerTest {
    public static final String[] PNC_ALERTS = new String[]{
            "PNC 1"
    };
    public static final String[] PNC_SERVICES = new String[]{
            "PNC 1"
    };
    @Mock
    private AllEligibleCouples allEligibleCouples;
    @Mock
    private AllBeneficiaries allBeneficiaries;
    @Mock
    private AlertService alertService;
    @Mock
    private ServiceProvidedService serviceProvidedService;

    private PNCSmartRegistryController controller;
    private Map<String, String> emptyMap;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        emptyMap = Collections.emptyMap();
        controller = new PNCSmartRegistryController(serviceProvidedService, alertService, allEligibleCouples, allBeneficiaries, new Cache<String>());
    }

    @Test
    public void shouldSortPNCsByWifeName() throws Exception {
        Map<String, String> details = mapOf("edd", "Tue, 25 Feb 2014 00:00:00 GMT");
        EligibleCouple ec2 = new EligibleCouple("EC Case 2", "Woman B", "Husband B", "EC Number 2", "kavalu_hosur", "Bherya SC", emptyMap);
        EligibleCouple ec3 = new EligibleCouple("EC Case 3", "Woman C", "Husband C", "EC Number 3", "Bherya", "Bherya SC", emptyMap);
        EligibleCouple ec1 = new EligibleCouple("EC Case 1", "Woman A", "Husband A", "EC Number 1", "Bherya", null, emptyMap);
        Mother m1 = new Mother("Entity X", "EC Case 1", "thayi 1", "2013-05-25").withDetails(details);
        Mother m2 = new Mother("Entity Y", "EC Case 2", "thayi 2", "2013-05-25").withDetails(details);
        Mother m3 = new Mother("Entity Z", "EC Case 3", "thayi 3", "2013-05-25").withDetails(details);
        PNCClient expectedClient1 = createPNCClient("Entity Z", "Woman A", "Bherya", "thayi 3", "2013-05-25").withECNumber("EC Number 1").withHusbandName("Husband A");
        PNCClient expectedClient2 = createPNCClient("Entity X", "Woman B", "kavalu_hosur", "thayi 1", "2013-05-25").withECNumber("EC Number 2").withHusbandName("Husband B");
        PNCClient expectedClient3 = createPNCClient("Entity Y", "Woman C", "Bherya", "thayi 2", "2013-05-25").withECNumber("EC Number 3").withHusbandName("Husband C");
        when(allBeneficiaries.allPNCsWithEC()).thenReturn(asList(Pair.of(m1, ec2), Pair.of(m2, ec3), Pair.of(m3, ec1)));

        String clients = controller.get();

        List<PNCClient> actualClients = new Gson().fromJson(clients, new TypeToken<List<PNCClient>>() {
        }.getType());
        assertEquals(asList(expectedClient1, expectedClient2, expectedClient3), actualClients);
    }

    @Test
    public void shouldMapPNCToPNCClient() throws Exception {
        Map<String, String> ecDetails = create("wifeAge", "22")
                .put("currentMethod", "condom")
                .put("familyPlanningMethodChangeDate", "2013-01-02")
                .put("isHighPriority", Boolean.toString(false))
                .put("deliveryDate", "2011-05-05")
                .put("womanDOB", "2011-05-05")
                .put("caste", "sc")
                .put("economicStatus", "bpl")
                .put("iudPlace", "iudPlace")
                .put("iudPerson", "iudPerson")
                .put("numberOfCondomsSupplied", "20")
                .put("numberOfCentchromanPillsDelivered", "10")
                .put("numberOfOCPDelivered", "5")
                .map();
        Map<String, String> motherDetails = create("deliveryPlace", "PHC")
                .put("deliveryType", "live_birth")
                .put("deliveryComplications", "Headache")
                .put("otherDeliveryComplications", "Vomiting")
                .map();
        EligibleCouple eligibleCouple = new EligibleCouple("EC Case 1", "Woman A", "Husband A", "EC Number 1", "Bherya", null, ecDetails).asOutOfArea();
        Mother mother = new Mother("Entity X", "EC Case 1", "thayi 1", "2013-05-25").withDetails(motherDetails);
        when(allBeneficiaries.allPNCsWithEC()).thenReturn(asList(Pair.of(mother, eligibleCouple)));
        PNCClient expectedPNCClient = new PNCClient("Entity X", "Bherya", "Woman A", "thayi 1", "2013-05-25")
                .withECNumber("EC Number 1")
                .withIsHighPriority(false)
                .withAge("22")
                .withWomanDOB("2011-05-05")
                .withEconomicStatus("bpl")
                .withIUDPerson("iudPerson")
                .withIUDPlace("iudPlace")
                .withHusbandName("Husband A")
                .withIsOutOfArea(true)
                .withIsHighRisk(false)
                .withCaste("sc")
                .withFPMethod("condom")
                .withFamilyPlanningMethodChangeDate("2013-01-02")
                .withNumberOfCondomsSupplied("20")
                .withNumberOfCentchromanPillsDelivered("10")
                .withNumberOfOCPDelivered("5")
                .withDeliveryPlace("PHC")
                .withDeliveryType("live_birth")
                .withDeliveryComplications("Headache")
                .withOtherDeliveryComplications("Vomiting")
                .withPhotoPath("../../img/woman-placeholder.png")
                .withAlerts(Collections.<AlertDTO>emptyList())
                .withServicesProvided(Collections.<ServiceProvidedDTO>emptyList());

        String clients = controller.get();

        List<PNCClient> actualClients = new Gson().fromJson(clients, new TypeToken<List<PNCClient>>() {
        }.getType());
        assertEquals(asList(expectedPNCClient), actualClients);
    }

    @Test
    public void shouldCreatePNCClientsWithPNC1Alert() throws Exception {
        EligibleCouple ec = new EligibleCouple("entity id 1", "Woman C", "Husband C", "EC Number 1", "Bherya", "Bherya SC", emptyMap);
        Mother mother = new Mother("Entity X", "EC Case 1", "thayi 1", "2013-05-25");

        Alert pnc1Alert = new Alert("entity id 1", "PNC", "PNC 1", normal, "2013-01-01", "2013-02-01");
        when(allBeneficiaries.allPNCsWithEC()).thenReturn(asList(Pair.of(mother, ec)));
        when(alertService.findByEntityIdAndAlertNames("Entity X", PNC_ALERTS)).thenReturn(asList(pnc1Alert));

        String clients = controller.get();

        List<PNCClient> actualClients = new Gson().fromJson(clients, new TypeToken<List<PNCClient>>() {
        }.getType());
        verify(alertService).findByEntityIdAndAlertNames("Entity X", PNC_ALERTS);
        AlertDTO expectedAlertDto = new AlertDTO("PNC 1", "normal", "2013-01-01");
        PNCClient expectedEC = createPNCClient("Entity X", "Woman C", "Bherya", "thayi 1", "2013-05-25")
                .withECNumber("EC Number 1")
                .withHusbandName("Husband C")
                .withAlerts(asList(expectedAlertDto));
        assertEquals(asList(expectedEC), actualClients);
    }

    @Test
    public void shouldCreatePNCClientsWithServicesProvided() throws Exception {
        EligibleCouple ec = new EligibleCouple("entity id 1", "Woman C", "Husband C", "EC Number 1", "Bherya", "Bherya SC", emptyMap);
        Mother mother = new Mother("Entity X", "EC Case 1", "thayi 1", "2013-05-25");
        when(allBeneficiaries.allPNCsWithEC()).thenReturn(asList(Pair.of(mother, ec)));
        when(alertService.findByEntityIdAndAlertNames("Entity X", PNC_ALERTS)).thenReturn(Collections.<Alert>emptyList());
        when(serviceProvidedService.findByEntityIdAndServiceNames("Entity X", PNC_SERVICES))
                .thenReturn(asList(new ServiceProvided("entity id 1", "PNC 1", "2013-01-01", mapOf("dose", "100")), new ServiceProvided("entity id 1", "PNC 1", "2013-02-01", emptyMap)));

        String clients = controller.get();

        List<PNCClient> actualClients = new Gson().fromJson(clients, new TypeToken<List<PNCClient>>() {
        }.getType());
        verify(alertService).findByEntityIdAndAlertNames("Entity X", PNC_ALERTS);
        verify(serviceProvidedService).findByEntityIdAndServiceNames("Entity X", PNC_SERVICES);
        List<ServiceProvidedDTO> expectedServicesProvided = asList(new ServiceProvidedDTO("PNC 1", "2013-01-01", mapOf("dose", "100")),
                new ServiceProvidedDTO("PNC 1", "2013-02-01", emptyMap));
        PNCClient expectedEC = createPNCClient("Entity X", "Woman C", "Bherya", "thayi 1", "2013-05-25")
                .withECNumber("EC Number 1")
                .withHusbandName("Husband C")
                .withServicesProvided(expectedServicesProvided);
        assertEquals(asList(expectedEC), actualClients);
    }

    @Test
    public void shouldLoadVillages() throws Exception {
        List<Village> expectedVillages = asList(new Village("village1"), new Village("village2"));
        when(allEligibleCouples.villages()).thenReturn(asList("village1", "village2"));

        String villages = controller.villages();
        List<Village> actualVillages = new Gson().fromJson(villages, new TypeToken<List<Village>>() {
        }.getType());
        assertEquals(actualVillages, expectedVillages);
    }

    private PNCClient createPNCClient(String entityId, String name, String village, String thayi, String deliveryDate) {
        return new PNCClient(entityId, village, name, thayi, deliveryDate)
                .withPhotoPath("../../img/woman-placeholder.png")
                .withIsOutOfArea(false)
                .withAlerts(Collections.<AlertDTO>emptyList())
                .withServicesProvided(Collections.<ServiceProvidedDTO>emptyList());
    }
}

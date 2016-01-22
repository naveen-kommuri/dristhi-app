package org.ei.telemedicine.test.service;

import org.ei.telemedicine.repository.AllBeneficiaries;
import org.ei.telemedicine.repository.AllEligibleCouples;
import org.ei.telemedicine.repository.AllSharedPreferences;
import org.ei.telemedicine.service.ANMService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by naveen on 1/21/16.
 */
public class ANMServiceTest {
    private ANMService anmService;
    @Mock
    private AllSharedPreferences allSharedPreferences;
    @Mock
    private AllBeneficiaries allBeneficiaries;
    @Mock
    private AllEligibleCouples allEligibleCouples;

    @Before
    public void setUp() {
        initMocks(this);
        anmService = new ANMService(allSharedPreferences, allBeneficiaries, allEligibleCouples);
    }

    @Test
    public void testingANM() {
        long l = 1;
        when(allSharedPreferences.fetchRegisteredANM()).thenReturn("anm1");
        when(allEligibleCouples.count()).thenReturn(l);
        when(allEligibleCouples.fpCount()).thenReturn(l);
        when(allBeneficiaries.ancCount()).thenReturn(l);
        when(allBeneficiaries.pncCount()).thenReturn(l);
        when(allBeneficiaries.childCount()).thenReturn(l);
        anmService.fetchDetails();
        assertEquals(l, anmService.fetchDetails().ancCount());
    }
}

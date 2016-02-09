package org.ei.telemedicine.test.domain;

import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.domain.FPMethod;
import org.ei.telemedicine.domain.TimelineEvent;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FPMethodTest {

    TimelineEvent timelineEvent;
    private LocalDate referenceDate=new LocalDate("2015-02-28");


    @Mock
    Context context;

    @Mock
    private android.content.Context applicationContext;
    @Before
    public void setUp(){
        initMocks(this);
        when(context.applicationContext()).thenReturn(applicationContext);
        Context.setInstance(context);

    }

    @Test
    public void shouldParseStringToFPMethod() throws Exception {
        assertEquals(FPMethod.CONDOM, FPMethod.tryParse("condom", FPMethod.NONE));
        assertEquals(FPMethod.DMPA_INJECTABLE, FPMethod.tryParse("dmpa_injectable", FPMethod.NONE));
        assertEquals(FPMethod.ECP, FPMethod.tryParse("ecp", FPMethod.NONE));
        assertEquals(FPMethod.FEMALE_STERILIZATION, FPMethod.tryParse("female_sterilization", FPMethod.NONE));
        assertEquals(FPMethod.IUD, FPMethod.tryParse("iud", FPMethod.NONE));
        assertEquals(FPMethod.LAM, FPMethod.tryParse("lam", FPMethod.NONE));
        assertEquals(FPMethod.MALE_STERILIZATION, FPMethod.tryParse("male_sterilization", FPMethod.NONE));
        assertEquals(FPMethod.NONE, FPMethod.tryParse("none", FPMethod.NONE));
        assertEquals(FPMethod.OCP, FPMethod.tryParse("ocp", FPMethod.NONE));
        assertEquals(FPMethod.TRADITIONAL_METHODS, FPMethod.tryParse("traditional_methods", FPMethod.NONE));
    }

    @Test
    public void shouldParseBlankStringAsDefaultFPMethod() throws Exception {
        FPMethod defaultMethod = FPMethod.NONE;
        assertEquals(defaultMethod, FPMethod.tryParse("", defaultMethod));
    }

    @Test
    public void shouldParseInvalidStringAsDefaultFPMethod() throws Exception {
        FPMethod defaultMethod = FPMethod.CONDOM;
        assertEquals(defaultMethod, FPMethod.tryParse("---", defaultMethod));
    }


    @Test
    public void displayNameCondomn() throws  Exception{
     /*   when(context.applicationContext()).thenReturn(applicationContext);
        Context.setInstance(context);*/
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_condom)).thenReturn("Condomn");
        assertEquals("Condomn", FPMethod.valueOf("CONDOM").displayName());
    }

    @Test
    public void timelineEvent() throws Exception{
        TimelineEvent time=new TimelineEvent("caseId", "FPRENEW",referenceDate, "FP Renewed", "", null);
        Map<String, String> detail=new HashMap<String, String>();
        detail.put("familyPlanningMethodChangeDate", "2015-02-28");
        TimelineEvent str=FPMethod.valueOf("CONDOM").getTimelineEventForRenew("caseId",detail);
        assertEquals(time,str);
    }

    @Test
    public void timelineEventocp() throws Exception{
        TimelineEvent time=new TimelineEvent("caseId", "FPRENEW",referenceDate, "FP Renewed", "", null);
        Map<String, String> detail=new HashMap<String, String>();
        detail.put("familyPlanningMethodChangeDate", "2015-02-28");
        TimelineEvent str=FPMethod.valueOf("OCP").getTimelineEventForRenew("caseId",detail);
        assertEquals(time,str);
    }

    @Test
    public void timelineEventDmpa() throws Exception{
        TimelineEvent time=new TimelineEvent("caseId", "FPRENEW",referenceDate, "FP Renewed", "", null);
        Map<String, String> detail=new HashMap<String, String>();
        detail.put("familyPlanningMethodChangeDate", "2015-02-28");
        TimelineEvent str=FPMethod.valueOf("CONDOM").getTimelineEventForRenew("caseId",detail);
        assertEquals(time,str);
    }


 /*   @Test
    public void timelineEsventDmpa() throws Exception{
        TimelineEvent time=new TimelineEvent("caseId", "FPRENEW",referenceDate, "FP Renewed", "", null);
        *//*Map<String, String> detail= null;
        detail.put("familyPlanningMethodChangeDate", "2015-02-28");*//*
        TimelineEvent str= time;
        assertEquals(time,str);
    }*/

    @Test
    public void displayNameIud() throws  Exception{
     /*   when(context.applicationContext()).thenReturn(applicationContext);
        Context.setInstance(context);*/
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_iucd)).thenReturn("Iud1");
        assertEquals("Iud1", FPMethod.valueOf("IUD").displayName());
    }


    @Test
    public void displayNameOcp() throws  Exception{
     /*   when(context.applicationContext()).thenReturn(applicationContext);
        Context.setInstance(context);*/
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_ocp)).thenReturn("Ocp");
        assertEquals("Ocp", FPMethod.valueOf("OCP").displayName());
    }

    @Test
    public void displayNameInjuctable() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_dmpa)).thenReturn("dmpa");
        assertEquals("dmpa", FPMethod.valueOf("DMPA_INJECTABLE").displayName());
    }

    @Test
    public void displayNameMaleSterilization() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_male_sterilization)).thenReturn("malest");
        assertEquals("malest", FPMethod.valueOf("MALE_STERILIZATION").displayName());

        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_female_sterilization)).thenReturn("femalest");
        assertEquals("femalest", FPMethod.valueOf("FEMALE_STERILIZATION").displayName());
    }

    /*@Test
    public void displayNamefemaleSterilization() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_female_sterilization)).thenReturn("femalest");
        assertEquals("femalest", FPMethod.valueOf("FEMALE_STERILIZATION").displayName());
    }*/

    @Test
    public void displayNameEcp() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_ecp)).thenReturn("ecp");
        assertEquals("ecp", FPMethod.valueOf("ECP").displayName());
    }

    @Test
    public void displayNameTreditionalMethods() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_traditional)).thenReturn("treditional");
        assertEquals("treditional", FPMethod.valueOf("TRADITIONAL_METHODS").displayName());
    }
    @Test
    public void displayNameLam() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_lam)).thenReturn("lam");
        assertEquals("lam", FPMethod.valueOf("LAM").displayName());
    }

    @Test
    public void displayNameCentChroman() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_centchroman)).thenReturn("cent");
        assertEquals("cent", FPMethod.valueOf("CENTCHROMAN").displayName());
    }
    @Test
    public void displayNameNonePs() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_none_ps)).thenReturn("noneps");
        assertEquals("noneps", FPMethod.valueOf("NONE_PS").displayName());
    }

    @Test
    public void displayNameNoneSs() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.fp_register_service_mode_none_ss)).thenReturn("noness");
        assertEquals("noness", FPMethod.valueOf("NONE_SS").displayName());
    }

    @Test
    public void displayNameNone() throws  Exception{
        when(context.getInstance().applicationContext().getString(R.string.ec_register_no_fp)).thenReturn("none");
        assertEquals("none", FPMethod.valueOf("NONE").displayName());
    }


}

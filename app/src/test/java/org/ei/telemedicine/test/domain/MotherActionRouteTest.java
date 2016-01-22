package org.ei.telemedicine.test.domain;

import org.ei.telemedicine.Context;
import org.ei.telemedicine.domain.MotherActionRoute;
import org.ei.telemedicine.dto.Action;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by administrator on 1/12/16.
 */
public class MotherActionRouteTest {
    MotherActionRoute motherActionRoute;
    public String identifier;
    //public String identifier = "sdh";
    Action action;


    @Mock
    Context context;

    @Mock
    private android.content.Context applicationContext;

    @Before
    public void setUp() {
        initMocks(this);
        when(context.applicationContext()).thenReturn(applicationContext);
        Context.setInstance(context);
        //motherActionRoute = new MotherActionRoute.CLOSE("close");
    }
//    @Test
//    public void identifierTest(){
//        String s = motherActionRoute.identifier();
//        assertEquals(s, "sdh");
//    }


//    @Test
//    public void caseIdTest() {
//        String identifier = "sdh";
//        String s = motherActionRoute.identifier("sdh");
//        Assert.assertEquals(s, "sdh");
//
//    }
}

//    @Test
//    public void shouldParseBlankStringAsDefaultMothrtServiceType() throws Exception {
//        MotherActionRoute defaultMethod = MotherActionRoute.valueOf("close");
//        assertEquals(defaultMethod, MotherActionRoute.valueOf("close"));
//    }


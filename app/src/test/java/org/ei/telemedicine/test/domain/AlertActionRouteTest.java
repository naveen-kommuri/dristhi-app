package org.ei.telemedicine.test.domain;

import org.ei.telemedicine.Context;
import org.ei.telemedicine.domain.AlertActionRoute;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by administrator on 1/7/16.
 */
public class AlertActionRouteTest {
    AlertActionRoute alertActionRoute;
    String identifier = "sudheer";
    @Mock
    Context context;

    @Mock
    private android.content.Context applicationContext;
    @Before
    public void setUp(){
        initMocks(this);
        Context.setInstance(context);
        when(context.applicationContext()).thenReturn(applicationContext);

    }
    /*@Test
    public void compsletionDateTest(){
        identifier = alertActionRoute.identifier();
        identifier = alertActionRoute.identifier();
        String s3 = "sudheer";
        Assert.assertEquals(identifier, s3);
    }*/
}

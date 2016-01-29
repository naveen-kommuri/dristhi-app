package org.ei.telemedicine.test.doctor;


import android.test.ActivityTestCase;

import org.ei.telemedicine.doctor.DoctorPlanofCareActivity;
import org.ei.telemedicine.doctor.NativeDoctorActivity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by BELOVED on 19/01/16.
 */
public class NativeDoctorActivityTest extends ActivityTestCase{



    NativeDoctorActivity nativeDoctorActivity;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        nativeDoctorActivity =(NativeDoctorActivity)getActivity();
    }




    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

package org.ei.telemedicine.view.controller;

import android.app.Activity;
import android.content.Intent;

import org.ei.telemedicine.view.activity.NativeANCSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativeChildSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativeECSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativeFPSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativePNCSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativeReportsActivity;

import static org.ei.telemedicine.view.controller.ProfileNavigationController.navigateToANCProfile;
import static org.ei.telemedicine.view.controller.ProfileNavigationController.navigateToChildProfile;
import static org.ei.telemedicine.view.controller.ProfileNavigationController.navigateToECProfile;
import static org.ei.telemedicine.view.controller.ProfileNavigationController.navigateToFPProfile;
import static org.ei.telemedicine.view.controller.ProfileNavigationController.navigateToPNCProfile;
import static org.ei.telemedicine.view.controller.ProfileNavigationController.navigateToProfile;

public class NavigationController {
    private Activity activity;
    private ANMController anmController;

    public NavigationController(Activity activity, ANMController anmController) {
        this.activity = activity;
        this.anmController = anmController;
    }

    public void startReports() {
        activity.startActivity(new Intent(activity, NativeReportsActivity.class));
    }


    public void startECSmartRegistry() {
        activity.startActivity(new Intent(activity, NativeECSmartRegisterActivity.class));
    }

    public void startFPSmartRegistry() {
        activity.startActivity(new Intent(activity, NativeFPSmartRegisterActivity.class));
    }

    public void startANCSmartRegistry() {
        activity.startActivity(new Intent(activity, NativeANCSmartRegisterActivity.class));
    }

    public void startPNCSmartRegistry() {
        activity.startActivity(new Intent(activity, NativePNCSmartRegisterActivity.class));
    }

    public void startChildSmartRegistry() {
        activity.startActivity(new Intent(activity, NativeChildSmartRegisterActivity.class));
    }

    public String get() {
        return anmController.get();
    }

    public void goBack() {
        activity.finish();
    }

    public void startEC(String entityId) {
        navigateToECProfile(activity, entityId);
//        navigateToProfile(activity, entityId);
    }
    public void startFP(String entityId) {
        navigateToFPProfile(activity, entityId);
//        navigateToProfile(activity, entityId);
    }
    public void startDoctor(String formallData, String formData) {
        navigateToProfile(activity, formallData, formData);
    }

    public void startANC(String entityId) {
//        navigateToProfile(activity, entityId);
        navigateToANCProfile(activity, entityId);
    }

    public void startPNC(String entityId) {
        navigateToPNCProfile(activity, entityId);
    }

    public void startChild(String entityId) {
        navigateToChildProfile(activity, entityId);
    }

}

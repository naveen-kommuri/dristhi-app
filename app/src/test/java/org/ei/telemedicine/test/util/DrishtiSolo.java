package org.ei.telemedicine.test.util;

import android.app.Activity;
import android.app.Instrumentation;

import com.robotium.solo.Solo;

import org.ei.telemedicine.view.activity.LoginActivity;
import org.ei.telemedicine.view.activity.NativeHomeActivity;

public class DrishtiSolo extends Solo {
    public DrishtiSolo(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
        Wait.waitForProgressBarToGoAway(activity);
    }

    public DrishtiSolo assertCanLogin(String userName, String password) {
        enterText(0, userName);
        enterText(1, password);
        clickOnButton(0);
        waitForActivity(NativeHomeActivity.class.getSimpleName());
        Wait.waitForFilteringToFinish();
        return this;
    }

    public DrishtiSolo assertCannotLogin(String userName, String password) {
        enterText(0, userName);
        enterText(1, password);
        clickOnButton(0);
        waitForActivity(LoginActivity.class.getSimpleName());
        Wait.waitForFilteringToFinish();
        return this;
    }

    public void logout() {
        sendKey(MENU);
        clickOnActionBarItem(3);
        //clickOnText("Logout");
        waitForActivity(LoginActivity.class.getSimpleName());
    }
}

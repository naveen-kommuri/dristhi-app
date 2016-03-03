package org.ei.telemedicine.view.activity;

import android.app.Application;
import android.content.res.Configuration;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.sync.DrishtiSyncScheduler;

import java.util.Locale;

import static org.ei.telemedicine.util.Log.logInfo;

@ReportsCrashes(
        formKey = "",
        formUri = "https://opensrp.cloudant.com/acra-opensrpp/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.POST,
        formUriBasicAuthLogin = "llseveraffeassireseralmi",
        formUriBasicAuthPassword = "88ec26943ef02c171008f860371f4e100bba3f36",
        mode = ReportingInteractionMode.SILENT
)
public class DrishtiApplication extends Application {
    private Locale locale = null;
    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();
//        ACRA.init(this);

        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
//        applyUserLanguagePreference();
        cleanUpSyncState();
    }

    private void cleanUpSyncState() {
        DrishtiSyncScheduler.stop(getApplicationContext());
        context.allSharedPreferences().saveIsSyncInProgress(false);
    }

    @Override
    public void onTerminate() {
        logInfo("Application is terminating. Stopping Dristhi Sync scheduler and resetting isSyncInProgress setting.");
        cleanUpSyncState();
    }

    private void applyUserLanguagePreference() {
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = context.allSharedPreferences().fetchLanguagePreference();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            locale = new Locale(lang);
            updateConfiguration(config);
        }
    }

    private void updateConfiguration(Configuration config) {
        config.locale = locale;
        Locale.setDefault(locale);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
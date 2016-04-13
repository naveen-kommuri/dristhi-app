package org.ei.telemedicine.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.util.Log;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.sync.DrishtiSyncScheduler;
import org.ei.telemedicine.view.activity.ActionActivity;
import org.ei.telemedicine.view.activity.LoginActivity;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

import static org.ei.telemedicine.util.Log.logInfo;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private org.ei.telemedicine.Context context;
    private final String CALLER = "name";
    private String TAG = "RECEIVER";
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(final Context context, Intent intent) {
        logInfo("Connectivity change receiver triggered.");
        if (intent.getExtras() != null) {
            if (isDeviceDisconnectedFromNetwork(intent)) {
                logInfo("Device got disconnected from network. Stopping Dristhi Sync scheduler.");
                DrishtiSyncScheduler.stop(context);
                LoginActivity.disconnectWS();
                return;
            }
            if (isDeviceConnectedToNetwork(intent)) {
                logInfo("Device got connected to network. Trying to start Dristhi Sync scheduler.");
                DrishtiSyncScheduler.start(context);
                try {
                    if (LoginActivity.mConnection == null || !LoginActivity.mConnection.isConnected())
                        LoginActivity.connectWS();
                } catch (Exception e) {
                    LoginActivity.disconnectWS();
                }
            }
        }
    }

    private boolean isDeviceDisconnectedFromNetwork(Intent intent) {
        return intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
    }

    private boolean isDeviceConnectedToNetwork(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        return networkInfo != null && networkInfo.isConnected();
    }
}


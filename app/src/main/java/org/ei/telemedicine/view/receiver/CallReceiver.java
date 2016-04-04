package org.ei.telemedicine.view.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.view.activity.ActionActivity;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by BELOVED on 08/03/2016.
 */
public class CallReceiver extends BroadcastReceiver {


    private org.ei.telemedicine.Context context;
    private final String CALLER = "name";
    private String TAG = "RECEIVER";
    private final WebSocketConnection mConnection = new WebSocketConnection();


    @Override
    public void onReceive(Context context, Intent intent) {


        if(isConnected(context))
          {
              StartWebConnection(context);
          }
        else {
            Log.d("CONNECTION","LOST connection");
        }
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }


    private void StartWebConnection(final Context mcontext) {

        context = org.ei.telemedicine.Context.getInstance().updateApplicationContext(mcontext.getApplicationContext());
        final String wsuri = context.configuration().drishtiWSURL() + AllConstants.WEBSOCKET;
        final String username =  context.allSharedPreferences().fetchRegisteredANM();
        try {
            final String url = String.format(wsuri, username);
            Log.d("WEBSOCKET_URL",url);

            mConnection.connect(url, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + url);
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);
                    try {
                        JSONObject jObject = new JSONObject(payload);
                        String status = jObject.getString("status");
                        String msg = jObject.getString("msg_type");
                        String caller = jObject.getString("caller");
                        //Log.d(TAG, check);
                        String match = "INI";
                        boolean response = (status.equals(match));
                        if (response) {
                            Intent i = new Intent(mcontext.getApplicationContext(), ActionActivity.class);
                            i.putExtra(CALLER, caller);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mcontext.startActivity(i);
                        }

                    } catch (Exception ex) {
                        Log.d(TAG, ex.toString());
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");

                }
            });
        } catch (WebSocketException e) {
            Log.d(TAG, e.toString());
        }
    }
}

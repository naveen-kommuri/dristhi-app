package org.ei.telemedicine.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.text.WordUtils;
import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.R;
import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

//import org.ei.telemedicine.Context;

public class ActionActivity extends Activity {

    private org.ei.telemedicine.Context context;
    public static boolean isBusy = false;
    protected String callUrl;
    boolean callStatus = false;
    private Ringtone ringtone;
    private String packName = "org.mozilla.firefox";
    final WebSocketConnection mConnection = new WebSocketConnection();
    String callerId = "";

    public String getUsern() {
        context = org.ei.telemedicine.Context.getInstance().updateApplicationContext(this.getApplicationContext());
        return context.allSharedPreferences().fetchRegisteredANM();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            ActionActivity.isBusy = true;
            Intent myIntent = getIntent();
            callerId = myIntent.getStringExtra("name");
            Log.e("ANM", "Before Temp WEb Socket Connection");
            createTempWebSocket(callerId);
            Log.e("ANM", "After Temp WEb Socket Connection");

            Thread ft = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(40000);
                        checkForNoAnswer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            ft.start();

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setContentView(R.layout.activity_action);
        this.setFinishOnTouchOutside(false);
        callUrl = org.ei.telemedicine.Context.getInstance().configuration().drishtiVideoURL() + AllConstants.RECEIVING_URL;

        TextView showCaller = (TextView) findViewById(R.id.txtCaller);
        showCaller.setText(WordUtils.capitalize(callerId) + " is calling....");
        findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                Log.e("Receive URL", callUrl);
                try {
                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call is accepted").put("status", "accept").put("receiver", getUsern()).toString());
                    callStatus = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri url = Uri.parse(String.format(callUrl, getUsern(), callerId));
                if (mConnection.isConnected())
                    mConnection.disconnect();
                ActionActivity.isBusy = false;
                Intent _broswer = new Intent(Intent.ACTION_VIEW, url);

                startActivity(_broswer);

                finish();
                //                if (isPackageExist(packName)) {
                //                _broswer.setComponent(new ComponentName(org.ei.telemedicine.Context.getInstance().configuration().getClientBrowserUrl(), context.configuration().getClientBrowserAPPUrl()));
//                } else {
//                    Toast.makeText(ActionActivity.this, "Video call is compatible with FireFox. Please install", Toast.LENGTH_SHORT).show();
//                    String firefoxUrl = org.ei.telemedicine.Context.getInstance().configuration().getClientAPPURL();
//                    Uri ul = Uri.parse(firefoxUrl);
//                    Intent downLoadfire = new Intent(Intent.ACTION_VIEW, ul);
//                    startActivity(downLoadfire);
//                }
            }
        });

        findViewById(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                try {
                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call is Rejected").put("status", "reject").put("receiver", getUsern()).toString());
                    if (mConnection.isConnected())
                        mConnection.disconnect();
                    ActionActivity.isBusy = false;
                    callStatus = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });


    }

    private void checkForNoAnswer() {
        if (!callStatus) {
            try {
                if (ringtone != null && ringtone.isPlaying())
                    ringtone.stop();
                if (mConnection != null) {
                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "No Answer").put("status", "no_answer").put("receiver", getUsern()).toString());
                    if (mConnection.isConnected())
                        mConnection.disconnect();
                }
                ActionActivity.isBusy = false;
                finish();
//                new AlertDialog.Builder(ActionActivity.this).setMessage("You are missed " + callerId + " call").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private WebSocketConnection createTempWebSocket(final String callerId) {
        try {
            mConnection.connect("ws://202.153.34.169:8004/wscall?id=cli:" + getUsern() + "&peer_id=cli:" + callerId, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    Log.e("Initiate", "ANM Temp WebSocket");
                    try {
                        mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call is ringing").put("status", "ring_starts").put("receiver", getUsern()).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    Log.e("Get ANM ", payload);
                    String status = getDataFromJson(payload, "status");
                    switch (status) {
                        case "disconnect":
                            ringtone.stop();
                            ActionActivity.isBusy = false;
                            Toast.makeText(ActionActivity.this, "Call is disconnected by " + callerId, Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
        return mConnection;
    }

    public Ringtone ringAlarm(Context context) {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            // alert is null, using backup
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {  // I can't see this ever being null (as always have a default notification) but just incase
                // alert backup is null, using 2nd backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), alert);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolumeAlarm = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        //int maxVolumeRing = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolumeAlarm, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        //audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolumeRing,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//        Toast.makeText(context.getApplicationContext(), "alarm started", Toast.LENGTH_LONG).show();
        return r;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    private String getDataFromJson(String jsonStr, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject.has(key) ? jsonObject.getString(key) : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isPackageExist(String packName) {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(packName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

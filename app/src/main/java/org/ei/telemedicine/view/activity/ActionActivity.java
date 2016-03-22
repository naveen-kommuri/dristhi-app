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

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.R;

//import org.ei.telemedicine.Context;

public class ActionActivity extends Activity {

    private org.ei.telemedicine.Context context;

    protected String callUrl;

    private Ringtone ringtone;
    private String packName = "org.mozilla.firefox";

    public String getUsern() {

        context = org.ei.telemedicine.Context.getInstance().updateApplicationContext(this.getApplicationContext());
        return context.allSharedPreferences().fetchRegisteredANM();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
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
        callUrl = org.ei.telemedicine.Context.getInstance().configuration().drishtiVideoURL() + AllConstants.RECEIVING_URL;
        Intent myIntent = getIntent();
        final String callerId = myIntent.getStringExtra("name");
        TextView showCaller = (TextView) findViewById(R.id.txtCaller);
        showCaller.setText(callerId);
        findViewById(R.id.img_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                Log.e("Receive URL", callUrl);
//                if (isPackageExist(packName)) {
                Uri url = Uri.parse(String.format(callUrl, getUsern(), callerId));
                Intent _broswer = new Intent(Intent.ACTION_VIEW, url);
//                _broswer.setComponent(new ComponentName(org.ei.telemedicine.Context.getInstance().configuration().getClientBrowserUrl(), context.configuration().getClientBrowserAPPUrl()));
                startActivity(_broswer);
                finish();
//                } else {
//                    Toast.makeText(ActionActivity.this, "Video call is compatible with FireFox. Please install", Toast.LENGTH_SHORT).show();
//                    String firefoxUrl = org.ei.telemedicine.Context.getInstance().configuration().getClientAPPURL();
//                    Uri ul = Uri.parse(firefoxUrl);
//                    Intent downLoadfire = new Intent(Intent.ACTION_VIEW, ul);
//                    startActivity(downLoadfire);
//                }
            }
        });

        findViewById(R.id.img_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                finish();
            }
        });


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
        Toast.makeText(context.getApplicationContext(), "alarm started", Toast.LENGTH_LONG).show();
        return r;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
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

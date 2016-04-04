package org.ei.telemedicine.doctor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.event.Listener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ei.telemedicine.doctor.DoctorFormDataConstants.formData;

/**
 * Created by naveen on 6/18/15.
 */
public abstract class DoctorPatientDetailSuperActivity extends Activity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private Bundle bundle;
    private String formInfo, documentId, phoneNumber, caseId;
    private String[] details;
    ProgressDialog playProgressDialog;
    MediaPlayer player;
    static String referedDoctor = null;
    int val = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(formData)) {

            formInfo = bundle.getString(DoctorFormDataConstants.formData);
            setupViews();
            details = setDatatoViews(formInfo);

            documentId = details[0] != null ? details[0] : "";
            phoneNumber = details[1] != null ? details[1] : "";
            caseId = details[2] != null ? details[2] : "";

        }
    }

    protected abstract String[] setDatatoViews(String formInfo);

    protected abstract void setupViews();

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
        }
    }

    public void pausePlay() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    public void turnSpeaker() {
        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.STREAM_MUSIC);
        audioManager.setSpeakerphoneOn(true);
    }

    public void playData(String url, final ImageButton ib_play_stehoscope, final ImageButton ib_pause_stehoscope) {
        try {
            turnSpeaker();
        } catch (Exception e) {
            Toast.makeText(DoctorPatientDetailSuperActivity.this, "Unable to play, check audio settings", Toast.LENGTH_SHORT).show();
        }
        try {
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepare();
            playProgressDialog = new ProgressDialog(DoctorPatientDetailSuperActivity.this);
            playProgressDialog.setTitle("Playing Heart sound");
            playProgressDialog.setMessage("Playing Heart sound");
            playProgressDialog.setCancelable(false);
            playProgressDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (player != null && player.isPlaying()) {
                        player.stop();
                        player.release();
                    }
                    ib_play_stehoscope.setVisibility(View.VISIBLE);
                    ib_pause_stehoscope.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                }
            });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playProgressDialog.show();
                    mp.start();
                }
            });

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (playProgressDialog != null && playProgressDialog.isShowing())
                        playProgressDialog.dismiss();
                    ib_play_stehoscope.setVisibility(View.VISIBLE);
                    ib_pause_stehoscope.setVisibility(View.INVISIBLE);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getVitalsData(final String vitalType, final String visitId) {
        getData(AllConstants.VITALS_INFO_URL_PATH + visitId, new Listener<String>() {
            @Override
            public void onEvent(String data) {
                Log.e("Result", visitId);
//                Intent intent = new Intent(DoctorPatientDetailSuperActivity.this, NativeGraphActivity.class);
                Intent intent = new Intent(DoctorPatientDetailSuperActivity.this, NativeChartActivity.class);
                intent.putExtra(AllConstants.VITALS_INFO_RESULT, data);
                intent.putExtra(AllConstants.VITAL_TYPE, vitalType);
                startActivity(intent);
            }
        });
    }

    private Map<String, String> getDoctorsData() {
        String parentDoctors = Context.getInstance().allSettings().fetchParentDoctors();
        if (parentDoctors != null) {
            try {
                JSONArray doctorsArray = new JSONArray(parentDoctors);
                Map<String, String> doctorMapData = new HashMap<String, String>();

                for (int i = 0; i < doctorsArray.length(); i++) {
                    JSONObject docjsonObject = doctorsArray.getJSONObject(i);
                    doctorMapData.put(docjsonObject.getString("name"), docjsonObject.getString("userid"));
                }
                return doctorMapData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void showListDialog(final String doctorId, final String visitId, final String entityId, final String documentId, final String visitType, final String wifeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Doctor");
        ListView modeList = new ListView(this);
        String[] stringArray = new String[]{"Bright Mode", "Normal Mode"};

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        modeList.setAdapter(modeAdapter);
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(DoctorPatientDetailSuperActivity.this, "C_______________________________--" + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
//        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (view != null)
//                    referedDoctor = ((TextView) view).getText().toString();
//
//                getData(AllConstants.DOCTOR_REFER_URL_PATH + doctorId + "&visitid=" + visitId + "&entityid=" + entityId + "&patientname=" + (visitType.equalsIgnoreCase("CHILD") ? "Baby%20of%20" + wifeName : wifeName) + "&doctor_referred=" + referedDoctor, new Listener<String>() {
//                    @Override
//                    public void onEvent(String data) {
//                        Context.getInstance().allDoctorRepository().deleteUseCaseId(visitId);
//                        startActivity(new Intent(DoctorPatientDetailSuperActivity.this, NativeDoctorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//
//                    }
//                });
//                finish();
//            }
//        });
        builder.setView(modeList);
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        final Dialog dialog = builder.create();
        dialog.show();
    }

    private void showRadios(final String doctorId, final String visitId, final String entityId, final String documentId, final String visitType, final String wifeName, final String referedDoctorId) {
        final Map<String, String> doctorsInfo = getDoctorsData();

        if (doctorsInfo != null) {
            final ArrayList<String> doctorIdsList = new ArrayList<>();
            doctorIdsList.addAll(doctorsInfo.values());
            String[] doctors = doctorsInfo.keySet().toArray(new String[doctorsInfo.keySet().size()]);
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this)
                    .setTitle("Choose Doctor")
                    .setSingleChoiceItems(doctors, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            val = which;
                        }
                    });
            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (val != -1) {
                        String doctor = doctorIdsList.get(val);
                        referDoctor(doctorId, visitId, entityId, documentId, visitType, wifeName, doctor);

                    } else
                        Toast.makeText(DoctorPatientDetailSuperActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertdialog2 = builder2.create();
            alertdialog2.show();
        } else
            Toast.makeText(this, "There is no doctor to refer", Toast.LENGTH_SHORT).show();
    }

    private void showRadioButtonDialog() {

        // custom dialog
        final String selected = null;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        dialog.setCancelable(false);
        List<String> stringList = new ArrayList<>();  // here is list
        for (int i = 0; i < 5; i++) {
            stringList.add("RadioButton " + (i + 1));
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.e("selected RadioButton->", btn.getText().toString());
//                        Toast.makeText(DoctorPatientDetailSuperActivity.this, "___" + btn.getText(), Toast.LENGTH_SHORT).show();
                        referedDoctor = btn.getText().toString();
                    }
                }
            }
        });

        dialog.show();

    }

    public void referAnotherDoctor(final String doctorId, final String visitId, final String entityId, final String documentId, final String visitType, final String wifeName, final String referedDoctorId) {
        new AlertDialog.Builder(this).setTitle("Refer Doctor Selection").setMessage("Do you want to choose particular doctor to refer?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showRadios(doctorId, visitId, entityId, documentId, visitType, wifeName, referedDoctorId);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                referDoctor(doctorId, visitId, entityId, documentId, visitType, wifeName, null);
            }
        }).show();

    }

    private void referDoctor(final String doctorId, final String visitId, final String entityId, final String documentId, final String visitType, final String wifeName, final String referedDoctorId) {
        getData(AllConstants.DOCTOR_REFER_URL_PATH + doctorId + "&visitid=" + visitId + "&entityid=" + entityId + "&patientname=" + ((visitType.equalsIgnoreCase("CHILD") ? "Baby%20of%20" + wifeName : wifeName) + "&refdoc=" + referedDoctorId), new Listener<String>() {
            @Override
            public void onEvent(String data) {
                if (data != null && getDatafromJson(data, "hospital").trim().length() != 0) {
                    new AlertDialog.Builder(DoctorPatientDetailSuperActivity.this).setMessage("Record is referred to " + getDatafromJson(data, "hospital")).setCancelable(false).setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Context.getInstance().allDoctorRepository().deleteUseCaseId(visitId);
                            startActivity(new Intent(DoctorPatientDetailSuperActivity.this, NativeDoctorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }
                    }).show();
                }
            }
        });
    }

    public void getDrugData() {
        getData(AllConstants.DRUG_INFO_URL_PATH, new Listener<String>() {
            //        getDataFromServer(new Listener<String>() {
            public void onEvent(String resultData) {
                if (resultData != null) {
//                    Toast.makeText(DoctorPatientDetailSuperActivity.this, "Document Id " + documentId, Toast.LENGTH_SHORT).show();
                    Log.e("Document Id", documentId);
//                    Context.getInstance().allDoctorRepository().updatePocInLocal(documentId, "", "");
                    Intent intent = new Intent(DoctorPatientDetailSuperActivity.this, DoctorPlanofCareActivity.class);
                    intent.putExtra(AllConstants.DRUG_INFO_RESULT, resultData);
                    intent.putExtra(DoctorFormDataConstants.formData, formInfo);
                    intent.putExtra(DoctorFormDataConstants.documentId, documentId);
                    intent.putExtra(DoctorFormDataConstants.phoneNumber, phoneNumber);
                    intent.putExtra(DoctorFormDataConstants.anc_entityId, caseId);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorPatientDetailSuperActivity.this, "Something went wrong! Please try again ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData(final String url, final Listener<String> afterResult) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Context context = Context.getInstance();
                Log.e("URL", context.configuration().dristhiDjangoBaseURL() + url);
                String result = context.userService().gettingFromRemoteURL(context.configuration().dristhiDjangoBaseURL() + url);
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DoctorPatientDetailSuperActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.dialog_message));
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String resultData) {
                super.onPostExecute(resultData);
                if (progressDialog.isShowing())
                    progressDialog.hide();
                afterResult.onEvent(resultData);
            }
        }.execute();
    }

//    public void createFileFrombyte(byte[] audioBytes) {
//        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//            path += "/audiorecordtest.wav";
//            File dstFile = new File(path);
//            if (!dstFile.exists())
//                dstFile.createNewFile();
//            FileOutputStream fout = new FileOutputStream(dstFile);
//            fout.write(audioBytes);
//            fout.flush();
//            fout.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void startPlaying(String path) {
//        MediaPlayer mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(path);
//            mPlayer.prepare();
//            mPlayer.start();
//            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mp.stop();
//                    mp.release();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public String getDatafromJsonArray(String jsonArrayStr) {
        if (jsonArrayStr.trim().length() != 0)
            try {
                String result = "";
                JSONArray jsonArray = new JSONArray(jsonArrayStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getString(i).trim().length() != 0)
                        result = !result.equals("") ? result + jsonArray.getString(i).trim().replace("_", " ") + "," : jsonArray.getString(i).trim().replace("_", " ") + ",";
                }
                return jsonArray.length() == 1 ? result.replace(",", "") : result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return "";
    }

    public String getDatafromJson(String jsonStr, String key) {
        if (jsonStr != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonStr);
                return (jsonData.has(key) && jsonData.getString(key) != null && !jsonData.getString(key).equalsIgnoreCase("null") && !jsonData.getString(key).equalsIgnoreCase("0")) ? jsonData.getString(key).trim().replace(" ", ",").replace("_", " ") : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}

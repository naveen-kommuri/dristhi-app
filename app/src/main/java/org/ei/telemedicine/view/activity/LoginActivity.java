package org.ei.telemedicine.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.doctor.NativeDoctorActivity;
import org.ei.telemedicine.domain.LoginResponse;
import org.ei.telemedicine.event.Listener;
import org.ei.telemedicine.service.PendingFormSubmissionService;
import org.ei.telemedicine.sync.DrishtiSyncScheduler;
import org.ei.telemedicine.view.BackgroundAction;
import org.ei.telemedicine.view.LockingBackgroundTask;
import org.ei.telemedicine.view.ProgressIndicator;
import org.ei.telemedicine.view.UserSettingsActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static org.ei.telemedicine.domain.LoginResponse.SUCCESS;
import static org.ei.telemedicine.event.Event.SYNC_COMPLETED;
import static org.ei.telemedicine.event.Event.SYNC_STARTED;
import static org.ei.telemedicine.util.Log.logError;
import static org.ei.telemedicine.util.Log.logVerbose;

public class LoginActivity extends Activity {
    private Context context;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private final String CALLER = "name";
    ImageView login_logo;
    private ProgressDialog progressDialog, _progressDialog;
    private String TAG = "LoginActivity";
    static boolean isSettingsEnable = false;
    public int settingsBtCount = 0;
    private int waitTime = 5000;
    boolean isAnotherLogin = false;
    public static WebSocketConnection mConnection = null;
    private static android.content.Context mContext;

    public void start() {
        if (LoginActivity.mConnection == null || !LoginActivity.mConnection.isConnected())
            LoginActivity.connectWS();
    }

    private Listener<Boolean> onSyncStartListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            _progressDialog = new ProgressDialog(LoginActivity.this);
            _progressDialog.setMessage("Data is Syncing");
            _progressDialog.setCancelable(false);
            _progressDialog.show();
        }
    };
    private Listener<Boolean> onNetworkChangeListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            if (!data) {
                if (_progressDialog != null) {
                    _progressDialog.dismiss();
                }
            }
        }
    };

    private Listener<Boolean> onSyncCompleteListener = new Listener<Boolean>() {
        @Override
        public void onEvent(Boolean data) {
            //#TODO: RemainingFormsToSyncCount cannot be updated from a back ground thread!!

            if (_progressDialog != null && _progressDialog.isShowing())
                _progressDialog.dismiss();

            PendingFormSubmissionService pendingFormSubmissionService = context.pendingFormSubmissionService();
            final long pendingCount = pendingFormSubmissionService.pendingFormSubmissionCount();
            if (pendingCount == 0)
                logoutUser("complete");
            SYNC_STARTED.removeListener(onSyncStartListener);
            SYNC_COMPLETED.removeListener(onSyncCompleteListener);
        }
    };

    private void checkSettingButton() {
        if (isSettingsEnable) {
            (findViewById(R.id.login_logo)).setClickable(false);
            findViewById(R.id.settings_Button).setVisibility(View.VISIBLE);
        } else
            findViewById(R.id.settings_Button).setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logVerbose("Initializing ...");
        isAnotherLogin = false;
        setContentView(R.layout.login);
        mContext = this.getApplicationContext();

        context = Context.getInstance().updateApplicationContext(this.getApplicationContext());
        initializeLoginFields();
        initializeBuildDetails();
        setDoneActionHandlerOnPasswordField();
        initializeProgressDialog();

        Button settingsButton = (Button) findViewById(R.id.settings_Button);
        settingsButton.setVisibility(View.VISIBLE);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserSettingsActivity.class);
                startActivity(i);
            }
        });

        Button anotherLogin = (Button) findViewById(R.id.clear_Button);
        if (context.allSharedPreferences().fetchRegisteredANM().trim().length() != 0 && context.allSharedPreferences().getPwd().trim().length() != 0) {
            anotherLogin.setText("Not " + context.allSharedPreferences().fetchRegisteredANM() + "! Sign in as another user");
            (findViewById(R.id.ll_another)).setVisibility(View.VISIBLE);
        } else
            (findViewById(R.id.ll_another)).setVisibility(View.GONE);
        anotherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameEditText.getText().toString().trim().length() != 0 && context.allSharedPreferences().getPwd().trim().length() != 0) {
                    isAnotherLogin = true;
                    localLoginWith(userNameEditText.getText().toString(), context.allSharedPreferences().getPwd(), true);
                    PendingFormSubmissionService pendingFormSubmissionService = context.pendingFormSubmissionService();
                    final long pendingCount = pendingFormSubmissionService.pendingFormSubmissionCount();
                    if (pendingCount != 0)
                        new AlertDialog.Builder(LoginActivity.this).setTitle("Previous User Data is not synced completely.").setNeutralButton("Sync Old Data", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isNetworkAvailable()) {
                                    SYNC_STARTED.addListener(onSyncStartListener);
                                    SYNC_COMPLETED.addListener(onSyncCompleteListener);
                                    DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext(), context.allSharedPreferences().getUserRole());
                                    dialog.dismiss();
                                } else
                                    Toast.makeText(LoginActivity.this, "Please check internet connectivity to Sync old user data", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                    else
                        logoutUser("No Data---------+++++++++++");
                } else
                    Toast.makeText(LoginActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private boolean executeOldUserData() {
//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext(), context.allSharedPreferences().getUserRole());
//                return null;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//            }
//        }
//        try {
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void initializeBuildDetails() {
        TextView buildDetailsTextView = (TextView) findViewById(R.id.login_build);
        try {
            buildDetailsTextView.setText("Version " + getVersion() + ", Built on: " + getBuildDate());
        } catch (Exception e) {
            logError("Error fetching build details: " + e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isAnotherLogin && !context.IsUserLoggedOut()) {
            goToHome(context.userService().getUserRole());
        }

        fillUserIfExists();

    }

    public void login(final View view) {
        hideKeyboard();
        view.setClickable(false);

        final String userName = userNameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString();
//        String str = "";
//        if (!context.userService().sameUser(userName)) {
//            str = "exist";
//            new AlertDialog.Builder(this).setTitle(context.allSharedPreferences().fetchRegisteredANM() + " is already login.Do you want proceed as new login?").setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (!context.allSharedPreferences().getIsSyncInProgress() && context.pendingFormSubmissionService().pendingFormSubmissionCount() == 0) {
//                        context.userService().clearData();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Must sync all the data to server for User: " + userName, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }).show();
//        } else {
//            str = "old";
//        }
//        if (str.trim().length() != 0)
        if (context.userService().hasARegisteredUser()) {
            localLogin(view, userName, password);
        } else {
            remoteLogin(view, userName, password);
        }
    }

    public void logoutUser(String str) {
        Log.e("val", str);
        context.userService().logout();
        this.finish();
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private void initializeLoginFields() {
        userNameEditText = ((EditText) findViewById(R.id.login_userNameText));
        passwordEditText = ((EditText) findViewById(R.id.login_passwordText));
    }

    private void setDoneActionHandlerOnPasswordField() {
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login(findViewById(R.id.login_loginButton));
                }
                return false;
            }
        });
    }

    private void initializeProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.loggin_in_dialog_title));
        progressDialog.setMessage(getString(R.string.loggin_in_dialog_message));
    }

    private void localLogin(View view, String userName, String password) {
        if (context.userService().isValidLocalLogin(userName, password)) {
            localLoginWith(userName, password, false);
        } else {
            showErrorDialog(getString(R.string.login_failed_dialog_message));
            view.setClickable(true);
        }
    }

    private void remoteLogin(final View view, final String userName, final String password) {
        tryRemoteLogin(userName, password, new Listener<LoginResponse>() {
            public void onEvent(LoginResponse loginResponse) {
                if (loginResponse == SUCCESS) {
                    remoteLoginWith(userName, password, loginResponse.payload());

                } else {
                    if (loginResponse == null) {
                        showErrorDialog("Login failed. Unknown reason. Try Again");
                    } else {
                        showErrorDialog(loginResponse.message());
                    }
                    view.setClickable(true);
                }
            }
        });
    }

    private void showErrorDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.login_failed_dialog_title))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        dialog.show();
    }

    private void tryRemoteLogin(final String userName, final String password, final Listener<LoginResponse> afterLoginCheck) {
        LockingBackgroundTask task = new LockingBackgroundTask(new ProgressIndicator() {
            @Override
            public void setVisible() {
                if (progressDialog != null)
                    progressDialog.show();
            }

            @Override
            public void setInvisible() {
                progressDialog.dismiss();
            }
        });

        task.doActionInBackground(new BackgroundAction<LoginResponse>() {
            public LoginResponse actionToDoInBackgroundThread() {
                LoginResponse loginResponse = context.userService().isValidRemoteLogin(userName, password);
//                Log.e(TAG, "Payload Data on Login Activity" + loginResponse.payload() != null ? loginResponse.payload() : "No Response");
                return loginResponse;
            }

            public void postExecuteInUIThread(LoginResponse result) {
                afterLoginCheck.onEvent(result);
            }
        });
    }


    private void fillUserIfExists() {
        if (context.userService().hasARegisteredUser()) {
            userNameEditText.setText(context.allSharedPreferences().fetchRegisteredANM());
            userNameEditText.setEnabled(false);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);
    }

    private void localLoginWith(String userName, String password, boolean isAnother) {
        context.userService().localLogin(userName, password);
        context.allSharedPreferences().updateIsFirstLogin(false);
        String userRole = context.userService().getUserRole();
        if (!isAnother) {
            goToHome(userRole);
            if (isNetworkAvailable()) {
                start();
            } else {
                Log.d(TAG, "No connection");
            }
            DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext(), userRole);
        } else {

        }
        //DrishtiCallScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
    }


    private String getFromJson(String jsonStr, String keyValue) {
        try {
            JSONObject jsonData = new JSONObject(jsonStr);
            if (jsonData != null) {
                return jsonData.has(keyValue) ? jsonData.getString(keyValue) : "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void remoteLoginWith(String userName, String password, String loginResponse) {
        String userRole = null, personalInfo = null, location = null, drugs = null, configuration = null, countryCode = null, formFields = null, doctors = null;
        if (loginResponse != null) {
            context.allSharedPreferences().updateIsFirstLogin(true);
            userRole = getFromJson(loginResponse, AllConstants.ROLE);
            doctors = getFromJson(loginResponse, AllConstants.PARENT_DOCTORS);

            personalInfo = getFromJson(loginResponse, AllConstants.PERSONAL_INFO);
            location = getFromJson(personalInfo, "location");
            drugs = getFromJson(personalInfo, "drugs");
            configuration = getFromJson(personalInfo, "configuration");
            countryCode = getFromJson(personalInfo, "countryCode");
            formFields = getFromJson(personalInfo, "formLabels");
            context.userService()
                    .remoteLogin(userName, password, userRole, location, drugs, configuration, countryCode, formFields, doctors);
        }
//        if (userRole != null && userRole.equals(AllConstants.DOCTOR_ROLE)) {
//            context.allSharedPreferences().savePwd(password);
//        }
        context.allSharedPreferences().savePwd(password);
        goToHome(userRole);
        if (isNetworkAvailable()) {
            start();
        } else {
            Log.d(TAG, "No connection");
        }
        DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext(), userRole);

        //DrishtiCallScheduler.startOnlyIfConnectedToNetwork(getApplicationContext());
    }

    private void goToHome(String userRole) {
        Intent intent = new Intent(this, (userRole.equals(AllConstants.ANM_ROLE)) ? NativeHomeActivity.class : NativeDoctorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String getVersion() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        return packageInfo.versionName;
    }

    private String getBuildDate() throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
        ZipFile zf = new ZipFile(applicationInfo.sourceDir);
        ZipEntry ze = zf.getEntry("classes.dex");
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new java.util.Date(ze.getTime()));
    }

    public static void disconnectWS() {
        if (LoginActivity.mConnection != null) {
            LoginActivity.mConnection.disconnect();
            LoginActivity.mConnection = null;
        }
    }

    public static void connectWS() {

        LoginActivity.mConnection = new WebSocketConnection();
        final String wsuri = Context.getInstance().configuration().drishtiWSURL() + AllConstants.WEBSOCKET;
        final String userName = Context.getInstance().allSharedPreferences().fetchRegisteredANM();
        try {
            final String url = String.format(wsuri, userName);
            Log.e("FFFFF", url);

            mConnection.connect(url, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d("LoginActivity-WS Open", "Status: Connected to " + url);
                    //Toast.makeText(getApplicationContext(), String.format(wsuri, getUsern()), Toast.LENGTH_SHORT).show();
                    //mConnection.sendTextMessage("Hello, world!");
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d("LoginActivity-WS onTextMsg", "Got echo: " + payload);
                    try {
                        JSONObject jObject = new JSONObject(payload);
                        String status = jObject.getString("status");
                        String msg = jObject.getString("msg_type");
                        String caller = jObject.getString("caller");
                        String receiver = jObject.getString("receiver");
                        //Log.d(TAG, check);
                        String match = "INI";
                        boolean response = (status.equals(match));
                        if (receiver.equalsIgnoreCase(userName) && response && !ActionActivity.isBusy) {
                            //Toast.makeText(getApplicationContext(),"call started",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(mContext, ActionActivity.class);
                            i.putExtra("name", caller);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(i);
                        }
                    } catch (Exception ex) {
                        Log.d("LoginActivity-WS Exception", ex.toString());
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d("LoginActivity-WS Close", "Connection lost.");
                    //Toast.makeText(getApplicationContext(),"closed",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (WebSocketException e) {
            Log.d("LoginActivity-WS wsException", e.toString());
        }

    }

}

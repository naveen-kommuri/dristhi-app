package org.ei.telemedicine.doctor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.event.Listener;
import org.ei.telemedicine.util.StringUtil;
import org.ei.telemedicine.view.customControls.CustomFontTextView;
import org.ei.telemedicine.view.receiver.ConnectivityChangeReceiver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by naveen on 6/1/15.
 */
public class DoctorPlanofCareActivity extends Activity {
    String url = "", callStatus = "call is initiated";
    boolean isPeerUnavailalbe = false, isCall = false;
    AutoCompleteTextView act_icd10Diagnosis, act_tests, act_drug_name;
    ListView lv_selected_icd10, lv_selected_tests, lv_selected_drugs;
    Switch swich_poc_pending, switch_poc_physical_consultation;
    Spinner sp_services, sp_drug_frequency, sp_drug_direction, sp_drug_dosage;
    EditText et_drug_qty, et_drug_no_of_days, et_reason, et_advice;
    Button bt_save_plan_of_care;
    ImageButton ib_stop_by, ib_add_drug, ib_anm_logo;
    CustomFontTextView tv_stop_date, tv_health_worker_name, tv_health_worker_village, tv_doc_name, tv_doc_type, tv_mother_name, tv_age, tv_visit_type, tv_village;
    WebSocketConnection mConnection = new WebSocketConnection();
    Dialog popup_dialog;
    Object obj;
    ProgressDialog callProgressDialog;
    static PocBaseAdapter pocDiagnosisBaseAdapter, pocTestBaseAdapter;
    static PocDrugBaseAdapter pocDrugBaseAdapter;

    final ArrayList<String> servicesArrayList = new ArrayList<String>();
    ArrayList<String> selectICD10Diagnosis = new ArrayList<String>();
    ArrayList<String> selectTests = new ArrayList<String>();
    ArrayList<PocDrugData> selectDrugs = new ArrayList<PocDrugData>();

    ArrayList<String> pocDrugNamesList = new ArrayList<String>();
    ArrayList<String> pocDrugFrequenciesList = new ArrayList<String>();
    ArrayList<String> pocDrugDirectionsList = new ArrayList<String>();
    ArrayList<String> pocDrugDosagesList = new ArrayList<String>();
    ArrayList<PocDrugData> pocDrugDatas = new ArrayList<PocDrugData>();

    ArrayList<String> pocTestsList = new ArrayList<String>();
    ArrayList<String> pocServicesList = new ArrayList<String>();
    ArrayList<PocDiagnosis> pocDiagnosises = new ArrayList<PocDiagnosis>();
    ArrayList<PocInvestigation> pocInvestigations = new ArrayList<PocInvestigation>();
    SimpleDateFormat dateFormatter;
    Context context;
    private String TAG = "DoctorPlanOfCareActivity";
    String visitType, visitNumber;
    String documentId, formData, phoneNumber, caseId;
    public String CALLER_URL;
    private String packName = "org.mozilla.firefox";
    String nus_name = "";
    String doc_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(AllConstants.DRUG_INFO_RESULT) != null && bundle.getString(DoctorFormDataConstants.documentId) != null && bundle.getString("formData") != null) {
            String resultData = bundle.getString(AllConstants.DRUG_INFO_RESULT);
            caseId = bundle.getString("visitId");
            documentId = bundle.getString(DoctorFormDataConstants.documentId);
            phoneNumber = bundle.getString(DoctorFormDataConstants.phoneNumber);
            formData = bundle.getString("formData");
            try {
                setContentView(R.layout.doc_plan_of_care);
                context = Context.getInstance();
                act_icd10Diagnosis = (AutoCompleteTextView) findViewById(R.id.mact_icd10_diagnosis);
                act_tests = (AutoCompleteTextView) findViewById(R.id.act_tests);
                lv_selected_icd10 = (ListView) findViewById(R.id.lv_selected_icd10);
                lv_selected_tests = (ListView) findViewById(R.id.lv_selected_tests);
                lv_selected_drugs = (ListView) findViewById(R.id.lv_selected_drugs);
                sp_services = (Spinner) findViewById(R.id.sp_services);
                ib_add_drug = (ImageButton) findViewById(R.id.ib_add_drug);

                bt_save_plan_of_care = (Button) findViewById(R.id.bt_save_plan_of_care);
                ib_anm_logo = (ImageButton) findViewById(R.id.iv_anm_logo);
                tv_health_worker_name = (CustomFontTextView) findViewById(R.id.tv_health_worker_name);

                tv_health_worker_village = (CustomFontTextView) findViewById(R.id.tv_health_worker_village_name);
                tv_doc_name = (CustomFontTextView) findViewById(R.id.tv_doc_name);
                tv_doc_type = (CustomFontTextView) findViewById(R.id.tv_doc_type);
                tv_mother_name = (CustomFontTextView) findViewById(R.id.tv_mother_name);
                tv_age = (CustomFontTextView) findViewById(R.id.tv_age);
                tv_visit_type = (CustomFontTextView) findViewById(R.id.tv_visit_type);
                tv_village = (CustomFontTextView) findViewById(R.id.tv_village);


                ib_stop_by = (ImageButton) findViewById(R.id.ib_stop_by_date);
                tv_stop_date = (CustomFontTextView) findViewById(R.id.tv_stop_by_date);

                act_drug_name = (AutoCompleteTextView) findViewById(R.id.act_drug_name);
                swich_poc_pending = (Switch) findViewById(R.id.switch_poc_pending);
                switch_poc_physical_consultation = (Switch) findViewById(R.id.switch_poc_physical_consultation);
                sp_drug_direction = (Spinner) findViewById(R.id.sp_drug_direction);
                sp_drug_dosage = (Spinner) findViewById(R.id.sp_drug_dosage);
                sp_drug_frequency = (Spinner) findViewById(R.id.sp_drug_frequency);
                et_drug_no_of_days = (EditText) findViewById(R.id.et_drug_no_of_days);
                et_drug_qty = (EditText) findViewById(R.id.et_drug_qty);
                et_reason = (EditText) findViewById(R.id.et_reason);
                et_advice = (EditText) findViewById(R.id.et_advice);

                dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

                callProgressDialog = new ProgressDialog(DoctorPlanofCareActivity.this);
                callProgressDialog.setCancelable(false);
                callProgressDialog.setButton("End Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mConnection != null) {
                            try {
                                mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call disconnected").put("status", "disconnect").put("receiver", doc_name).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        disconnect(mConnection);
                        dialog.dismiss();
                    }
                });

                callProgressDialog.setMessage(callStatus);

//                progressDialog.setMessage(getString(R.string.loggin_in_dialog_message));s

                pocServicesList.add("");
//                pocDrugNamesList.add(getString(R.string.please_select_drug));
                pocDrugFrequenciesList.add(getString(R.string.please_select_frequency));
                pocDrugDirectionsList.add(getString(R.string.please_select_direction));
                pocDrugDosagesList.add(getString(R.string.please_select_dosage));
                Log.e("Exist Case Id", caseId);
                String existPocInfo = context.allDoctorRepository().getPocInfoCaseId(caseId);

                if (existPocInfo != null && !existPocInfo.equals("")) {
                    JSONObject pocInfo = new JSONObject(existPocInfo);
                    JSONArray diagnosisArray = pocInfo.has("diagnosis") ? pocInfo.getJSONArray("diagnosis") : new JSONArray();
                    JSONArray investigationsArray = pocInfo.has("investigations") ? pocInfo.getJSONArray("investigations") : new JSONArray();
                    JSONArray drugsArray = pocInfo.has("drugs") ? pocInfo.getJSONArray("drugs") : new JSONArray();
                    Log.e(TAG, "PocInfoCaseId " + diagnosisArray.length() + "--" + drugsArray.length() + "---" + investigationsArray.length() + "----" + existPocInfo);
                    for (int i = 0; i < diagnosisArray.length(); i++) {
                        lv_selected_icd10.setVisibility(View.VISIBLE);
                        selectICD10Diagnosis.add(diagnosisArray.get(i).toString());
                    }
                    for (int i = 0; i < investigationsArray.length(); i++) {
                        lv_selected_tests.setVisibility(View.VISIBLE);
                        selectTests.add(investigationsArray.get(i).toString());
                    }
                    for (int i = 0; i < drugsArray.length(); i++) {
                        lv_selected_drugs.setVisibility(View.VISIBLE);
                        JSONObject drugData = drugsArray.getJSONObject(i);
                        PocDrugData pocDrugData = new PocDrugData();
                        pocDrugData.setDrugName(getData(drugData, "drugName"));
                        pocDrugData.setDosage(getData(drugData, "dosage"));
                        pocDrugData.setFrequncy(getData(drugData, "frequency"));
                        pocDrugData.setDrugNoofDays(getData(drugData, "drugNoOfDays"));
                        pocDrugData.setDrugQty(getData(drugData, "drugQty"));
                        pocDrugData.setDirection(getData(drugData, "direction"));
                        pocDrugData.setDrugStopByDate(getData(drugData, "drugStopDate"));
                        selectDrugs.add(pocDrugData);
                    }
                    et_advice.setText(getData(pocInfo, "advice"));
                }
                final JSONObject pocData = new JSONObject(resultData);
                final JSONObject formDataJson = new JSONObject(formData);
                JSONArray diagnosisJsonArray = pocData.has(AllConstants.POC_DIAGNOSIS) ? pocData.getJSONArray(AllConstants.POC_DIAGNOSIS) : new JSONArray();
                Log.e(TAG, "Diagnosis Size" + diagnosisJsonArray.length());
                for (int i = 0; i < diagnosisJsonArray.length(); i++) {
                    JSONObject diagnosisDataJson = diagnosisJsonArray.getJSONObject(i);
                    PocDiagnosis pocDiagnosis = new PocDiagnosis();
                    pocDiagnosis.setIcd10_name(diagnosisDataJson.getString("ICD10_Name") + "");
                    pocDiagnosis.setIcd10_chapter(diagnosisDataJson.getString("ICD10_Chapter") + "");
                    pocDiagnosis.setIcd10_code(diagnosisDataJson.getString("ICD10_Code") + "");
                    pocDiagnosises.add(pocDiagnosis);
                }

                JSONArray investigationJsonArray = pocData.has(AllConstants.POC_INVESTIGATIONS) ? pocData.getJSONArray(AllConstants.POC_INVESTIGATIONS) : new JSONArray();
                for (int i = 0; i < investigationJsonArray.length(); i++) {
                    JSONObject investigationDataJson = investigationJsonArray.getJSONObject(i);
                    PocInvestigation pocInvestigation = new PocInvestigation();
                    pocInvestigation.setInvestigation_name(investigationDataJson.getString("investigation_name") + "");
                    pocInvestigation.setService_group_name(investigationDataJson.getString("service_group_name") + "");
                    if (!pocServicesList.contains(investigationDataJson.getString("service_group_name")))
                        pocServicesList.add(investigationDataJson.getString("service_group_name"));
                    pocInvestigations.add(pocInvestigation);
                }

                final JSONArray drugsJsonArray = pocData.has(AllConstants.POC_DRUGS) ? pocData.getJSONArray(AllConstants.POC_DRUGS) : new JSONArray();
                for (int i = 0; i < drugsJsonArray.length(); i++) {
                    JSONObject drugJsonObject = drugsJsonArray.getJSONObject(i);
                    PocDrugData pocDrugData = new PocDrugData();
                    pocDrugData.setDrugName(drugJsonObject.getString("name"));
                    pocDrugData.setDirection(drugJsonObject.getString("direction"));
                    pocDrugData.setDosage(drugJsonObject.getString("dosage"));
                    pocDrugData.setFrequncy(drugJsonObject.getString("frequency"));
                    pocDrugDatas.add(pocDrugData);
                    Log.e("sad", drugJsonObject.getString("direction") + pocDrugDirectionsList.contains(drugJsonObject.getString("direction").trim()));

                    pocDrugNamesList.add(WordUtils.capitalize(drugJsonObject.getString("name")));

                    if (!pocDrugDirectionsList.contains(drugJsonObject.getString("direction").trim()))
                        pocDrugDirectionsList.add(drugJsonObject.getString("direction").trim());
                    if (!pocDrugDosagesList.contains(drugJsonObject.getString("dosage").trim()))
                        pocDrugDosagesList.add(drugJsonObject.getString("dosage").trim());
                    if (!pocDrugFrequenciesList.contains(drugJsonObject.getString("frequency").trim()))
                        pocDrugFrequenciesList.add(drugJsonObject.getString("frequency").trim());

                }

                doc_name = Context.getInstance().allSharedPreferences().fetchRegisteredANM();
                nus_name = getData(formDataJson, DoctorFormDataConstants.anmId);
                tv_doc_name.setText(doc_name);
                tv_mother_name.setText(getData(formDataJson, DoctorFormDataConstants.wife_name));
                tv_age.setText(getData(formDataJson, DoctorFormDataConstants.age));
                visitType = getData(formDataJson, DoctorFormDataConstants.visit_type);
                visitNumber = getData(formDataJson, DoctorFormDataConstants.anc_visit_number);

                tv_visit_type.setText(visitType);
                tv_village.setText(getData(formDataJson, DoctorFormDataConstants.village_name));
                tv_health_worker_name.setText(nus_name);
                tv_health_worker_village.setText(getData(formDataJson, DoctorFormDataConstants.village_name));

                Log.e(TAG, selectICD10Diagnosis.size() + "--" + selectDrugs.size() + "--" + selectTests.size());

                pocDiagnosisBaseAdapter = new PocBaseAdapter(DoctorPlanofCareActivity.this, selectICD10Diagnosis);
                lv_selected_icd10.setAdapter(pocDiagnosisBaseAdapter);

                pocTestBaseAdapter = new PocBaseAdapter(DoctorPlanofCareActivity.this, selectTests);
                lv_selected_tests.setAdapter(pocTestBaseAdapter);

                pocDrugBaseAdapter = new PocDrugBaseAdapter(DoctorPlanofCareActivity.this, selectDrugs);
                lv_selected_drugs.setAdapter(pocDrugBaseAdapter);

//                ArrayAdapter adapter = ArrayAdapter.createFromResource(this, pocServicesList, R.layout.spinner_item);
//                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

                sp_services.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.spinner_item, pocServicesList));
                act_icd10Diagnosis.setAdapter(new DiagnosisArrayAdapter(DoctorPlanofCareActivity.this, R.layout.diagnosis_list_item, pocDiagnosises));
//                ArrayAdapter drugAdapter = new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.diagnosis_list_item, R.id.tv_name, pocDrugNamesList);
                Collections.sort(pocDrugNamesList);
                for (String str : pocDrugNamesList) {
                    Log.e("val", str);
                }
                act_drug_name.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.diagnosis_list_item, R.id.tv_name, pocDrugNamesList));

//                sp_drug_name.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.spinner_item, pocDrugNamesList));
                sp_drug_dosage.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.spinner_item, pocDrugDosagesList));
                sp_drug_frequency.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.spinner_item, pocDrugFrequenciesList));
                sp_drug_direction.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.spinner_item, pocDrugDirectionsList));

                ib_anm_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(DoctorPlanofCareActivity.this, VideoActivity.class));
//                        try {
//                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.setComponent(new ComponentName("org.appspot.apprtc",
//                                    "org.appspot.apprtc.ConnectActivity"));
//                            startActivity(intent);
//                        } catch (Exception e) {
//                            Toast.makeText(DoctorPlanofCareActivity.this, "Please install Apprtc APK", Toast.LENGTH_SHORT).show();
//                        }
//                        if (isPackageExist(packName)) {
//                            Uri url = Uri.parse(caller_url);
//                            Intent _broswer = new Intent(Intent.ACTION_VIEW, url);
//                            _broswer.setComponent(new ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App"));
//                            startActivity(_broswer);
//                        } else {
//                            Toast.makeText(DoctorPlanofCareActivity.this, "Video call is compatible with FireFox. Please install", Toast.LENGTH_SHORT).show();
//
//                            Uri ul = Uri.parse(firefoxUrl);
//                            Intent downLoadfire = new Intent(Intent.ACTION_VIEW, ul);
//                            startActivity(downLoadfire);
//                        }
//                        try {
                        if (isNetworkAvailable())
                            createTempWebSocket(doc_name, nus_name);
                        else
                            Toast.makeText(DoctorPlanofCareActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();

                        //Stops
//                        openWeb(doc_name, nus_name);

//Stops

//                        Intent intent = new Intent(Intent.ACTION_VIEW, null);
//                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
////                            intent.setComponent(new ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App"));
//                        //intent.setAction("org.mozilla.gecko.BOOKMARK");
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("args", "--url=" + caller_url);
//                        intent.setData(Uri.parse(url));
//                        startActivity(intent);
//                        } catch (ActivityNotFoundException ex) {
//                            String firefoxUrl = org.ei.telemedicine.Context.getInstance().configuration().getClientAPPURL();
//                            Uri ul = Uri.parse(firefoxUrl);
//                            Intent downLoadfire = new Intent(Intent.ACTION_VIEW, ul);
//                            startActivity(downLoadfire);
//                        }
                    }
                });


                sp_services.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                                      {
                                                          @Override
                                                          public void onItemSelected(AdapterView<?> parent, View view,
                                                                                     int position, long id) {
                                                              TextView tc = (TextView) view;
                                                              if (!tc.getText().toString().equals("")) {
                                                                  String serviceGroupName = pocServicesList.get(position);
                                                                  for (int i = 0; i < pocInvestigations.size(); i++) {
                                                                      PocInvestigation pocInvestigation = pocInvestigations.get(i);
                                                                      if (pocInvestigation.getService_group_name().equals(serviceGroupName) && !pocTestsList.contains(pocInvestigation.getInvestigation_name())) {
                                                                          pocTestsList.add(pocInvestigation.getInvestigation_name());
                                                                      }
                                                                  }
                                                                  for (int i = 0; i < pocTestsList.size(); i++) {
                                                                      Log.e(TAG, pocTestsList.get(i));
                                                                  }
                                                                  act_tests.setAdapter(new ArrayAdapter(DoctorPlanofCareActivity.this, R.layout.diagnosis_list_item, R.id.tv_name, pocTestsList));
                                                              }
                                                          }

                                                          @Override
                                                          public void onNothingSelected(AdapterView<?> parent) {

                                                          }
                                                      }

                );

                act_drug_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                         @Override
                                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                                             TextView tv_drugName = (TextView) view;
                                                             String drugName = parent.getItemAtPosition(position).toString();
                                                             for (int i = 0; i < pocDrugDatas.size(); i++) {
                                                                 PocDrugData pocDrugData = pocDrugDatas.get(i);
                                                                 if (pocDrugData.getDrugName().equals(drugName)) {
                                                                     sp_drug_direction.setSelection(pocDrugDirectionsList.indexOf(pocDrugData.getDirection()), true);
                                                                     sp_drug_frequency.setSelection(pocDrugFrequenciesList.indexOf(pocDrugData.getFrequncy()), true);
                                                                     sp_drug_dosage.setSelection(pocDrugDosagesList.indexOf(pocDrugData.getDosage()), true);
                                                                 }
                                                             }
                                                         }

                                                     }

                );

                act_icd10Diagnosis.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                          {
                                                              @Override
                                                              public void onItemClick(AdapterView parent, View view, int position,
                                                                                      long id) {
                                                                  act_icd10Diagnosis.setText("");
                                                                  lv_selected_icd10.setVisibility(View.VISIBLE);
                                                                  String diagnoseCode = pocDiagnosises.get(position).getIcd10_code().toString();
                                                                  String diagnoseName = pocDiagnosises.get(position).getIcd10_name().toString();
                                                                  if (!selectICD10Diagnosis.contains(diagnoseCode + " - " + diagnoseName)) {
                                                                      selectICD10Diagnosis.add(diagnoseCode + " - " + diagnoseName);
                                                                      pocDiagnosisBaseAdapter.notifyDataSetChanged();
                                                                  }

                                                              }
                                                          }

                );
                act_tests.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                 {
                                                     @Override
                                                     public void onItemClick(AdapterView parent, View view, int position,
                                                                             long id) {
                                                         act_tests.setText("");
                                                         lv_selected_tests.setVisibility(View.VISIBLE);

                                                         TextView tv = (TextView) view.findViewById(R.id.tv_name);
//                                                         String actTestName = pocTestsList.get(position).toString();
                                                         String testName = sp_services.getSelectedItem().toString() + "-" + tv.getText().toString();
                                                         if (!selectTests.contains(testName)) {
                                                             selectTests.add(testName);
                                                             pocTestBaseAdapter.notifyDataSetChanged();
                                                         }
                                                         for (int i = 0; i < selectTests.size(); i++) {
                                                             Log.e(TAG, selectTests.get(i));
                                                         }

                                                     }
                                                 }

                );
                ib_add_drug.setOnClickListener(new View.OnClickListener()

                                               {
                                                   @Override
                                                   public void onClick(View v) {
                                                       lv_selected_drugs.setVisibility(View.VISIBLE);
                                                       boolean isDrugExist = false;
//                                                       String drugName = sp_drug_name.getSelectedItem().toString();
                                                       String drugName = act_drug_name.getText().toString();
                                                       String drugFrequency = sp_drug_frequency.getSelectedItem().toString();
                                                       String drugDosage = sp_drug_dosage.getSelectedItem().toString();
                                                       String drugDirection = sp_drug_direction.getSelectedItem().toString();
                                                       String drugNoOfDays = et_drug_no_of_days.getText().toString();
                                                       String drugQty = et_drug_qty.getText().toString();
                                                       String drugStopDate = tv_stop_date.getText().toString();

                                                       PocDrugData pocDrugData = new PocDrugData();
                                                       pocDrugData.setDrugName(drugName);
                                                       pocDrugData.setDosage(drugDosage);
                                                       pocDrugData.setFrequncy(drugFrequency);
                                                       pocDrugData.setDirection(drugDirection);
                                                       pocDrugData.setDrugNoofDays(drugNoOfDays);
                                                       pocDrugData.setDrugQty(drugQty);

                                                       for (int i = 0; i < selectDrugs.size(); i++) {
                                                           PocDrugData pocDrugData1 = selectDrugs.get(i);
                                                           if (pocDrugData1.getDrugName().equals(drugName)) {
                                                               tv_stop_date.setVisibility(View.VISIBLE);
                                                               ib_stop_by.setVisibility(View.VISIBLE);
                                                               isDrugExist = true;
                                                               pocDrugData.setIsDrugDuplicate(true);
                                                               pocDrugData.setDrugStopByDate(drugStopDate);
                                                           }

                                                       }
                                                       if (drugName.trim().length() != 0 && !drugDirection.equals(getString(R.string.please_select_direction)) && !drugFrequency.equals(getString(R.string.please_select_frequency)) && !drugDosage.equals(getString(R.string.please_select_dosage)) && !drugNoOfDays.equals("") && !drugQty.equals("")) {
                                                           if (isDrugExist) {
                                                               Log.e(TAG, "length " + drugQty.length() + "" + drugNoOfDays.length() + "" + drugStopDate.length() + drugStopDate);
                                                               if (drugQty.length() != 0 && drugNoOfDays.length() != 0 && drugStopDate.trim().length() != 0) {
                                                                   selectDrugs.add(pocDrugData);
                                                                   pocDrugBaseAdapter.notifyDataSetChanged();
                                                               } else {
                                                                   Toast.makeText(DoctorPlanofCareActivity.this, "Please enter Stop by date.", Toast.LENGTH_SHORT).show();
                                                               }
                                                           } else {
                                                               selectDrugs.add(pocDrugData);
                                                               pocDrugBaseAdapter.notifyDataSetChanged();
                                                           }
                                                       } else
                                                           Toast.makeText(DoctorPlanofCareActivity.this, "All Drug fields are mandatory", Toast.LENGTH_SHORT).show();
                                                       tv_stop_date.setText("");
                                                       et_drug_no_of_days.setText("");
                                                       et_drug_qty.setText("");
                                                   }
                                               }
                );
                bt_save_plan_of_care.setOnClickListener(new View.OnClickListener()

                                                        {
                                                            @Override
                                                            public void onClick(View v) {
                  //if (!swich_poc_pending.isChecked() && et_reason.getText().length()@) {
                                                                try {
                                                                    JSONObject resultJson = new JSONObject();
                                                                    JSONArray drugsArray = new JSONArray();
                                                                    JSONArray diagnosisArray = new JSONArray();
                                                                    JSONArray testsArray = new JSONArray();

                                                                    for (int i = 0; i < selectICD10Diagnosis.size(); i++) {
                                                                        diagnosisArray.put(StringUtils.capitalize(selectICD10Diagnosis.get(i).toString()));
                                                                    }
                                                                    Log.e(TAG, "Drugs Size " + selectDrugs.size() + "");
                                                                    for (int i = 0; i < selectDrugs.size(); i++) {
                                                                        JSONObject drugsJson = new JSONObject();
                                                                        PocDrugData pocDrugData = selectDrugs.get(i);
                                                                        drugsJson.put("direction", StringUtils.capitalize(pocDrugData.getDirection()));
                                                                        drugsJson.put("dosage", StringUtils.capitalize(pocDrugData.getDosage()));
                                                                        drugsJson.put("drugName", StringUtils.capitalize(pocDrugData.getDrugName()));
                                                                        drugsJson.put("drugNoOfDays", StringUtils.capitalize(pocDrugData.getDrugNoofDays()));
                                                                        drugsJson.put("drugQty", StringUtils.capitalize(pocDrugData.getDrugQty()));
                                                                        drugsJson.put("drugStopDate", pocDrugData.getDrugStopByDate());
                                                                        drugsJson.put("frequency", StringUtils.capitalize(pocDrugData.getFrequncy()));
                                                                        drugsArray.put(drugsJson);
                                                                    }

                                                                    for (int i = 0; i < selectTests.size(); i++) {
                                                                        testsArray.put(StringUtils.capitalize(selectTests.get(i).toString()));
                                                                    }
                                                                    resultJson.put("visitType", visitType);
                                                                    resultJson.put("visitNumber", visitNumber);
                                                                    resultJson.put("doctorName", Context.getInstance().allSharedPreferences().fetchRegisteredANM());
                                                                    resultJson.put("planofCareDate", dateFormatter.format(new Date()));
                                                                    resultJson.put("diagnosis", diagnosisArray);
                                                                    resultJson.put("drugs", drugsArray);
                                                                    resultJson.put("investigations", testsArray);
                                                                    resultJson.put("pocDate", getCurrentDate());
                                                                    String adviceStr = et_advice.getText().toString() + (switch_poc_physical_consultation.isChecked() ? "\n Physical Examination is Required." : "");
                                                                    resultJson.put("advice", StringUtils.capitalize(adviceStr));
                                                                    //                            resultJson.put("reason", et_reason.getText().toString());
                                                                    Log.e(TAG, "Reason" + et_reason.getText().toString() + "---" + switch_poc_physical_consultation.isChecked() + "");
                                                                    Log.e(TAG, "selected Json" + resultJson.toString());
                                                                    Log.e("Hello", documentId + "::::::::::::::::::::::::::::");

                                                                    if (swich_poc_pending.isChecked()) {
                                                                        if (et_reason.getText().toString().trim().length() != 0) {
                                                                            saveDatainLocal(documentId, resultJson.toString(), et_reason.getText().toString());
                                                                            saveData(documentId, resultJson.toString(), formDataJson, et_reason.getText().toString(), phoneNumber);
                                                                        } else {
                                                                            Toast.makeText(DoctorPlanofCareActivity.this, "Pending Reason must be given", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        if ((diagnosisArray.length() != 0 || drugsArray.length() != 0 || testsArray.length() != 0 || et_advice.getText().toString().trim().length() != 0)) {
                                                                            saveData(documentId, resultJson.toString(), formDataJson, et_reason.getText().toString(), phoneNumber);
                                                                        } else {
                                                                            Toast.makeText(DoctorPlanofCareActivity.this, "Plan of care must be given", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
//                                                                } else
//                                                                    Toast.makeText(DoctorPlanofCareActivity.this, "Reason mandatory", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                );
                swich_poc_pending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

                                                             {
                                                                 @Override
                                                                 public void onCheckedChanged(CompoundButton buttonView,
                                                                                              boolean isChecked) {
                                                                     if (isChecked) {
                                                                         et_reason.setVisibility(View.VISIBLE);
                                                                     } else {
                                                                         et_reason.setText("");
                                                                         et_reason.setVisibility(View.GONE);
                                                                     }
                                                                 }
                                                             }

                );
                ib_stop_by.setOnClickListener(new View.OnClickListener()

                                              {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Calendar calendar = Calendar.getInstance();
                                                      new DatePickerDialog(DoctorPlanofCareActivity.this, new DatePickerDialog.OnDateSetListener() {
                                                          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                              Calendar newDate = Calendar.getInstance();
                                                              newDate.set(year, monthOfYear, dayOfMonth);
                                                              tv_stop_date.setVisibility(View.VISIBLE);
                                                              tv_stop_date.setText(dateFormatter.format(newDate.getTime()));
                                                          }

                                                      }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                                                  }
                                              }

                );

            } catch (
                    Exception e
                    )

            {
                e.printStackTrace();
                Toast.makeText(DoctorPlanofCareActivity.this, "Something went wrong! Please try again" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else

        {
            Log.e(TAG, "No Data");
        }
    }

    private void checkForNoAnswer() {
        if (callProgressDialog != null && callProgressDialog.isShowing()) {
            callProgressDialog.dismiss();
            if (mConnection != null && mConnection.isConnected()) {
                mConnection.disconnect();
            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DoctorPlanofCareActivity.this, "Unable to reach anm. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openWeb(String doc_name, String nus_name) {
        CALLER_URL = context.configuration().drishtiVideoURL() + AllConstants.CALLING_URL;
        String caller_url = String.format(CALLER_URL, doc_name, nus_name);
        Log.e("Calling URL", caller_url);
//                        startActivity(new Intent(DoctorPlanofCareActivity.this, VideoCallActivity.class).putExtra("loadUrl", "http://www.google.com"));
        Uri url = Uri.parse(caller_url);
        Intent _broswer = new Intent(Intent.ACTION_VIEW, url);
        startActivity(_broswer);

    }

    private String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
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

    private String getData(JSONObject jsonData, String key) {
        Log.e(TAG, key);
        if (jsonData != null) try {
//            JSONObject jsonData = new JSONObject(formData);
            Log.e(TAG, jsonData.get(key) + key);
            return (jsonData != null && jsonData.has(key)) ? jsonData.getString(key) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveDatainLocal(String documentId, String pocJsonInfo, String
            pocPendingInfo) {
        Log.e("Case Id Local", caseId);
        context.allDoctorRepository().updatePocInLocal(caseId, pocJsonInfo, pocPendingInfo);
//        if (!pocPendingInfo.equals(""))
//            gotoHome();
    }

    private void saveData(final String documentID, String pocJsonData, JSONObject formDataJson, final String pocPendingReason, final String phoneNumber) {
        savePocData(documentID, pocJsonData, formDataJson, pocPendingReason, phoneNumber, new Listener<String>() {
                    //        getDataFromServer(new Listener<String>() {
                    public void onEvent(String resultData) {
                        if (resultData != null) {
                            if (pocPendingReason.length() == 0) {
                                Log.e("Case Id", documentID + "------------" + caseId);
                                context.allDoctorRepository().deleteUseCaseId(caseId);
                            }
                            Toast.makeText(DoctorPlanofCareActivity.this, "Plan of care is submitted successfully.", Toast.LENGTH_SHORT).show();
                            gotoHome();
                        } else {
                            Toast.makeText(DoctorPlanofCareActivity.this, "Plan of care is not saved!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );
    }

    private void gotoHome() {
        startActivity(new Intent(DoctorPlanofCareActivity.this, NativeDoctorActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void savePocData(final String docId, final String pocJsonData,
                            final JSONObject formDataJson, final String pendingReason, final String phoneNumber, final Listener<String> afterResult) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String result = null;
                try {
                    List<NameValuePair> _params = new ArrayList<NameValuePair>();
                    _params.add(new BasicNameValuePair("docid", docId));
                    _params.add(new BasicNameValuePair("doctorid", context.allSharedPreferences().fetchRegisteredANM()));
                    _params.add(new BasicNameValuePair("pocinfo", pocJsonData));
                    _params.add(new BasicNameValuePair("pending", pendingReason));
                    _params.add(new BasicNameValuePair("entityid", formDataJson.getString(DoctorFormDataConstants.entityId)));
                    _params.add(new BasicNameValuePair("patientph", phoneNumber));

                    switch (formDataJson.getString(DoctorFormDataConstants.visit_type)) {
                        case DoctorFormDataConstants.ancvisit:
                            _params.add(new BasicNameValuePair("visitid", formDataJson.getString(DoctorFormDataConstants.anc_entityId)));
                            break;
                        case DoctorFormDataConstants.pncVisit:
                            _params.add(new BasicNameValuePair("visitid", formDataJson.getString(DoctorFormDataConstants.pnc_entityId)));
                            break;
                        case DoctorFormDataConstants.childVisit:
                            _params.add(new BasicNameValuePair("visitid", formDataJson.getString(DoctorFormDataConstants.child_entityId)));
                            break;
                    }

                    String encodedParams = URLEncodedUtils.format(_params, "utf-8");
                    String url = context.configuration().dristhiDjangoBaseURL() + AllConstants.POC_DATA_SAVE_URL_PATH + encodedParams;
                    Log.e("Url", url);
                    result = context.userService().gettingFromRemoteURL(url);
                    Log.e(TAG, "Save POC Info " + result);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String resultData) {
                super.onPostExecute(resultData);
                afterResult.onEvent(resultData);
            }
        }.execute();

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

    private void createTempWebSocket(final String doc_name, final String nus_name) {
//        context = org.ei.telemedicine.Context.getInstance().updateApplicationContext(this.getApplicationContext());
        final String wsuri = "ws://202.153.34.169:8004/wscall?id=cli:" + doc_name + "&peer_id=cli:" + nus_name;

        try {
            if (mConnection != null && mConnection.isConnected())
                mConnection.disconnect();
            Log.e("WsUrl", wsuri);
            if (mConnection == null)
                mConnection = new WebSocketConnection();
            mConnection.connect(wsuri, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    Thread ft = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(45000);
                                checkForNoAnswer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ft.start();
                    callProgressDialog.setTitle("Call to " + nus_name);
                    callProgressDialog.show();
                    Log.e("Temp", "Socket is created");
                    try {
                        mConnection.sendTextMessage(new JSONObject().put("msg_type", "initated").put("status", "INI").put("caller", doc_name).put("receiver", nus_name).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    Log.e("Temp Payload", "Getting Data" + payload);

                    String status = getDataFromJson(payload, "status");
                    switch (status) {
                        case "ring_starts":
                            isPeerUnavailalbe = false;
                            callStatus = "Ringing";
                            runOnUiThread(changeMessage);
                            Toast.makeText(DoctorPlanofCareActivity.this, "Waiting for response from " + getDataFromJson(payload, "receiver"), Toast.LENGTH_SHORT).show();
//                            new AlertDialog.Builder(TempWebSocket.this).setMessage("Ring Start").show();
                            break;
                        case "accept":
                            isPeerUnavailalbe = false;
                            if (callProgressDialog != null && callProgressDialog.isShowing())
                                callProgressDialog.dismiss();
                            Toast.makeText(DoctorPlanofCareActivity.this, getDataFromJson(payload, "receiver") + " accepted the call", Toast.LENGTH_SHORT).show();
                            openWeb(DoctorPlanofCareActivity.this.doc_name, DoctorPlanofCareActivity.this.nus_name);
                            disconnect(mConnection);
//                            new AlertDialog.Builder(TempWebSocket.this).setMessage("Ring Start").show();
                            break;
                        case "reject":
                            isPeerUnavailalbe = false;
                            if (callProgressDialog != null && callProgressDialog.isShowing())
                                callProgressDialog.dismiss();
                            Toast.makeText(DoctorPlanofCareActivity.this, getDataFromJson(payload, "receiver") + " rejected the call", Toast.LENGTH_SHORT).show();
                            disconnect(mConnection);
                            break;
                        case "no_answer":
                            isPeerUnavailalbe = false;
                            if (callProgressDialog != null && callProgressDialog.isShowing())
                                callProgressDialog.dismiss();
                            new AlertDialog.Builder(DoctorPlanofCareActivity.this).setMessage(getDataFromJson(payload, "receiver") + " is not responding. Please try later").
                                    setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                            Toast.makeText(DoctorPlanofCareActivity.this, getDataFromJson(payload, "receiver") + " is not responding. Please try later", Toast.LENGTH_SHORT).show();
                            disconnect(mConnection);
                            break;
                        default:
                            isPeerUnavailalbe = true;
                            if (callProgressDialog != null && callProgressDialog.isShowing())
                                callProgressDialog.dismiss();

                            disconnect(mConnection);
                            break;
                    }
                    if (isPeerUnavailalbe) {
                        new AlertDialog.Builder(DoctorPlanofCareActivity.this).setMessage("Unable to reach " + nus_name + ". Please try again").
                                setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                        Toast.makeText(DoctorPlanofCareActivity.this, "Unable to reach " + nus_name + ". Please try again", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    Log.e("Temp", "Socket is closed");
                    mConnection = null;
                }
            });

        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void disconnect(WebSocketConnection mConnection) {
        if (mConnection != null) {
            mConnection.disconnect();
        }
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

    private Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            //Log.v(TAG, strCharacters);
            callProgressDialog.setMessage(callStatus);
        }
    };
}
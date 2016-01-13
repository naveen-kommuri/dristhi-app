package org.ei.telemedicine.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.Context;
import org.ei.telemedicine.R;
import org.ei.telemedicine.adapter.ReportsInfoAdapter;
import org.ei.telemedicine.domain.ReportInfo;
import org.ei.telemedicine.event.Listener;
import org.ei.telemedicine.view.customControls.CustomFontTextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by naveen on 1/6/16.
 */
public class ReportsInfoActivity extends Activity {
    org.ei.telemedicine.view.customControls.CustomFontTextView tv_heading;
    ListView lv_services;
    String service;
    ProgressDialog progressDialog;
    ArrayList<ReportInfo> reports;
    ReportsInfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        tv_heading = (CustomFontTextView) findViewById(R.id.tv_heading);
        reports = new ArrayList<ReportInfo>();
        lv_services = (ListView) findViewById(R.id.lv_services);
        infoAdapter = new ReportsInfoAdapter(ReportsInfoActivity.this, reports);
        lv_services.setAdapter(infoAdapter);
        if (getIntent().getExtras() != null) {
            service = getIntent().getExtras().getString("service");
            tv_heading.setText(service);
            reports.clear();
            switch (service) {
                case AllConstants.Reports.FP_SERVICES:
                    reports = getReports("fp");
//                    reports.add(new ReportInfo("Sample", "12", "34", "234"));
                    infoAdapter.notifyDataSetChanged();
                    break;
                case AllConstants.Reports.ANC_SERVICES:
                    reports = getReports("anc");
                    infoAdapter.notifyDataSetChanged();
                    break;
                case AllConstants.Reports.PREGNANCY:
                    reports = getReports("pregnancy");
                    infoAdapter.notifyDataSetChanged();
                    break;
                case AllConstants.Reports.PNC_SERVICES:
                    reports = getReports("pnc");
                    infoAdapter.notifyDataSetChanged();
                    break;
                case AllConstants.Reports.CHILD_SERVICES:
                    reports = getReports("child");
                    infoAdapter.notifyDataSetChanged();
                    break;
                case AllConstants.Reports.MORTALITY:
                    reports = getReports("mortality");
                    infoAdapter.notifyDataSetChanged();
                    break;

            }
        }
    }

    private ArrayList<ReportInfo> getReports(final String serviceType) {
        final ArrayList<ReportInfo> reports = new ArrayList<ReportInfo>();
        String reporingUrl = String.format(AllConstants.REPORTS_URL, serviceType, Context.getInstance().allSharedPreferences().fetchRegisteredANM());
//        String reporingUrl = "http://10.10.11.6:8000/reporting/?activity=fp&anmid=anm333";
        getData(reporingUrl, new Listener<String>() {
            @Override
            public void onEvent(String data) {
                reports.clear();
                if (data != null) {
                    try {
                        Log.e("Dat", data);
                        JSONArray reportsArray = new JSONArray(data);
                        for (int i = 0; i < reportsArray.length(); i++) {
                            JSONObject jsonObject = reportsArray.getJSONObject(i);
                            reports.add(new ReportInfo(jsonObject.getString("name"), jsonObject.getString("annual_target"), jsonObject.getString("month"), jsonObject.getString("year")));
                        }
                        lv_services.setAdapter(new ReportsInfoAdapter(ReportsInfoActivity.this, reports));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Log.e("Data", reports.size() + "");
        infoAdapter.notifyDataSetChanged();
        return reports;
    }

    private void getData(final String url, final Listener<String> afterResult) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                Context context = Context.getInstance();
                String callingUrl = context.configuration().dristhiDjangoBaseURL() + url;
                Log.e("URL", callingUrl);
                String result = context.userService().gettingFromRemoteURL(callingUrl);
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ReportsInfoActivity.this);
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
}

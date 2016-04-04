package org.ei.telemedicine.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.R;

import java.util.ArrayList;

/**
 * Created by naveen on 1/6/16.
 */
public class NativeReportsActivity extends Activity {
    ListView lv_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        lv_services = (ListView) findViewById(R.id.lv_services);
        final ArrayList<String> servicesList = new ArrayList<String>();
        servicesList.add(AllConstants.Reports.FP_SERVICES);
        servicesList.add(AllConstants.Reports.ANC_SERVICES);
        servicesList.add(AllConstants.Reports.PREGNANCY);
//        servicesList.add(AllConstants.Reports.PNC_SERVICES);
        servicesList.add(AllConstants.Reports.CHILD_SERVICES);
        servicesList.add(AllConstants.Reports.MORTALITY);

        lv_services.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, servicesList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setPadding(5, 5, 5, 5);
                text.setTextSize(20);
                return view;
            }
        });
        lv_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(NativeReportsActivity.this, servicesList.get(i).toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NativeReportsActivity.this, ReportsInfoActivity.class).putExtra("service", servicesList.get(i).toString()));
            }
        });
    }
}

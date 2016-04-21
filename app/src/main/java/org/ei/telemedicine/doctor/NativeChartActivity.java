package org.ei.telemedicine.doctor;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.lang3.text.WordUtils;
import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static org.ei.telemedicine.AllConstants.GraphFields.BLOODGLUCOSEDATA;
import static org.ei.telemedicine.AllConstants.GraphFields.BP_DIA;
import static org.ei.telemedicine.AllConstants.GraphFields.BP_SYS;
import static org.ei.telemedicine.AllConstants.GraphFields.CHILD_TEMPERATURE;
import static org.ei.telemedicine.AllConstants.GraphFields.FETALDATA;
import static org.ei.telemedicine.AllConstants.GraphFields.TEMPERATURE;
import static org.ei.telemedicine.AllConstants.GraphFields.VISITNUMBER;
import static org.ei.telemedicine.AllConstants.GraphFields.VISIT_DATE;

/**
 * Created by Naveen on 3/24/2016.
 */
public class NativeChartActivity extends Activity {
    String vitalsData, vitalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_chart);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vitalsData = bundle.getString(AllConstants.VITALS_INFO_RESULT);
            vitalType = bundle.getString(AllConstants.VITAL_TYPE);
            BarChart chart = (BarChart) findViewById(R.id.chart);
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(30f);
            xAxis.setTextColor(Color.RED);
            xAxis.setDrawAxisLine(true);
//            xAxis.setAdjustXLabels(true);


            try {
                if (vitalsData != null && new JSONArray(vitalsData).length() != 0)
                    if (vitalType.equals(AllConstants.GraphFields.BP)) {
                        BarData data = new BarData(getXAxisValues(vitalsData), getBPDataSets(vitalsData));
                        chart.setData(data);
                        chart.setNoDataText("No Data");
                        chart.setDescription("BP");

                        chart.setDescriptionTextSize(20f);
                        chart.animateX(2000);
                        chart.invalidate();

                        Legend legend = chart.getLegend();
                        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                        legend.setTextSize(20);
                        legend.setFormSize(15);
                        legend.setTextColor(Color.WHITE);

                    } else if (vitalType.equals(AllConstants.GraphFields.TEMPERATURE) || vitalType.equals(AllConstants.GraphFields.CHILD_TEMPERATURE)) {
                        BarData data = new BarData(getXAxisValues(vitalsData), getTempDataSets(vitalsData, vitalType));
                        chart.setData(data);
                        chart.setNoDataText("No Data");
                        chart.setDescription("Temperature");

                        chart.setDescriptionTextSize(20f);
                        chart.animateX(2000);
                        chart.invalidate();

                        Legend legend = chart.getLegend();
                        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                        legend.setTextSize(20);
                        legend.setFormSize(15);
                        legend.setTextColor(Color.WHITE);

                    } else {
                        BarData data = new BarData(getXAxisValues(vitalsData), getVitalDataSet(vitalsData, vitalType));
                        chart.setData(data);
                        chart.setNoDataText("No Data");
                        chart.setDescription(WordUtils.capitalize(vitalType));
                        chart.setDescriptionTextSize(20f);
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
                        Legend legend = chart.getLegend();
                        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                        legend.setTextSize(20);
                        legend.setFormSize(15);
                        legend.setTextColor(Color.WHITE);

                    }
                else {
                    Toast.makeText(this, "There is no previous vital readings.", Toast.LENGTH_SHORT).show();
                    this.finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private ArrayList<BarDataSet> getVitalDataSet(String vitalsData, String vitalType) {

        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> vitalDatas = new ArrayList<>();
        String units = "";
        JSONArray vitalsArray = null;
        try {
            vitalsArray = new JSONArray(vitalsData);
            for (int i = 0; i < vitalsArray.length(); i++) {
                switch (vitalType) {

                    case BLOODGLUCOSEDATA:
                        float bgmVal;
                        units = "mmol/L";
                        try {
                            bgmVal = Float.parseFloat(getDatafromJson(vitalsArray.getJSONObject(i).toString(), BLOODGLUCOSEDATA));
                        } catch (Exception e) {
                            bgmVal = 0;
                        }
                        vitalDatas.add(new BarEntry(bgmVal, i));
                        break;
                    case FETALDATA:
                        float fetalVal;
                        units = "bpm";
                        try {
                            fetalVal = Float.parseFloat(getDatafromJson(vitalsArray.getJSONObject(i).toString(), FETALDATA));
                        } catch (Exception e) {
                            fetalVal = 0;
                        }
                        vitalDatas.add(new BarEntry(fetalVal, i));
                        break;
                }
            }
            BarDataSet barDataSet1 = new BarDataSet(vitalDatas, WordUtils.capitalize(vitalType) + ((units.trim().length() != 0) ? (" in " + units) : ""));
            barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

            dataSets = new ArrayList<>();
            dataSets.add(barDataSet1);
            return dataSets;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataSets;


    }

    private ArrayList<BarDataSet> getBPDataSets(String vitalsData) {
        ArrayList<BarDataSet> bpSets = null;
        ArrayList<BarEntry> bpSysData = new ArrayList<>();
        ArrayList<BarEntry> bpDiaData = new ArrayList<>();
        JSONArray vitalsArray = null;
        try {
            vitalsArray = new JSONArray(vitalsData);
            for (int i = 0; i < vitalsArray.length(); i++) {
                bpSysData.add(new BarEntry(getIntDatafromJson(vitalsArray.getJSONObject(i).toString(), BP_SYS), i));
                bpDiaData.add(new BarEntry(getIntDatafromJson(vitalsArray.getJSONObject(i).toString(), BP_DIA), i));
            }
            BarDataSet barDataSet1 = new BarDataSet(bpSysData, "Bp Systolic in mmHg");
            barDataSet1.setValueTextSize(20f);
            barDataSet1.setColor(getResources().getColor(android.R.color.holo_green_dark));
//            barDataSet1.setValueTextColor(Color.GREEN);

            BarDataSet barDataSet2 = new BarDataSet(bpDiaData, "Bp Diastolic in mmHg");
            barDataSet2.setColor(getResources().getColor(android.R.color.holo_blue_dark));
            barDataSet2.setValueTextSize(20f);

            bpSets = new ArrayList<>();
            bpSets.add(barDataSet1);
            bpSets.add(barDataSet2);
            return bpSets;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bpSets;
    }

    private ArrayList<BarDataSet> getTempDataSets(String vitalsData, String tag) {
        ArrayList<BarDataSet> tempSets = null;
        ArrayList<BarEntry> tempCData = new ArrayList<>();
        ArrayList<BarEntry> tempFData = new ArrayList<>();
        JSONArray vitalsArray = null;
        try {
            vitalsArray = new JSONArray(vitalsData);
//            String result = new String(resultData);
//            String result_in_faren = String.format("%.02f", (Float.parseFloat(result) * 1.8) + 32);
            for (int i = 0; i < vitalsArray.length(); i++) {
                String temp = getStringDatafromJson(vitalsArray.getJSONObject(i).toString(), tag);
                if (temp.trim().length() != 0) {
                    if (!temp.contains("-"))
                        temp = temp + "-C";
                    String[] temps = temp.split("-");
                    String _temperature = temps[0].trim().length() != 0 ? temps[0] : "0";
                    if (temps[1].equalsIgnoreCase("c")) {
                        double val = 0;
                        if (!_temperature.equalsIgnoreCase("0"))
                            val = (Float.parseFloat(_temperature) * 1.8) + 32;
                        String result_in_faren = String.format("%.02f", val);
                        tempCData.add(new BarEntry(Float.parseFloat(_temperature), i));
                        tempFData.add(new BarEntry(Float.parseFloat(result_in_faren), i));
                    } else if (temps[1].equalsIgnoreCase("f")) {
                        float val = 0;
                        if (!_temperature.equalsIgnoreCase("0"))
                            val = (((Float.parseFloat(_temperature) - 32) * 5) / 9);
                        String result_in_cen = String.format("%.02f", val);
                        tempFData.add(new BarEntry(Float.parseFloat(_temperature), i));
                        tempCData.add(new BarEntry(Float.parseFloat(result_in_cen), i));
                    }
                }
            }
            BarDataSet barDataSet1 = new BarDataSet(tempCData, "Temperature in °C");
            barDataSet1.setValueTextSize(20f);
            barDataSet1.setColor(getResources().getColor(android.R.color.holo_green_dark));
//            barDataSet1.setValueTextColor(Color.GREEN);

            BarDataSet barDataSet2 = new BarDataSet(tempFData, "Temperature in °F");
            barDataSet2.setColor(getResources().getColor(android.R.color.holo_blue_dark));
            barDataSet2.setValueTextSize(20f);
            tempSets = new ArrayList<>();
            tempSets.add(barDataSet1);
            tempSets.add(barDataSet2);
            return tempSets;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempSets;
    }

    private ArrayList<String> getXAxisValues(String vitalsData) {
        ArrayList<String> xAxis = new ArrayList<>();
        JSONArray vitalsArray = null;
        try {
            vitalsArray = new JSONArray(vitalsData);
            for (int i = 0; i < vitalsArray.length(); i++) {
                String visitNo = !getStringDatafromJson(vitalsArray.getJSONObject(i).toString(), VISITNUMBER).equals("") ? "ANC- " + getStringDatafromJson(vitalsArray.getJSONObject(i).toString(), VISITNUMBER) : "";
                String visitDate = getStringDatafromJson(vitalsArray.getJSONObject(i).toString(), VISIT_DATE);
                xAxis.add(String.format(visitNo, "\n", visitDate));
            }
            for (int j = xAxis.size(); j < 5; j++) {
                xAxis.add(j + 1 + "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xAxis;
    }

//    private ArrayList<BarDataSet> getDataSet() {
//        ArrayList<BarDataSet> dataSets = null;
//
//        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
//        valueSet1.add(v1e6);
//
//        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
//        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
//        valueSet2.add(v2e1);
//        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
//        valueSet2.add(v2e2);
//        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
//        valueSet2.add(v2e3);
//        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
//        valueSet2.add(v2e4);
//        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
//        valueSet2.add(v2e5);
//        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
//        valueSet2.add(v2e6);
//
//        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
//        barDataSet1.setColor(Color.rgb(0, 155, 0));
//        barDataSet1.setValueTextColor(Color.GREEN);
//
//        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        dataSets = new ArrayList<>();
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
//        return dataSets;
//    }

    public int getIntDatafromJson(String jsonStr, String key) {
        if (jsonStr != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonStr);
                return jsonData.has(key) ? jsonData.getInt(key) : 0;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public String getStringDatafromJson(String jsonStr, String key) {
        if (jsonStr != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonStr);
                return jsonData.has(key) ? jsonData.getString(key) : "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getDatafromJson(String jsonStr, String key) {
        if (jsonStr != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonStr);
                return jsonData.has(key) && jsonData.getString(key).trim().length() != 0 ? jsonData.getString(key) : "0";
            } catch (JSONException e) {
                Log.e("Ersrot", e.toString());
                e.printStackTrace();
            }
        }
        return "0";
    }

    public String getStrDatafromJson(String jsonStr, String key) {
        if (jsonStr != null) {
            try {
                JSONObject jsonData = new JSONObject(jsonStr);
                String tempVal = "0";
                if (jsonData.has(key) && jsonData.getString(key).contains("-")) {
                    String[] temp = jsonData.getString(key).split("-");
                    tempVal = temp[0].trim().length() == 0 ? "0" : temp[0];
                }
                return tempVal;
            } catch (JSONException e) {
                Log.e("Ersrot", e.toString());
                e.printStackTrace();
            }
        }
        return "0";
    }

}

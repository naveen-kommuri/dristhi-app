package org.ei.telemedicine.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.ei.telemedicine.AllConstants;
import org.ei.telemedicine.R;
import org.ei.telemedicine.view.customControls.CustomFontTextView;

/**
 * Created by Naveen on 2/29/2016.
 */
public class BluetoothInstructionsActivity extends Activity {
    org.ei.telemedicine.view.customControls.CustomFontTextView tv_dev_desc;
    LinearLayout ll_dev_inst;
    String devType;
    CustomFontTextView bt_ok;
    LinearLayout ll_bgm, ll_bp, ll_eet, ll_fetal, ll_steh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.bluetooth_instructions);
        bt_ok = (CustomFontTextView) findViewById(R.id.bt_ok);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            devType = bundle.getString("device");
        }
        ll_bgm = (LinearLayout) findViewById(R.id.ll_bgm);
        ll_bp = (LinearLayout) findViewById(R.id.ll_bp);
        ll_eet = (LinearLayout) findViewById(R.id.ll_eet);
        ll_fetal = (LinearLayout) findViewById(R.id.ll_fetal);
        ll_steh = (LinearLayout) findViewById(R.id.ll_steh);

        switch (devType) {
            case "bgm":
                changeVisibility(ll_bgm);
                changeTitle("Blood Glucose Meter");
                break;
            case "bp":
                changeVisibility(ll_bp);
                changeTitle("Electronic Sphygmomanometer");
                break;
            case "eet":
                changeVisibility(ll_eet);
                changeTitle("Ear Thermometer");
                break;
            case "fetal":
                changeVisibility(ll_fetal);
                changeTitle("Fetal Doppler");
                break;
            case "steh":
                changeVisibility(ll_steh);
                changeTitle("E-Stethoscope");
                break;
        }
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(AllConstants.INSTRUCTS_INFO_RESULT_CODE, new Intent().putExtra("device", devType));
                finish();
            }
        });
//        tv_dev_desc = (CustomFontTextView) findViewById(R.id.tv_bt_dev_desc);
//        ll_dev_inst = (LinearLayout) findViewById(R.id.ll_bt_dev_inst);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            devType = bundle.getString("device");
//        }
//        switch (devType.toLowerCase()) {
//            case "bp":
////                loadData();
//                break;
//        }
//    }
//
//    private void loadData(String desc, ArrayList<String> list) {
//        tv_dev_desc.setText(desc);
//        for (String str : list) {
//            TextView tv_diseaseName = new TextView(this);
//            tv_diseaseName.setText(str);
//            tv_diseaseName.setTextColor(org.ei.telemedicine.Context.getInstance().getColorResource(R.color.light_blue));
//            tv_diseaseName.setTextSize(25);
//            tv_diseaseName.setPadding(10, 10, 10, 10);
//            ll_dev_inst.addView(tv_diseaseName);
//        }
    }

    private void changeVisibility(LinearLayout visibleLayout) {
        ll_bgm.setVisibility(View.GONE);
        ll_bp.setVisibility(View.GONE);
        ll_eet.setVisibility(View.GONE);
        ll_fetal.setVisibility(View.GONE);
        ll_steh.setVisibility(View.GONE);
        visibleLayout.setVisibility(View.VISIBLE);
    }

    private void changeTitle(String title) {
        if (this.getActionBar() != null)
            getActionBar().setTitle(title);
    }
}

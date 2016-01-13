package org.ei.telemedicine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ei.telemedicine.R;
import org.ei.telemedicine.domain.ReportInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by naveen on 1/6/16.
 */
public class ReportsInfoAdapter extends BaseAdapter {
    Context context;
    ArrayList<ReportInfo> reportInfos;

    public ReportsInfoAdapter(Context context, ArrayList<ReportInfo> reportInfos) {
        this.context = context;
        this.reportInfos = reportInfos;
    }

    @Override
    public int getCount() {
        return reportInfos.size();
    }

    @Override
    public ReportInfo getItem(int i) {
        return reportInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder {
        TextView tv_annual_count, tv_month_count, tv_month, tv_total_count, tv_total, tv_percent, tv_service;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.report_info_list_item, null);
        ViewHolder holder = new ViewHolder();

        holder.tv_annual_count = (TextView) view.findViewById(R.id.tv_item_annual_count);
        holder.tv_month_count = (TextView) view.findViewById(R.id.tv_item_month_count);
        holder.tv_month = (TextView) view.findViewById(R.id.tv_item_month);
        holder.tv_total_count = (TextView) view.findViewById(R.id.tv_item_total_count);
        holder.tv_total = (TextView) view.findViewById(R.id.tv_item_total);
        holder.tv_percent = (TextView) view.findViewById(R.id.tv_item_percent);
        holder.tv_service = (TextView) view.findViewById(R.id.tv_item_service_name);

        holder.tv_service.setText(reportInfos.get(i).getIndicator());
        holder.tv_total.setText("Total to " + getMonth().toUpperCase());
        holder.tv_total_count.setText(reportInfos.get(i).getTotalTarget());
        holder.tv_month.setText("in " + getMonth().toUpperCase());
        holder.tv_month_count.setText(reportInfos.get(i).getMonthlySummaries());
        holder.tv_annual_count.setText(reportInfos.get(i).getAnnualTarget());
        String percent = (Integer.parseInt(reportInfos.get(i).getTotalTarget()) / ((Integer.parseInt(reportInfos.get(i).getAnnualTarget()) == 0) ? 1 : Integer.parseInt(reportInfos.get(i).getAnnualTarget()))) * 100 + "%";
        holder.tv_percent.setText(percent);

        return view;
    }

    private String getMonth() {
        DateFormat df = new SimpleDateFormat("MMM");
        return df.format(new Date());
    }
}

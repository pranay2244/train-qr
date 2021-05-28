package com.example.trainqr.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trainqr.R;

public class MyTicketAdapter extends ArrayAdapter<String> {
    Activity activity;
    String[] tName;
    String[] date;
    String[] PNR;
    String[] time;

    public MyTicketAdapter(Activity activity,String[] tName,String[] date,String[] PNR,String[] time) {
        super(activity, R.layout.list_ticket,tName);
        this.activity=activity;
        this.tName=tName;
        this.date=date;
        this.PNR = PNR;
        this.time=time;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = activity.getLayoutInflater();
        View row= li.inflate(R.layout.list_ticket,null,true);
        TextView tTrainName,tDate,tPNR,tTime;
        tTrainName = row.findViewById(R.id.train_name);
        tDate = row.findViewById(R.id.journey_date);
        tPNR = row.findViewById(R.id.PNR);
        tTime = row.findViewById(R.id.journey_time);
        tTrainName.setText(tName[position]);
        if(tName.length==1) {
            tDate.setText(" ");
            tPNR.setText(" ");
            tTime.setText(" ");
        }
        else {
            tDate.setText("Date: " + date[position]);
            tPNR.setText("PNR: " + PNR[position]);
            tTime.setText("Timings: " + time[position]);
        }
//        if(tName[position]==null)
//            row.setVisibility(View.GONE);
        return row;
    }
}

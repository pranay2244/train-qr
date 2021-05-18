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

public class MyAdapter extends ArrayAdapter<String> {
    Activity activity;
    String[] tName;
    int[] price;
    String[] time;
    int[] seats;

    public MyAdapter(Activity activity, String[] tName, int[] price, String[] time, int[] seats){
        super(activity, R.layout.list_train,tName);
        this.activity=activity;
        this.tName=tName;
        this.price=price;
        this.time=time;
        this.seats=seats;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = activity.getLayoutInflater();
        View row= li.inflate(R.layout.list_train,null,true);
        TextView title,berth,timings,cost;
        title = row.findViewById(R.id.trainName);
        berth=row.findViewById(R.id.seats);
        timings=row.findViewById(R.id.time);
        cost=row.findViewById(R.id.price);

        title.setText(tName[position]);
        berth.setText("Seats: "+ seats[position]);
        timings.setText("Time: "+time[position]);
        cost.setText("Price: "+price[position]+"/-");
        return row;
    }
}

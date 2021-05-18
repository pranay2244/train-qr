package com.example.trainqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trainqr.adapter.MyAdapter;
import com.example.trainqr.models.TrainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class TrainListActivity extends AppCompatActivity {

    ListView listView;
    String[] tName = {};
    int[] price = {};
    String[] time = {};
    int[] seats = {};
    ArrayList<String> alTrainId;
    ArrayList<Long> alTrainSeat;
    ArrayList<TrainModel> alModel;
    String from, to, date;
    DatabaseReference databaseReference;
    HashMap<String, LinkedTreeMap> hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        to = intent.getStringExtra("to");
        date = intent.getStringExtra("date");
        listView = findViewById(R.id.list);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        alTrainId = new ArrayList<>();
        alTrainSeat = new ArrayList<>();
        alModel = new ArrayList<>();
        findTrainId();

        Context context = this;
        InputStream is = context.getResources().openRawResource(R.raw.db);
        String s = new Scanner(is).useDelimiter("\\A").next();
        hm = new Gson().fromJson(s,HashMap.class);
        MyAdapter adapter = new MyAdapter(this, tName, price, time, seats);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(TrainListActivity.this, alModel.get(i).getTrain_name(),Toast.LENGTH_SHORT).show());
    }

    public void findTrainId() {
        databaseReference.child("days").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Long seatCnt = (Long) dataSnapshot.getValue();
                    if (seatCnt != 0) {
                        alTrainId.add(dataSnapshot.getKey());
                        alTrainSeat.add(seatCnt);
                    }
                }
                findTrainData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void findTrainData(){
        for(String s:alTrainId){
            LinkedTreeMap<String,Object> tm = hm.get(s);
            List<String> path = (List<String>) tm.get("path");
            if(path.contains(from)&&path.contains(to))
            {
                TrainModel model = new TrainModel();
                model.setTrain_id(s);
                model.setTo(to);
                model.setFrom(from);
                model.setTimings((String) tm.get("timings"));
                model.setTrain_name((String) tm.get("train_name"));
                model.setFare((List<String>) tm.get("Fare"));
                model.setPath(path);
                model.setTime((List<String>) tm.get("path"));
                alModel.add(model);
            }
        }
        setData();
    }

    private void setData() {
        int i=0;
        int size = alModel.size();
        if(size==0)
        {
            Toast.makeText(this, "No trains available", Toast.LENGTH_SHORT).show();
            finish();
        }
        String[] tNameList = new String[size], timeList = new String[size];
        int[] priceList = new int[size], seatsList = new int[size];
        for(TrainModel tm: alModel){
            tNameList[i] = tm.getTrain_name();
            timeList[i] = tm.getTimings();
            int c = Integer.parseInt(tm.getFare().get(0));
            priceList[i] = c;
            seatsList[i] = Integer.parseInt(String.valueOf(alTrainSeat.get(i)));
            i++;
        }
        MyAdapter adapter = new MyAdapter(this, tNameList, priceList, timeList, seatsList);
        listView.setAdapter(adapter);
    }
}
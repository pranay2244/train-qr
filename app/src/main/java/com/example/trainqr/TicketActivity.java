package com.example.trainqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trainqr.adapter.MyTicketAdapter;
import com.example.trainqr.models.TicketModel;
import com.example.trainqr.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class TicketActivity extends AppCompatActivity {
    String[] tName = {"Loading Please Wait"};
    String[] date = {" "};
    String[] PNR = {" "};
    String[] time = {" "};
    ListView lv;
    DatabaseReference db;
    FirebaseAuth auth;
    UserModel user;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        lv = findViewById(R.id.ticket_list);
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        MyTicketAdapter ticketAdapter = new MyTicketAdapter(this, tName, date, PNR, time);
        lv.setAdapter(ticketAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
            intent.putExtra("pnr", PNR[position]);
            startActivity(intent);
        });
        getData();
    }



    private void getTicketData() {
        List<String> current = user.getCurrent();
        if(current!=null)
        {
            int size = current.size();
            tName = new String[size];
            date = new String[size];
            PNR = new String[size];
            time = new String[size];
            for(String s:current){
                db.child("tickets").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TicketModel model = snapshot.getValue(TicketModel.class);
                        tName[i] = model.getTrain_name();
                        date[i] = model.getDate();
                        PNR[i] = s;
                        time[i] = model.getTimings();
                        i++;
                        MyTicketAdapter ticketAdapter = new MyTicketAdapter(TicketActivity.this,tName,date,PNR,time);
                        lv.setAdapter(ticketAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        else{
            String[] temp = {"No bookings done!"};
            MyTicketAdapter ticketAdapter = new MyTicketAdapter(TicketActivity.this,temp,date,PNR,time);
            lv.setAdapter(ticketAdapter);
        }
    }

    private void getData() {
        db.child("users").child(auth.getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(UserModel.class);
                if(user==null){
                    Intent intent = new Intent(TicketActivity.this,ProfileRegistrationActivity.class);
                    Toast.makeText(TicketActivity.this, "Complete Profile Registration ", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                getTicketData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
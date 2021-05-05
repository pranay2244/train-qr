package com.example.trainqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.trainqr.models.TicketModel;
import com.example.trainqr.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final UserModel[] user = new UserModel[1];
        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseReference.child("users").child(auth.getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user[0] = snapshot.getValue(UserModel.class);
                bookTicket(databaseReference,user[0],auth);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void bookTicket(DatabaseReference databaseReference, UserModel user, FirebaseAuth auth) {
        TicketModel ticketModel = new TicketModel();
        ticketModel.setDate("05 06 2021");
        ticketModel.setPassengers(Arrays.asList("Pranay", "Kumar"));
        ticketModel.setSeats(Arrays.asList("1", "2"));
        ticketModel.setPrice("400");
        ticketModel.setTimings("11:00 - 17:00");
        ticketModel.setTrain_name("Pk Express");
        ticketModel.setTrain_no("11032");
        DatabaseReference childRef = databaseReference.child("tickets").push();
        String key = childRef.getKey();
        childRef.setValue(ticketModel);
        List<String> al = user.getCurrent();
        if(al==null)
            al = new ArrayList<>();
        al.add(key);
        user.setCurrent(al);
        databaseReference.child("users").child(auth.getCurrentUser().getPhoneNumber()).setValue(user);
        Toast.makeText(this, "Ticket booked successfully", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "PNR: "+key , Toast.LENGTH_SHORT).show();
    }
}
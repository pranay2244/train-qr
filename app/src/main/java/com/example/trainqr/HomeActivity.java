package com.example.trainqr;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trainqr.models.TrainModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    ArrayList<TrainModel> al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
        Button signOut = findViewById(R.id.signOut);
        TextView numberText = findViewById(R.id.numberText);
        String s = "Welcome "+firebaseAuth.getCurrentUser().getPhoneNumber();
        numberText.setText(s);
        signOut.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        Button register = findViewById(R.id.register);
        register.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ProfileRegistrationActivity.class);
            startActivity(intent);
        });
        al = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("train").child("Friday");
        //getData(databaseReference);
        findViewById(R.id.book).setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, TrainActivity.class));
        });
    }

    private void getData(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot dataSnapshot:snapshot.getChildren())
                 {
                     String key = dataSnapshot.getKey();
                     try {
                         TrainModel model = dataSnapshot.getValue(TrainModel.class);
                         model.setTrain_id(key);
                         al.add(model);
                     }
                     catch(Exception e) {
                         Toast.makeText(HomeActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                     }
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
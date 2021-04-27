package com.example.trainqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

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
    }
}
package com.example.trainqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MobileActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        phoneNumber = findViewById(R.id.phoneNumber);
        login = findViewById(R.id.login);
        login.setEnabled(false);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==10)
                    login.setEnabled(true);
                if(charSequence.length()!=10)
                    login.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        login.setOnClickListener(view -> {
            String phone = phoneNumber.getText().toString().trim();
            Intent intent = new Intent(MobileActivity.this,VerificationActivity.class);
            intent.putExtra("phone","+91"+phone);
            startActivity(intent);
        });
    }
}
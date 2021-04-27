package com.example.trainqr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String verificationId;
    private EditText OTP;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificattion);
        OTP = findViewById(R.id.otp);
        login = findViewById(R.id.login);
        TextView numberText = findViewById(R.id.numberText);
        String phone = getIntent().getStringExtra("phone");
        String s = "Entered Phone number is: "+ phone.substring(3);
        numberText.setText(s);

        getCode();
        firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(VerificationActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnSuccessListener(authResult -> {
                            Intent intent = new Intent(VerificationActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }).addOnFailureListener(e -> Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerificationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void getCode() {
        login.setOnClickListener(view -> {
            String code = OTP.getText().toString().trim();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
            firebaseAuth.signInWithCredential(credential).addOnSuccessListener(authResult -> {
                Intent intent = new Intent(VerificationActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }).addOnFailureListener(e -> Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
        });
    }
}
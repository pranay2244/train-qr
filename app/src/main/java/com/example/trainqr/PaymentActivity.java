package com.example.trainqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        String s = "Amount to be Paid: "+intent.getIntExtra("price",0)+"/-";
        String pnr = intent.getStringExtra("pnr");
        TextView tv = findViewById(R.id.amount);
        tv.setText(s);
        findViewById(R.id.pay_button).setOnClickListener(view ->{
            Toast.makeText(this, "Ticket successfully booked with the PNR number\n"+pnr, Toast.LENGTH_SHORT).show();
            Intent move = new Intent(this,HomeActivity.class);
            move.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(move);
        });
    }
}
package com.example.trainqr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ImageView qr = findViewById(R.id.qr);
        try{
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            String s = "-Ma4548ORnqzrhwRDKh3";
            Bitmap bitmap = barcodeEncoder.encodeBitmap(s, BarcodeFormat.QR_CODE,400,400);
            qr.setImageBitmap(bitmap);
        }
        catch(Exception ignored){}
    }
}
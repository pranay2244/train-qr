package com.example.trainqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainqr.models.TicketModel;
import com.example.trainqr.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class DetailsActivity extends AppCompatActivity {
    CardView[] card;
    int i = 0;
    String trainName, trainNo, seats, timings, price, date;
    TextView textView;
    Integer[] editNameId = {R.id.name0,R.id.name1,R.id.name2,R.id.name3,R.id.name4,R.id.name5};
    Integer[] editAgeId = {R.id.age0,R.id.age1,R.id.age2,R.id.age3,R.id.age4,R.id.age5};
    Integer[] genderId = {R.id.gender0,R.id.gender1,R.id.gender2,R.id.gender3,R.id.gender4,R.id.gender5};
    String[] name, age, gender;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //Getting data from previous intents
        trainName = getIntent().getStringExtra("name");
        trainNo = getIntent().getStringExtra("number");
        seats = getIntent().getStringExtra("seats");
        timings = getIntent().getStringExtra("timings");
        price = getIntent().getStringExtra("price");
        date = getIntent().getStringExtra("date");

        TextView tv = findViewById(R.id.trainName);
        tv.setText(trainName);
        card = new CardView[6];
        card[0] = findViewById(R.id.card0);
        card[1] = findViewById(R.id.card1);
        card[2] = findViewById(R.id.card2);
        card[3] = findViewById(R.id.card3);
        card[4] = findViewById(R.id.card4);
        card[5] = findViewById(R.id.card5);
        textView = findViewById(R.id.pass_no);
        Button addPass = findViewById(R.id.add);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        addPass.setOnClickListener(view -> {
            i++;
            if(i<6){
                card[i].setVisibility(View.VISIBLE);
                int temp = i+1;
                String s = String.format(Locale.UK,"Passenger Count: %d",temp);
                textView.setText(s);
            }
            else {
                Toast.makeText(this, "Only up to 6 passengers", Toast.LENGTH_SHORT).show();
                i--;
            }
        });
        Button confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(view -> {
            confirm.setEnabled(false);
            getData();
        });
    }

    private void getData() {
        name = new String[i+1];
        age = new String[i+1];
        gender = new String[i+1];
        for(int a=0;a<=i;a++){
            EditText editName = findViewById(editNameId[a]);
            EditText editAge = findViewById(editAgeId[a]);
            RadioGroup radioGen = findViewById(genderId[a]);
            name[a] = editName.getText().toString().trim();
            age[a] = editAge.getText().toString().trim();
            int id = radioGen.getCheckedRadioButtonId();
            RadioButton rb = findViewById(id);
            gender[a] = rb.getText().toString();
        }
        getUserData();
    }

    private void getUserData() {
        final UserModel[] user = new UserModel[1];
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user[0] = snapshot.getValue(UserModel.class);
                if(user[0]==null)
                {
                    Intent intent = new Intent(DetailsActivity.this,ProfileRegistrationActivity.class);
                    startActivity(intent);
                }
                bookTicket(user[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void bookTicket(UserModel userModel) {
        TicketModel ticketModel = new TicketModel();
        ticketModel.setTrain_no(trainNo);
        ticketModel.setTrain_name(trainName);
        ticketModel.setTimings(timings);
        int totalPrice = Integer.parseInt(price)*(i+1);
        ticketModel.setPrice(String.valueOf(totalPrice));
        int intSeats = Integer.parseInt(seats);
        List<String> seatStr = new ArrayList<>(i+1);
        for(int a=0;a<=i;a++)
            seatStr.add(String.valueOf(intSeats-a));
        intSeats = intSeats - i - 1;
        ticketModel.setSeats(seatStr);
        ticketModel.setPassengers(Arrays.asList(name));
        ticketModel.setDate(date);
        ticketModel.setAge(Arrays.asList(age));
        ticketModel.setGender(Arrays.asList(gender));
        DatabaseReference childRef = databaseReference.child("tickets").push();
        String key = childRef.getKey();
        childRef.setValue(ticketModel);
        List<String> al = userModel.getCurrent();
        if(al==null)
            al = new ArrayList<>();
        al.add(key);
        al = al.stream().filter(Objects::nonNull).collect(Collectors.toList());
        userModel.setCurrent(al);
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getPhoneNumber()).setValue(userModel)
                .addOnFailureListener(e -> findViewById(R.id.confirm).setEnabled(true));
        updateSeats(intSeats,key,totalPrice);
    }

    private void updateSeats(int seats,String key,int totalPrice) {
        databaseReference.child("days").child(date).child(trainNo).setValue(seats);
        Intent intent = new Intent(DetailsActivity.this,PaymentActivity.class);
        intent.putExtra("pnr",key);
        intent.putExtra("price",totalPrice);
        startActivity(intent);
    }
}
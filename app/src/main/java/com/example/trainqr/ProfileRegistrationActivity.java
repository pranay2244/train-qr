package com.example.trainqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.trainqr.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileRegistrationActivity extends AppCompatActivity {

    EditText name, email, age, address;
    Button update;
    RadioGroup gender;
    FirebaseAuth firebaseAuth;
    UserModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_registration);
        findViewById(R.id.rel).setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        findViews();
    }

    private void findUser() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getPhoneNumber());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user =  snapshot.getValue(UserModel.class);
                checkUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser() {
        if(user==null){
            user = new UserModel();
            findViewById(R.id.rel).setVisibility(View.VISIBLE);
            findViewById(R.id.rel2).setVisibility(View.GONE);
            setButtons();
        }
        else{
            userExist();
        }
    }

    private void setButtons() {
        update.setOnClickListener(view -> {
            int id = gender.getCheckedRadioButtonId();
            RadioButton genButton = findViewById(id);
            if(id != -1)
                register(genButton.getText().toString());
            else
                Toast.makeText(this, "Select a gender", Toast.LENGTH_SHORT).show();
        });
    }

    private void userExist() {
        findViewById(R.id.rel).setVisibility(View.VISIBLE);
        findViewById(R.id.rel2).setVisibility(View.GONE);
        name.setText(user.getName());
        email.setText(user.getEmail());
        String ageStr = String.format("%s",user.getAge());
        age.setText(ageStr);
        address.setText(user.getAddress());
        RadioButton temp;
        if(user.getGender().equals("Male"))
            temp = findViewById(R.id.male);
        else if(user.getGender().equals("Female"))
            temp = findViewById(R.id.female);
        else
            temp = findViewById(R.id.other);
        temp.toggle();
        update.setText("Update");
        setButtons();
    }

    private void findViews() {
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
         gender = findViewById(R.id.gender);
         update = findViewById(R.id.update);
        findUser();
    }

    public void register(String gender) {
        String nameStr, emailStr, addressStr;
        int ageInt = 0;
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        addressStr = address.getText().toString().trim();
        try {
            ageInt = Integer.parseInt(age.getText().toString().trim());
        }
        catch(Exception e){
            Toast.makeText(this, "Enter an age",Toast.LENGTH_SHORT).show();
        }
        if(!falseVal(nameStr,emailStr,addressStr, ageInt, gender))
            return;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getPhoneNumber());

        user.setName(nameStr);
        user.setEmail(emailStr);
        user.setAge(ageInt);
        user.setAddress(addressStr);
        user.setGender(gender);
        database.setValue(user).addOnSuccessListener(aVoid -> {
            Toast.makeText(ProfileRegistrationActivity.this, "Done Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileRegistrationActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }).addOnFailureListener(e -> Toast.makeText(ProfileRegistrationActivity.this, "Please try again later", Toast.LENGTH_SHORT).show());
    }

    private boolean falseVal(String nameStr, String emailStr, String addressStr, int ageInt, String gender) {
        if( nameStr.equals("")) {
            Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(emailStr.equals(""))
        {
            Toast.makeText(this, "Enter an email",Toast.LENGTH_SHORT).show();
            return false;
        }
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailStr);
        if(!matcher.matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(addressStr.equals(""))
        {
            Toast.makeText(this, "Enter an address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ageInt > 100 || ageInt < 0)
        {
            Toast.makeText(this, "Enter a valid age",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(gender.equals(""))
        {
            Toast.makeText(this, "Select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
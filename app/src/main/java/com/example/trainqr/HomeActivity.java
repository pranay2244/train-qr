package com.example.trainqr;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.trainqr.models.TrainModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<TrainModel> al;
    FirebaseAuth firebaseAuth;
    Button search;
    TextInputEditText from, to , selectDate;
    int day, month, year;
    long milli;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hooks
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        search = findViewById(R.id.button);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        selectDate = findViewById(R.id.date);
        toolbar.setTitle("menu");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth =  FirebaseAuth.getInstance();
        selectDate.setOnClickListener(view -> initDate());
        search.setOnClickListener(view -> searchTrain());

//        al = new ArrayList<>();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("train").child("Friday");
//        getData(databaseReference);
//        findViewById(R.id.book).setOnClickListener(view -> {
//            startActivity(new Intent(HomeActivity.this, TrainActivity.class));
//        });
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        milli = calendar.getTimeInMillis();
        setDate();
    }
    @SuppressWarnings("deprecation")
    public void setDate(){
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==999){
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);
            dialog.getDatePicker().setMinDate(milli);
            long num = 2592000000L;
            dialog.getDatePicker().setMaxDate(milli+num);
            return dialog;
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = (datePicker, i, i1, i2) -> {
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        showDate(date, month, year);
    };
    private void showDate(int dat, int month, int year) {
        month++;
        String stringE;
        if(month<10)
            stringE = dat + " " + "0" + month + " " +year;
        else
            stringE = dat + " " + month + " " +year;
        selectDate.setText(stringE);
        date = stringE;
    }
    private void searchTrain() {
        String fromString = from.getText().toString().trim();
        String toString = to.getText().toString().trim();
        Intent intent = new Intent(HomeActivity.this, TrainListActivity.class);
        intent.putExtra("from",fromString);
        intent.putExtra("to",toString);
        intent.putExtra("date",date);
        startActivity(intent);
    }

//    private void getData(DatabaseReference databaseReference) {
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                 for(DataSnapshot dataSnapshot:snapshot.getChildren())
//                 {
//                     String key = dataSnapshot.getKey();
//                     try {
//                         TrainModel model = dataSnapshot.getValue(TrainModel.class);
//                         model.setTrain_id(key);
//                         al.add(model);
//                     }
//                     catch(Exception e) {
//                         Toast.makeText(HomeActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
//                     }
//                 }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        findViewById(R.id.select_train).setOnClickListener(view -> {
//            startActivity(new Intent(HomeActivity.this, SelectActivity.class));
//        });
//    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileRegistrationActivity.class);
            startActivity(intent);
        }
        if(id == R.id.nav_logout){
            firebaseAuth.signOut();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return false;
    }
}
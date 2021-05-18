package com.example.trainqr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class SelectActivity extends AppCompatActivity {

    private EditText start,end,date;
    private Button show;
    private TextView showText;
    private long milli;
    int year, month, day;
    String[] days = new String[]{"Sunday","Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        findViews();
    }
    public void findViews(){
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        date = findViewById(R.id.date);
        date.setOnClickListener(view -> {
            initDate();
        });
        show = findViewById(R.id.show);
        showText = findViewById(R.id.showText);
        show.setOnClickListener(view -> {
            getData();
        });
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
        Calendar temp = Calendar.getInstance();
        temp.set(year, month,dat);
        int i = temp.get(Calendar.DAY_OF_WEEK);
        String s = days[i - 1];
        String stringE = dat+" - "+(month+1)+" - "+year;
       date.setText(stringE);
    }

    public void getData(){

    }
}
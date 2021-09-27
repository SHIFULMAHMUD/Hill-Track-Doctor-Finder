package com.android.hilltrackdoctorfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allyants.notifyme.NotifyMe;
import com.android.hilltrackdoctorfinder.R;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class ReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    TextInputEditText etTitle,etContent;
    TextView title;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        title.setText(R.string.set_reminder);
        CardView btnNotify = findViewById(R.id.cardViewAdd);
        CardView btnCancel = findViewById(R.id.cardViewCancel);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dpd = DatePickerDialog.newInstance(
                ReminderActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd = TimePickerDialog.newInstance(
                ReminderActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyMe.cancel(getApplicationContext(),"test");
                Toasty.success(ReminderActivity.this,"Reminder Deleted",Toasty.LENGTH_SHORT).show();
            }
        });
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        intent.putExtra("test","I am a String");
        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title(etTitle.getText().toString())
                .content(etContent.getText().toString())
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(now)
                .addAction(intent,"Snooze",false)
                .key("test")
                .addAction(new Intent(),"Dismiss",true,false)
                .addAction(intent,"Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2")
                .build();

        Toasty.success(ReminderActivity.this,"Reminder Set Successfully",Toasty.LENGTH_SHORT).show();
    }

}

package com.codetroopers.makemytrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventActivity extends NavigationActivity {
    private EditText txtName,txtSource,txtDestination,txtDescription,startDate,endDate,txtAttendees;
    private CheckBox tglHotels;
    private boolean edit=false;
    private Events mEvent;
    private Calendar myCalendar1 = Calendar.getInstance();
    private Calendar myCalendar2 = Calendar.getInstance();
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_event, frameLayout);
        getSupportActionBar().setTitle("Add Event");
        Intent i=getIntent();
        edit=i.getBooleanExtra("Edit",false);
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference("Events");
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtName=(EditText)findViewById(R.id.txtEventName);
        txtSource=(EditText)findViewById(R.id.txtSource);
        txtDestination=(EditText)findViewById(R.id.txtDestination);
        txtDescription=(EditText)findViewById(R.id.txtDescription);
        startDate=(EditText) findViewById(R.id.startDate);
        endDate=(EditText) findViewById(R.id.endDate);
        txtAttendees=(EditText) findViewById(R.id.txtAttendees);
        tglHotels=(CheckBox)findViewById(R.id.tglHotels);
        Button btnSubmit=(Button)findViewById(R.id.btnSubmit);
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }
        };
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit) {
                    mEvent = new Events();
                    mEvent.Id= Calendar.getInstance().getTimeInMillis()+"";
                    mEvent.EventName = txtName.getText().toString();
                    mEvent.Description = txtDescription.getText().toString();
                    mEvent.Source = txtSource.getText().toString();
                    mEvent.Destination = txtDestination.getText().toString();
                    mEvent.Attendees=User.acct.getDisplayName();
                    mEvent.StartDate=myCalendar1.getTime().getTime();
                    mEvent.EndDate=myCalendar2.getTime().getTime();
                    mEvent.HotelsBooked = tglHotels.isChecked();
                    mDatabase.child(mEvent.Id).setValue(mEvent);
                    mDatabase.push();
                    onBackPressed();
                }
                else{
                    mEvent.Attendees+=";"+User.acct.getDisplayName();
                    //String key = mDatabase.child("Events").push().getKey();
                    Map<String, Object> postValues = mEvent.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(mEvent.Id, postValues);
                    mDatabase.updateChildren(childUpdates);
                    //mDatabase.child(mEvent.Id).removeValue();
                    onBackPressed();
                }
            }
        });
        if(!edit)
        {
            mEvent=EventListActivity.events.get(i.getIntExtra("Events",0));
            txtName.setKeyListener(null);
            txtDescription.setKeyListener(null);
            txtDestination.setKeyListener(null);
            txtSource.setKeyListener(null);
            startDate.setKeyListener(null);
            endDate.setKeyListener(null);
            txtAttendees.setKeyListener(null);
            txtName.setFocusable(false);
            txtDescription.setFocusable(false);
            txtDestination.setFocusable(false);
            txtSource.setFocusable(false);
            startDate.setFocusable(false);
            endDate.setFocusable(false);
            txtName.setText(mEvent.EventName);
            txtDescription.setText(mEvent.Description);
            txtSource.setText(mEvent.Source);
            txtDestination.setText(mEvent.Destination);
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(mEvent.StartDate);
            startDate.setText(cal.getTime().toString());
            cal.setTimeInMillis(mEvent.EndDate);
            endDate.setText(cal.getTime().toString());
            tglHotels.setChecked(mEvent.HotelsBooked);
            btnSubmit.setText("Join");
            txtAttendees.setText(mEvent.Attendees);
            tglHotels.setClickable(false);
            //tglHotels.setEnabled(false);
            if(mEvent.Attendees.contains(User.acct.getDisplayName()))
            {
                btnSubmit.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            txtAttendees.setKeyListener(null);
            txtAttendees.setText(User.acct.getDisplayName());
            startDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                        new DatePickerDialog(AddEventActivity.this, date1, myCalendar1
                            .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                            myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    }
                    return false;
                }
            });
            endDate.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                        new DatePickerDialog(AddEventActivity.this, date2, myCalendar2
                                .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                                myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
                        return true;
                    }
                    return false;
                }
            });
        }
    }
    private void updateLabel1() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate.setText(sdf.format(myCalendar1.getTime()));
    }
    private void updateLabel2() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        endDate.setText(sdf.format(myCalendar2.getTime()));
    }
}

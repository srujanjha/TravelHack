package com.codetroopers.makemytrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, frameLayout);
        getSupportActionBar().setTitle("Profile");
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        int points=SP.getInt("MMTPoints",0);
        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences SP1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor1 = SP1.edit();
                editor1.putInt("MMTPoints", 0);
                editor1.putInt("MMTCurr",0);
                editor1.apply();
                startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                finish();
            }
        });
        TextView txtPoints=(TextView)findViewById(R.id.txtPoints);
        txtPoints.setText("Total Points Earned: "+points);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,StoriesActivity.class));finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(User.acct==null)return;
        ImageView imageView=(ImageView)findViewById(R.id.imgUser);
        Picasso.with(this)
                .load(User.acct.getPhotoUrl())
                .into(imageView);
        TextView txtName=(TextView)findViewById(R.id.txtUserName);
        TextView txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtName.setText(User.acct.getDisplayName());
        txtEmail.setText(User.acct.getEmail());

    }
}

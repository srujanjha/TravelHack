package com.codetroopers.makemytrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.codetroopers.makemytrip.User.acct;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    public static FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        if(acct==null)return;
        ImageView imageView=(ImageView)hView.findViewById(R.id.imgUser);
        Picasso.with(this)
                .load(acct.getPhotoUrl())
                .into(imageView);
        //imageView.setImageURI(SplashScreen.acct.getPhotoUrl());
        TextView txtName=(TextView)hView.findViewById(R.id.txtUserName);
        TextView txtEmail=(TextView)hView.findViewById(R.id.txtEmail);
        txtName.setText(acct.getDisplayName());
        txtEmail.setText(acct.getEmail());
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateProfile();
            }
        });
    }
    private void navigateProfile()
    {
        startActivity(new Intent(NavigationActivity.this,ProfileActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(NavigationActivity.this,ProfileActivity.class));
            finish();
        } else if (id == R.id.nav_stories) {
            startActivity(new Intent(NavigationActivity.this,StoriesActivity.class));
            finish();
        } else if (id == R.id.nav_chatbot) {
            startActivity(new Intent(NavigationActivity.this,MainActivity.class));
            finish();
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(NavigationActivity.this,EventListActivity.class));
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_logout) {
            SplashScreen.signIn=false;
            startActivity(new Intent(NavigationActivity.this, SplashScreen.class));
            //SplashScreen.signOut();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

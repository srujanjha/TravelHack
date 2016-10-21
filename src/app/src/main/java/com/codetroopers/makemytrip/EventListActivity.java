package com.codetroopers.makemytrip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListActivity extends NavigationActivity {
    private RecyclerView mRecyclerView;
    private EventAdapter adapter;
    private ProgressBar prgBar;
    public static ArrayList<Events>events=new ArrayList<>();
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_event_list, frameLayout);
        getSupportActionBar().setTitle("MakeMyTrip Events");

        //setContentView(R.layout.activity_event_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference("Events");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    events.clear();
                    for(DataSnapshot d:dataSnapshot.getChildren())
                    {
                        HashMap s= (HashMap) d.getValue(true);
                        Events mEvent=new Events();
                        mEvent.Id=d.getKey();
                        mEvent.Attendees=s.get("Attendees").toString();
                        mEvent.EventName=s.get("EventName").toString();
                        mEvent.Description=s.get("Description").toString();
                        mEvent.Destination=s.get("Destination").toString();
                        mEvent.HotelsBooked=Boolean.parseBoolean(s.get("HotelsBooked").toString());
                        mEvent.Source=s.get("Source").toString();
                        mEvent.StartDate= Long.parseLong(s.get("StartDate").toString());
                        mEvent.EndDate= Long.parseLong(s.get("EndDate").toString());
                        events.add(mEvent);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new EventAdapter(events,EventListActivity.this);
                            mRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    prgBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    prgBar.setVisibility(View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventListActivity.this,AddEventActivity.class);
                i.putExtra("Edit",true);
                startActivity(i);
            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.rvEvents);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        prgBar=(ProgressBar)findViewById(R.id.prgBar);
        prgBar.setVisibility(View.VISIBLE);
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }
}

package com.codetroopers.makemytrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class StoriesActivity extends NavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //NavigationActivity.mActive = R.id.navigation_item_2;
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_stories, frameLayout);
        getSupportActionBar().setTitle("Stories");
        CardView cvHyderabad=(CardView)findViewById(R.id.cvHyderabad);
        cvHyderabad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoriesActivity.this,MapsActivity.class));
            }
        });
    }
}

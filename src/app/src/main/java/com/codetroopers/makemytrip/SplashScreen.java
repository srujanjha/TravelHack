package com.codetroopers.makemytrip;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.funtory.slideshowimageview.SlideshowImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import mbanje.kurt.fabbutton.FabButton;

import static com.codetroopers.makemytrip.User.acct;

public class SplashScreen extends AppCompatActivity implements com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {
    private SlideshowImageView bgImage;
    int resD[]={R.drawable.hyderabad,R.drawable.banaglore,R.drawable.delhi,R.drawable.mumbai};
    private static GoogleApiClient mGoogleApiClient;
    private FabButton fab;
    private final int RC_SIGN_IN=124;
    private boolean mCompleted=false;
    public static boolean signIn=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        fab=(FabButton)findViewById(R.id.determinate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCompleted) {
                    startActivity(new Intent(SplashScreen.this, StoriesActivity.class));
                    finish();
                }else signIn();
            }
        });
        bgImage=(SlideshowImageView)findViewById(R.id.bgImage);
        bgImage.setImages(resD);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signIn();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    public static void signOut() {
        mGoogleApiClient.connect();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            fab.setProgress(1.0f);
            mCompleted=true;
            if(!signIn){signOut();signIn=true;signIn();}
        } else {
            Snackbar.make(bgImage, result.getStatus().getStatusMessage(), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            signIn();
                        }
                    });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(bgImage, connectionResult.getErrorMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(View v) {
                        signIn();
                    }
                });
    }

}

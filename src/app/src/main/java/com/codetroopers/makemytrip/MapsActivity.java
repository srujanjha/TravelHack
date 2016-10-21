package com.codetroopers.makemytrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.drakeet.materialdialog.MaterialDialog;

public class MapsActivity extends NavigationActivity implements
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,GoogleMap.OnMyLocationChangeListener,
        OnMapReadyCallback {
    private final String TAG="MAP";
    private GoogleMap mMap;
    private MaterialDialog mMaterialDialog;
    private final LatLng mLatLng[] = new LatLng[]{new LatLng(17.3615635,78.4724759),
            new LatLng(17.3713733, 78.4782306),
            new LatLng(17.4062367, 78.4668714),
            new LatLng(17.425864,78.4836303),
            new LatLng(17.4241053,78.4657618),
            new LatLng(17.3510843,78.4467119),
            new LatLng(17.3761529,78.3759124),
            new LatLng(17.383309,78.3988641),

            new LatLng(17.4048268,78.4567505),
            new LatLng(17.4142983,78.4808241)};
    private ArrayList<Marker> mPlaces = new ArrayList<Marker>();
    private final String mPlaceName[] = new String[]{"Charminar", "Salar Jung Museum", "Birla Mandir","Hussainsagar Lake","Chow Mohalla Palace","Nehru Zoological Park","Taramati Baradari","Golconda Fort",
    "Paradise Biryani","Snow World"};
    private final String mDescription[] = new String[]{"Iconic 16th-century mosque with 4 grand arches, 48m tall minarets and views over the Laad Bazaar.The Charminar is as much the signature of Hyderabad as the Taj Mahal is of Agra or the Eiffel Tower is of Paris. Mohammed Quli Qutb Shah, the founder of Hyderabad, built Charminar in 1591 at the centre of the original city layout. It is said to be built as a charm to ward off a deadly epidemic raging at that time. Four graceful minarets soar to a height of 48.7 m above the ground. Charminar has 45 prayer spaces and a mosque in it. Visitors can view the architectural splendour inside the Charminar. The monument is illuminated in the evenings and a pedestrianisation project around the monument is under implementation.",
            "Former art collection of the Salar Jung family from around the world, now a museum.",
            "Imposing, hilltop Hindu temple built of white marble, with towers, shrines and interior carvings.",
            "Excavated in 1562 A.D. by Hussain Shah Wali during the time of Ibrahim Quli Qutb Shah, the lake has a promenade that is a busy thoroughfare today. Boating and water sports are a regular feature in the Hussainsagar. One of the World’s tallest monolithic statues of the Buddha stands on the ‘Rock of Gibraltar’, in the middle of the lake. Added to all these, AP Tourism has additional boating facilities like speed boats, motor boats, 48 seater launch etc. Starlit dinner on-board and private parties also can be arranged on the Launch.Surroundings of Hussainsagar Lake provide marvellous entertainment options like NTR Gardens, Necklace Road, Tank Bund, Prasads Multiplex, Lumbini Park, Sanjeevaiah Park etc.",
            "Built in several phases by the Nizams between 1857-1869, this is now one of the heritage buildings. The complex comprises four palaces in Moghal and European styles, of which the main palace is double storeyed with the others being single-storeyed blocks.",
            "Nehru Zoological Park is a zoo located near Mir Alam Tank in Hyderabad, Telangana, India. It is one of the most visited destinations in Hyderabad. Zoo hours vary by season, and the zoo is closed on Mondays.",
            "Open pavilion built by a Qutb Shahi Sultan of Golconda & known for its acoustics.",
            "Capital of a kingdom that flourished from the 14th to 16th century, with 87 semi-circular bastions",

    "Paradise Food Court is a hotel located on MG Road, Secunderabad, in the Indian state of Telangana. It is reported to be popular for its Hyderabadi biryani and other Hyderabadi dishes.",
    "Snow World is an amusement park located in Hyderabad, Telangana, India within an area of about 2 acres. Located beside Indira Park and along the Hussain Sagar lake, the park was inaugurated on 28 January 2004."};
    private boolean mTask[]={true,true,true,true,true,true,true,true,false,false};
    private boolean mVisited[]=new boolean[mLatLng.length];
    private boolean mCompleted[]=new boolean[mLatLng.length];
    private int curr=0;
    private Snackbar snackbar;
    private TextView textViewTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_maps, frameLayout);
        //setContentView(R.layout.activity_maps);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        curr=SP.getInt("MMTCurr",0);
        for(int i=0;i<curr;i++)mCompleted[i]=true;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        snackbar = Snackbar.make(frameLayout, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.layout_snackbar, null);
        textViewTop = (TextView) snackView.findViewById(R.id.snckTxt);
        textViewTop.setTextColor(Color.WHITE);
        layout.addView(snackView, 0);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);;
        UiSettings set=mMap.getUiSettings();
        set.setAllGesturesEnabled(true);
        requestPermission();
        IconGenerator iconFactory = new IconGenerator(this);
        for (int i = 0; i < mLatLng.length; i++) {
            if (mCompleted[i]) iconFactory.setStyle(IconGenerator.STYLE_GREEN);
            else if(mTask[i])iconFactory.setStyle(IconGenerator.STYLE_RED);
            else iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(mLatLng[i])
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon((i+1)+"")))
                    .title((i + 1) + ":" + mPlaceName[i])
                    .snippet(mDescription[i]));
            mPlaces.add(m);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng[0], 17.0f));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMyLocationChangeListener(this);
    }
    /**
     * Called when the user clicks a marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        /*int i = Integer.parseInt(marker.getTitle().split(":")[0]) - 1;
        mMaterialDialog = new MaterialDialog(this)
                .setTitle(mPlaceName[i])
                .setMessage(mDescription[i])
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();*/
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        if(marker.getTitle().equals("Current Position"))return;
        if(mLastLocation!=null) {
            Location locationA = new Location("point A");
            locationA.setLatitude(mLastLocation.getLatitude());
            locationA.setLongitude(mLastLocation.getLongitude());
            Location locationB = new Location("point B");
            locationB.setLatitude(marker.getPosition().latitude);
            locationB.setLongitude(marker.getPosition().longitude);
            double d = locationA.distanceTo(locationB);
            if (d < 500.0f) {
                int i = Integer.parseInt(marker.getTitle().split(":")[0]) - 1;
                if((!mTask[i]) || (curr>=i)){
                mMaterialDialog = new MaterialDialog(this)
                        .setTitle(mPlaceName[i])
                        .setMessage(mDescription[i])
                        .setPositiveButton("Complete Task", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int i = Integer.parseInt(marker.getTitle().split(":")[0]);
                                if(mCompleted[i-1]){textViewTop.setText("You have already completed the task.");snackbar.show();return;}
                                IconGenerator iconFactory = new IconGenerator(getApplicationContext());
                                iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(i+"")));
                                mVisited[i-1]=true;
                                mMaterialDialog.dismiss();
                                mMaterialDialog=null;
                                if(mTask[i-1]){
                                    //dispatchTakePictureIntent();
                                    //textViewTop.setText("Congratulations! You have earned a Temple-Run Badge!");snackbar.show();
                                    textViewTop.setText("Congratulations! You have earned 20 MMT Points.");snackbar.show();
                                    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    int points=SP.getInt("MMTPoints",0);
                                    SharedPreferences.Editor editor1 = SP.edit();
                                    editor1.putInt("MMTPoints", points+20);
                                    editor1.putInt("MMTCurr",curr+1);
                                    editor1.apply();
                                    mCompleted[i-1]=true;
                                    curr++;}
                                else {
                                    textViewTop.setText("Congratulations! You have earned 10 MMT Points.");snackbar.show();
                                    SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    int points=SP.getInt("MMTPoints",0);
                                    SharedPreferences.Editor editor1 = SP.edit();
                                    editor1.putInt("MMTPoints", points+10);
                                    editor1.apply();
                                    mCompleted[i-1]=true;
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMaterialDialog.dismiss();
                            }
                        });
                }
                else {
                    mMaterialDialog = new MaterialDialog(this)
                            .setTitle(mPlaceName[i])
                            .setMessage("You need to complete the previous task(s) to enable this.")
                            .setPositiveButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMaterialDialog.dismiss();
                                }
                            });
                }
                //mMaterialDialog.setBackgroundResource(R.drawable.hyderabad);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.show();
            }
        }
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 124;

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        else mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {requestPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    Location mLastLocation=null;
    Marker mCurrLocationMarker;

    @Override
    public void onMyLocationChange(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        for(Marker marker:mPlaces)
        {
            Location locationA = new Location("point A");
            locationA.setLatitude(mLastLocation.getLatitude());
            locationA.setLongitude(mLastLocation.getLongitude());
            Location locationB = new Location("point B");
            locationB.setLatitude(marker.getPosition().latitude);
            locationB.setLongitude(marker.getPosition().longitude);
            double d = locationA.distanceTo(locationB);
            if(d<500.0f)
            {
                int i = Integer.parseInt(marker.getTitle().split(":")[0]);
                if(!mVisited[i-1]){
                IconGenerator iconFactory = new IconGenerator(getApplicationContext());
                iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(i+"")));}
            }
            else
            {
                int i = Integer.parseInt(marker.getTitle().split(":")[0]);
                IconGenerator iconFactory = new IconGenerator(getApplicationContext());
                if(mVisited[i-1])iconFactory.setStyle(IconGenerator.STYLE_GREEN);
                else if(mTask[i-1]) iconFactory.setStyle(IconGenerator.STYLE_RED);
                else iconFactory.setStyle(IconGenerator.STYLE_ORANGE);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(i+"")));
            }
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
        }
    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                galleryAddPic();
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}

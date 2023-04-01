package com.example.edunav;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        LocationListener {


    private GoogleMap mMap;
    private final static int REQUEST_CODE=1;
    FusedLocationProviderClient fusedLocationProviderClient;


    // creating a variable
    // for search view.
    SearchView searchView;



    Map<String, Integer> markers = new HashMap<String, Integer>();
//polyline store
    List<Polyline> polylines = new ArrayList<Polyline>();

//for getting directions
    private LatLng mOrigin;
    private LatLng mDestination;

    ArrayList<Integer> mMarkerPoints = new ArrayList<Integer>();;
    private Polyline mPolyline;
    private LatLng MarkerPos;




    //for resizing drawables
    public Bitmap resizeBitmap(String drawableName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }


    //polyline design
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final PatternItem DOT = new Dot();
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP,DOT);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_maps);

        // initializing our search view.
        searchView = findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        // adding on query listener for our search view.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();


                if (location.equalsIgnoreCase("admin building")||location.equalsIgnoreCase("admin")){
                    LatLng loc = new LatLng(13.7924604,121.0025501);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Admin Building", Toast.LENGTH_LONG).show();

                } else if (location.equalsIgnoreCase("rizal park")||location.equalsIgnoreCase("rizal")){
                    LatLng loc = new LatLng(13.792288, 121.003093);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Rizal Park", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("field")) {
                    LatLng loc = new LatLng(13.792933, 121.002053);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Field", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("poolside")) {
                    LatLng loc = new LatLng(13.793341, 121.002542);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Poolside", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("shs building b")||location.equalsIgnoreCase("shs b")){
                    LatLng loc = new LatLng(13.7935735,121.0024519);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to SHS Building B", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("shs classrooms")||location.equalsIgnoreCase("shs rooms")){
                    LatLng loc = new LatLng(13.7932733,121.0023886);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to SHS Classrooms", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("beauty care")||location.equalsIgnoreCase("beauty care room")){
                    LatLng loc = new LatLng(13.7930047,121.0025058);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Beauty Care Room", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("cookery")||location.equalsIgnoreCase("cookery rooms")){
                    LatLng loc = new LatLng(13.7930606,121.0026786);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Cookery Rooms", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("guidance")){
                    LatLng loc = new LatLng(13.7929052,121.0025095);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Guidance", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("h.e room")||location.equalsIgnoreCase("h.e")){
                    LatLng loc = new LatLng(13.7928834,121.0026764);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to H.E Room", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("canteen")){
                    LatLng loc = new LatLng(13.7927337,121.0026818);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Canteen", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("tvl classroom")||location.equalsIgnoreCase("tvl")){
                    LatLng loc = new LatLng(13.7925837,121.0027081);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to TVL Classroom", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("science park")||location.equalsIgnoreCase("science")){
                    LatLng loc = new LatLng(13.7922604,121.0026845);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Science Park", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("stve Classrooms")||location.equalsIgnoreCase("stve")){
                    LatLng loc = new LatLng(13.7923624,121.0037623);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to STVE Classrooms", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("main gate")){
                    LatLng loc = new LatLng(13.7922103,121.0039427);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Main Gate", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("gate")){
                    LatLng loc = new LatLng(13.7933748,121.0017241);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Gate", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("shs building a")||location.equalsIgnoreCase("shs a")){
                    LatLng loc = new LatLng(13.7925286,121.0020808);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to SHS Building A", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("gym")||location.equalsIgnoreCase("gymnasium")){
                    LatLng loc = new LatLng(13.7922342,121.0020808);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Gymnasium", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("grade 10 building a")||location.equalsIgnoreCase("grade 10 a")){
                    LatLng loc = new LatLng(13.7919917,121.0023384);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Grade 10 Building A", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("electricity")){
                    LatLng loc = new LatLng(13.7918212,121.0027778);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Electricity", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("grade 10 building b")||location.equalsIgnoreCase("grade 10 b")){
                    LatLng loc = new LatLng(13.791917,121.001679);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Grade 10 Building B", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("grade 8 classrooms")||location.equalsIgnoreCase("grade 8")){
                    LatLng loc = new LatLng(13.7918133,121.002288);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Grade 8 Classrooms", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("main")||location.equalsIgnoreCase("main building")){
                    LatLng loc = new LatLng(13.7925991,121.0031756);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Main Building", Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(getApplicationContext(),"No result please try another word", Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }

        });
        // at last we calling our map fragment to update.
        mapFragment.getMapAsync(this);


    }

    public void about_us(View view){
        Intent school_info = new Intent(MapsActivity.this, school_info.class);
        startActivity(school_info);
    }

    public void home(View view){
        Intent home = new Intent(MapsActivity.this, MainActivity2.class);
        startActivity(home);
    }

    public void questions(View view){
        Intent questions = new Intent(MapsActivity.this, questions.class);
        startActivity(questions);
    }

    public void sections(View view){
        Intent sections = new Intent(MapsActivity.this, sections.class);
        startActivity(sections);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);


        // Add a marker and move the camera
        //markers
        //school marker

        LatLng admin = new LatLng(13.7924604,121.0025501);
        MarkerOptions adminM = new MarkerOptions()
                .position(admin)
                .rotation(0)
                .title("Admin Building")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("school",160,160)));

        Marker mkr1 = mMap.addMarker(adminM);
        markers.put(mkr1.getId(), 1);

        //evacuation area-a marker
        LatLng area_a = new LatLng(13.792288, 121.003093);
        MarkerOptions area_a_M = new MarkerOptions()
                .position(area_a)
                .title("Rizal Park")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("evacuation",100,100)));

        Marker mkr2 = mMap.addMarker(area_a_M);
        markers.put(mkr2.getId(), 2);


        //field marker
        LatLng area_b = new LatLng(13.792933, 121.002053);
        MarkerOptions area_b_M = new MarkerOptions()
                .position(area_b)
                .title("Field")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("evacuation",100,100)));

        Marker mkr3 = mMap.addMarker(area_b_M);
        markers.put(mkr3.getId(), 3);

        //poolside
        LatLng poolside = new LatLng(13.793341, 121.002542);
        MarkerOptions poolside_M = new MarkerOptions()
                .position(poolside)
                .title("Poolside")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("evacuation",100,100)));

        Marker mkr4 = mMap.addMarker(poolside_M);
        markers.put(mkr4.getId(), 4);


        //shs building b
        LatLng shs_b = new LatLng(13.7935735,121.0024519);
        MarkerOptions shs_b_M = new MarkerOptions()
                .position(shs_b)
                .title("SHS Building B")
                .snippet("HUMSS & ABM")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr5 = mMap.addMarker(shs_b_M);
        markers.put(mkr5.getId(), 5);

        //shs classrooms
        LatLng shsclassrooms = new LatLng(13.7932733,121.0023886);
        MarkerOptions shsclassrooms_M = new MarkerOptions()
                .position(shsclassrooms)
                .title("SHS Classrooms")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr6 = mMap.addMarker(shsclassrooms_M);
        markers.put(mkr6.getId(), 6);

        //beauty care
        LatLng beauty = new LatLng(13.7930047,121.0025058);
        MarkerOptions beauty_M = new MarkerOptions()
                .position(beauty)
                .title("Beauty Care Room")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("beauty",100,100)));

        Marker mkr7 = mMap.addMarker(beauty_M);
        markers.put(mkr7.getId(), 7);

        //cookery
        LatLng cookery = new LatLng(13.7930606,121.0026786);
        MarkerOptions cookery_M = new MarkerOptions()
                .position(cookery)
                .title("Cookery Rooms")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("canteen",100,100)));

        Marker mkr8 = mMap.addMarker(cookery_M);
        markers.put(mkr8.getId(), 8);


        //guidance
        LatLng guidance = new LatLng(13.7929052,121.0025095);
        MarkerOptions guidance_M = new MarkerOptions()
                .position(guidance)
                .title("Guidance Office")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("room",100,100)));

        Marker mkr9 = mMap.addMarker(guidance_M);
        markers.put(mkr9.getId(), 9);

        //he
        LatLng he = new LatLng(13.7928834,121.0026764);
        MarkerOptions he_M = new MarkerOptions()
                .position(he)
                .title("H.E Room")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr10 = mMap.addMarker(he_M);
        markers.put(mkr10.getId(), 10);


        //canteen
        LatLng canteen = new LatLng(13.7927337,121.0026818);
        MarkerOptions canteen_M = new MarkerOptions()
                .position(canteen)
                .title("Canteen")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("canteen",100,100)));

        Marker mkr11 = mMap.addMarker(canteen_M);
        markers.put(mkr11.getId(), 11);

        //tvl
        LatLng tvl = new LatLng(13.7925837,121.0027081);
        MarkerOptions tvl_M = new MarkerOptions()
                .position(tvl)
                .title("TVL Classroom")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr12 = mMap.addMarker(tvl_M);
        markers.put(mkr12.getId(), 12);


        //science park
        LatLng science = new LatLng(13.7922604,121.0026845);
        MarkerOptions science_M = new MarkerOptions()
                .position(science)
                .title("Science Park")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("room",100,100)));

        Marker mkr13 = mMap.addMarker(science_M);
        markers.put(mkr13.getId(), 13);

        //stve classrooms
        LatLng stve = new LatLng(13.7923624,121.0037623);
        MarkerOptions stve_M = new MarkerOptions()
                .position(stve)
                .title("STVE Classrooms")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr14 = mMap.addMarker(stve_M);
        markers.put(mkr14.getId(), 14);

        //main gate
        LatLng maingate = new LatLng(13.7922103,121.0039427);
        MarkerOptions maingate_M = new MarkerOptions()
                .position(maingate)
                .title("Main Gate")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("gate",100,100)));

        Marker mkr15 = mMap.addMarker(maingate_M);
        markers.put(mkr15.getId(), 15);


        //gate
        LatLng gate = new LatLng(13.7933748,121.0017241);
        MarkerOptions gate_M = new MarkerOptions()
                .position(gate)
                .title("Gate")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("gate",100,100)));

        Marker mkr16 = mMap.addMarker(gate_M);
        markers.put(mkr16.getId(), 16);

        //shs bulding a
        LatLng shs_a = new LatLng(13.7925286,121.0020808);
        MarkerOptions shs_a_M = new MarkerOptions()
                .position(shs_a)
                .title("SHS Building A")
                .snippet("STEM")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr17 = mMap.addMarker(shs_a_M);
        markers.put(mkr17.getId(), 17);

        //gym
        LatLng gym = new LatLng(13.7922342,121.0020808);
        MarkerOptions gym_M = new MarkerOptions()
                .position(gym)
                .title("Gymnasium")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("gym",100,100)));

        Marker mkr18 = mMap.addMarker(gym_M);
        markers.put(mkr18.getId(), 18);


        //g10 building a
        LatLng g10buildinga = new LatLng(13.7919917,121.0023384);
        MarkerOptions g10buildinga_M = new MarkerOptions()
                .position(g10buildinga)
                .title("Grade 10 Building A")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr19 = mMap.addMarker(g10buildinga_M);
        markers.put(mkr19.getId(), 19);

        //electricity room
        LatLng electricity = new LatLng(13.7918212,121.0027778);
        MarkerOptions electricity_M = new MarkerOptions()
                .position(electricity)
                .title("Electricity Room")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("electric",100,100)));

        Marker mkr20 = mMap.addMarker(electricity_M);
        markers.put(mkr20.getId(), 20);

        //grade 10 building b
        LatLng g10buildingb = new LatLng(13.791917,121.001679);
        MarkerOptions g10buildingb_M = new MarkerOptions()
                .position(g10buildingb)
                .title("Grade 10 Building B")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr21 = mMap.addMarker(g10buildingb_M);
        markers.put(mkr21.getId(), 21);


        //grade 8 classrooms
        LatLng g8rooms = new LatLng(13.7918133,121.002288);
        MarkerOptions g8rooms_M = new MarkerOptions()
                .position(g8rooms)
                .title("Grade 8 Classrooms")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr22 = mMap.addMarker(g8rooms_M);
        markers.put(mkr22.getId(), 22);



        //main building
        LatLng main = new LatLng(13.7925991,121.0031756);
        MarkerOptions main_M = new MarkerOptions()
                .position(main)
                .title("Main Building")
                .snippet("Grade 9 & Grade 7")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr23 = mMap.addMarker(main_M);
        markers.put(mkr23.getId(), 23);







        mMap.moveCamera(CameraUpdateFactory.newLatLng(admin));


        //map view
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //marker on click listener
        mMap.setOnMarkerClickListener(this);


        //
        //get latlong for corners for specified city

        LatLng one = new LatLng(13.7916692,121.0014455);//SW
        LatLng two = new LatLng(13.7936997,121.0038988);//NE

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //add them to builder
        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();

        //get width and height to current display screen
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 20% padding
        int padding = (int) (width * 0.20);

        //set latlong bounds
        mMap.setLatLngBoundsForCameraTarget(bounds);

        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds

        mMap.animateCamera(CameraUpdateFactory.zoomTo( 19.0f ) );
        mMap.setMinZoomPreference(18.6f);

        //move camera to fill the bound to screen
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {


        int id = markers.get(marker.getId());

        mMarkerPoints.add(id);

        MarkerPos = marker.getPosition();

        String id1 = marker.getTitle();

        //clear polyline for every marker click
        for(Polyline line : polylines)
        {
            line.remove();
        }
        polylines.clear();


        getCurrentlocation();
        getPath();



        return false;
    }




    private void getCurrentlocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){

                                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                                List<Address> addresses = null;
                                try {


                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    LatLng userloc = new LatLng((addresses.get(0).getLatitude()), (addresses.get(0).getLongitude()));


                                    MarkerOptions userlocM = new MarkerOptions()
                                            .position(userloc)
                                            .title("You")
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("userloc",150,150)));

                                    Marker mkr = mMap.addMarker(userlocM);
                                    markers.put(mkr.getId(), 0);



                                    mOrigin = userloc;
                                    mDestination = MarkerPos;



                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            }


                        }

                    });

        } else {
            askPermission();
        }

    }


    private void getPath(){

        if (mMarkerPoints.size() == 2) {

            // Admin Bdlg to Rizal Park
            if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 1) {


                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926088, 121.0025078),
                                new LatLng(13.7924463, 121.0025413),
                                new LatLng(13.792438, 121.002805),
                                new LatLng(13.792281, 121.002861),
                                new LatLng(13.792288, 121.003093))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();

                //admin bldg - field
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7929594, 121.00234),
                                new LatLng(13.7928543, 121.0021602),
                                new LatLng(13.792933, 121.002053))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();

                //admin bldg - shs building b
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7930222, 121.0023341),
                                new LatLng(13.7930876, 121.0025171),
                                new LatLng(13.7935735, 121.0024519))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();

                //admin bldg - poolside
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7930222, 121.0023341),
                                new LatLng(13.7930876, 121.0025171),
                                new LatLng(13.793341, 121.002542))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();


                //admin bldg - shs classrooms
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7930222, 121.0023341),
                                new LatLng(13.7930876, 121.0025171),
                                new LatLng(13.7932733, 121.0023886))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();


                //admin bldg - beauty care
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7930222, 121.0023341),
                                new LatLng(13.7930876, 121.0025171))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - cookery
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7930222, 121.0023341),
                                new LatLng(13.7930876, 121.0025171),
                                new LatLng(13.7931182, 121.0026665))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - canteen
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7926653, 121.0023703),
                                new LatLng(13.7927289, 121.0026057))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - tvl classroom
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7926653, 121.0023703),
                                new LatLng(13.7926226, 121.0026481))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - guidance
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7926653, 121.0023703),
                                new LatLng(13.7927234, 121.0025613),
                                new LatLng(13.7928348, 121.0025863))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - h.e room
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.7926653, 121.0023703),
                                new LatLng(13.7927234, 121.0025613),
                                new LatLng(13.7928348, 121.0025863))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - shs building a
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.79264012781666, 121.0023789848412),
                                new LatLng(13.7925995, 121.0020713))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - gym
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792411, 121.002414),
                                new LatLng(13.79227004348421, 121.00232621646784),
                                new LatLng(13.792285336671819, 121.00225031661394))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - electric
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.792075, 121.0027812),
                                new LatLng(13.7919033, 121.0027801))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - g8 classrooms
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.7919995, 121.0025903),
                                new LatLng(13.791901, 121.0023677))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - g10 building a
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.7920935, 121.0024609),
                                new LatLng(13.7920698, 121.002338))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - science park
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.7921895, 121.0026105),
                                new LatLng(13.7922604, 121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - stve
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.7920773, 121.0028928),
                                new LatLng(13.79221, 121.0036171),
                                new LatLng(13.7922672, 121.0037836))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - main bulding
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.7920773, 121.0028928),
                                new LatLng(13.7921469, 121.0031955),
                                new LatLng(13.7924435, 121.0032114))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - gate
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.792444, 121.0024047),
                                new LatLng(13.7933741, 121.0022825),
                                new LatLng(13.7933453, 121.0017203))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - main gate
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7921697, 121.0024995),
                                new LatLng(13.7920773, 121.0028928),
                                new LatLng(13.79221, 121.0036171),
                                new LatLng(13.7922539, 121.0038847),
                                new LatLng(13.79221781663594, 121.003935301053))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - g10 building b
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353, 121.0024724),
                                new LatLng(13.7924935, 121.0024029),
                                new LatLng(13.7924609, 121.0022389),
                                new LatLng(13.7924154, 121.0018621),
                                new LatLng(13.7920825, 121.0018384),
                                new LatLng(13.7919818, 121.0017186))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //start of tvl to all

                //tvl - admin
                //already exist


                //tvl - rizal park
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7925379, 121.0027563),
                                new LatLng(13.7923906, 121.0028362),
                                new LatLng(13.7923654, 121.003036),
                                new LatLng(13.792288, 121.003093))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - field
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792628039624304, 121.00238041890603),
                                new LatLng(13.7929849, 121.0023308),
                                new LatLng(13.7928751, 121.0021733),
                                new LatLng(13.792933, 121.002053))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - poolside
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792628039624304, 121.00238041890603),
                                new LatLng(13.7930208, 121.0023293),
                                new LatLng(13.7930832, 121.0025107),
                                new LatLng(13.793341, 121.002542))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - shs building b
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792628039624304, 121.00238041890603),
                                new LatLng(13.7930208, 121.0023293),
                                new LatLng(13.7930832, 121.0025107),
                                new LatLng(13.7935735, 121.0024519))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - shs classrooms
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792628039624304, 121.00238041890603),
                                new LatLng(13.7930208, 121.0023293),
                                new LatLng(13.7930832, 121.0025107),
                                new LatLng(13.7932733, 121.0023886))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - beauty
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792628039624304, 121.00238041890603),
                                new LatLng(13.7930208, 121.0023293),
                                new LatLng(13.7930832, 121.0025107))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - cookery
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792628039624304, 121.00238041890603),
                                new LatLng(13.7930208, 121.0023293),
                                new LatLng(13.7930832, 121.0025107),
                                new LatLng(13.79311, 121.002678))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - guidance
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.7926575, 121.0025103),
                                new LatLng(13.7928454, 121.0025115))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - he
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.7926575, 121.0025103),
                                new LatLng(13.7928454, 121.0025115),
                                new LatLng(13.7928607, 121.0026429))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - canteen
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.7926575, 121.0025103),
                                new LatLng(13.7927461, 121.0026593))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - science
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7922604, 121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - stve
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7920644, 121.0027939),
                                new LatLng(13.7922652, 121.0037217))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - main gate
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7920644, 121.0027939),
                                new LatLng(13.7922493, 121.0038734),
                                new LatLng(13.792206999212251, 121.00394013713252))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - gate
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.793397, 121.002277),
                                new LatLng(13.7933308, 121.0017443),
                                new LatLng(13.7934088, 121.0017222))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - shs building a
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7925697, 121.0020829))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - gym
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7923352, 121.0024202),
                                new LatLng(13.7922676, 121.0022619))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - grade 10 building a
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921604, 121.002495),
                                new LatLng(13.7920477, 121.0023503))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - electricity
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921604, 121.002495),
                                new LatLng(13.7920739, 121.0027725),
                                new LatLng(13.7918703, 121.0027764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - grade 10 building b
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7924935, 121.0024029),
                                new LatLng(13.7924609, 121.0022389),
                                new LatLng(13.7924154, 121.0018621),
                                new LatLng(13.7920825, 121.0018384),
                                new LatLng(13.7919818, 121.0017186))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //tvl - grade 8 classrooms
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7926587, 121.0027003),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.792193, 121.0025003),
                                new LatLng(13.7920046, 121.0025812),
                                new LatLng(13.7919026, 121.0024627),
                                new LatLng(13.7919009, 121.0023125))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //tvl - main building
            } else if (mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 12) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7925333, 121.0027686),
                                new LatLng(13.7923796, 121.0028705),
                                new LatLng(13.792411, 121.0032002),
                                new LatLng(13.7924923, 121.0032087))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //start of rizal park to all

                //rizal park - admin
                //already exist


                //rizal park - field
            } else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7929733, 121.0023292),
                                new LatLng(13.792887, 121.002181),
                                new LatLng(13.792933, 121.002053))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //rizal park - poolside
            } else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7930298, 121.0023394),
                                new LatLng(13.7931268, 121.0025579),
                                new LatLng(13.793341, 121.002542))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //rizal park - shs building b
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7930298, 121.0023394),
                                new LatLng(13.7931268, 121.0025579),
                                new LatLng(13.7935735, 121.0024519))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }
            //rizal park - shs classrooms
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7930298, 121.0023394),
                                new LatLng(13.7931268, 121.0025579),
                                new LatLng(13.7932733, 121.0023886))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - beauty
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7930298, 121.0023394),
                                new LatLng(13.7931268, 121.0025579),
                                new LatLng(13.7930047, 121.0025058))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }
            //rizal park - cookery
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7930298, 121.0023394),
                                new LatLng(13.7931268, 121.0025579),
                                new LatLng(13.7930606, 121.0026786))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - guidance
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7926137, 121.0023899),
                                new LatLng(13.7929052, 121.0025095))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - he
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7926137, 121.0023899),
                                new LatLng(13.7928834, 121.0026764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - canteen
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7926137, 121.0023899),
                                new LatLng(13.7927337, 121.0026818))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - tvl
            //already exist


            //rizal park - science park
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7921546, 121.0025742),
                                new LatLng(13.7922604, 121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - stve
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7922662, 121.0037485))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - main gate
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7922538, 121.0039031),
                                new LatLng(13.79221722217941, 121.00393419387413))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - gate
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7920832, 121.0026845),
                                new LatLng(13.7923624, 121.0024102),
                                new LatLng(13.7933993, 121.0022835),
                                new LatLng(13.7933603, 121.0017306))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - shs building a
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.792249,121.0024379),
                                new LatLng(13.792634683383765, 121.00238906138188),
                                new LatLng(13.7925778, 121.0020911))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - gym
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7920832, 121.0026845),
                                new LatLng(13.7923518, 121.0024227),
                                new LatLng(13.7922619, 121.0022435))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - grade 10 building a
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7920832, 121.0026845),
                                new LatLng(13.792169, 121.0025045),
                                new LatLng(13.7920434, 121.0023425))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - electricity
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920769, 121.0027835),
                                new LatLng(13.7918929, 121.0027737))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - grade 10 building b
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.792249,121.0024379),
                                new LatLng(13.7924942,121.0024109),
                                new LatLng(13.792463, 121.002317),
                                new LatLng(13.7924243, 121.001855),
                                new LatLng(13.7920727, 121.0018269),
                                new LatLng(13.791917, 121.001679))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7920682,121.0027727),
                                new LatLng(13.7920832, 121.0026845),
                                new LatLng(13.7920034, 121.002549),
                                new LatLng(13.7919163, 121.0024591),
                                new LatLng(13.7919091, 121.0023413))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //rizal park - main building
            else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.7925991, 121.0031756))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

//start of field to all
            //field - admin
            //already exist

            //field - rizal park
            //already exist


            //field - poolside
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7930718, 121.0025107),
                                new LatLng(13.793341, 121.002542))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - shs building b
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7930718, 121.0025107),
                                new LatLng(13.7935735, 121.0024519))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - shs classrooms
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7930718, 121.0025107),
                                new LatLng(13.7932733, 121.0023886))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //field - beaurt care
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7930718, 121.0025107))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - cookery
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7930718, 121.0025107),
                                new LatLng(13.7930606, 121.0026786))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //field - guidance
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7928394, 121.0025175))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - he
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7928834, 121.0026764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - canteen
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7927337, 121.0026818))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - tvl
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7926548, 121.0026965))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - science park
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7921619, 121.0025128),
                                new LatLng(13.7922604, 121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - stve
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7921619, 121.0025128),
                                new LatLng(13.7920838, 121.0029532),
                                new LatLng(13.7922597, 121.0037366))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - main gate
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7921619, 121.0025128),
                                new LatLng(13.7920838, 121.0029532),
                                new LatLng(13.7922308, 121.0039476))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - gate
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7931696, 121.0019427),
                                new LatLng(13.7933722, 121.0017209))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - shs building a
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7925286, 121.0020808))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - gym
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7923427, 121.0024247),
                                new LatLng(13.7922465, 121.0022611))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //field - grade 10 building a
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7921696, 121.0024971),
                                new LatLng(13.7920551, 121.0023364))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - electricity
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7921619, 121.0025128),
                                new LatLng(13.7920704, 121.0027859),
                                new LatLng(13.7918754, 121.0027736))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - grade 10 building b
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7926335, 121.0023164),
                                new LatLng(13.7924711, 121.0023337),
                                new LatLng(13.7924135, 121.0018604),
                                new LatLng(13.7920795, 121.0018321),
                                new LatLng(13.791917, 121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //field - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.7921696, 121.0024971),
                                new LatLng(13.7919917, 121.0025468),
                                new LatLng(13.7919203, 121.0024708),
                                new LatLng(13.7918835, 121.0023258))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //field - main building
            else if (mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 3) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792933, 121.002053),
                                new LatLng(13.7928966, 121.0021911),
                                new LatLng(13.793019, 121.0023331),
                                new LatLng(13.79264635234936, 121.00237642772647),
                                new LatLng(13.7921619, 121.0025128),
                                new LatLng(13.7921396, 121.003196),
                                new LatLng(13.7925991, 121.0031756))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of poolside to all

            //poolside - admin
            //already exist

            //poolside - rizal park
            //already exist

            //poolside - field
            //already exist

            //poolside - shs building b
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7935735, 121.0024519))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - shs classrooms
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7932733, 121.0023886))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - beauty
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930047, 121.0025058))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - cookery
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930606, 121.0026786))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - guidance
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7926588, 121.0023737),
                                new LatLng(13.7926756, 121.0025271),
                                new LatLng(13.792845, 121.0025156))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //poolside - guidance
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7926588, 121.0023737),
                                new LatLng(13.7926756, 121.0025271),
                                new LatLng(13.792845, 121.0025156))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - guidance
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7926588, 121.0023737),
                                new LatLng(13.7926756, 121.0025271),
                                new LatLng(13.7928834, 121.0026764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - canteen
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7926588, 121.0023737),
                                new LatLng(13.7926756, 121.0025271),
                                new LatLng(13.7927337, 121.0026818))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - tvl
            //already exist

            //poolside - science park
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7922947, 121.0024394),
                                new LatLng(13.7921375, 121.0025974),
                                new LatLng(13.7922604, 121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - science park
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7922947, 121.0024394),
                                new LatLng(13.7920673, 121.0027424),
                                new LatLng(13.7922584, 121.003732))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - main gate
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7922947, 121.0024394),
                                new LatLng(13.7920673, 121.0027424),
                                new LatLng(13.792227845415418, 121.00394145389237))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - gate
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7933958, 121.0022815),
                                new LatLng(13.7933558, 121.0017295))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //poolside - shs building a
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.792630372969306, 121.00238115704053),
                                new LatLng(13.79259619550618, 121.00210819541388))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //poolside - gym
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 4) {

            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(

                            new LatLng(13.793341, 121.002542),
                            new LatLng(13.7930604, 121.0024802),
                            new LatLng(13.7930226, 121.0023349),
                            new LatLng(13.7923444, 121.0024198),
                            new LatLng(13.7922551, 121.0022542))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));

            mMarkerPoints.clear();
            }


            //poolside - grade 10 bulding a
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7923444, 121.0024198),
                                new LatLng(13.7920721,121.0023534))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();
            }

            //poolside - electricity
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7923444, 121.0024198),
                                new LatLng(13.7920694,121.0027845),
                                new LatLng(13.7918212,121.0027778))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();
            }

            //poolside - grade 10 building b
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7924741,121.0023871),
                                new LatLng(13.792419,121.0018621),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();
            }


            //poolside - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604, 121.0024802),
                                new LatLng(13.7930226, 121.0023349),
                                new LatLng(13.7921901,121.0024701),
                                new LatLng(13.7919986,121.0025622),
                                new LatLng(13.7919228,121.0024657),
                                new LatLng(3.7918133,121.002288))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();
            }


            //poolside - main building
            else if (mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 4) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.793341, 121.002542),
                                new LatLng(13.7930604,121.0024802),
                                new LatLng(13.7930226,121.0023349),
                                new LatLng(13.7922947,121.0024394),
                                new LatLng(13.7920673,121.0027424),
                                new LatLng(13.7921485,121.0031984),
                                new LatLng(13.7925991,121.0031756))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

//start of shs building b to all

            //admin, rizal, field , poolside already exist


            //shs building b - shs classrooms
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7932733,121.0023886))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - beauty
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930047,121.0025058))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - cookery
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7932185,121.0024819),
                                new LatLng(13.7930606,121.0026786))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - guidance
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7929052,121.0025095))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - he
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7928834,121.0026764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - canteen
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7927337,121.0026818))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - tvl already exist

            //shs building b - science park
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7922001,121.0024601),
                                new LatLng(13.7920909,121.0026506),
                                new LatLng(13.7922604,121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - stve
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7922001,121.0024601),
                                new LatLng(13.7920909,121.0026506),
                                new LatLng(13.7922441,121.0037366))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - main gate
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7922001,121.0024601),
                                new LatLng(13.7920909,121.0026506),
                                new LatLng(13.7922441,121.0037366),
                                new LatLng(13.7922103,121.0039427))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - gate
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7934011,121.002279),
                                new LatLng(13.7933447,121.0017282))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - shs building a
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7926325,121.0023649),
                                new LatLng(13.7925902,121.0020999))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - gym
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7923361,121.0024239),
                                new LatLng(13.7922055,121.0022568))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - grade 10 building a
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7923361,121.0024239),
                                new LatLng(13.7921422,121.0025099),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - electricity
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7923361,121.0024239),
                                new LatLng(13.7921422,121.0025099),
                                new LatLng(13.7920516,121.0027784),
                                new LatLng(13.7918212,121.0027778))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

        //shs building b - grade 10 building b
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7924741,121.0023897),
                                new LatLng(13.7924208,121.0018697),
                                new LatLng(13.7920771,121.0018542),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7930589,121.00248),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7921572,121.0024852),
                                new LatLng(13.7919967,121.0025535),
                                new LatLng(13.7919283,121.0024748),
                                new LatLng(13.7918133,121.002288))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building b - main building
            else if (mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 5) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(


                                new LatLng(13.7935735,121.0024519),
                                new LatLng(13.7931268, 121.0025579),
                                new LatLng(13.7930298, 121.0023394),
                                new LatLng(13.7921878, 121.0024778),
                                new LatLng(13.7921221, 121.0031098),
                                new LatLng(13.7925991,121.0031756))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of shs classrooms to all
            //admin,rizal,field,poolside,shs building b already exist


            //shs classrooms - beauty
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(


                                new LatLng(13.7932733,121.0023886),
                                new LatLng(13.7932216,121.0025134),
                                new LatLng(13.7930822,121.0025178))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - cookery
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(


                                new LatLng(13.7932733,121.0023886),
                                new LatLng(13.7932216,121.0025134),
                                new LatLng(13.7930606,121.0026786))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - guidance
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733,121.0023886),
                                new LatLng(13.7932216,121.0025134),
                                new LatLng(13.7930822,121.0025178),
                                new LatLng(13.7930188,121.0023394),
                                new LatLng(13.79264079697799, 121.00237362140093),
                                new LatLng(13.792691735515653, 121.0025322532776),
                                new LatLng(13.7929052,121.0025095))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - he
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733,121.0023886),
                                new LatLng(13.7932216,121.0025134),
                                new LatLng(13.7930822,121.0025178),
                                new LatLng(13.7930188,121.0023394),
                                new LatLng(13.79264079697799, 121.00237362140093),
                                new LatLng(13.792691735515653, 121.0025322532776),
                                new LatLng(13.7928834,121.0026764))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - canteen
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733,121.0023886),
                                new LatLng(13.7932216,121.0025134),
                                new LatLng(13.7930822,121.0025178),
                                new LatLng(13.7930188,121.0023394),
                                new LatLng(13.79264079697799, 121.00237362140093),
                                new LatLng(13.792691735515653, 121.0025322532776),
                                new LatLng(13.7927337,121.0026818))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - tvl already exist

            //shs classrooms - science
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7921908, 121.0024631),
                                new LatLng(13.7922604, 121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //shs classrooms - stve
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7921908, 121.0024631),
                                new LatLng(13.7920657,121.002852),
                                new LatLng(13.7922356,121.0037265))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - main gate
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7921908, 121.0024631),
                                new LatLng(13.7920657,121.002852),
                                new LatLng(13.792216851413919, 121.00394089180128))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - gate
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7933973,121.0022839),
                                new LatLng(13.7933516,121.001722))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - shs building a
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(

                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.792643766919056, 121.00235626045226),
                                new LatLng(13.7925913,121.0021111))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - gym
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.792643766919056, 121.00235626045226),
                                new LatLng(13.7923406,121.0023959),
                                new LatLng(13.7922476,121.0022608))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - grade 10 building a
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.792643766919056, 121.00235626045226),
                                new LatLng(13.7921441,121.0025192),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - electricity
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.792643766919056, 121.00235626045226),
                                new LatLng(13.7921441,121.0025192),
                                new LatLng(13.7920623,121.0027862),
                                new LatLng(13.7918212,121.0027778))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - grade 10 building b
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7924766,121.0023815),
                                new LatLng(13.7924275,121.0018631),
                                new LatLng(13.7920791,121.0018544),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs classrooms - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7921637,121.0024959),
                                new LatLng(13.7919917,121.0025469),
                                new LatLng(13.7919354,121.002486),
                                new LatLng(13.7918133,121.002288))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //shs classrooms - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 6) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7932733, 121.0023886),
                                new LatLng(13.7932216, 121.0025134),
                                new LatLng(13.7930822, 121.0025178),
                                new LatLng(13.7930188, 121.0023394),
                                new LatLng(13.7921637,121.0024959),
                                new LatLng(13.7920674,121.0027376),
                                new LatLng(13.7921307,121.0031976),
                                new LatLng(13.7925991,121.0031756))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of beauty to all

            //admin, shs building b, rizal park, field, tvl, shs classrooms already exist

            //beauty - cookery
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930606,121.0026786))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //beauty - guidance
        else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7929052,121.0025095))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //beauty - he
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7928834,121.0026764))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - canteen
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7927337,121.0026818))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - tvl already exist

            //beauty - science
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.79219,121.0024636),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - stve
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.79219,121.0024636),
                                new LatLng(13.7920677,121.002718),
                                new LatLng(13.7922463,121.0037216))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - main gate
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.79219,121.0024636),
                                new LatLng(13.7920677,121.002718),
                                new LatLng(13.7922463,121.0037216),
                                new LatLng(13.792222727456805, 121.00393664801717))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - gate
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7933898,121.0022906),
                                new LatLng(13.7933514,121.0017075))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - shs bulding a
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792647690749568, 121.00237974347657),
                                new LatLng(13.792590206354399, 121.00210517333143))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - gym
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7923236,121.0024081),
                                new LatLng(13.7922439,121.0022547))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //beauty - grade 10 building a
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792181,121.0024966),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - electricity
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792181,121.0024966),
                                new LatLng(13.792062,121.0027835),
                                new LatLng(13.7918212,121.0027778))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - grade 10 building b
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792466,121.0023928),
                                new LatLng(13.7924171,121.0018592),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7921716,121.0024819),
                                new LatLng(13.7919955,121.0025481),
                                new LatLng(13.7919384,121.0024843),
                                new LatLng(13.7918976,121.0022592))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //beauty - main building
            else if (mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 7) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7930047,121.0025058),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7921716,121.0024819),
                                new LatLng(13.7920574,121.0027788),
                                new LatLng(13.7921431,121.0031948),
                                new LatLng(13.7925991,121.0031756))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of cookery to all

            //admin, shs building b, rizal park, field, tvl, shs classrooms, beauty already exist


            //cookery - guidance
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7929052,121.0025095))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //cookery - he
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7928834,121.0026764))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - canteen
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.792721902923642, 121.00252619776028),
                                new LatLng(13.7927337,121.0026818))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - tvl already exist

            //cookery - science
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.79219,121.0024636),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - stve
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.79219,121.0024636),
                                new LatLng(13.7920677,121.002718),
                                new LatLng(13.7922463,121.0037216))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - main gate
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792671011031544, 121.00237734987955),
                                new LatLng(13.79219,121.0024636),
                                new LatLng(13.7920677,121.002718),
                                new LatLng(13.7922463,121.0037216),
                                new LatLng(13.792222727456805, 121.00393664801717))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - gate
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7933898,121.0022906),
                                new LatLng(13.7933514,121.0017075))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - shs bulding a
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792647690749568, 121.00237974347657),
                                new LatLng(13.792590206354399, 121.00210517333143))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - gym
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7923236,121.0024081),
                                new LatLng(13.7922439,121.0022547))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //cookery - grade 10 building a
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792181,121.0024966),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - electricity
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792181,121.0024966),
                                new LatLng(13.792062,121.0027835),
                                new LatLng(13.7918212,121.0027778))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - grade 10 building b
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.792466,121.0023928),
                                new LatLng(13.7924171,121.0018592),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7921716,121.0024819),
                                new LatLng(13.7919955,121.0025481),
                                new LatLng(13.7919384,121.0024843),
                                new LatLng(13.7918976,121.0022592))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //cookery - main building
            else if (mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 8) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7931104,121.002668),
                                new LatLng(13.7930279,121.0023415),
                                new LatLng(13.7921716,121.0024819),
                                new LatLng(13.7920574,121.0027788),
                                new LatLng(13.7921431,121.0031948),
                                new LatLng(13.7925991,121.0031756))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }




            //start of guidance to all

            // 1- 8  and 12 already exist

            //guidance - he
       else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7928834,121.0026764))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();

            //guidance - canteen
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7927461, 121.0026593))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();

            //guidance - science
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7921713, 121.0024783),
                            new LatLng(13.7922604, 121.0026845))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();

            //guidance - stve
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7921713, 121.0024783),
                            new LatLng(13.7920644, 121.0027939),
                            new LatLng(13.7922652, 121.0037217))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - main gate
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7921713, 121.0024783),
                            new LatLng(13.7920644, 121.0027939),
                            new LatLng(13.7922493, 121.0038734),
                            new LatLng(13.792206999212251, 121.00394013713252))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - gate
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.793397, 121.002277),
                            new LatLng(13.7933308, 121.0017443),
                            new LatLng(13.7934088, 121.0017222))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();

            //guidance - shs building a
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7925697, 121.0020829))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();

            //guidance - gym
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7923352, 121.0024202),
                            new LatLng(13.7922676, 121.0022619))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - grade 10 building a
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7921604, 121.002495),
                            new LatLng(13.7920477, 121.0023503))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - electricity
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7921604, 121.002495),
                            new LatLng(13.7920739, 121.0027725),
                            new LatLng(13.7918703, 121.0027764))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - grade 10 building b
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7924935, 121.0024029),
                            new LatLng(13.7924609, 121.0022389),
                            new LatLng(13.7924154, 121.0018621),
                            new LatLng(13.7920825, 121.0018384),
                            new LatLng(13.7919818, 121.0017186))
                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - grade 8 classrooms
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052,121.0025095),
                            new LatLng(13.7926814,121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.792193, 121.0025003),
                            new LatLng(13.7920046, 121.0025812),
                            new LatLng(13.7919026, 121.0024627),
                            new LatLng(13.7919009, 121.0023125))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();


            //guidance - main building
        } else if (mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 9) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7929052, 121.0025095),
                            new LatLng(13.7926814, 121.0025217),
                            new LatLng(13.792638045570444, 121.00237803828715),
                            new LatLng(13.7921556, 121.0025067),
                            new LatLng(13.7921135, 121.0031927),
                            new LatLng(13.7924923, 121.0032087))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

            //start of he to all

            // 1- 10  and 12 already exist

            //he - canteen
            else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.79274,121.0025241),
                                new LatLng(13.7927461, 121.0026593))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //he - science
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7922604, 121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //he - stve
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7920644, 121.0027939),
                                new LatLng(13.7922652, 121.0037217))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - main gate
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7920644, 121.0027939),
                                new LatLng(13.7922493, 121.0038734),
                                new LatLng(13.792206999212251, 121.00394013713252))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - gate
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.793397, 121.002277),
                                new LatLng(13.7933308, 121.0017443),
                                new LatLng(13.7934088, 121.0017222))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //he - shs building a
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7925697, 121.0020829))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //he - gym
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7923352, 121.0024202),
                                new LatLng(13.7922676, 121.0022619))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - grade 10 building a
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921604, 121.002495),
                                new LatLng(13.7920477, 121.0023503))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - electricity
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921604, 121.002495),
                                new LatLng(13.7920739, 121.0027725),
                                new LatLng(13.7918703, 121.0027764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - grade 10 building b
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7924935, 121.0024029),
                                new LatLng(13.7924609, 121.0022389),
                                new LatLng(13.7924154, 121.0018621),
                                new LatLng(13.7920825, 121.0018384),
                                new LatLng(13.7919818, 121.0017186))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - grade 8 classrooms
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.792193, 121.0025003),
                                new LatLng(13.7920046, 121.0025812),
                                new LatLng(13.7919026, 121.0024627),
                                new LatLng(13.7919009, 121.0023125))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //he - main building
            } else if (mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 10) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7928834,121.0026764),
                                new LatLng(13.7926814, 121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921556, 121.0025067),
                                new LatLng(13.7921135, 121.0031927),
                                new LatLng(13.7924923, 121.0032087))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of canteen to all
            // 1- 10  and 12 already exist
            //canteen - science
            else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7922604, 121.0026845))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //canteen - stve
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7920644, 121.0027939),
                                new LatLng(13.7922652, 121.0037217))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - main gate
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921713, 121.0024783),
                                new LatLng(13.7920644, 121.0027939),
                                new LatLng(13.7922493, 121.0038734),
                                new LatLng(13.792206999212251, 121.00394013713252))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - gate
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.793397, 121.002277),
                                new LatLng(13.7933308, 121.0017443),
                                new LatLng(13.7934088, 121.0017222))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //canteen - shs building a
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7925697, 121.0020829))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //canteen - gym
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7923352, 121.0024202),
                                new LatLng(13.7922676, 121.0022619))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - grade 10 building a
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921604, 121.002495),
                                new LatLng(13.7920477, 121.0023503))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - electricity
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921604, 121.002495),
                                new LatLng(13.7920739, 121.0027725),
                                new LatLng(13.7918703, 121.0027764))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - grade 10 building b
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7924935, 121.0024029),
                                new LatLng(13.7924609, 121.0022389),
                                new LatLng(13.7924154, 121.0018621),
                                new LatLng(13.7920825, 121.0018384),
                                new LatLng(13.7919818, 121.0017186))
                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - grade 8 classrooms
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814,121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.792193, 121.0025003),
                                new LatLng(13.7920046, 121.0025812),
                                new LatLng(13.7919026, 121.0024627),
                                new LatLng(13.7919009, 121.0023125))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //canteen - main building
            } else if (mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 11) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7927337,121.0026818),
                                new LatLng(13.7926814, 121.0025217),
                                new LatLng(13.792638045570444, 121.00237803828715),
                                new LatLng(13.7921556, 121.0025067),
                                new LatLng(13.7921135, 121.0031927),
                                new LatLng(13.7924923, 121.0032087))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //start of science park to all
            //1-12 already exist

            //science park - stve
        else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 13) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.7922604,121.0026845),
                            new LatLng(13.7921425,121.0026091),
                            new LatLng(13.792059,121.0028307),
                            new LatLng(13.7922488,121.0037248))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }


        //science park - main gate
         else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 13) {
             polylines.add(this.mMap.addPolyline(new PolylineOptions()
                       .clickable(true)
                       .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7921425,121.0026091),
                                new LatLng(13.792059,121.0028307),
                                new LatLng(13.792226,121.0037267),
                                new LatLng(13.792217107675725, 121.00395124101244))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - gate
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7921269,121.0025855),
                                new LatLng(13.7923414,121.0024059),
                                new LatLng(13.7934016,121.0022671),
                                new LatLng(13.7933501,121.0017251))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - shs building a
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7921269,121.0025855),
                                new LatLng(13.7923414,121.0024059),
                                new LatLng(13.792635777574002, 121.00238412769744),
                                new LatLng(13.7925775,121.00207))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - gym
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7921269,121.0025855),
                                new LatLng(13.7923414,121.0024059),
                                new LatLng(13.7922556,121.0022615))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - grade 10 building a
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.792144,121.0024996),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - electricity
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7920706,121.0027809),
                                new LatLng(13.7918893,121.0027789))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - grade 10 building b
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7921269,121.0025855),
                                new LatLng(13.7923414,121.0024059),
                                new LatLng(13.792475,121.0024068),
                                new LatLng(13.7924219,121.0018592),
                                new LatLng(13.7920888,121.0018439),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.792146,121.0025285),
                                new LatLng(13.791994,121.0025452),
                                new LatLng(13.791924,121.0024814),
                                new LatLng(13.7918941,121.0023024))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //science park - main building
            else if (mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 13) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922604,121.0026845),
                                new LatLng(13.7921095,121.002655),
                                new LatLng(13.7920804,121.0028993),
                                new LatLng(13.7921327,121.0032022),
                                new LatLng(13.7924541,121.0032135))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of stve to all
            //1-13 already exist

            //stve - main gate
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792230471040574, 121.00394273500467))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - gate
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792067,121.0027765),
                                new LatLng(13.7922234,121.0024368),
                                new LatLng(13.7933968,121.0022755),
                                new LatLng(13.7933585,121.0017296))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - shs building a
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792067,121.0027765),
                                new LatLng(13.7922234,121.0024368),
                                new LatLng(13.792639441647879, 121.00237280639303),
                                new LatLng(13.792584690492864, 121.00210090530567))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - gym
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792067,121.0027765),
                                new LatLng(13.7922234,121.0024368),
                                new LatLng(13.7922654,121.002243))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - grade 10 building a
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792067,121.0027765),
                                new LatLng(13.7921379,121.0025039),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - electricity
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792069,121.0027826),
                                new LatLng(13.7918915,121.0027777))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - grade 10 building b
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792067,121.0027765),
                                new LatLng(13.7922234,121.0024368),
                                new LatLng(13.7924644,121.0023816),
                                new LatLng(13.7924154, 121.0018621),
                                new LatLng(13.7920825, 121.0018384),
                                new LatLng(13.7919818, 121.0017186))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.792067,121.0027765),
                                new LatLng(13.7921347,121.0025326),
                                new LatLng(13.7919914,121.0025544),
                                new LatLng(13.7919106,121.002447),
                                new LatLng(13.7918792,121.0022806))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //stve - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 14) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7923624,121.0037623),
                                new LatLng(13.7922348,121.0037827),
                                new LatLng(13.7921586,121.0033195),
                                new LatLng(13.7924603,121.0032112))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //start of main gate to all
            //1-14 already exist

            //main gate - gate
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920753,121.0027801),
                                new LatLng(13.7922329,121.0024505),
                                new LatLng(13.7933966,121.0022781),
                                new LatLng(13.7933621,121.0017242))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - shs building a
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920753,121.0027801),
                                new LatLng(13.7922329,121.0024505),
                                new LatLng(13.792616678298163, 121.00237392024714),
                                new LatLng(13.792592258348938, 121.00210519673657))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - gym
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920753,121.0027801),
                                new LatLng(13.7922329,121.0024505),
                                new LatLng(13.7922711,121.0022597))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - grade 10 building a
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920753,121.0027801),
                                new LatLng(13.7921387,121.0025078),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - electricity
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920702,121.0027837),
                                new LatLng(13.7918969,121.0027756))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - grade 10 building b
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920753,121.0027801),
                                new LatLng(13.7922329,121.0024505),
                                new LatLng(13.7924609, 121.0022389),
                                new LatLng(13.7924154, 121.0018621),
                                new LatLng(13.7920825, 121.0018384),
                                new LatLng(13.7919818, 121.0017186))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7920753,121.0027801),
                                new LatLng(13.7921397,121.0025166),
                                new LatLng(13.7919925,121.0025501),
                                new LatLng(13.7919395,121.0024894),
                                new LatLng(13.7919009,121.0022522))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //main gate - main building
            else if (mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 15) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7922103,121.0039427),
                                new LatLng(13.7922443,121.0038113),
                                new LatLng(13.7921635,121.0033131),
                                new LatLng(13.792465,121.00322))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of gate to all
            //1-15 already exist

            //gate - shs building a
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7925995,121.0018317),
                                new LatLng(13.7925915,121.0020909))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gate - gym
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7933935,121.0022756),
                                new LatLng(13.792349,121.0024103),
                                new LatLng(13.792258,121.00226))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gate - grade 10 building a
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7933935,121.0022756),
                                new LatLng(13.792349,121.0024103),
                                new LatLng(13.7921545,121.0024979),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gate - electricity
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7933935,121.0022756),
                                new LatLng(13.792349,121.0024103),
                                new LatLng(13.7921545,121.0024979),
                                new LatLng(13.7920786,121.0027767),
                                new LatLng(13.7918969,121.0027779))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gate - grade 10 building b
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7924235,121.0018632),
                                new LatLng(13.7921403,121.0018756),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gate - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7933942,121.0022804),
                                new LatLng(13.792237,121.002464),
                                new LatLng(13.7919932,121.002559),
                                new LatLng(13.7919342,121.002479),
                                new LatLng(13.7918949,121.0022413))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gate - main building
            else if (mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 16) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7933475,121.0017245),
                                new LatLng(13.7933849,121.0022845),
                                new LatLng(13.79224,121.0024611),
                                new LatLng(13.7921011,121.0026774),
                                new LatLng(13.7921279,121.0031941),
                                new LatLng(13.7924539,121.0032034))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of shs building a to all
            //1-16 already exist

            //shs building a - gym
            else if (mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 17) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792587,121.0020808),
                                new LatLng(13.7926222780602, 121.00238924674805),
                                new LatLng(13.792354193267174, 121.00240161029457),
                                new LatLng(13.792283146987712, 121.00233919732776),
                                new LatLng(13.7922692323624, 121.0022498705479))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building a - grade 10 building a
            else if (mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 17) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792587,121.0020808),
                                new LatLng(13.7926222780602, 121.00238924674805),
                                new LatLng(13.792169296458942, 121.00250237101922),
                                new LatLng(13.7919917,121.0023384))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building a - electricity
            else if (mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 17) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792587,121.0020808),
                                new LatLng(13.7926222780602, 121.00238924674805),
                                new LatLng(13.792169296458942, 121.00250237101922),
                                new LatLng(13.792067425312192, 121.00277680669059),
                                new LatLng(13.791893555563782, 121.00277446446404))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building a - electricity
            else if (mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 17) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792587,121.0020808),
                                new LatLng(13.792565807525962, 121.00184604812803),
                                new LatLng(13.792199361001321, 121.00188018481802),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //shs building a - g8 classrooms
            else if (mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 17) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792587,121.0020808),
                                new LatLng(13.7926222780602, 121.00238924674805),
                                new LatLng(13.792168778057942, 121.00249645347016),
                                new LatLng(13.79199604564219, 121.0025465468969),
                                new LatLng(13.791944731292936, 121.00247945099473),
                                new LatLng(13.79190767528785, 121.00226422761494))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs building a - main building
            else if (mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 17) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792587,121.0020808),
                                new LatLng(13.7926222780602, 121.00238924674805),
                                new LatLng(13.792168778057942, 121.00249645347016),
                                new LatLng(13.792062862649685, 121.00276904405517),
                                new LatLng(13.792161760130522, 121.00330585932832),
                                new LatLng(13.792456722816512, 121.00320891603997))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of gym to all
            //1-17 already exist



            //gym to grade 10 building a
            else if (mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 18) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792254308951142, 121.00225456815986),
                                new LatLng(13.792053467150723, 121.00228728372488))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gym to electricity
            else if (mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 18) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792254308951142, 121.00225456815986),
                                new LatLng(13.792321719756533, 121.00241803270278),
                                new LatLng(13.792148407204982, 121.0025153881858),
                                new LatLng(13.79208115136344, 121.00277343596888),
                                new LatLng(13.791895972340258, 121.00277521139422))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gym to grade 10 building b
            else if (mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 18) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792175643538014, 121.00191102910057),
                                new LatLng(13.792046727797361, 121.00181109417004),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }
            //gym to g8 classrooms
            else if (mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 18) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79205366697434, 121.00209741538276),
                                new LatLng(13.792027904295834, 121.00197340197376),
                                new LatLng(13.79190122706046, 121.00198650856326),
                                new LatLng(13.7919062903946, 121.00223514991467))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //gym to main building
            else if (mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 18) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792247115999619, 121.00225466927095),
                                new LatLng(13.79231434950641, 121.00242402230727),
                                new LatLng(13.79213237583828, 121.00256646703706),
                                new LatLng(13.792062204564864, 121.00284918199723),
                                new LatLng(13.792161468878598, 121.00329544461943),
                                new LatLng(13.792453735867124, 121.00321227050748))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of grade 10 building a to all
            //1-18 already exist


            //grade 10 building a - electricity
            else if (mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 19) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.7919917,121.0023384),
                                new LatLng(13.792166273177019, 121.00251648867342),
                                new LatLng(13.792075395422009, 121.00277408400657),
                                new LatLng(13.791892516930966, 121.00277862589249))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //grade 10 building a - grade 10 building b
            else if (mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 19) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792067777218298, 121.00232574227772),
                                new LatLng(13.792028994268454, 121.0019456143203),
                                new LatLng(13.792074886476092, 121.00185164847792),
                                new LatLng(13.79198106478802, 121.00170565019312))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //grade 10 building a - g8 classrooms
            else if (mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 19) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058697510182, 121.00231435399212),
                                new LatLng(13.792020210747356, 121.00197294983013),
                                new LatLng(13.791902044729003, 121.00197824556837),
                                new LatLng(13.791909402294477, 121.0022400239503))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //grade 10 building a - main building
            else if (mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 19) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058697510182, 121.00231435399212),
                                new LatLng(13.79216050028262, 121.00253964413493),
                                new LatLng(13.792073279520293, 121.00275517462268),
                                new LatLng(13.792160487975547, 121.00331364860469),
                                new LatLng(13.792457970524575, 121.00321061533771))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //electricity - grade 10 building b
            else if (mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 20) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.791896226942507, 121.00277693311035),
                                new LatLng(13.792076360486092, 121.0027779686278),
                                new LatLng(13.792166839086068, 121.00252622427627),
                                new LatLng(13.79208010016839, 121.00244101098025),
                                new LatLng(13.792035580756473, 121.00193935100629),
                                new LatLng(13.79207389889197, 121.00186308909123),
                                new LatLng(13.791917,121.001679))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //electricity - g8 classrooms
            else if (mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 20) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.791883509009336, 121.00273799286668),
                                new LatLng(13.79187511236969, 121.00247520380282),
                                new LatLng(13.791906839835969, 121.00224638581984))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //electricity - main building
            else if (mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 20) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.791896226942507, 121.00277693311035),
                                new LatLng(13.792076360486092, 121.0027779686278),
                                new LatLng(13.79214549251284, 121.0033039072567),
                                new LatLng(13.792450117786354, 121.00321655810994))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //start of grade 10 building b to all
            //1-20 already exist

            //grade 10 building b - g8 classrooms
                else if (mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 21) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79198577338214, 121.00167559863648),
                                new LatLng(13.792071407298417, 121.00186517335024),
                                new LatLng(13.792029073533643, 121.00196628062838),
                                new LatLng(13.791908842387294, 121.00197952161592),
                                new LatLng(13.791912161672764, 121.00226906246462))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //grade 10 building b - main bulding
            else if (mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 21) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79198577338214, 121.00167559863648),
                                new LatLng(13.792071407298417, 121.00186517335024),
                                new LatLng(13.792029073533643, 121.00196628062838),
                                new LatLng(13.79208178552341, 121.00245660395146),
                                new LatLng(13.792154382708842, 121.00252699944508),
                                new LatLng(13.79207247873998, 121.00277590953914),
                                new LatLng(13.792157427300163, 121.00329724521347),
                                new LatLng(13.792452987642113, 121.00320855810237))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of g8 classrooms - all
            //1-21 already exist

            //g8 classrooms - main building

            else if (mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 22) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.791904763881496, 121.00224960407513),
                                new LatLng(13.791953329181721, 121.0024898128863),
                                new LatLng(13.791988698149629, 121.00253666292615),
                                new LatLng(13.792152940843405, 121.00252898596433),
                                new LatLng(13.792077329378209, 121.00277671704951),
                                new LatLng(13.792155339693233, 121.0033064259069),
                                new LatLng(13.792460788972617, 121.00321051212347))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }







            else {
                mMarkerPoints.clear();
            }
        }

    }








    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }




    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                List<PatternItem> pattern = null;
                pattern = PATTERN_POLYGON_ALPHA;
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                lineOptions.color(Color.CYAN);
                lineOptions.pattern(pattern);

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);


            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }







    private void askPermission() {
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_LONG).show();
                getCurrentlocation();
            }

            else {
                Toast.makeText(this, "REQUIRED PERMISSION", Toast.LENGTH_SHORT).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {

    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }


}
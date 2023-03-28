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


                if (location.equalsIgnoreCase("school")){
                    LatLng loc = new LatLng(13.792659, 121.002470);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

                } else if (location.equalsIgnoreCase("evacuation area - a")) {
                    LatLng loc = new LatLng(13.792288, 121.003093);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }
                else{
                    Toast.makeText(getApplicationContext(),"No result please try again", Toast.LENGTH_LONG).show();
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
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();

            //admin bldg - field
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7929594,121.00234),
                                new LatLng(13.7928543,121.0021602),
                                new LatLng(13.792933, 121.002053))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();

                //admin bldg - shs building b
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7930222,121.0023341),
                                new LatLng(13.7930876,121.0025171),
                                new LatLng(13.7935735,121.0024519))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();

                //admin bldg - poolside
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7930222,121.0023341),
                                new LatLng(13.7930876,121.0025171),
                                new LatLng(13.793341, 121.002542))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();


                //admin bldg - shs classrooms
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7930222,121.0023341),
                                new LatLng(13.7930876,121.0025171),
                                new LatLng(13.7932733,121.0023886))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));

                mMarkerPoints.clear();


                //admin bldg - beauty care
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7930222,121.0023341),
                                new LatLng(13.7930876,121.0025171))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - cookery
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7930222,121.0023341),
                                new LatLng(13.7930876,121.0025171),
                                new LatLng(13.7931182,121.0026665))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - canteen
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7926653,121.0023703),
                                new LatLng(13.7927289,121.0026057))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - tvl classroom
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7926653,121.0023703),
                                new LatLng(13.7926226,121.0026481))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - guidance
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7926653,121.0023703),
                                new LatLng(13.7927234,121.0025613),
                                new LatLng(13.7928348,121.0025863))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - h.e room
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.7926653,121.0023703),
                                new LatLng(13.7927234,121.0025613),
                                new LatLng(13.7928348,121.0025863))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - shs building a
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.79264012781666, 121.0023789848412),
                                new LatLng(13.7925995,121.0020713))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - gym
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792411,121.002414),
                                new LatLng(13.79227004348421, 121.00232621646784),
                                new LatLng(13.792285336671819, 121.00225031661394))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - electric
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.792075,121.0027812),
                                new LatLng(13.7919033,121.0027801))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - g8 classrooms
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.7919995,121.0025903),
                                new LatLng(13.791901,121.0023677))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - g10 building a
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.7920935,121.0024609),
                                new LatLng(13.7920698,121.002338))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - science park
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.7921895,121.0026105),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - stve
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.7920773,121.0028928),
                                new LatLng(13.79221,121.0036171),
                                new LatLng(13.7922672,121.0037836))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - main bulding
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.7920773,121.0028928),
                                new LatLng(13.7921469,121.0031955),
                                new LatLng(13.7924435,121.0032114))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - gate
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.792444,121.0024047),
                                new LatLng(13.7933741,121.0022825),
                                new LatLng(13.7933453,121.0017203))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

                //admin bldg - main gate
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7921697,121.0024995),
                                new LatLng(13.7920773,121.0028928),
                                new LatLng(13.79221,121.0036171),
                                new LatLng(13.7922539,121.0038847),
                                new LatLng(13.79221781663594, 121.003935301053))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();


                //admin bldg - g10 building b
            } else if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 1) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792353,121.0024724),
                                new LatLng(13.7924935,121.0024029),
                                new LatLng(13.7924609,121.0022389),
                                new LatLng(13.7924154,121.0018621),
                                new LatLng(13.7920825,121.0018384),
                                new LatLng(13.7919818,121.0017186))
                        .color(Color.BLUE)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();




                //rizal park - field
            } else if (mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 2) {

                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792288, 121.003093),
                                new LatLng(13.792422, 121.002807),
                                new LatLng(13.7924555, 121.0025448),
                                new LatLng(13.792812, 121.002367),
                                new LatLng(13.792991, 121.002333),
                                new LatLng(13.792886, 121.002179),
                                new LatLng(13.792933, 121.002053))
                                .color(Color.BLUE)
                                .width(20)
                                .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();

            }
            else {

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
                lineOptions.color(Color.BLUE);
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
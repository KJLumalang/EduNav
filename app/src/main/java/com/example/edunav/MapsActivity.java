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
import com.google.android.gms.maps.model.PolygonOptions;
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

    List<Polygon> polygons = new ArrayList<Polygon>();

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

                else if (location.equalsIgnoreCase("stve classrooms")||location.equalsIgnoreCase("stve")){
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

                else if (location.equalsIgnoreCase("electricity room")){
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

                else if (location.equalsIgnoreCase("guard")||location.equalsIgnoreCase("guard house")){
                    LatLng loc = new LatLng(13.792283599003385, 121.00394303301627);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Guard House", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("cr")||location.equalsIgnoreCase("toilet")){
                    LatLng loc = new LatLng(13.791826362467983, 121.00195866536703);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to CR", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("immersion room")){
                    LatLng loc = new LatLng(13.792819660413048, 121.00237291632888);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Immersion Room", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("jhs clinic")){
                    LatLng loc = new LatLng(13.791980009565064, 121.00284380215881);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to JHS Clinic", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("shs clinic")){
                    LatLng loc = new LatLng(13.792724993937938, 121.00236887996458);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to SHS Clinic", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("welding rooms")||location.equalsIgnoreCase("welding room")){
                    LatLng loc = new LatLng(13.792401819503414, 121.00359582828865);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Welding Rooms", Toast.LENGTH_LONG).show();
                }

                else if (location.equalsIgnoreCase("dump site")){
                    LatLng loc = new LatLng(13.79354610932843, 121.00225956481562);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                    Toast.makeText(getApplicationContext(),"Moved to Dump Site", Toast.LENGTH_LONG).show();
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
        Intent about_us = new Intent(MapsActivity.this, about_us.class);
        startActivity(about_us);
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
                .snippet("Principal’s Office\n" +
                        "AVR\n" +
                        "JHS Faculty\n" +
                        " SHS and JHS Registrar")
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
                .snippet("STEM Rooms\n" +
                        "SHS Faculty\n" +
                        "Research Center\n" +
                        "SHS Focal Person Room")
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
                .snippet("Grade 7, 8 and 9")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));

        Marker mkr23 = mMap.addMarker(main_M);
        markers.put(mkr23.getId(), 23);

        //guard house
        LatLng guard = new LatLng(13.792283599003385, 121.00394303301627);
        MarkerOptions guard_M = new MarkerOptions()
                .position(guard)
                .title("Guard House")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("guardhouse",100,100)));
        Marker mkr24 = mMap.addMarker(guard_M);
        markers.put(mkr24.getId(), 24);

        //cr1
        LatLng cr = new LatLng(13.791822035874409, 121.00196433254081);
        MarkerOptions cr_M = new MarkerOptions()
                .position(cr)
                .title("CR")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("cr",100,100)));
        Marker mkr25 = mMap.addMarker(cr_M);
        markers.put(mkr25.getId(), 25);

        //cr2
        LatLng cr2 = new LatLng(13.792537359716832, 121.00179665292717);
        MarkerOptions cr2_M = new MarkerOptions()
                .position(cr2)
                .title("CR")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("cr",100,100)));
        Marker mkr26 = mMap.addMarker(cr2_M);
        markers.put(mkr26.getId(), 26);

        //Under Construction
        LatLng construction = new LatLng(13.792723033440355, 121.00167142914209);
        MarkerOptions construction_M = new MarkerOptions()
                .position(construction)
                .title("Under Construction")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("warning",100,100)));
        Marker mkr27 = mMap.addMarker(construction_M);
        markers.put(mkr27.getId(), 27);

        //jhs clinic
        LatLng jhsclinic = new LatLng(13.791980009565064, 121.00284380215881);
        MarkerOptions jhsclinic_M = new MarkerOptions()
                .position(jhsclinic)
                .title("JHS Clinic")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("clinic",100,100)));
        Marker mkr28 = mMap.addMarker(jhsclinic_M);
        markers.put(mkr28.getId(), 28);

        //shs clinic
        LatLng shsclinic = new LatLng(13.79272234739986, 121.00240767287328);
        MarkerOptions shsclinic_M = new MarkerOptions()
                .position(shsclinic)
                .title("SHS Clinic")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("clinic",100,100)));
        Marker mkr29 = mMap.addMarker(shsclinic_M);
        markers.put(mkr29.getId(), 30);

        //immersion room
        LatLng immersion = new LatLng(13.792818481678143, 121.00239288126149);
        MarkerOptions immersion_M = new MarkerOptions()
                .position(immersion)
                .title("Immersion Room")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("classroom",100,100)));
        Marker mkr30 = mMap.addMarker(immersion_M);
        markers.put(mkr30.getId(), 29);

        //welding room
        LatLng weld = new LatLng(13.792401819503414, 121.00359582828865);
        MarkerOptions weld_M = new MarkerOptions()
                .position(weld)
                .title("Welding Rooms")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("welder",100,100)));
        Marker mkr31 = mMap.addMarker(weld_M);
        markers.put(mkr31.getId(), 31);

        //dump site
        LatLng dump = new LatLng(13.79354610932843, 121.00225956481562);
        MarkerOptions dump_M = new MarkerOptions()
                .position(dump)
                .title("Dump Site")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("dump",100,100)));
        Marker mkr32 = mMap.addMarker(dump_M);
        markers.put(mkr32.getId(), 32);



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


        for(Polygon poly : polygons)
        {
            poly.remove();
        }
        polygons.clear();


        if(id == 22){

            polygons.add(this.mMap.addPolygon(new PolygonOptions()
                    .add(
                            new LatLng(13.791975450769938, 121.00249050473879),
                            new LatLng(13.791986548459562, 121.00277127187307),
                            new LatLng(13.791886000232603, 121.00277073166302),
                            new LatLng(13.791886041957477, 121.00250037694592)
                    )
                    .strokeColor(Color.GREEN)

            ));


            polygons.add(this.mMap.addPolygon(new PolygonOptions()
                    .add(
                            new LatLng(13.792013050476305, 121.00200087706307),
                            new LatLng(13.792029757921291, 121.00216933345934),
                            new LatLng(13.791937901426598, 121.0021793426512),
                            new LatLng(13.791925155631747, 121.00201206779901)
                    )
                    .strokeColor(Color.GREEN)

            ));

            polygons.add(this.mMap.addPolygon(new PolygonOptions()
                    .add(
                            new LatLng(13.791874460524664, 121.00267084155406),
                            new LatLng(13.79178036453216, 121.00266631054123),
                            new LatLng(13.791796331616906, 121.00198957477593),
                            new LatLng(13.791891600001101, 121.00199170192603)
                    )
                    .strokeColor(Color.GREEN)

            ));

            polygons.add(this.mMap.addPolygon(new PolygonOptions()
                    .add(
                            new LatLng(13.791829998192497, 121.00183691295847),
                            new LatLng(13.79184070945756, 121.0019602588699),
                            new LatLng(13.792048721271977, 121.00194408556735),
                            new LatLng(13.792031803189992, 121.00181208540079)
                    )
                    .strokeColor(Color.GREEN)

            ));
        }


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
               getPath2();
            }
        }

    }

    private void getPath2(){

        //guard house - all
        //guard - admin
            if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791904763881496, 121.00224960407513),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792379257835293, 121.00248915528495))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }


        //guard - rizal
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792136523449873, 121.00319574760053),
                            new LatLng(13.792288, 121.003093))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - field
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792981518655639, 121.00231498032328),
                            new LatLng(13.792842789198291, 121.0021775974826),
                            new LatLng(13.792933, 121.002053))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - poolside
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79302822563518, 121.00233544339473),
                            new LatLng(13.7930750423031, 121.00250682196061),
                            new LatLng(13.793341, 121.002542))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - shs building b
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79302822563518, 121.00233544339473),
                            new LatLng(13.7930750423031, 121.00250682196061),
                            new LatLng(13.7935735,121.0024519))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - shs classrooms
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79302822563518, 121.00233544339473),
                            new LatLng(13.7930750423031, 121.00250682196061),
                            new LatLng(13.7932733,121.0023886))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - beauty care
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79302822563518, 121.00233544339473),
                            new LatLng(13.7930750423031, 121.00250682196061))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - cookery
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79302822563518, 121.00233544339473),
                            new LatLng(13.7930750423031, 121.00250682196061),
                            new LatLng(13.793114204167907, 121.00265744927867))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - guidance
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792639241808955, 121.00237504132983),
                            new LatLng(13.792669216258103, 121.00252207577726),
                            new LatLng(13.7929052,121.0025095))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - he
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792639241808955, 121.00237504132983),
                            new LatLng(13.792669216258103, 121.00252207577726),
                            new LatLng(13.7928834,121.0026764))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - canteen
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792639241808955, 121.00237504132983),
                            new LatLng(13.792669216258103, 121.00252207577726),
                            new LatLng(13.7927337,121.0026818))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - tvl
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792639241808955, 121.00237504132983),
                            new LatLng(13.792669216258103, 121.00252207577726),
                            new LatLng(13.79266984583216, 121.00269964170724))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - science
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.7922604,121.0026845))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - stve
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792249581750694, 121.00371166659855))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - main gate
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.7922103,121.0039427))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - gate
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79340243338584, 121.00226816146396),
                            new LatLng(13.793357311164717, 121.00170659756525))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - shs building a
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.79262918171325, 121.00238033579183),
                            new LatLng(13.792588580997746, 121.00209124359793))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - gym
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792330023598632, 121.00241165136683),
                            new LatLng(13.792243872556014, 121.0022513705977))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - grade 10 building a
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792154966550724, 121.00250573152543),
                            new LatLng(13.792060379958857, 121.00234087498825))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - electricity
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.791890073752459, 121.00277770968121))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - grade 10 building b
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792309837533391, 121.00241223912477),
                            new LatLng(13.792467513521661, 121.00238317716787),
                            new LatLng(13.792421962993716, 121.0018618535133),
                            new LatLng(13.792096327594209, 121.00185674482455),
                            new LatLng(13.791917,121.001679))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - g8 classrooms
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792154966550724, 121.00250573152543),
                            new LatLng(13.79198544840255, 121.00253661411408),
                            new LatLng(13.791942017380896, 121.00247621352663),
                            new LatLng(13.791910048963489, 121.0022273583981))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - main building
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792169276757537, 121.0033053372781),
                            new LatLng(13.792456224290751, 121.00321876707098))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - cr
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.792154966550724, 121.00250573152543),
                            new LatLng(13.792060379958857, 121.00234087498825),
                            new LatLng(13.792026487008409, 121.00195624124383),
                            new LatLng(13.791824430412545, 121.00196588335413))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //guard - cr2
        else if (mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 24) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792283599003385, 121.00394303301627),
                            new LatLng(13.792247463055196, 121.00382443157419),
                            new LatLng(13.792074138630426, 121.00277843862366),
                            new LatLng(13.792157266385741, 121.00252879432715),
                            new LatLng(13.7924141420695, 121.00238678149893),
                            new LatLng(13.79261801978812, 121.00237036756465),
                            new LatLng(13.792570233057358, 121.0018246942881))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }




        //start of cr to all

        //cr - admin
            else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792309722630728, 121.00243877695777),
                            new LatLng(13.792368853884465, 121.00248566842612))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - rizal
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792071763949501, 121.00282569607285),
                            new LatLng(13.792138869992174, 121.00319068958359),
                            new LatLng(13.792288, 121.003093))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - field
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792391642242334, 121.00241065946369),
                            new LatLng(13.792991299806886, 121.00232866954916),
                            new LatLng(13.792897998860468, 121.00215882522082),
                            new LatLng(13.792933, 121.002053))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - poolside
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792391642242334, 121.00241065946369),
                            new LatLng(13.793033016444934, 121.00232385047143),
                            new LatLng(13.793078052229243, 121.00251058791791),
                            new LatLng(13.793341, 121.002542))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - shs building b
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792391642242334, 121.00241065946369),
                            new LatLng(13.793033016444934, 121.00232385047143),
                            new LatLng(13.793078052229243, 121.00251058791791),
                            new LatLng(13.7935735,121.0024519))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - shs classrooms
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792391642242334, 121.00241065946369),
                            new LatLng(13.793033016444934, 121.00232385047143),
                            new LatLng(13.793078052229243, 121.00251058791791),
                            new LatLng(13.7932733,121.0023886))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - beauty
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792391642242334, 121.00241065946369),
                            new LatLng(13.793033016444934, 121.00232385047143),
                            new LatLng(13.793078052229243, 121.00251058791791))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - cookery
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792391642242334, 121.00241065946369),
                            new LatLng(13.793033016444934, 121.00232385047143),
                            new LatLng(13.793078052229243, 121.00251058791791),
                            new LatLng(13.79311861290168, 121.0026577100484))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - guidance
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.79262760874268, 121.00237449244699),
                            new LatLng(13.792652827614617, 121.0025208187897),
                            new LatLng(13.7929052,121.0025095))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - he
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.79262760874268, 121.00237449244699),
                            new LatLng(13.792652827614617, 121.0025208187897),
                            new LatLng(13.792852453617309, 121.00260148644388))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - canteen
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.79262760874268, 121.00237449244699),
                            new LatLng(13.792652827614617, 121.0025208187897),
                            new LatLng(13.792735767769708, 121.00260828471592))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - tvl
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.79262760874268, 121.00237449244699),
                            new LatLng(13.792652827614617, 121.0025208187897),
                            new LatLng(13.792668511039897, 121.00269101145872))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - science
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.7922604,121.0026845))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - stve
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792097604221054, 121.00264127391428),
                            new LatLng(13.792130853673617, 121.0032075841258),
                            new LatLng(13.792255280692675, 121.00372334903068))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - main gate
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792097604221054, 121.00264127391428),
                            new LatLng(13.792130853673617, 121.0032075841258),
                            new LatLng(13.792244236522112, 121.00394027309858))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - gate
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792387072447903, 121.00240662670305),
                            new LatLng(13.79339367278069, 121.00228108243424),
                            new LatLng(13.793358759294792, 121.00170951483234))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - shs building a
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792387072447903, 121.00240662670305),
                            new LatLng(13.792611662371923, 121.00238227247777),
                            new LatLng(13.792586682655815, 121.00210539669884))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - gym
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792055094079567, 121.00210540057229))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - grade 10 building a
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792069080261742, 121.00233791986423),
                            new LatLng(13.79216645548774, 121.00250644616968))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - electricity
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.79207318310827, 121.00277808825948),
                            new LatLng(13.791897666808886, 121.0027822203923))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - grade 10 building b
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792037930618473, 121.00195618676744),
                            new LatLng(13.792078450840469, 121.0018515537301),
                            new LatLng(13.791917,121.001679))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - grade 10 building b
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.79190997682534, 121.00196763455955),
                            new LatLng(13.79190500214567, 121.00225932000923))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - main building
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792028926280711, 121.00196498935114),
                            new LatLng(13.792091166710373, 121.00246023892282),
                            new LatLng(13.79216645548774, 121.00250644616968),
                            new LatLng(13.792097604221054, 121.00264127391428),
                            new LatLng(13.792130853673617, 121.0032075841258),
                            new LatLng(13.792452015233316, 121.00321047343101))


                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr - cr2
        else if (mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 25) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.791822035874409, 121.00196433254081),
                            new LatLng(13.792032572923013, 121.00196135044114),
                            new LatLng(13.792073840151138, 121.00188782056159),
                            new LatLng(13.792523520033612, 121.00184119989419))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //start of cr2 to all

        //cr2 - admin
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792330650832186, 121.00242808792213),
                            new LatLng(13.792377992406626, 121.00248551815443))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - rizal
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792330650832186, 121.00242808792213),
                            new LatLng(13.792090476625187, 121.00265894562169),
                            new LatLng(13.792136169101337, 121.00319720296406),
                            new LatLng(13.792288, 121.003093))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - field
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.792933, 121.002053))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - poolside
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.79302611462101, 121.00232731761623),
                            new LatLng(13.79307416028177, 121.00251237699908),
                            new LatLng(13.793341, 121.002542))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - shs building b
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.79302611462101, 121.00232731761623),
                            new LatLng(13.79307416028177, 121.00251237699908),
                            new LatLng(13.7935735,121.0024519))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - shs classrooms
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.79302611462101, 121.00232731761623),
                            new LatLng(13.79307416028177, 121.00251237699908),
                            new LatLng(13.79328436238715, 121.00241525067261))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - beauty
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.79302611462101, 121.00232731761623),
                            new LatLng(13.79307416028177, 121.00251237699908))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }


        //cr2 - cookery
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.79302611462101, 121.00232731761623),
                            new LatLng(13.79307416028177, 121.00251237699908),
                            new LatLng(13.793118630239254, 121.0026521354381))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - cookery
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792664144749953, 121.00252070493234),
                            new LatLng(13.7929052,121.0025095))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - he
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792664144749953, 121.00252070493234),
                            new LatLng(13.792873932933292, 121.00260208917595))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - canteen
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792664144749953, 121.00252070493234),
                            new LatLng(13.792730534963253, 121.00261153166299))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - tvl
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792664144749953, 121.00252070493234),
                            new LatLng(13.792657171916654, 121.00268136549434))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }


        //cr2 - science
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792172593429923, 121.00249520522496),
                            new LatLng(13.7922604,121.0026845))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - stve
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792172593429923, 121.00249520522496),
                            new LatLng(13.792067271810907, 121.00276328218341),
                            new LatLng(13.792251154073396, 121.00374165895894))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - main gate
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792172593429923, 121.00249520522496),
                            new LatLng(13.792067271810907, 121.00276328218341),
                            new LatLng(13.792251154073396, 121.00374165895894),
                            new LatLng(13.792229839636304, 121.00394490549495))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - gate
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.793373098378233, 121.00171846063996))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }


        //cr2 - shs building a
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.792594895580988, 121.00210514678767))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - gym
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792488127669326, 121.00185465863919),
                            new LatLng(13.792186961987372, 121.0019032510352))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - grade 10 building a
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792488127669326, 121.00185465863919),
                            new LatLng(13.792186961987372, 121.0019032510352),
                            new LatLng(13.792053863051697, 121.00191465496648),
                            new LatLng(13.79202497975975, 121.00196447370477),
                            new LatLng(13.792061998057289, 121.00232193799673))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - electricity
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792172593429923, 121.00249520522496),
                            new LatLng(13.792067271810907, 121.00276328218341),
                            new LatLng(13.791896587797298, 121.00277501462517))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - grade 10 building b
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792488127669326, 121.00185465863919),
                            new LatLng(13.792186961987372, 121.0019032510352),
                            new LatLng(13.791917,121.001679))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - g8 classrooms
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792488127669326, 121.00185465863919),
                            new LatLng(13.792186961987372, 121.0019032510352),
                            new LatLng(13.792053863051697, 121.00191465496648),
                            new LatLng(13.79202497975975, 121.00196447370477),
                            new LatLng(13.791906691648892, 121.00198568917227),
                            new LatLng(13.791906730810181, 121.00224424825406))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //cr2 - main building
        else if (mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 26) {
            polylines.add(this.mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(13.792537359716832, 121.00179665292717),
                            new LatLng(13.792578625234137, 121.00185708145972),
                            new LatLng(13.79264411644884, 121.00239150864043),
                            new LatLng(13.792330650832186, 121.00242808792213),
                            new LatLng(13.792090476625187, 121.00265894562169),
                            new LatLng(13.792136169101337, 121.00319720296406),
                            new LatLng(13.7925991,121.0031756))

                    .color(Color.CYAN)
                    .width(20)
                    .pattern(PATTERN_POLYGON_ALPHA)));
            mMarkerPoints.clear();
        }

        //start of construction to all
        // construction - admin
        else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 27) {
        polylines.add(this.mMap.addPolyline(new PolylineOptions()
        .clickable(true)
        .add(
             new LatLng(13.79274875594662, 121.00177790052591),
             new LatLng(13.792713916321743, 121.00201611612972),
             new LatLng(13.792991649928616, 121.00233182988521),
             new LatLng(13.792322200813723, 121.00243500258402),
             new LatLng(13.792367818714974, 121.00248577361617))

        .color(Color.CYAN)
        .width(20)
        .pattern(PATTERN_POLYGON_ALPHA)));
        mMarkerPoints.clear();
        }

            // construction - rizal park
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.792991649928616, 121.00233182988521),
                                new LatLng(13.792322200813723, 121.00243500258402),
                                new LatLng(13.792169322463659, 121.00251336383363),
                                new LatLng(13.792054844680516, 121.00279930238214),
                                new LatLng(13.792144655290688, 121.00320044461064),
                                new LatLng(13.792288, 121.003093))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - field
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.792933, 121.002053))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - poolside
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.793075495898586, 121.00251765054146),
                                new LatLng(13.793343421802662, 121.00254750851754))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - shs building b
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.793075495898586, 121.00251765054146),
                                new LatLng(13.793503823588937, 121.00243964708223))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - shs classrooms
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.793075495898586, 121.00251765054146),
                                new LatLng(13.793281156115771, 121.00242779572427))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - beauty care
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.793075495898586, 121.00251765054146))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - cookery
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.793075495898586, 121.00251765054146),
                                new LatLng(13.793121358221414, 121.00265573373656))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - guidance
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792634120516258, 121.00239088902191),
                                new LatLng(13.792657043947349, 121.00252024478591),
                                new LatLng(13.792828190829233, 121.00251906664575))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - he
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792634120516258, 121.00239088902191),
                                new LatLng(13.792657043947349, 121.00252024478591),
                                new LatLng(13.792854622911111, 121.00260373844476))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - canteen
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792634120516258, 121.00239088902191),
                                new LatLng(13.792657043947349, 121.00252024478591),
                                new LatLng(13.792730324810043, 121.00260751514))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - tvl
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792634120516258, 121.00239088902191),
                                new LatLng(13.792657043947349, 121.00252024478591),
                                new LatLng(13.792665438252012, 121.00268299475132))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - science
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792313044897396, 121.00242542404993),
                                new LatLng(13.792139781722508, 121.00256652017444),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - stve
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792313044897396, 121.00242542404993),
                                new LatLng(13.792139781722508, 121.00256652017444),
                                new LatLng(13.792081257943774, 121.00289960471297),
                                new LatLng(13.792242722945002, 121.00373758086866))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - main gate
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792313044897396, 121.00242542404993),
                                new LatLng(13.792139781722508, 121.00256652017444),
                                new LatLng(13.792081257943774, 121.00289960471297),
                                new LatLng(13.792247648687413, 121.00393584362531))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - gate
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.793355470738994, 121.00173071523764))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - shs building a
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.792586408535971, 121.00205356281114))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - gym
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792575335740459, 121.00184177953409),
                                new LatLng(13.792185186850315, 121.00190609072449))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - grade 10 building a
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792575335740459, 121.00184177953409),
                                new LatLng(13.792057354498002, 121.00191159288532),
                                new LatLng(13.792022602532276, 121.00196168884871),
                                new LatLng(13.792061834177861, 121.00231291554906))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - electricity
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792313044897396, 121.00242542404993),
                                new LatLng(13.792139781722508, 121.00256652017444),
                                new LatLng(13.792065424073673, 121.00277876642737),
                                new LatLng(13.791943939883337, 121.00277888892056))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - grade 10 building b
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792575335740459, 121.00184177953409),
                                new LatLng(13.792185186850315, 121.00190609072449),
                                new LatLng(13.791981096746213, 121.00172015293106))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792575335740459, 121.00184177953409),
                                new LatLng(13.792056957720884, 121.00191568570038),
                                new LatLng(13.792020659385424, 121.001970624074),
                                new LatLng(13.791905081887817, 121.00199102358656),
                                new LatLng(13.791905079162579, 121.00225953159278))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - main building
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.792991649928616, 121.00233182988521),
                                new LatLng(13.792322200813723, 121.00243500258402),
                                new LatLng(13.792169322463659, 121.00251336383363),
                                new LatLng(13.792054844680516, 121.00279930238214),
                                new LatLng(13.792144655290688, 121.00320044461064),
                                new LatLng(13.79244299602192, 121.00318425132562))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - guard house
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 24 || mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792313044897396, 121.00242542404993),
                                new LatLng(13.792139781722508, 121.00256652017444),
                                new LatLng(13.792081257943774, 121.00289960471297),
                                new LatLng(13.792247648687413, 121.00393584362531))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - cr1
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792580121134764, 121.00183917208355),
                                new LatLng(13.79206048823797, 121.0019057788846),
                                new LatLng(13.792025134078477, 121.00197489316217),
                                new LatLng(13.791854239925835, 121.00197094849239))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - cr2
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792591625650177, 121.00180069086213))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - jhs clinic
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 28 || mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792313044897396, 121.00242542404993),
                                new LatLng(13.792139781722508, 121.00256652017444),
                                new LatLng(13.792065424073673, 121.00277876642737),
                                new LatLng(13.791943939883337, 121.00277888892056))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - immersion room
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 29 || mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792909573878463, 121.00235413975419))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            // construction - shs clinic
            else if (mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 30 || mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 27) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79274875594662, 121.00177790052591),
                                new LatLng(13.792713916321743, 121.00201611612972),
                                new LatLng(13.79301745551901, 121.00233737880308),
                                new LatLng(13.792909573878463, 121.00235413975419))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //jhs clinic to all
            //jhs clinic - admin
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792146805928667, 121.0025450992897),
                                new LatLng(13.792310474618034, 121.00244007672481),
                                new LatLng(13.792368392531786, 121.0024872165948))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - rizal
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792137674066304, 121.00320070553883),
                                new LatLng(13.792288, 121.003093))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - field
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792988832668966, 121.0023306169094),
                                new LatLng(13.792879732030492, 121.0021635338552),
                                new LatLng(13.792933, 121.002053))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - poolside
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.793025320630464, 121.00233827000994),
                                new LatLng(13.793071290543216, 121.00251058148031),
                                new LatLng(13.793341, 121.002542))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - shs building b
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.793025320630464, 121.00233827000994),
                                new LatLng(13.793071290543216, 121.00251058148031),
                                new LatLng(13.793504385373447, 121.00245167857265))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - shs classrooms
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.793025320630464, 121.00233827000994),
                                new LatLng(13.793071290543216, 121.00251058148031),
                                new LatLng(13.793291497074708, 121.00242751113277))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - shs classrooms
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.793025320630464, 121.00233827000994),
                                new LatLng(13.793071290543216, 121.00251058148031))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - cookery
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.793025320630464, 121.00233827000994),
                                new LatLng(13.793071290543216, 121.00251058148031),
                                new LatLng(13.793121286328315, 121.00265696728727))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - guidance
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792638978227332, 121.00237937190161),
                                new LatLng(13.792669656869109, 121.00253604373482),
                                new LatLng(13.792836765382399, 121.00252979629063))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - he
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792638978227332, 121.00237937190161),
                                new LatLng(13.792669656869109, 121.00253604373482),
                                new LatLng(13.792854184993708, 121.00259894314266))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //jhs clinic - canteen
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792638978227332, 121.00237937190161),
                                new LatLng(13.792669656869109, 121.00253604373482),
                                new LatLng(13.792726264747003, 121.0026110192522))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - tvl
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792638978227332, 121.00237937190161),
                                new LatLng(13.792669656869109, 121.00253604373482),
                                new LatLng(13.792663167063811, 121.00268965793909))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - tvl
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - stve
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.7922414140219, 121.00373687981575))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - main gate
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792246504120017, 121.00393065466808))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - gate
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.79339442892875, 121.0022708315853),
                                new LatLng(13.793355812553672, 121.00172320716915))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - shs building a
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792629141288202, 121.0023949452066),
                                new LatLng(13.79259160964496, 121.00208702598404))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - gym
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.792355807984087, 121.00242116906219),
                                new LatLng(13.792256894326188, 121.00225685481047))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - grade 10 building a
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792143586934591, 121.00252491192137),
                                new LatLng(13.79206458064608, 121.00233125977664))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - electricity
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792064836949283, 121.00276916190916),
                                new LatLng(13.791901412453344, 121.00277844342844))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }



            //jhs clinic - grade 10 building b
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
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


            //jhs clinic - g8 classrooms
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792156782002294, 121.00252051850732),
                                new LatLng(13.7919947140872, 121.0025433388073),
                                new LatLng(13.791953478990978, 121.00248048530432),
                                new LatLng(13.791902274607216, 121.00223034973243))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - main building
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792076360486092, 121.0027779686278),
                                new LatLng(13.79214549251284, 121.0033039072567),
                                new LatLng(13.792450117786354, 121.00321655810994))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - guard house
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 24 || mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792255436434347, 121.0039344363963))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - cr1
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792154132700958, 121.00252165991026),
                                new LatLng(13.792083278101034, 121.00244122826426),
                                new LatLng(13.792018892927931, 121.00195820410768),
                                new LatLng(13.791849781205526, 121.00196774278439))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - cr2
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792154132700958, 121.00252165991026),
                                new LatLng(13.792317963446209, 121.00242259653491),
                                new LatLng(13.792627013156919, 121.00238455390873),
                                new LatLng(13.792575617258832, 121.0018315200444))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - immersion room
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 29 || mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792154132700958, 121.00252165991026),
                                new LatLng(13.792317963446209, 121.00242259653491),
                                new LatLng(13.792627013156919, 121.00238455390873),
                                new LatLng(13.792927991227613, 121.00236012876617))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //jhs clinic - shs clinic
            else if (mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 30 || mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 28) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792058032088015, 121.00282641076649),
                                new LatLng(13.792154132700958, 121.00252165991026),
                                new LatLng(13.792317963446209, 121.00242259653491),
                                new LatLng(13.792627013156919, 121.00238455390873),
                                new LatLng(13.792927991227613, 121.00236012876617))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //start of immersion to all
            //immersion - admin
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792338461906338, 121.00243221019437),
                                new LatLng(13.792371126212407, 121.00247939731992))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - rizal park
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792338461906338, 121.00243221019437),
                                new LatLng(13.792153306018376, 121.00251656806857),
                                new LatLng(13.792067142678016, 121.00277424587381),
                                new LatLng(13.792138558155873, 121.0031955490362),
                                new LatLng(13.792288, 121.003093))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - field
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792999424370205, 121.00233100956534),
                                new LatLng(13.79287982338916, 121.00218681513658),
                                new LatLng(13.792933, 121.002053))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - poolside
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.793341, 121.002542))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - shs building b
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.7935735,121.0024519))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - shs classrooms
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.793276998103224, 121.00242373865257))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - beauty
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - cookery
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.79312491529873, 121.00265352634382))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - guidance
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792831891479306, 121.00251706593285))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - he
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792863324238763, 121.00260267005508))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - canteen
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792729756739277, 121.00260981591808))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - tvl
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792665205802356, 121.00268940022536))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - science park
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - stve
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7920697698929, 121.00278035783042),
                                new LatLng(13.792237284004043, 121.0037224917242))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - main gate
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7920697698929, 121.00278035783042),
                                new LatLng(13.79225209154315, 121.0039407172401))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - gate
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.793387260243236, 121.00228940934326),
                                new LatLng(13.793349262855983, 121.00172330678409))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - shs building a
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792593573062328, 121.00208464886649))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - gym
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792253843954832, 121.00225454955081))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - grade 10 building a
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.79206439189002, 121.00236464486727))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - electricity
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.792061867758676, 121.0027765006169),
                                new LatLng(13.791902034294612, 121.00277861516335))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - grade 10 building b
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.7924757639145, 121.00240082394261),
                                new LatLng(13.792425855932539, 121.00186878607778),
                                new LatLng(13.79208578718721, 121.00188917777294),
                                new LatLng(13.791982952490027, 121.00173708273942))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.792094202503879, 121.00249874886367),
                                new LatLng(13.791992244221467, 121.00255080882887),
                                new LatLng(13.791952435510037, 121.0024871679683),
                                new LatLng(13.791908472140035, 121.00224377690513))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - main
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792338461906338, 121.00243221019437),
                                new LatLng(13.792153306018376, 121.00251656806857),
                                new LatLng(13.792067142678016, 121.00277424587381),
                                new LatLng(13.792138558155873, 121.0031955490362),
                                new LatLng(13.79245287866572, 121.00321164731994))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - guard
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 24 || mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7920697698929, 121.00278035783042),
                                new LatLng(13.79225209154315, 121.0039407172401))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - cr1
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.792094202503879, 121.00249874886367),
                                new LatLng(13.792030049863174, 121.00196116701063),
                                new LatLng(13.791862374275107, 121.00197396192623))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //immersion - cr2
            else if (mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 29) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79282408408828, 121.00236705400452),
                                new LatLng(13.792614572757326, 121.00239411613636),
                                new LatLng(13.792566431586076, 121.00182772267809))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


// start of shs clinic to all
//shs clinic - admin
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792338461906338, 121.00243221019437),
                                new LatLng(13.792371126212407, 121.00247939731992))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - rizal park
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792338461906338, 121.00243221019437),
                                new LatLng(13.792153306018376, 121.00251656806857),
                                new LatLng(13.792067142678016, 121.00277424587381),
                                new LatLng(13.792138558155873, 121.0031955490362),
                                new LatLng(13.792288, 121.003093))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - field
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792999424370205, 121.00233100956534),
                                new LatLng(13.79287982338916, 121.00218681513658),
                                new LatLng(13.792933, 121.002053))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - poolside
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.793341, 121.002542))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - shs building b
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.7935735,121.0024519))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - shs classrooms
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.793276998103224, 121.00242373865257))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - beauty
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - cookery
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.793025977820491, 121.00233863659054),
                                new LatLng(13.79308292497572, 121.00251272426809),
                                new LatLng(13.79312491529873, 121.00265352634382))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - guidance
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792831891479306, 121.00251706593285))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - he
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792863324238763, 121.00260267005508))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - canteen
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792729756739277, 121.00260981591808))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - tvl
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792636154789662, 121.00239137222577),
                                new LatLng(13.792666140025583, 121.00253270572618),
                                new LatLng(13.792665205802356, 121.00268940022536))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - science park
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - stve
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7920697698929, 121.00278035783042),
                                new LatLng(13.792237284004043, 121.0037224917242))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - main gate
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7920697698929, 121.00278035783042),
                                new LatLng(13.79225209154315, 121.0039407172401))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - gate
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.793387260243236, 121.00228940934326),
                                new LatLng(13.793349262855983, 121.00172330678409))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - shs building a
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792593573062328, 121.00208464886649))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - gym
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792253843954832, 121.00225454955081))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - grade 10 building a
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.79206439189002, 121.00236464486727))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - electricity
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.792061867758676, 121.0027765006169),
                                new LatLng(13.791902034294612, 121.00277861516335))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - grade 10 building b
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.7924757639145, 121.00240082394261),
                                new LatLng(13.792425855932539, 121.00186878607778),
                                new LatLng(13.79208578718721, 121.00188917777294),
                                new LatLng(13.791982952490027, 121.00173708273942))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.792094202503879, 121.00249874886367),
                                new LatLng(13.791992244221467, 121.00255080882887),
                                new LatLng(13.791952435510037, 121.0024871679683),
                                new LatLng(13.791908472140035, 121.00224377690513))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - main
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792338461906338, 121.00243221019437),
                                new LatLng(13.792153306018376, 121.00251656806857),
                                new LatLng(13.792067142678016, 121.00277424587381),
                                new LatLng(13.792138558155873, 121.0031955490362),
                                new LatLng(13.79245287866572, 121.00321164731994))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - guard
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 24 || mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792318117077626, 121.00241643620109),
                                new LatLng(13.792168259512177, 121.00252168796258),
                                new LatLng(13.7920697698929, 121.00278035783042),
                                new LatLng(13.79225209154315, 121.0039407172401))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - cr1
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792620138402217, 121.00238599940748),
                                new LatLng(13.792355993936269, 121.00241491864982),
                                new LatLng(13.792154382912551, 121.00251627150104),
                                new LatLng(13.792094202503879, 121.00249874886367),
                                new LatLng(13.792030049863174, 121.00196116701063),
                                new LatLng(13.791862374275107, 121.00197396192623))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //shs clinic - cr2
            else if (mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 30) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792718321266811, 121.00239150201453),
                                new LatLng(13.792614572757326, 121.00239411613636),
                                new LatLng(13.792566431586076, 121.00182772267809))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //start of welding rooms to all
            //welding room - admin
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792368578816436, 121.00248819985121))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - rizal
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792288, 121.003093))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - field
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792977129320192, 121.00233722297624),
                                new LatLng(13.792872356026894, 121.00216436915746),
                                new LatLng(13.792933, 121.002053))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - poolside
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.7930289994082, 121.00233289968962),
                                new LatLng(13.7930711122439, 121.00251406252222),
                                new LatLng(13.793341, 121.002542))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - shs building b
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.7930289994082, 121.00233289968962),
                                new LatLng(13.7930711122439, 121.00251406252222),
                                new LatLng(13.793505034654576, 121.00242938777333))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - shs classrooms
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.7930289994082, 121.00233289968962),
                                new LatLng(13.7930711122439, 121.00251406252222),
                                new LatLng(13.793254401551371, 121.00243858838148))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - beauty
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.7930289994082, 121.00233289968962),
                                new LatLng(13.7930711122439, 121.00251406252222))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - cookery
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.7930289994082, 121.00233289968962),
                                new LatLng(13.7930711122439, 121.00251406252222),
                                new LatLng(13.793118857465135, 121.00265312133138))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - guidance
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792631798590413, 121.00239883140068),
                                new LatLng(13.792668939844601, 121.00254305055007),
                                new LatLng(13.792832424575101, 121.00253148689872))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - he
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792631798590413, 121.00239883140068),
                                new LatLng(13.792668939844601, 121.00254305055007),
                                new LatLng(13.792843531345273, 121.00260267730656))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - canteen
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792631798590413, 121.00239883140068),
                                new LatLng(13.792668939844601, 121.00254305055007),
                                new LatLng(13.792730085769705, 121.00261168140156))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - tvl
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792631798590413, 121.00239883140068),
                                new LatLng(13.792668939844601, 121.00254305055007),
                                new LatLng(13.792662891726637, 121.00268912559719))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - science park
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792126810452388, 121.00258682205201),
                                new LatLng(13.7922604,121.0026845))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - stve
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.79223882018008, 121.00371405872752))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - main gate
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792243617930012, 121.00393519721186))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - gate
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.793396942879564, 121.00228124685627),
                                new LatLng(13.793357117694521, 121.00172706610981))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - shs building a
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792633161002696, 121.00239454424833),
                                new LatLng(13.792598585819032, 121.00210241142156))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - gym
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.79226148072736, 121.00224978971882))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - grade 10 building a
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.79217059892074, 121.00251849996502),
                                new LatLng(13.792061113465282, 121.00233328392511))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - electricity
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.791894454953711, 121.00278138416957))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - grade 10 building b
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.79248349370679, 121.00240744240612),
                                new LatLng(13.792422106324778, 121.00186772284059),
                                new LatLng(13.792105119223645, 121.00188121238683),
                                new LatLng(13.791977615128221, 121.00172219253281))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.79217059892074, 121.00251849996502),
                                new LatLng(13.791989853729874, 121.00254654496555),
                                new LatLng(13.79195037501239, 121.0024791941146),
                                new LatLng(13.791913732421351, 121.00227862409817))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - main building
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792370426627, 121.00323772291091),
                                new LatLng(13.792448086424228, 121.00322006586521))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - guard house
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 24 || mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792243617930012, 121.00393519721186))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //welding room - cr1
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.79217059892074, 121.00251849996502),
                                new LatLng(13.792061113465282, 121.00233328392511),
                                new LatLng(13.792025202119678, 121.00196379752465),
                                new LatLng(13.791848377849972, 121.00196492440631))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - cr2
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792633161002696, 121.00239454424833),
                                new LatLng(13.79256470795442, 121.00182571357134))



                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - cr2
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 27 || mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792633161002696, 121.00239454424833),
                                new LatLng(13.792631055749144, 121.00210893483127),
                                new LatLng(13.792639565936021, 121.00177671993207))



                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - jhs clinic
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 28 || mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.791971826896555, 121.00277980754652))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //welding room - shs clinic
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 29 || mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.79283087947509, 121.00236965863118))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }


            //welding room - shs clinic
            else if (mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 30 || mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 31) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.792424252019979, 121.00357222791324),
                                new LatLng(13.792404410939664, 121.0034939518925),
                                new LatLng(13.792202447447202, 121.00354066105565),
                                new LatLng(13.792071781788158, 121.0027679270084),
                                new LatLng(13.792151409645228, 121.00253411253844),
                                new LatLng(13.792328396107122, 121.00245099121568),
                                new LatLng(13.792733009860742, 121.00239396170201))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump site to all
            //dump- admin
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 1 || mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792328920559253, 121.00242334837097),
                                new LatLng(13.792371496631928, 121.00248178813618))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - rizal
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792328920559253, 121.00242334837097),
                                new LatLng(13.792196400920382, 121.00248855192666),
                                new LatLng(13.79207164445683, 121.00277184482607),
                                new LatLng(13.792134088856344, 121.00320144289134),
                                new LatLng(13.792288, 121.003093))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - field
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 3 || mMarkerPoints.get(0) == 3 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.793003859940013, 121.00232944670178),
                                new LatLng(13.792885004250799, 121.00216424585908),
                                new LatLng(13.792933, 121.002053))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - poolside
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 4 || mMarkerPoints.get(0) == 4 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79302175203825, 121.00233722991686),
                                new LatLng(13.793085589033995, 121.00250643647483),
                                new LatLng(13.793341, 121.002542))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - shs building b
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 5 || mMarkerPoints.get(0) == 5 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79302175203825, 121.00233722991686),
                                new LatLng(13.793085589033995, 121.00250643647483),
                                new LatLng(13.79349743180145, 121.00242511911887))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - shs classrooms
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 6 || mMarkerPoints.get(0) == 6 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79302175203825, 121.00233722991686),
                                new LatLng(13.793085589033995, 121.00250643647483),
                                new LatLng(13.793272076486828, 121.00242540419362))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - beauty care
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 7 || mMarkerPoints.get(0) == 7 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79302175203825, 121.00233722991686),
                                new LatLng(13.793085589033995, 121.00250643647483))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - cookery
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 8 || mMarkerPoints.get(0) == 8 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79302175203825, 121.00233722991686),
                                new LatLng(13.793085589033995, 121.00250643647483),
                                new LatLng(13.793121696100384, 121.00265192945093))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - guidance
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 9 || mMarkerPoints.get(0) == 9 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79263206000108, 121.00238493510464),
                                new LatLng(13.79266835616661, 121.0025519472306),
                                new LatLng(13.792832523353614, 121.0025247830341))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - he
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 10 || mMarkerPoints.get(0) == 10 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79263206000108, 121.00238493510464),
                                new LatLng(13.79266835616661, 121.0025519472306),
                                new LatLng(13.792853346567059, 121.00260343571324))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - canteen
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 11 || mMarkerPoints.get(0) == 11 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79263206000108, 121.00238493510464),
                                new LatLng(13.79266835616661, 121.0025519472306),
                                new LatLng(13.792732200270352, 121.00260719417409))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - tvl
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 12 || mMarkerPoints.get(0) == 12 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.79263206000108, 121.00238493510464),
                                new LatLng(13.79266835616661, 121.0025519472306),
                                new LatLng(13.792661046807922, 121.00268431552806))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - science park
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 13 || mMarkerPoints.get(0) == 13 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792344466886638, 121.0024209996323),
                                new LatLng(13.79216603817516, 121.00252323506191),
                                new LatLng(13.7922604,121.0026845))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - stve
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 14 || mMarkerPoints.get(0) == 14 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792344466886638, 121.0024209996323),
                                new LatLng(13.79216603817516, 121.00252323506191),
                                new LatLng(13.792070940950097, 121.00277525621324),
                                new LatLng(13.792241589820788, 121.00371317716642))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - main gate
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 15 || mMarkerPoints.get(0) == 15 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792344466886638, 121.0024209996323),
                                new LatLng(13.79216603817516, 121.00252323506191),
                                new LatLng(13.792070940950097, 121.00277525621324),
                                new LatLng(13.792250162776584, 121.00394673802))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - gate
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 16 || mMarkerPoints.get(0) == 16 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.793397842042085, 121.00228366353006),
                                new LatLng(13.793355431742107, 121.00172608163538))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - shs building a
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 17 || mMarkerPoints.get(0) == 17 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792613395696728, 121.00238328013927),
                                new LatLng(13.79259988660366, 121.00210142434634))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - gym
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 18 || mMarkerPoints.get(0) == 18 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792243277887682, 121.0022672351728))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump- grade 10 building a
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 19 || mMarkerPoints.get(0) == 19 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792172534296787, 121.0025056609826),
                                new LatLng(13.792066472110168, 121.00236202626994))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - electricity
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 20 || mMarkerPoints.get(0) == 20 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792172534296787, 121.0025056609826),
                                new LatLng(13.792069751579467, 121.00277730771202),
                                new LatLng(13.791896647497536, 121.00277868445369))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - grade 10 building b
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 21 || mMarkerPoints.get(0) == 21 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792474165364348, 121.00240333340678),
                                new LatLng(13.792424306107538, 121.0018644040429),
                                new LatLng(13.792086854189238, 121.001867337207),
                                new LatLng(13.79197897025378, 121.00170961787127))

                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - grade 8 classrooms
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 22 || mMarkerPoints.get(0) == 22 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792172534296787, 121.0025056609826),
                                new LatLng(13.791988850398454, 121.0025430035201),
                                new LatLng(13.791939468341942, 121.00248508920528),
                                new LatLng(13.791908682556377, 121.00223954368266))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - main building
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 23 || mMarkerPoints.get(0) == 23 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792172534296787, 121.0025056609826),
                                new LatLng(13.792067517995404, 121.00277557013534),
                                new LatLng(13.792148994195761, 121.00330050823379),
                                new LatLng(13.792445274489182, 121.00321301427397))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - guard house
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 24 || mMarkerPoints.get(0) == 24 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792344466886638, 121.0024209996323),
                                new LatLng(13.79216603817516, 121.00252323506191),
                                new LatLng(13.792070940950097, 121.00277525621324),
                                new LatLng(13.792250162776584, 121.00394673802))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - cr1
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 25 || mMarkerPoints.get(0) == 25 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792172534296787, 121.0025056609826),
                                new LatLng(13.792066472110168, 121.00236202626994),
                                new LatLng(13.79203114817382, 121.0019705114846),
                                new LatLng(13.79185379586591, 121.00196555297214))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - cr2
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 26 || mMarkerPoints.get(0) == 26 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.793005278280724, 121.0023287540219),
                                new LatLng(13.792769364334474, 121.00208579778669),
                                new LatLng(13.792573621950805, 121.00182948922708))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - construction
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 27 || mMarkerPoints.get(0) == 27 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.793005278280724, 121.0023287540219),
                                new LatLng(13.792769364334474, 121.00208579778669),
                                new LatLng(13.792719992892087, 121.00177838080515))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - jhs clinic
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 28 || mMarkerPoints.get(0) == 28 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792349460292822, 121.00241932728389),
                                new LatLng(13.792172534296787, 121.0025056609826),
                                new LatLng(13.792069751579467, 121.00277730771202),
                                new LatLng(13.791960841745759, 121.00277772230292))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - jhs clinic
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 29 || mMarkerPoints.get(0) == 29 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792820926296667, 121.00236419201292))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - jhs clinic
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 30 || mMarkerPoints.get(0) == 30 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792735743727652, 121.00238430343308))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }

            //dump - jhs clinic
            else if (mMarkerPoints.get(0) == 32 && mMarkerPoints.get(1) == 31 || mMarkerPoints.get(0) == 31 && mMarkerPoints.get(1) == 32) {
                polylines.add(this.mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(13.79354610932843, 121.00225956481562),
                                new LatLng(13.792344466886638, 121.0024209996323),
                                new LatLng(13.79216603817516, 121.00252323506191),
                                new LatLng(13.792070940950097, 121.00277525621324),
                                new LatLng(13.792209584688644, 121.00352843782642),
                                new LatLng(13.792401819503414, 121.00359582828865))


                        .color(Color.CYAN)
                        .width(20)
                        .pattern(PATTERN_POLYGON_ALPHA)));
                mMarkerPoints.clear();
            }



            else{
            mMarkerPoints.clear();
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
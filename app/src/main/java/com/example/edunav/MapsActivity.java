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

        LatLng school = new LatLng(13.792659, 121.002470);
        MarkerOptions schoolM = new MarkerOptions()
                .position(school)
                .snippet("Technical Integrated High School")
                .rotation(0)
                .title("BTIHS")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("school",160,160)));

        Marker mkr = mMap.addMarker(schoolM);
        markers.put(mkr.getId(), 1);

        //evacuation area-a marker
        LatLng area_a = new LatLng(13.792288, 121.003093);
        MarkerOptions area_a_M = new MarkerOptions()
                .position(area_a)
                .title("EVACUATION AREA - A")
                .snippet("Bauan Technical Integrated High School Evacuation Area - A")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("evacuation",140,140)));


        Marker mkr1 = mMap.addMarker(area_a_M);
        markers.put(mkr1.getId(), 2);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));


        //map view
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //marker on click listener
        mMap.setOnMarkerClickListener(this);


        //
        //get latlong for corners for specified city

        LatLng one = new LatLng(13.791816, 121.002222);//SW
        LatLng two = new LatLng(13.793309, 121.003593);//NE

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



        getPath();
        getCurrentlocation();


        return false;
    }

    private void getPath(){

        if (mMarkerPoints.size() == 2){


            if (mMarkerPoints.get(0) == 1 && mMarkerPoints.get(1) == 2 || mMarkerPoints.get(0) == 2 && mMarkerPoints.get(1) == 1){

                    // Admin Bdlg to Evacuation Area A
                    polylines.add(this.mMap.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .add(
                                    new LatLng(13.7926088,121.0025078),
                                    new LatLng( 13.7924463,121.0025413),
                                    new LatLng(13.792438, 121.002805),
                                    new LatLng(13.792281, 121.002861),
                                    new LatLng(13.792288, 121.003093))));

            }





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
                lineOptions.color(Color.BLUE);
                lineOptions.pattern(pattern);
                lineOptions.startCap(new RoundCap());
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
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("userloc",160,160)));

                                    Marker mkr3 = mMap.addMarker(userlocM);
                                    markers.put(mkr3.getId(), 3);


                                    mOrigin = userloc;
                                    mDestination = MarkerPos;
                                    drawRoute();


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
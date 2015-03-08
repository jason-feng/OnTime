package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EventDisplayActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final String TAG = "EventDisplayActivity.java";
    public static final String EVENT_ID = "edu.dartmouth.cs.myruns.entry_id";
    public static final String DATE = "date";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String ATTENDEES = "attendees";
    public static final String TIME = "time";
    public String eventId;
    public String date, time, title;
    private Event displayedEvent;
    private ParseObject result;
    private TextView eventDisplayTextView, eventDisplayDate;
    private LinearLayout progressBarLinearLayout;
    private String eventTitle, eventLocationName;
    public ArrayList<String> attendees;
    private Location finalLocation;
    private LatLng latLngLocation;
    private double latitude, longitude;
    protected LocationManager mLocationManager;
    private int LOCATION_REFRESH_TIME = 1000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates;

    @Override
    protected void onResume() {
        super.onResume();
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "requestingLocationUpdates");
            mRequestingLocationUpdates = true;
            mGoogleApiClient.connect();
        }
    }

    private void initLocationTracking() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mRequestingLocationUpdates = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        initLocationTracking();

        //query the database for specific event. not sure if this works yet...
        if (savedInstanceState == null) {
            title = getIntent().getStringExtra(TITLE);
            attendees = getIntent().getStringArrayListExtra(ATTENDEES);

            finalLocation = getIntent().getParcelableExtra(LOCATION);
            date = getIntent().getStringExtra(DATE);
            time = getIntent().getStringExtra(TIME);

            latitude = finalLocation.getLatitude();
            longitude = finalLocation.getLongitude();
            //geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

//            if (eventId != "") {
//
//                ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
//                try {
//                    result = query.get(eventId);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        //get event title
        eventDisplayTextView = (TextView) findViewById(R.id.event_display_text_view);
        eventDisplayTextView.setTextColor(Color.WHITE);
        eventDisplayTextView.setText("    " + title);

        //get event date and time
        eventDisplayDate = (TextView) findViewById(R.id.event_display_date);
        eventDisplayDate.setTextColor(Color.BLACK);

        //TODO: get this working to make date prettier if possible
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
//        try {
//            cal.setTime(sdf.parse(date));
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        String displayTime = "";
//        displayTime += cal.get(Calendar.DAY_OF_WEEK);
//        displayTime += ", ";
//        displayTime += cal.get(Calendar.MONTH);
//        displayTime += " ";
//        displayTime += cal.get(Calendar.DATE);
//        displayTime += ", at ";
//        displayTime += cal.get(Calendar.HOUR);
//        displayTime += ":";
//        displayTime += cal.get(Calendar.MINUTE);
//        displayTime += cal.get(Calendar.AM_PM);

       // String displayTime = cal.get(Calendar.DATE) + " " + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE);

        eventDisplayDate.setText(date);

        //set event location

        latLngLocation = new LatLng(latitude, longitude);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        GoogleMap mmap = supportMapFragment.getMap();

        mmap.addMarker(new MarkerOptions()
                .position(latLngLocation)
                .title("Marker"));

        mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation,
                17));

        progressBarLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_linear_layout);


        //dynamically add friends
        for (int i = 0; i < attendees.size(); i++) {
            TextView newView = new TextView(this, null, R.style.CustomTextViewDani);
            //TODO: get actual person not the person's fb id number
            //query parse and iterate through the users and see which one has a matching fb id
            newView.setText(attendees.get(i));
            newView.setTextSize(15);
            newView.setTextAppearance(this, R.style.boldText);
            progressBarLinearLayout.addView(newView);

            //ProgressBar newBar = new ProgressBar(this, null, R.style.CustomProgressBarHorizontal);
            //ProgressBar newBar = new ProgressBar(this, null, );
            //ProgressBar newBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            //TODO: set to android.widget.ProgressBar.Horizontal somehow!!
            ProgressBar newBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);

            //newBar.setProgress(Integer.parseInt(attendees.get(i).getId()));
            newBar.setMinimumWidth(40);
            //newBar.setBackgroundColor(Color.WHITE);
            newBar.setMinimumHeight(50);
           // newBar.setProgressTintList();

            newBar.setScrollBarSize(200);
            progressBarLinearLayout.addView(newBar);
        }
    }

//    private void syncDistances() {
//        Log.d(TAG, "syncDistances");
//        new AsyncTask<Void, Void, String>() {
//        @Override
//        protected String doInBackground(Void... location) {
//            Location current =
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String res) {
//                Log.d(TAG, "EEAsyncTask onPostExecute()");
//            }
//
//        }.execute();
//    }

    //from Android developers
    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_display, menu);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

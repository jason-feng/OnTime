package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.graphics.Color;
import android.location.Location;
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
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This is the eventDisplayActivity, it displays a specific event's information along with each
 * invitee's current status with regard to reaching the final location.
 */
public class EventDisplayActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final String TAG = "EventDisplayActivity.java";
    public static final String OBJECT_ID = "event_id";
    public static final String DATE = "date";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String ATTENDEES = "attendees";
    public static final String TIME = "time";
    private static final String ATTENDEES_INSTANCE_STATE_KEY = "saved_attendees";
    private static final String EVENT_TITLE_INSTANCE_STATE_KEY = "saved_event_title";
    private static final String DATE_INSTANCE_STATE_KEY = "saved_date";
    private static final String LATLNG_INSTANCE_STATE_KEY = "saved_latlng";
    private double distance;
    public String object_id, date, time, title;
    private Event displayedEvent;
    private ParseObject result;
    private TextView eventDisplayTextView, eventDisplayDate;
    private LinearLayout progressBarLinearLayout;
    private String eventTitle, eventLocationName;
    public ArrayList<String> attendees;
    public ArrayList<String> user_names;
//    private OnLocationChangedTask mAsyncTask;
    public ArrayList<ParseUser> parse_users;
    public ArrayList<ParseGeoPoint> userLocations;
    public ArrayList<Double> userDistances;
    public ArrayList<Double> init_distances;
    private Location finalLocation;
    private ParseGeoPoint finalGeoPoint;
    private ParseGeoPoint current_location;
    private LatLng latLngLocation;
    private double latitude, longitude;
    private int LOCATION_REFRESH_TIME = 1000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private ParseUser currentUser;
    private String currentFbId;
    private int position;
    private boolean mRequestingLocationUpdates;
    private ArrayList<ProgressBar> progBarArrayList;

    @Override
    /**
     * When the location is changed, update the locations and distances on the cloud
     */
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        current_location = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        if (distance == -1.0) {
            distance = current_location.distanceInMilesTo(finalGeoPoint);
            init_distances.set(position,distance);
            userLocations.set(position, current_location);
            userDistances.set(position, distance);
        }
        else {
            distance = current_location.distanceInMilesTo(finalGeoPoint);
            userLocations.set(position, current_location);
            userDistances.set(position, distance);
        }
        for (int i = 0; i < userDistances.size(); i++) {
            ProgressBar currentUserBar = progBarArrayList.get(position);

            double status = userDistances.get(i) / init_distances.get(i);
            int intStatus = 1 - ((int) status * 100);
            currentUserBar.setProgress(intStatus);
        }

        displayedEvent.setUserLocations(userLocations);
        displayedEvent.setUserDistances(userDistances);
        try {
            displayedEvent.save();
        }
        catch (ParseException e) {

        }
//        updateProgressBars();
    }

//    public void updateProgressBars() {
//        //query distances
//
//        ParseQuery<Event> distancesQuery = ParseQuery.getQuery("event");
//        distancesQuery.whereEqualTo("objectId", object_id);
//        try{
//            Event distancesEvent = distancesQuery.getFirst();
//        }
//        catch (ParseException e){
//        }
//
//        for (int i = 0; i < queriedDistances.size(); i++) {
//            int curPosition = queriedDistances.get(position);
//
//            ProgressBar currentUserBar = progBarArrayList.get(position);
//
//            double status = distance / initialDist;
//            int intStatus = 1 - ((int) status * 100);
//            currentUserBar.setProgress(intStatus);
//        }
//
//    }

    //Function that is executed when location is changed.
//    private class OnLocationChangedTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            while (true) {
//                try {
//                    // need to check if the AsyncTask is cancelled or not in the while loop
//                    if (isCancelled() == true) {
//                        return null;
//                    }
//                    ProgressBar currentUserBar = progBarArrayList.get(position);
//
//                    double status = distance / initialDist;
//                    int intStatus = 1 - ((int) status * 100);
//                    currentUserBar.setProgress(intStatus);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }



    /**
     * Gets the necessary information from the cloud including the corresponding position on the
     * array list and the userlocations. If userlocations don't exist, init a new one
     */
    private void getParseInformation() {
        currentUser = ParseUser.getCurrentUser();
        currentFbId = currentUser.getString("fbId");
        for (int i = 0; i < attendees.size(); i++) {
            if (attendees.get(i) == currentFbId)
                position = i;
        }

        if (displayedEvent.getUserLocations() != null && displayedEvent.getUserDistances() != null && displayedEvent.getInitDistances() != null) {
            userLocations = displayedEvent.getUserLocations();
            userDistances = displayedEvent.getUserDistances();
            init_distances = displayedEvent.getInitDistances();
        }
        else {
            Log.d(TAG, "new Parse Stuff");
            userLocations = new ArrayList<ParseGeoPoint>();
            userDistances = new ArrayList<Double>();
            init_distances = new ArrayList<Double>();
            for (int i = 0; i < attendees.size(); i++) {
                userLocations.add(i, null);
                userDistances.add(i,-1.0);
                init_distances.add(i,-1.0);
                distance = -1.0;
            }
            displayedEvent.setUserLocations(userLocations);
            displayedEvent.setUserDistances(userDistances);
            displayedEvent.setInitDistances(init_distances);
            try {
                displayedEvent.save();
            }
            catch (ParseException e) {

            }
        }

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

        //Gets event information from the MainActivity
        if (savedInstanceState == null) {
            object_id = getIntent().getStringExtra(OBJECT_ID);
            title = getIntent().getStringExtra(TITLE);
            attendees = getIntent().getStringArrayListExtra(ATTENDEES);

            finalLocation = getIntent().getParcelableExtra(LOCATION);
            finalGeoPoint = new ParseGeoPoint(finalLocation.getLatitude(),finalLocation.getLongitude());
            date = getIntent().getStringExtra(DATE);
            time = getIntent().getStringExtra(TIME);

            latitude = finalLocation.getLatitude();
            longitude = finalLocation.getLongitude();
            latLngLocation = new LatLng(latitude, longitude);

            Log.d(TAG, "ObjectID: " + object_id);

            ParseQuery<Event> currentEventQuery = ParseQuery.getQuery("event");
            currentEventQuery.whereEqualTo("objectId", object_id);
            try{
                displayedEvent = currentEventQuery.getFirst();
            }
            catch (ParseException e){
            }
        }

        if (savedInstanceState != null) {
            attendees = savedInstanceState.getStringArrayList(ATTENDEES_INSTANCE_STATE_KEY);
            title = savedInstanceState.getString(EVENT_TITLE_INSTANCE_STATE_KEY);
            date = savedInstanceState.getString(DATE_INSTANCE_STATE_KEY);
            latLngLocation = savedInstanceState.getParcelable(LATLNG_INSTANCE_STATE_KEY);

        }

        getParseInformation();

        //get event title
        eventDisplayTextView = (TextView) findViewById(R.id.event_display_text_view);
        eventDisplayTextView.setTextColor(Color.WHITE);
        eventDisplayTextView.setText("    " + title.toUpperCase());

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
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        GoogleMap mmap = supportMapFragment.getMap();

        mmap.addMarker(new MarkerOptions()
                .position(latLngLocation)
                .title("Marker"));

        mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngLocation,
                17));

        progressBarLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_linear_layout);

        ParseQuery query_names = ParseUser.getQuery();
        query_names.whereContainedIn("fbId", attendees);
        parse_users = new ArrayList<ParseUser>();
        try {
            parse_users = (ArrayList<ParseUser>) query_names.find();

        } catch (ParseException e) {

        }

        progBarArrayList = new ArrayList<ProgressBar>();

       // query.con
        //dynamically add friends
        for (int i = 0; i < parse_users.size(); i++) {
            TextView newView = new TextView(this, null, R.style.CustomTextViewDani);
            //query parse and iterate through the users and see which one has a matching fb id
//            query.whereContainedIn(attendees.get(i), attendees);

            newView.setText(parse_users.get(i).getString("name"));
            newView.setTextSize(15);
            newView.setTextAppearance(this, R.style.boldText);
            progressBarLinearLayout.addView(newView);

            ProgressBar newBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);


            //newBar.setProgress(0);
            newBar.setMinimumWidth(40);
            newBar.setMinimumHeight(50);
            newBar.setId(i);
           // newBar.setProgressTintList();

            newBar.setScrollBarSize(200);
            progressBarLinearLayout.addView(newBar);
            progBarArrayList.add(newBar);
        }


    }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putStringArrayList(ATTENDEES_INSTANCE_STATE_KEY, attendees);
        outState.putString(EVENT_TITLE_INSTANCE_STATE_KEY, title);
        outState.putString(DATE_INSTANCE_STATE_KEY, date);
        outState.putParcelable(LATLNG_INSTANCE_STATE_KEY, latLngLocation);
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
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_REFRESH_TIME); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "requestingLocationUpdates");
            mRequestingLocationUpdates = true;
            mGoogleApiClient.connect();
        }
    }

    /**
     * Start location tracking
     */
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
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }
}

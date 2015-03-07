package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EventDisplayActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EVENT_ID = "edu.dartmouth.cs.myruns.entry_id";
    public static final String DATE = "date";
    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String ATTENDEES = "attendees";
    public String eventId;
    public Date date;
    public String title;
    private Event displayedEvent;
    private ParseObject result;
    private TextView eventDisplayTextView, eventDisplayDate;
    private LinearLayout progressBarLinearLayout;
    private String eventTitle, eventLocationName;
    public ArrayList<String> attendees;
    private Location finalLocation;
    private LatLng latLngLocation;


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

        //query the database for specific event. not sure if this works yet...
        if (savedInstanceState == null) {
            title = getIntent().getStringExtra(TITLE);
            attendees = getIntent().getStringArrayListExtra(ATTENDEES);

            finalLocation = getIntent().getParcelableExtra(LOCATION);
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
        //TODO: when actually getting the event, set text to datetime
        eventDisplayDate.setText("Monday, February 2 @ 5:00pm-6:30pm");

        //get event location
        double latitude = finalLocation.getLatitude();
        double longitude = finalLocation.getLongitude();
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
            //TODO: get actual person not whatever this rando string is
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

            newBar.setScrollBarSize(200);
            progressBarLinearLayout.addView(newBar);
        }






        //for each invitee in event invitees list

        //add a progressbar for them into the scrollview


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
}

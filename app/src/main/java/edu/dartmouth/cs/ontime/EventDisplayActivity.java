package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import java.util.List;


public class EventDisplayActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String EVENT_ID = "edu.dartmouth.cs.myruns.entry_id";
    public String eventId;
    private Event displayedEvent;
    private ParseObject result;
    private TextView eventDisplayTextView, eventDisplayDate;
    private LinearLayout progressBarLinearLayout;
    private String eventTitle, eventLocationName;
    public ArrayList<Friend> invitees;
    private LatLng location;


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
            eventId = getIntent().getStringExtra(EVENT_ID);

            if (eventId != "") {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
                try {
                    result = query.get(eventId);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        //get all text fields from query

        eventDisplayTextView = (TextView) findViewById(R.id.event_display_text_view);
        eventDisplayTextView.setTextColor(Color.WHITE);
        //TODO: to be used once actually getting event
        //eventDisplayTextView.setText(eventTitle + "at" + eventLocationName);
        eventDisplayTextView.setText("    DINNER at Pine");
        eventDisplayDate = (TextView) findViewById(R.id.event_display_date);
        eventDisplayDate.setTextColor(Color.BLACK);
        //TODO: when actually getting the event, set text to datetime
        eventDisplayDate.setText("Monday, February 2 @ 5:00pm-6:30pm");

        //TODO: when actually getting the event, create new LatLng with event location
        //for now this is hard coded
        location = new LatLng(43.704441, -72.288693);

        //TODO: when actually getting the event, set arraylist of friends from query
        invitees = new ArrayList<Friend>();
        Friend friend1 = new Friend("Emily", "12", null);
        invitees.add(friend1);
        Friend friend2 = new Friend("Jason", "28", null);
        invitees.add(friend2);
        Friend friend3 = new Friend("Dani", "35", null);
        invitees.add(friend3);
        Friend friend4 = new Friend("Nick", "54", null);
        invitees.add(friend4);
        Friend friend5 = new Friend("test", "100", null);
        invitees.add(friend5);
        Friend friend6 = new Friend("test", "34", null);
        invitees.add(friend6);
        Friend friend7 = new Friend("test", "12", null);
        invitees.add(friend7);
        Friend friend8 = new Friend("plswork", "1", null);
        invitees.add(friend8);


        //Context myContext =
//        this.getSupportFragmentManager();
//        MapFragment mapFragment = (MapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        GoogleMap mmap = supportMapFragment.getMap();

        mmap.addMarker(new MarkerOptions()
                .position(location)
                .title("Marker"));

        mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,
                17));

        progressBarLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_linear_layout);


        //dynamically add friends
        for (int i = 0; i < invitees.size(); i++) {
            TextView newView = new TextView(this, null, R.style.CustomTextViewDani);
            //TODO: set text here to name of person
            newView.setText(invitees.get(i).getName());
            newView.setTextSize(15);
            newView.setTextAppearance(this, R.style.boldText);
            progressBarLinearLayout.addView(newView);

            //ProgressBar newBar = new ProgressBar(this, null, R.style.CustomProgressBarHorizontal);
            //ProgressBar newBar = new ProgressBar(this, null, );
            //ProgressBar newBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            //TODO: set to android.widget.ProgressBar.Horizontal somehow!!
            ProgressBar newBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);

            newBar.setProgress(Integer.parseInt(invitees.get(i).getId()));
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

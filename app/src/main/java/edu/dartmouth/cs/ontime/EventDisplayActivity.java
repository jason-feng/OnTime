package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class EventDisplayActivity extends Activity  {

    public static final String EVENT_ID = "edu.dartmouth.cs.myruns.entry_id";
    public String eventId;
    private Event displayedEvent;
    private ParseObject result;
    private TextView eventDisplayTextView, eventDisplayDate;
    private LinearLayout progressBarLinearLayout;
    private String eventTitle, eventLocationName;


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

//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);


        progressBarLinearLayout = (LinearLayout) findViewById(R.id.progress_bar_linear_layout);

        ProgressBar newBar = new ProgressBar(this, null, R.style.CustomProgressBarHorizontal);
        progressBarLinearLayout.addView(newBar);
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


}

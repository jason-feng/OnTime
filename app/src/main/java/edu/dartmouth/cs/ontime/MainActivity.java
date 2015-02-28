package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

import edu.dartmouth.cs.ontime.view.SlidingTabLayout;


public class MainActivity extends Activity {


    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Event> upcomingEvents;
    private SettingsFragment settingsFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new Settings())
//                    .commit();
//        }
        // how do you want me to do this if we are no longer doing fragments?

        LinearLayout layout =(LinearLayout)findViewById(R.id.background);

        //get person based on regId of phone (from server); for now this and events are hard-coded

        //sample event 1
        Event event1 = new Event();
        Calendar newDateTime = Calendar.getInstance();
        newDateTime.set(2015, 3, 1, 12, 25);
        event1.setmDateTime(newDateTime);
        Location loc = new Location("");
        loc.setLatitude(43.704441);
        loc.setLongitude(-72.288693);
        event1.setmLocation(loc);
        upcomingEvents.add(event1);

        Event event2 = new Event();
        Calendar newDateTime2 = Calendar.getInstance();
        newDateTime2.set(2015, 3, 2, 12, 25);
        event2.setmDateTime(newDateTime2);
        Location loc2 = new Location("");
        loc2.setLatitude(43.701728);
        loc2.setLongitude(-72.288848);
        event2.setmLocation(loc);
        upcomingEvents.add(event2);

        Event event3 = new Event();
        Calendar newDateTime3 = Calendar.getInstance();
        newDateTime3.set(2015, 3, 5, 12, 25);
        event3.setmDateTime(newDateTime3);
        Location loc3 = new Location("");
        loc3.setLatitude(43.704451);
        loc3.setLongitude(-72.286643);
        event3.setmLocation(loc3);
        upcomingEvents.add(event3);

        //TODO: iterate through upcomingEvents

        //for each event in upcoming events list
            //if it's today, add it to the today text
            //if it's tomorrow add it to tomorrow text
            //if it's this week, add it to this week text
            //else add it to upcoming events overall



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}

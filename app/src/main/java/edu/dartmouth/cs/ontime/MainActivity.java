package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.dartmouth.cs.ontime.view.SlidingTabLayout;


public class MainActivity extends Activity {


    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Event> upcomingEvents;
    private Settings settingsFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout =(LinearLayout)findViewById(R.id.background);
        layout.setBackgroundResource(R.drawable.background_welcome);

        //TODO: get person based on regId of phone (from server); for now this and events are hard-coded

        //upcomingEvents = person.getEvents()
        upcomingEvents = new ArrayList<Event>();

        //sample event 1
        Event event1 = new Event();
        Calendar newDateTime = Calendar.getInstance();
        newDateTime.set(2015, 3, 2, 12, 25);
        event1.setmDateTime(newDateTime);
        Location loc = new Location("");
        loc.setLatitude(43.704441);
        loc.setLongitude(-72.288693);
        event1.setmLocation(loc);
        event1.setmTitle("Dinner at Pine");
        upcomingEvents.add(event1);

        Event event2 = new Event();
        Calendar newDateTime2 = Calendar.getInstance();
        newDateTime2.set(2015, 3, 3, 12, 25);
        event2.setmDateTime(newDateTime2);
        Location loc2 = new Location("");
        loc2.setLatitude(43.701728);
        loc2.setLongitude(-72.288848);
        event2.setmLocation(loc);
        event2.setmTitle("Dinner w/ CS65");
        upcomingEvents.add(event2);

        Event event3 = new Event();
        Calendar newDateTime3 = Calendar.getInstance();
        newDateTime3.set(2015, 3, 3, 12, 25);
        event3.setmDateTime(newDateTime3);
        Location loc3 = new Location("");
        loc3.setLatitude(43.704451);
        loc3.setLongitude(-72.286643);
        event3.setmLocation(loc3);
        event3.setmTitle("DALI Group Meeting");
        upcomingEvents.add(event3);

        Event event4 = new Event();
        Calendar newDateTime4 = Calendar.getInstance();
        newDateTime4.set(2015, 3, 5, 12, 25);
        event4.setmDateTime(newDateTime4);
        Location loc4 = new Location("");
        loc4.setLatitude(43.702451);
        loc4.setLongitude(-72.286243);
        event4.setmLocation(loc4);
        event4.setmTitle("Lunch w/ Tim");
        upcomingEvents.add(event4);

        Event event5 = new Event();
        Calendar newDateTime5 = Calendar.getInstance();
        newDateTime5.set(2015, 3, 6, 12, 25);
        event5.setmDateTime(newDateTime5);
        Location loc5 = new Location("");
        loc5.setLatitude(43.703451);
        loc5.setLongitude(-72.286643);
        event5.setmLocation(loc5);
        event5.setmTitle("Work session");
        upcomingEvents.add(event5);

        Event event6 = new Event();
        Calendar newDateTime6 = Calendar.getInstance();
        newDateTime6.set(2015, 3, 7, 12, 25);
        event6.setmDateTime(newDateTime3);
        Location loc6 = new Location("");
        loc6.setLatitude(43.704451);
        loc6.setLongitude(-72.286653);
        event6.setmLocation(loc6);
        event6.setmTitle("Movie night");
        upcomingEvents.add(event6);

        Event event7 = new Event();
        Calendar newDateTime7 = Calendar.getInstance();
        newDateTime7.set(2015, 3, 8, 12, 25);
        event7.setmDateTime(newDateTime3);
        Location loc7 = new Location("");
        loc7.setLatitude(43.704451);
        loc7.setLongitude(-72.286643);
        event7.setmLocation(loc7);
        event5.setmTitle("Sledding");
        upcomingEvents.add(event7);

        String[] todayArray = new String[1];
        String[] tomorrowArray = new String[2];
        String[] thisWeekArray = new String[4];
        //for each event in upcoming events list
        for (int i = 0; i < upcomingEvents.size(); i++) {
            Calendar date = upcomingEvents.get(i).getmDateTime();
            long currentTime = System.currentTimeMillis();
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(currentTime);
            if (date.get(Calendar.DATE) == today.get(Calendar.DATE)) {
                //add to today's text
               // String[] todayArray = new String[1];
                todayArray[0] = upcomingEvents.get(0).getmTitle();

            }
            else if (date.get(Calendar.DATE) == today.get(Calendar.DATE) +1) {
               // String[] tomorrowArray = new String[2];
                tomorrowArray[0] = upcomingEvents.get(1).getmTitle();
                tomorrowArray[1] = upcomingEvents.get(2).getmTitle();
            }
            //this line will return -1 if today.getTime is before the last day of the week
            else if (today.getTime().compareTo(getStartEndOFWeek(date.get(Calendar.WEEK_OF_YEAR), date.get(Calendar.YEAR))) == -1) {
                //so add to this week text
              //  String[] thisWeekArray = new String[4];
                thisWeekArray[0] = upcomingEvents.get(3).getmTitle();
                thisWeekArray[1] = upcomingEvents.get(4).getmTitle();
                thisWeekArray[2] = upcomingEvents.get(5).getmTitle();
                thisWeekArray[3] = upcomingEvents.get(6).getmTitle();

            }

            else {
                //else, add to "upcoming events" field at bottom
            }

        }
    }

    //from stackoverflow
    public Date getStartEndOFWeek(int enterWeek, int enterYear){
    //enterWeek is week number
    //enterYear is year
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMM yyyy"); // PST`
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        System.out.println("...date..."+startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
        System.out.println("...date..."+endDaString);

        return enddate;

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}

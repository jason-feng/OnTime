package edu.dartmouth.cs.ontime;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jasonfeng on 3/1/15.
 */
@ParseClassName("event")
public class Event extends ParseObject {

    public static final String TAG = "Event";
    private static List<ParseObject> events;

    public Event() {

    }

    public Date getTime() {
        return getDate("time");
    }

    public void setTime(Date time) {
        put("time", time);
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    public ArrayList<String> getAcceptedList(){
        return (ArrayList<String>) get("accepted");
    }

    public void setAcceptedList(ArrayList<String> invitees){
        put("accepted", invitees);
    }

    public ArrayList<ParseGeoPoint> getUserLocations() { return (ArrayList<ParseGeoPoint>) get("distances"); }

    public void setUserLocations(ArrayList<ParseGeoPoint> user_locations) { put("user_locations", user_locations);}

    public static List<ParseObject> query() {
        Log.d(TAG, "query()");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "ParseQuery");
                    for (ParseObject object : objects) {
                        events.add(object);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        Log.d(TAG, Integer.toString(events.size()));
        return events;
    }
}

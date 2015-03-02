package edu.dartmouth.cs.ontime;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasonfeng on 3/1/15.
 */
public class Event extends ParseObject {

    public static final String TAG = "event";

    private static List<Event> events;

    public static ArrayList<Event> query() {
        ParseQuery<Event> query = ParseQuery.getQuery("event");
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "ParseQuery");
                    events = objects;
                } else {
                    e.printStackTrace();
                }
            }
        });
        return (ArrayList) events;
    }
}

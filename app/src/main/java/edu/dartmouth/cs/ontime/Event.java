package edu.dartmouth.cs.ontime;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by jasonfeng on 3/1/15.
 */
public class Event extends ParseObject {

    public static final String TAG = "Event";
    private static List<ParseObject> events;

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

package edu.dartmouth.cs.ontime;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by garygreene on 3/2/15.
 */
public class ParseFriend extends ParseObject {

    public static final String TAG = "Friend";
    private static List<ParseObject> friends;

    public static List<ParseObject> query() {
        Log.d(TAG, "query()");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "ParseQuery");
                    for (ParseObject object : objects) {
                        friends.add(object);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        Log.d(TAG, Integer.toString(friends.size()));
        return friends;
    }
}

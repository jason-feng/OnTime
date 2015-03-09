package edu.dartmouth.cs.ontime;

/**
 * Created by jasonfeng on 3/1/15.
 */

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Application
 */
public class App extends Application {

    private String mSearchText;

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public String getSearchText() {
        return mSearchText;
    }

    public void setSearchText(String searchText) {
        this.mSearchText = searchText;
    }

    /**
     * Setup parse
     */
    @Override public void onCreate() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Event.class);
        Parse.initialize(this, "mpKUYS0VHcJR1KQiVDQ8EUC0RDb5WRqB1gwUOuT4", "lP5IoGEkvcqBG9I3IxtXU5EtnEJiE2yHzX1bbZuq");

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        super.onCreate();

    }
}

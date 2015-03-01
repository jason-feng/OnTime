package edu.dartmouth.cs.ontime;

/**
 * Created by jasonfeng on 3/1/15.
 */
import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mpKUYS0VHcJR1KQiVDQ8EUC0RDb5WRqB1gwUOuT4", "lP5IoGEkvcqBG9I3IxtXU5EtnEJiE2yHzX1bbZuq");
    }
}

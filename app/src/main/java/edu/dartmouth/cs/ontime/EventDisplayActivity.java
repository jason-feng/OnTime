package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class EventDisplayActivity extends Activity {

    public static final String EVENT_ID = "edu.dartmouth.cs.myruns.entry_id";
    public String eventId;
    private Event displayedEvent;
    private ParseObject result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_display);

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

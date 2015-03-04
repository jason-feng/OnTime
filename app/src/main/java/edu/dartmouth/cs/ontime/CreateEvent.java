package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.ArrayList;


public class CreateEvent extends ListActivity {

    public static final String[] FACULTY = new String[] { "Title","Date", "Time", "Location", "Invitees"};
    private static final String TAG = "CreateEvent";
    public static final String INDEX = "CreateEvent.java";
    public static final int MAP_REQUEST = 1;
    public ArrayList mSelectedItems;
    public ListView list;
    public static int[] intCal;
    public String actType;
    private ParseObject event;
    private Context context;
    private Location mLocation;

    public ParseObject getEvent() { return event; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST) {
            if (resultCode != RESULT_OK) {
                return;
            }
            else {
                Log.d(TAG, "onActivityResult Success");
                ParseGeoPoint point = new ParseGeoPoint(
                        data.getDoubleExtra("LAT", 0),
                        data.getDoubleExtra("LONG", 0)
                );
                event.put("location", point);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        mLocation = null;
        context = this;
        intCal = new int[6];
        list = (ListView)this.findViewById(android.R.id.list);
        event = new ParseObject("event");
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, FACULTY);
        setListAdapter(mAdapter);
    }

    public void onSaveClicked(View v) {
        event.put("accepted", new ArrayList<Integer>());
        ParseInstallation.getCurrentInstallation().getInstallationId();
        event.saveInBackground();
        finish();
    }

    public void onCancelClicked(View v) {
        finish();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position != 3) {
            Log.d(TAG, "onListItemClick()");
            Bundle bundle = new Bundle();
            bundle.putInt(INDEX, position);
            EventDialogFragments fragment = new EventDialogFragments();
            fragment.setArguments(bundle);
            fragment.show(getFragmentManager(), "dialog");
        }
        else {
            Intent intent = new Intent(this, MapFragment.class);
            startActivityForResult(intent, MAP_REQUEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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



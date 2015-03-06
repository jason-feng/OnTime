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
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateEvent extends ListActivity {

    public static final String[] FACULTY = new String[] { "Title","Date", "Time", "Location", "Invitees"};
    private static final String TAG = "CreateEvent";
    public static final String INDEX = "CreateEvent.java";
    public static final int MAP_REQUEST = 1;
    public static final int INVITE_REQUEST = 2;
    public ArrayList mSelectedItems;
    public ListView list;
    public static int[] intCal;
    public String actType;
    private Event event;
    private Context context;
    private Location mLocation;
    private ArrayList<ParseUser> userList;
    private ArrayList<String> installationIDs;

    public Event getEvent() { return event; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == MAP_REQUEST) {


                Log.d(TAG, "onActivityResult Success");
                ParseGeoPoint point = new ParseGeoPoint(
                        data.getDoubleExtra("LAT", 0),
                        data.getDoubleExtra("LONG", 0)
                );
                event.setLocation(point);

            }

            else if (requestCode == INVITE_REQUEST){
                ArrayList<String> selectedIDs = data.getStringArrayListExtra("selected_friends");
                installationIDs = new ArrayList<String>();
                for (String id : selectedIDs){
                    ParseQuery query = ParseUser.getQuery();
                    query.whereContains("fbId", id);
                    try{
                        ParseUser user = (ParseUser) query.getFirst();
                        userList.add(user);
                        installationIDs.add(user.getString("installation_id"));
                    }
                    catch (ParseException e){

                    }
                    event.setInviteList(installationIDs);
                }
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

        userList = new ArrayList<ParseUser>();

        event = new Event();
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, FACULTY);
        setListAdapter(mAdapter);
    }

    public void onSaveClicked(View v) {
        if (installationIDs.size() != 0 && event.getTitle() != null) {
            ArrayList<String> accepted = new ArrayList<String>();
            accepted.add(ParseUser.getCurrentUser().getString("fbId"));
            event.put("accepted", accepted);
            event.put("host", ParseInstallation.getCurrentInstallation().getInstallationId());
            event.saveInBackground();

            for (ParseUser user : userList) {
                ArrayList<Event> inviteList = (ArrayList<Event>) user.get("invited");
                inviteList.add(event);
                user.put("invited", inviteList);
            }
            ParseUser me = ParseUser.getCurrentUser();
            ArrayList<Event> updatedEvents = (ArrayList<Event>) me.get("accepted");
            updatedEvents.add(event);
            me.put("accepted", updatedEvents);

            // create installation query
            ParseQuery installationQuery = ParseInstallation.getQuery();
            installationQuery.whereContainedIn("installationId", installationIDs);

            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(installationQuery); // Set our Installation query
            push.setMessage(ParseUser.getCurrentUser().get("name") + " invited you to " + event.getTitle());
            push.sendInBackground();

            finish();
        }
        else if (installationIDs.size() == 0 ){
            Toast.makeText(getApplicationContext(),
                    "Please select friends to invite",
                    Toast.LENGTH_SHORT).show();
        }
        else if (event.getTitle() == null){
            Toast.makeText(getApplicationContext(),
                    "Please set a title for your event",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onCancelClicked(View v) {
        finish();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (position != 3 && position != 4) {
            Log.d(TAG, "onListItemClick()");
            Bundle bundle = new Bundle();
            bundle.putInt(INDEX, position);
            EventDialogFragments fragment = new EventDialogFragments();
            fragment.setArguments(bundle);
            fragment.show(getFragmentManager(), "dialog");
        }
        else if (position == 3) {
            Intent intent = new Intent(this, MapFragment.class);
            startActivityForResult(intent, MAP_REQUEST);
        }
        else if (position == 4) {
            Intent intent = new Intent(this, FriendList.class);
            startActivityForResult(intent, INVITE_REQUEST);
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



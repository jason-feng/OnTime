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

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static boolean[] dialogFields = new boolean[5];
    private Event event;
    private Context context;
    private Location mLocation;
    private ArrayList<String> userList;
    private ArrayList<String> installationIDs;

    public Event getEvent() { return event; }
    public boolean[] getDialogFields() { return dialogFields; }

    public boolean allEventFields() {
        for (int i = 0; i < dialogFields.length; i++) {
            if (dialogFields[i] != true) {
                return false;
            }
        }
        return true;
    }

    public static void setDialogField(int index, boolean value) {
        if (index > dialogFields.length || index < 0) {
            return;
        }
        else {
            dialogFields[index] = value;
        }
    }

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
                CreateEvent.setDialogField(4,true);
            }

            else if (requestCode == INVITE_REQUEST){
                ArrayList<String> selectedIDs = data.getStringArrayListExtra("selected_friends");
                installationIDs = new ArrayList<String>();
                for (String id : selectedIDs){
                    ParseQuery query = ParseUser.getQuery();
                    query.whereContains("fbId", id);
                    try{
                        ParseUser user = (ParseUser) query.getFirst();
                        userList.add(user.getObjectId());
                        installationIDs.add(user.getString("installation_id"));
                    }
                    catch (ParseException e){

                    }
                }
            }
        }
        else if (resultCode == 14){
            if (requestCode == INVITE_REQUEST) {
                if (data.getBooleanExtra("retry", false)) {
                    Intent intent = new Intent(this, FriendList.class);
                    startActivityForResult(intent, INVITE_REQUEST);
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

        userList = new ArrayList<String>();

        event = new Event();
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, FACULTY);
        setListAdapter(mAdapter);
    }

    public void onSaveClicked(View v) {
        event.put("accepted", new ArrayList<Integer>());;
        if (installationIDs.size() != 0 && event.getTitle() != null && event.getTime() != null && event.getDate() != null) {
            ArrayList<String> accepted = new ArrayList<String>();
            accepted.add(ParseUser.getCurrentUser().getString("fbId"));
            event.put("accepted", accepted);
            event.put("host", ParseInstallation.getCurrentInstallation().getInstallationId());

            event.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        final String eventId = event.getObjectId();

                        HashMap<String, ArrayList<String>> params = new HashMap<>();
                        userList.add(eventId);
                        params.put("users", userList);

                        ParseCloud.callFunctionInBackground("InviteListSaver", params, new FunctionCallback<Object>() {
                            @Override
                            public void done(Object o, ParseException e) {
                            }
                        });

                        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                ParseUser me = (ParseUser) object;
                                // Check if there is current user info
                                if (me != null) {
                                    ArrayList<String> updatedEvents = (ArrayList<String>) me.get("accepted");
                                    updatedEvents.add(eventId);
                                    me.put("accepted", updatedEvents);
                                    try{
                                        me.save();
                                    }
                                    catch (ParseException k){

                                    }
                                    ((CreateFinished) App.getContext()).createEventDone();
                                }
                                else {
                                }
                            }
                        });;


                        // create installation query
                        ParseQuery installationQuery = ParseInstallation.getQuery();
                        installationQuery.whereContainedIn("installationId", installationIDs);

                        // Send push notification to query
                        ParsePush push = new ParsePush();
                        push.setQuery(installationQuery); // Set our Installation query
                        JSONObject jsonObj = new JSONObject();
                        try{
                            jsonObj.put("name", ParseUser.getCurrentUser().get("name"));
                            jsonObj.put("title", event.getTitle());
                            jsonObj.put("objectId", event.getObjectId());
                        }
                        catch (JSONException j){
                        }
                        push.setData(jsonObj);
                        push.sendInBackground();
                    } else {
                        Log.d(TAG, "failed to save!");
                    }

                }
            });

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

        else if (event.getDate() == null){
            Toast.makeText(getApplicationContext(),
                    "Please set a date for your event",
                    Toast.LENGTH_SHORT).show();
        }

        else if (event.getTime() == null){
            Toast.makeText(getApplicationContext(),
                    "Please set a time for your event",
                    Toast.LENGTH_SHORT).show();
        }

        else if (event.getLocation() == null){
            Toast.makeText(getApplicationContext(),
                    "Please set a location for your event",
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

    public interface CreateFinished{
        public void createEventDone();
    }
}



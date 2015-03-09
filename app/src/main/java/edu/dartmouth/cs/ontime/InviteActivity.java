package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InviteActivity extends Activity {

    public ListView mList;
    public InviteAdapter mAdapter;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        mContext = this;

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        // get invite list of event ids
        ParseUser me = ParseUser.getCurrentUser();
        ArrayList<String> invitedList = (ArrayList<String>) me.get("invited");

        // get actual event objects from ids
        ParseQuery query = ParseQuery.getQuery("event");
        query.whereContainedIn("objectId", invitedList);
        ArrayList<Event> invitedEvents = new ArrayList<Event>();
        try{
            invitedEvents = (ArrayList<Event>) query.find();
        }
        catch (ParseException e){

        }

        InviteAdapter mAdapter = new InviteAdapter(mContext, android.R.layout.simple_list_item_1, invitedEvents);

        mList = (ListView)findViewById(R.id.list);
        mList.setAdapter(mAdapter);

        InviteClickListener listener = new InviteClickListener();

        mList.setOnItemClickListener(listener);

        MainActivity.ListUtils.setDynamicHeight(mList);

        Intent received = getIntent();
        if (received != null){

            boolean accepted = received.getBooleanExtra("accept", false);
            boolean declined = received.getBooleanExtra("decline", false);
            String eventId = received.getStringExtra("eventId");
            if (eventId != null) {
                ParseQuery eventQuery = ParseQuery.getQuery("event");
                eventQuery.whereContains("objectId", eventId);
                Event event = null;
                try {
                    event = (Event) eventQuery.getFirst();
                } catch (ParseException e) {

                }
                if (event != null) {

                    if (declined) {

                        ArrayList<String> invited = (ArrayList<String>) me.get("invited");
                        invited.remove(event.getObjectId());
                        me.put("invited", invited);
                        me.saveInBackground();
                        String hostId = event.getString("host");

                        // get actual event objects from ids
                        ParseQuery declineQuery = ParseQuery.getQuery("event");
                        declineQuery.whereContainedIn("objectId", invited);
                        ArrayList<Event> declineEvents = new ArrayList<Event>();
                        try {
                            declineEvents = (ArrayList<Event>) query.find();
                        } catch (ParseException e) {

                        }

                        // create installation query
                        ParseQuery installationQuery = ParseInstallation.getQuery();
                        installationQuery.whereContains("installationId", hostId);

                        // Send push notification to query
                        ParsePush push = new ParsePush();
                        push.setQuery(installationQuery); // Set our Installation query
                        JSONObject jsonObj = new JSONObject();
                        try{
                            jsonObj.put("name", ParseUser.getCurrentUser().get("name"));
                            jsonObj.put("title", event.getTitle());
                            jsonObj.put("objectId", null);
                            jsonObj.put("accepted", true);
                        }
                        catch (JSONException j){
                        }
                        push.setData(jsonObj);
                        push.sendInBackground();


                        mAdapter = new InviteAdapter(mContext, android.R.layout.simple_list_item_1, declineEvents);
                        mList.setAdapter(mAdapter);
                    } else if (accepted) {

                        ArrayList<String> i = (ArrayList<String>) me.get("invited");
                        ArrayList<String> a = (ArrayList<String>) me.get("accepted");
                        i.remove(event.getObjectId());
                        a.add(event.getObjectId());
                        me.put("invited", i);
                        me.put("accepted", a);
                        me.saveInBackground();
                        String hostId = event.getString("host");

                        ParseQuery acceptQuery = ParseQuery.getQuery("event");
                        acceptQuery.whereContains("objectId", eventId);

                        try {
                            Event acceptEvent = (Event) query.getFirst();
                            ArrayList<String> acceptInvitees = acceptEvent.getAcceptedList();
                            acceptInvitees.add(ParseUser.getCurrentUser().getString("fbId"));
                            acceptEvent.setAcceptedList(acceptInvitees);
                            Log.d("InviteActivity", "Event saved with new accepted list");
                            acceptEvent.saveInBackground();
                        } catch (ParseException e) {
                        }

                        // get actual event objects from ids
                        ParseQuery acceptEventQuery = ParseQuery.getQuery("event");
                        query.whereContainedIn("objectId", i);
                        ArrayList<Event> acceptedEvents = new ArrayList<Event>();
                        try {
                            acceptedEvents = (ArrayList<Event>) query.find();
                        } catch (ParseException e) {

                        }

                        // create installation query
                        ParseQuery installationQuery = ParseInstallation.getQuery();
                        installationQuery.whereContains("installationId", hostId);

                        // Send push notification to query
                        ParsePush push = new ParsePush();
                        push.setQuery(installationQuery); // Set our Installation query
                        JSONObject jsonObj = new JSONObject();
                        try{
                            jsonObj.put("name", ParseUser.getCurrentUser().get("name"));
                            jsonObj.put("title", event.getTitle());
                            jsonObj.put("objectId", null);
                            jsonObj.put("accepted", false);
                        }
                        catch (JSONException j){
                        }
                        push.setData(jsonObj);
                        push.sendInBackground();

                        mAdapter = new InviteAdapter(mContext, android.R.layout.simple_list_item_1, acceptedEvents);
                        mList.setAdapter(mAdapter);
                    }
                }
            }
        }
    }

    private class InviteClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {

            Event titleEvent = (Event) parent.getAdapter().getItem(position);
            final int pos = position;

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:

                            Event event = (Event) parent.getAdapter().getItem(pos);

                            // remove event from invited list and add it to the accepted list
                            ParseUser me = ParseUser.getCurrentUser();
                            ArrayList<String> invited = (ArrayList<String>) me.get("invited");
                            ArrayList<String> accepted = (ArrayList<String>) me.get("accepted");
                            invited.remove(event.getObjectId());
                            accepted.add(event.getObjectId());
                            me.put("invited", invited);
                            me.put("accepted", accepted);
                            me.saveInBackground();


                            ParseQuery query = ParseQuery.getQuery("event");
                            query.whereContainedIn("objectId", invited);
                            ArrayList<Event> invitedEvents = new ArrayList<Event>();
                            try{
                                invitedEvents = (ArrayList<Event>) query.find();
                            }
                            catch (ParseException e){
                            }

                            // update event accepted list
                            ArrayList<String> invitees = event.getAcceptedList();
                            ArrayList<ParseGeoPoint> user_locations = event.getUserLocations();
                            user_locations.add(new ParseGeoPoint(0.0,0.0));
                            ArrayList<Double> init_distances = event.getInitDistances();
                            init_distances.add(-1.0);
                            ArrayList<Double> user_distances = event.getUserDistances();
                            user_distances.add(-1.0);
                            event.setInitDistances(init_distances);
                            event.setUserLocations(user_locations);
                            event.setUserDistances(user_distances);
                            invitees.add(ParseUser.getCurrentUser().getString("fbId"));
                            event.setAcceptedList(invitees);
                            event.saveInBackground();

                            // create installation query for the host
                            String hostId = event.getString("host");
                            ParseQuery installationQuery = ParseInstallation.getQuery();
                            installationQuery.whereContains("installationId", hostId);

                            // Send push notification to host that user accepted
                            ParsePush push = new ParsePush();
                            push.setQuery(installationQuery); // Set our Installation query
                            JSONObject jsonObj = new JSONObject();
                            try{
                                jsonObj.put("name", ParseUser.getCurrentUser().get("name"));
                                jsonObj.put("title", event.getTitle());
                                jsonObj.put("objectId", null);
                                jsonObj.put("accepted", true);
                            }
                            catch (JSONException j){
                            }
                            push.setData(jsonObj);
                            push.sendInBackground();

                            mAdapter = new InviteAdapter(mContext, android.R.layout.simple_list_item_1, invitedEvents);
                            mList.setAdapter(mAdapter);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            Event declineEvent = (Event) parent.getAdapter().getItem(pos);

                            // remove event from invite list
                            ParseUser me2 = ParseUser.getCurrentUser();
                            ArrayList<String> declineInvited = (ArrayList<String>) me2.get("invited");
                            declineInvited.remove(declineEvent.getObjectId());
                            me2.put("invited", declineInvited);
                            me2.saveInBackground();
                            String declineHostId = declineEvent.getString("host");

                            // get actual event objects from ids
                            ParseQuery declineQuery = ParseQuery.getQuery("event");
                            declineQuery.whereContainedIn("objectId", declineInvited);
                            ArrayList<Event> declineEvents = new ArrayList<Event>();
                            try {
                                declineEvents = (ArrayList<Event>) declineQuery.find();
                            } catch (ParseException e) {

                            }

                            // create installation query
                            ParseQuery declineInstallQ = ParseInstallation.getQuery();
                            declineInstallQ.whereContains("installationId", declineHostId);

                            // Send push notification to host that user declined invitation
                            ParsePush declinePush = new ParsePush();
                            declinePush.setQuery(declineInstallQ); // Set our Installation query
                            JSONObject jsonObj2 = new JSONObject();
                            try{
                                jsonObj2.put("name", ParseUser.getCurrentUser().get("name"));
                                jsonObj2.put("title", declineEvent.getTitle());
                                jsonObj2.put("objectId", null);
                                jsonObj2.put("accepted", false);
                            }
                            catch (JSONException j){
                            }
                            declinePush.setData(jsonObj2);
                            declinePush.sendInBackground();

                            mAdapter = new InviteAdapter(mContext, android.R.layout.simple_list_item_1, declineEvents);
                            mList.setAdapter(mAdapter);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(titleEvent.getTitle()).setPositiveButton("Accept", dialogClickListener)
                    .setNegativeButton("Decline", dialogClickListener).show();

        }
    }

    private class InviteAdapter extends ArrayAdapter<Event>{
        private InviteAdapter(Context context, int resource, ArrayList<Event> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            Event event = getItem(position);

            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(event.getTitle());

            return convertView;
        }
    }
}
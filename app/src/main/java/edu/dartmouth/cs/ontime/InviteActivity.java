package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class InviteActivity extends Activity {

    public ListView mList;
    public InviteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        ParseUser me = ParseUser.getCurrentUser();
        ArrayList<String> invitedList = (ArrayList<String>) me.get("invited");

        ParseQuery query = ParseQuery.getQuery("event");
        query.whereContainedIn("objectId", invitedList);
        ArrayList<Event> invitedEvents = new ArrayList<Event>();
        try{
            invitedEvents = (ArrayList<Event>) query.find();
        }
        catch (ParseException e){

        }


        InviteAdapter mAdapter = new InviteAdapter(this, android.R.layout.simple_list_item_1, invitedEvents);

        mList = (ListView)findViewById(R.id.list);
        mList.setAdapter(mAdapter);

        InviteClickListener listener = new InviteClickListener();

        mList.setOnItemClickListener(listener);

        MainActivity.ListUtils.setDynamicHeight(mList);
    }

    private class InviteClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Event event = (Event) parent.getAdapter().getItem(position);
            ParseUser me = ParseUser.getCurrentUser();
            ArrayList<String> invited = (ArrayList<String>) me.get("invited");
            ArrayList<String> accepted = (ArrayList<String>) me.get("accepted");
            invited.remove(event.getObjectId());
            accepted.add(event.getObjectId());
            me.put("invited", invited);
            me.put("accepted", accepted);
            me.saveInBackground();
            String hostId = event.getString("host");

            ParseQuery query = ParseQuery.getQuery("Event");
            query.whereContainedIn("objectId", invited);
            ArrayList<Event> invitedEvents = new ArrayList<Event>();
            try{
                invitedEvents = (ArrayList<Event>) query.find();
            }
            catch (ParseException e){
            }

            // create installation query
            ParseQuery installationQuery = ParseInstallation.getQuery();
            installationQuery.whereContains("installationId", hostId);

            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(installationQuery); // Set our Installation query
            push.setMessage(ParseUser.getCurrentUser().get("name") + " accepted the event: " + event.getTitle());
            push.sendInBackground();

            mAdapter = new InviteAdapter(getParent(), android.R.layout.simple_list_item_1, invitedEvents);
            mList.setAdapter(mAdapter);
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
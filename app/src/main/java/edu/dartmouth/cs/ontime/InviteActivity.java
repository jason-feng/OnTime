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
        ArrayList<Event> invitedList = (ArrayList<Event>) me.get("invited");

        InviteAdapter mAdapter = new InviteAdapter(this, R.layout.pending_list_item, invitedList);

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
            ArrayList<Event> invited = (ArrayList<Event>) me.get("invited");
            ArrayList<Event> accepted = (ArrayList<Event>) me.get("accepted");
            invited.remove(event);
            accepted.add(event);
            me.put("invited", invited);
            me.put("accepted", accepted);
            String hostId = event.getString("host");

            // create installation query
            ParseQuery installationQuery = ParseInstallation.getQuery();
            installationQuery.whereContains("installationId", hostId);

            // Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(installationQuery); // Set our Installation query
            push.setMessage(ParseUser.getCurrentUser().get("name") + " accepted the event: " + event.getTitle());
            push.sendInBackground();

            mAdapter = new InviteAdapter(getParent(), R.layout.pending_list_item, invited);
            mList.setAdapter(mAdapter);
        }
    }

    private class InviteAdapter extends ArrayAdapter<Event>{
        private InviteAdapter(Context context, int resource, ArrayList<Event> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Event event = getItem(position);
            TextView text = (TextView) convertView.findViewById(R.id.eventTitle);
            text.setText(event.getTitle());

            return convertView;
        }
    }
}
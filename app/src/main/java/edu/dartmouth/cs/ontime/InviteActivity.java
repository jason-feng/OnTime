package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class InviteActivity extends Activity {

    public ListView mList;

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

        mList = (ListView)findViewById(R.id.list);
        mList.setAdapter(new InviteAdapter(this, R.layout.pending_list_item, invitedList));

        MainActivity.ListUtils.setDynamicHeight(mList);
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
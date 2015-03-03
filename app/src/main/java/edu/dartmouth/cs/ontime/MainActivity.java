package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    private List<com.parse.ParseObject> upcomingEvents;
    private ListView mListToday,mListTomorrow,mListThisweek;
    private Context mContext;
    private ImageButton createEventButton, invitesButton, settingsButton;
    private NotificationManager mNotificationManager;
    private ArrayList<String> todayArray = new ArrayList<>();
    private ArrayList<String> tomorrowArray = new ArrayList<>();
    private ArrayList<String> thisWeekArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate");

        showNotification("Nick");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();
        //TODO: get person based on regId of phone (from server); for now this and events are hard-coded

        //upcomingEvents = person.getEvents()
        upcomingEvents = new ArrayList<ParseObject>();
        mContext = this;

        settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingsPage.class);
                startActivity(intent);
            }
        });

        invitesButton = (ImageButton)findViewById(R.id.invitesButton);
        invitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "invites");
                Intent intent = new Intent(mContext, InviteActivity.class);
                startActivity(intent);
            }
        });

        createEventButton = (ImageButton)findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "CreateEvent");
                Intent intent = new Intent(mContext, CreateEvent.class);
                startActivity(intent);
            }
        });

        Log.d(TAG, "createEvent init");

        query();

        todayArray = new ArrayList<>();
        tomorrowArray = new ArrayList<>();
        thisWeekArray = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        query();
    }

    public void loadEvents() {
        Log.d(TAG, "loadEvents");
        Log.d(TAG, "list size: " + upcomingEvents.size());
        //for each event in upcoming events list

        todayArray.clear();
        tomorrowArray.clear();
        thisWeekArray.clear();
        for (int i = 0; i < upcomingEvents.size(); i++) {

            ParseObject event = upcomingEvents.get(i);

            Calendar date = new GregorianCalendar();
            date.setTime(event.getDate("date"));
            long currentTime = System.currentTimeMillis();
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(currentTime);
            if (date.get(Calendar.DATE) == today.get(Calendar.DATE)) {
                Log.d(TAG, "COMPARING DATES: " + date.get(Calendar.DATE) + today.get(Calendar.DATE));
                todayArray.add(event.getString("title"));
            }
            else if (date.get(Calendar.DATE) == today.get(Calendar.DATE) +1) {
                Log.d(TAG, "COMPARING DATES: " + date.get(Calendar.DATE) + today.get(Calendar.DATE)+1);
                tomorrowArray.add(event.getString("title"));
            }
            //this line will return -1 if today.getTime is before the last day of the week
            else if (today.getTime().compareTo(getStartEndOFWeek(date.get(Calendar.WEEK_OF_YEAR), date.get(Calendar.YEAR))) == -1) {
                thisWeekArray.add(event.getString("title"));
            }
            else {
                //else, add to "upcoming events" field at bottom
            }

        }

        mListToday = (ListView)findViewById(R.id.listTd);
        mListToday.setBackgroundColor(Color.WHITE);
        mListTomorrow = (ListView)findViewById(R.id.listTm);
        mListTomorrow.setBackgroundColor(Color.WHITE);
        mListThisweek = (ListView)findViewById(R.id.listTw);
        mListThisweek.setBackgroundColor(Color.WHITE);

        mListToday.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todayArray));
        mListTomorrow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tomorrowArray));
        mListThisweek.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, thisWeekArray));

        ListUtils.setDynamicHeight(mListToday);
        ListUtils.setDynamicHeight(mListTomorrow);
        ListUtils.setDynamicHeight(mListThisweek);



        //when user selects event, fire EventDisplayActivity
        mListThisweek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
//                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
//                historyCode = cursor.getColumnIndex("_id");

                Intent intent = new Intent(getApplicationContext(), EventDisplayActivity.class);
                //intent.putExtra(EntryActivity.EXTRA_ENTRY_ID, historyCode);
                startActivity(intent);
            }
        });
    }

    public void query() {
        Log.d(TAG, "query()");
        ParseQuery<com.parse.ParseObject> query = ParseQuery.getQuery("event");
        query.findInBackground(new FindCallback<com.parse.ParseObject>() {
            @Override
            public void done(List<com.parse.ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "ParseQuery");
                    upcomingEvents = new ArrayList<ParseObject>();
                    for (ParseObject event : objects) {
                        Log.d(TAG, "Title : " + event.get("title"));
                        Log.d(TAG, "Date : " + event.get("date"));
                        upcomingEvents.add(event);
                    }
                    loadEvents();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public Date getStartEndOFWeek(int enterWeek, int enterYear){

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, enterWeek);
        calendar.set(Calendar.YEAR, enterYear);

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMM yyyy"); // PST`
        Date startDate = calendar.getTime();
        String startDateInStr = formatter.format(startDate);
        System.out.println("...date..."+startDateInStr);

        calendar.add(Calendar.DATE, 6);
        Date enddate = calendar.getTime();
        String endDaString = formatter.format(enddate);
        System.out.println("...date..."+endDaString);

        return enddate;

    }
    /**
     * Display a notification in the notification bar.
     */
    private void showNotification(String inviter) {

        // If notification pressed but not a button
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, InviteActivity.class), 0);

        // If accept is pressed
        PendingIntent acceptIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, InviteActivity.class), 0);

        // If decline is pressed
        PendingIntent declineIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, InviteActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(this.getString(R.string.service_label))
                .setContentText(
                        getResources().getString(R.string.service_started) + " " + inviter + "!")
                .setSmallIcon(R.drawable.notify)
                .setOngoing(true)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_accept, "Accept", acceptIntent)
                .addAction(R.drawable.ic_cancel, "Decline", declineIntent)
                .setContentIntent(contentIntent).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, notification);
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            else {
                int height = 0;
                int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                for (int i = 0; i < mListAdapter.getCount(); i++) {
                    View listItem = mListAdapter.getView(i, null, mListView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    height += listItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams params = mListView.getLayoutParams();
                params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
                mListView.setLayoutParams(params);
                mListView.requestLayout();
            }
        }
    }
}

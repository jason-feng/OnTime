package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.gcm.GoogleCloudMessaging;
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

    private static final String GCM_FILTER = "GCM_NOTIFY";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID_KEY = "registration_id";
    private static final String APP_VERSION_KEY = "appVersion";

    private ArrayList<ParseObject> upcomingEvents;
    private GoogleCloudMessaging gcm;
    private String regid;
    private Context mContext;
    private ImageButton createEventButton, settingsButton, invitesButton;
    private Button createEvent;
    private NotificationManager mNotificationManager;
    private IntentFilter mMessageIntentFilter;
    private BroadcastReceiver mMessageUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String inviter_name = intent.getStringExtra("invite_key");
            if (inviter_name != null) {
                showNotification(inviter_name);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "oncreate");

        showNotification("Nick");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FrameLayout layout =(FrameLayout)findViewById(R.id.background);
//        layout.setBackgroundResource(R.drawable.background_welcome);
//
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();
        //TODO: get person based on regId of phone (from server); for now this and events are hard-coded

        //upcomingEvents = person.getEvents()
        upcomingEvents = new ArrayList<ParseObject>();
        mContext = this;

        mMessageIntentFilter = new IntentFilter();
        mMessageIntentFilter.addAction(GCM_FILTER);

        createEvent = (Button)findViewById(R.id.createEvent);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "createEvent");
                Intent intent = new Intent(mContext, CreateEvent.class);
                startActivity(intent);
            }
        });
        settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log .d(TAG, "settings");
                Intent intent = new Intent(mContext, SettingsActivity.class);
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
            }
        });

        Log.d(TAG, "createEvent init");
        //get person based on regId of phone (from server); for now this and events are hard-coded

        ParseQuery<ParseObject> query = ParseQuery.getQuery("event");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    upcomingEvents = (ArrayList) objects;
                } else {
                    e.printStackTrace();
                }
            }
        });

        ArrayList<String> todayArray = new ArrayList<>();
        ArrayList<String> tomorrowArray = new ArrayList<>();
        ArrayList<String> thisWeekArray = new ArrayList<>();

        //for each event in upcoming events list
        for (int i = 0; i < upcomingEvents.size(); i++) {
            ParseObject event = upcomingEvents.get(i);
            Calendar date = new GregorianCalendar();
            date.setTime(event.getDate("date"));
            long currentTime = System.currentTimeMillis();
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(currentTime);
            if (date.get(Calendar.DATE) == today.get(Calendar.DATE)) {
                todayArray.add(event.getString("title"));

            }
            else if (date.get(Calendar.DATE) == today.get(Calendar.DATE) +1) {
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
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}

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
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    private static final String GCM_FILTER = "GCM_NOTIFY";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID_KEY = "registration_id";
    private static final String APP_VERSION_KEY = "appVersion";

    private ArrayList<Event> upcomingEvents;
    private GoogleCloudMessaging gcm;
    private String regid;
    private Context mContext;
    private ImageButton createEventButton, settingsButton, invitesButton;
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
        // Init parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mpKUYS0VHcJR1KQiVDQ8EUC0RDb5WRqB1gwUOuT4", "lP5IoGEkvcqBG9I3IxtXU5EtnEJiE2yHzX1bbZuq");

        Log.d(TAG, "oncreate");

        showNotification("Nick");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

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
        upcomingEvents = new ArrayList<Event>();
        mContext = this;

        mMessageIntentFilter = new IntentFilter();
        mMessageIntentFilter.addAction(GCM_FILTER);

        settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingsActivity.class);
                startActivity(intent);
            }
        });

        invitesButton = (ImageButton)findViewById(R.id.invitesButton);
        invitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InviteActivity.class);
                startActivity(intent);
            }
        });

        createEventButton = (ImageButton)findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "createEvent");
                Intent intent = new Intent(mContext, CreateEvent.class);
                startActivity(intent); 
            }
        });

        //get person based on regId of phone (from server); for now this and events are hard-coded
        /*
        if (checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(mContext);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        }
        **/

        Event event1 = new Event();
        Calendar newDateTime = Calendar.getInstance();
        newDateTime.set(2015, 3, 2, 12, 25);
        event1.setmDateTime(newDateTime);
        Location loc = new Location("");
        loc.setLatitude(43.704441);
        loc.setLongitude(-72.288693);
        event1.setmLocation(loc);
        event1.setmTitle("Dinner at Pine");
        upcomingEvents.add(event1);

        Event event2 = new Event();
        Calendar newDateTime2 = Calendar.getInstance();
        newDateTime2.set(2015, 3, 3, 12, 25);
        event2.setmDateTime(newDateTime2);
        Location loc2 = new Location("");
        loc2.setLatitude(43.701728);
        loc2.setLongitude(-72.288848);
        event2.setmLocation(loc);
        event2.setmTitle("Dinner w/ CS65");
        upcomingEvents.add(event2);

        Event event3 = new Event();
        Calendar newDateTime3 = Calendar.getInstance();
        newDateTime3.set(2015, 3, 3, 12, 25);
        event3.setmDateTime(newDateTime3);
        Location loc3 = new Location("");
        loc3.setLatitude(43.704451);
        loc3.setLongitude(-72.286643);
        event3.setmLocation(loc3);
        event3.setmTitle("DALI Group Meeting");
        upcomingEvents.add(event3);

        Event event4 = new Event();
        Calendar newDateTime4 = Calendar.getInstance();
        newDateTime4.set(2015, 3, 5, 12, 25);
        event4.setmDateTime(newDateTime4);
        Location loc4 = new Location("");
        loc4.setLatitude(43.702451);
        loc4.setLongitude(-72.286243);
        event4.setmLocation(loc4);
        event4.setmTitle("Lunch w/ Tim");
        upcomingEvents.add(event4);

        Event event5 = new Event();
        Calendar newDateTime5 = Calendar.getInstance();
        newDateTime5.set(2015, 3, 6, 12, 25);
        event5.setmDateTime(newDateTime5);
        Location loc5 = new Location("");
        loc5.setLatitude(43.703451);
        loc5.setLongitude(-72.286643);
        event5.setmLocation(loc5);
        event5.setmTitle("Work session");
        upcomingEvents.add(event5);

        Event event6 = new Event();
        Calendar newDateTime6 = Calendar.getInstance();
        newDateTime6.set(2015, 3, 7, 12, 25);
        event6.setmDateTime(newDateTime3);
        Location loc6 = new Location("");
        loc6.setLatitude(43.704451);
        loc6.setLongitude(-72.286653);
        event6.setmLocation(loc6);
        event6.setmTitle("Movie night");
        upcomingEvents.add(event6);

        Event event7 = new Event();
        Calendar newDateTime7 = Calendar.getInstance();
        newDateTime7.set(2015, 3, 8, 12, 25);
        event7.setmDateTime(newDateTime3);
        Location loc7 = new Location("");
        loc7.setLatitude(43.704451);
        loc7.setLongitude(-72.286643);
        event7.setmLocation(loc7);
        event5.setmTitle("Sledding");
        upcomingEvents.add(event7);

        String[] todayArray = new String[1];
        String[] tomorrowArray = new String[2];
        String[] thisWeekArray = new String[4];
        //for each event in upcoming events list
        for (int i = 0; i < upcomingEvents.size(); i++) {
            Calendar date = upcomingEvents.get(i).getmDateTime();
            long currentTime = System.currentTimeMillis();
            Calendar today = Calendar.getInstance();
            today.setTimeInMillis(currentTime);
            if (date.get(Calendar.DATE) == today.get(Calendar.DATE)) {
                //add to today's text
               // String[] todayArray = new String[1];
                todayArray[0] = upcomingEvents.get(0).getmTitle();

            }
            else if (date.get(Calendar.DATE) == today.get(Calendar.DATE) +1) {
               // String[] tomorrowArray = new String[2];
                tomorrowArray[0] = upcomingEvents.get(1).getmTitle();
                tomorrowArray[1] = upcomingEvents.get(2).getmTitle();
            }
            //this line will return -1 if today.getTime is before the last day of the week
            else if (today.getTime().compareTo(getStartEndOFWeek(date.get(Calendar.WEEK_OF_YEAR), date.get(Calendar.YEAR))) == -1) {
                //so add to this week text
              //  String[] thisWeekArray = new String[4];
                thisWeekArray[0] = upcomingEvents.get(3).getmTitle();
                thisWeekArray[1] = upcomingEvents.get(4).getmTitle();
                thisWeekArray[2] = upcomingEvents.get(5).getmTitle();
                thisWeekArray[3] = upcomingEvents.get(6).getmTitle();

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

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(REG_ID_KEY, "");
        if (registrationId.isEmpty()) {
            Log.i("", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(APP_VERSION_KEY, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("", "App version changed.");
            return "";
        }
        return registrationId;
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

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(mContext);
        int appVersion = getAppVersion(context);
        Log.d("", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID_KEY, regId);
        editor.putInt(APP_VERSION_KEY, appVersion);
        editor.commit();
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(getString(R.string.app_id));
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    ServerUtilities.sendRegistrationIdToBackend(mContext, regid);

                    // Persist the regID - no need to register again.
                    storeRegistrationId(mContext, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("", "gcm register msg: " + msg);
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onResume() {
        registerReceiver(mMessageUpdateReceiver, mMessageIntentFilter);
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageUpdateReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}

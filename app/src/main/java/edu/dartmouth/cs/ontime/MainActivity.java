package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import edu.dartmouth.cs.ontime.view.SlidingTabLayout;


public class MainActivity extends Activity {

    private static final String GCM_FILTER = "GCM_NOTIFY";
    private static final String SENDER_ID = "10622142242";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String REG_ID_KEY = "registration_id";
    private static final String APP_VERSION_KEY = "appVersion";

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Event> upcomingEvents;
    private Settings settingsFrag;

    GoogleCloudMessaging gcm;
    String regid;
    Context mContext;

    private IntentFilter mMessageIntentFilter;
    private BroadcastReceiver mMessageUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id_string = intent.getStringExtra("id_key");
            if (id_string != null) {
                // Do Stuff Here
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout =(LinearLayout)findViewById(R.id.background);

        mMessageIntentFilter = new IntentFilter();
        mMessageIntentFilter.addAction(GCM_FILTER);

        //get person based on regId of phone (from server); for now this and events are hard-coded
        if (checkPlayServices()){
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(mContext);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        }

        //sample event 1
        Event event1 = new Event();
        Calendar newDateTime = Calendar.getInstance();
        newDateTime.set(2015, 3, 1, 12, 25);
        event1.setmDateTime(newDateTime);
        Location loc = new Location("");
        loc.setLatitude(43.704441);
        loc.setLongitude(-72.288693);
        event1.setmLocation(loc);
        upcomingEvents.add(event1);

        Event event2 = new Event();
        Calendar newDateTime2 = Calendar.getInstance();
        newDateTime2.set(2015, 3, 2, 12, 25);
        event2.setmDateTime(newDateTime2);
        Location loc2 = new Location("");
        loc2.setLatitude(43.701728);
        loc2.setLongitude(-72.288848);
        event2.setmLocation(loc);
        upcomingEvents.add(event2);

        Event event3 = new Event();
        Calendar newDateTime3 = Calendar.getInstance();
        newDateTime3.set(2015, 3, 5, 12, 25);
        event3.setmDateTime(newDateTime3);
        Location loc3 = new Location("");
        loc3.setLatitude(43.704451);
        loc3.setLongitude(-72.286643);
        event3.setmLocation(loc3);
        upcomingEvents.add(event3);

        //TODO: iterate through upcomingEvents

        //for each event in upcoming events list
            //if it's today, add it to the today text
            //if it's tomorrow add it to tomorrow text
            //if it's this week, add it to this week text
            //else add it to upcoming events overall



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
                    regid = gcm.register(SENDER_ID);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}

package edu.dartmouth.cs.ontime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nick on 3/7/15.
 */
public class InviteReceiver extends ParsePushBroadcastReceiver{

    @Override
    public void onPushReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String message = extras != null ? extras.getString("com.parse.Data") : "";
        JSONObject jObject;
        String name = null;
        String title = null;
        String eventId = null;
        try {
            jObject = new JSONObject(message);
            name = (jObject.getString("name"));
            title = (jObject.getString("title"));
            eventId = (jObject.getString("objectId"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("NOTIFICATION", name + title + eventId);

        Intent mIntent = new Intent(App.getContext(), InviteActivity.class);

        // If notification pressed but not a button
        PendingIntent contentIntent = PendingIntent.getActivity(App.getContext(), 0, mIntent, 0);

        mIntent.putExtra("accept", true);
        mIntent.putExtra("eventId", eventId);

        // If accept is pressed
        PendingIntent acceptIntent = PendingIntent.getActivity(App.getContext(), 0, mIntent, 0);

        mIntent.putExtra("decline", true);

        // If decline is pressed
        PendingIntent declineIntent = PendingIntent.getActivity(App.getContext(), 0,
                new Intent(App.getContext(), InviteActivity.class), 0);

        Notification notification = new Notification.Builder(App.getContext())
                .setContentTitle("OnTime")
                .setContentText(name + " invited you to " + title + "!")
                .setSmallIcon(R.drawable.notify)
                .setOngoing(true)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_accept, "Accept", acceptIntent)
                .addAction(R.drawable.ic_cancel, "Decline", declineIntent)
                .setContentIntent(contentIntent).build();


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notification);

    }

}

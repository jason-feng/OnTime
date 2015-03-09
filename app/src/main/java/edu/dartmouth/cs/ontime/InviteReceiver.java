package edu.dartmouth.cs.ontime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nick on 3/7/15.
 */
public class InviteReceiver extends ParsePushBroadcastReceiver{

    @Override
    public void onPushReceive(Context context, Intent intent) {

        // get information from the JSONObject sent with the parse push
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

        // the notification is an accept notification if there is no eventId
        if (eventId == null){

            // handle accept/decline notification message
            String nMessage = "";
            try {
                jObject = new JSONObject(message);
                if (jObject.getBoolean("accepted")){
                    nMessage = " accepted the event ";
                }
                else{
                    nMessage = " declined the event ";
                }
            }
            catch (JSONException k){

            }


            PendingIntent contentIntent = PendingIntent.getActivity(App.getContext(), 0, new Intent(App.getContext(), MainActivity.class), 0);

            Notification notification = new Notification.Builder(App.getContext())
                    .setContentTitle("OnTime")
                    .setContentText(name + nMessage + title + "!")
                    .setSmallIcon(R.drawable.notify)
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent).build();

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, notification);
        }

        // Include option to accept/decline if it is an invite notification
        else{
            Intent mIntent = new Intent(App.getContext(), InviteActivity.class);
            Intent mIntent2 = new Intent(App.getContext(), MainActivity.class);
            Intent mIntent3 = new Intent(App.getContext(), MainActivity.class);

            // If notification pressed but not a button
            PendingIntent contentIntent = PendingIntent.getActivity(App.getContext(), 0, mIntent, 0);

            mIntent2.putExtra("accept", true);
            mIntent2.putExtra("eventId", eventId);

            // If accept is pressed
            PendingIntent acceptIntent = PendingIntent.getActivity(App.getContext(), 0, mIntent2, 0);

            mIntent3.putExtra("decline", true);
            mIntent3.putExtra("eventId", eventId);

            // If decline is pressed
            PendingIntent declineIntent = PendingIntent.getActivity(App.getContext(), 0, mIntent3, 0);

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

}

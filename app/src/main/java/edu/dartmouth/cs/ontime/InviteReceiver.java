package edu.dartmouth.cs.ontime;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by Nick on 3/7/15.
 */
public class InviteReceiver extends ParsePushBroadcastReceiver{
    @Override
    public void onPushOpen(Context context, Intent intent) {
        Intent i = new Intent(context, InviteActivity.class);
        context.startActivity(i);
    }

}

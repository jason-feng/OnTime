package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Activity that starts upon all launches of the app and determines whether the current user
 * is signed into facebook. If the user is, redirect to the main activity, if not redirect
 * to the sign in activity
 */
public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    ParseInstallation installation = null;
                    try{
                        installation = ParseInstallation.getCurrentInstallation().fetch();
                    }
                    catch (ParseException k){

                    }
                    ParseUser currUser = (ParseUser) object;
                    // Check if there is current user info
                    if (currUser != null) {
                        currUser.put("installationId", installation);
                        // Start an intent for the logged in activity
                        Log.d("F8Debug", "onCreate, got user,  "
                                + ParseUser.getCurrentUser().getUsername());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        // Start and intent for the logged out activity
                        Log.d("F8Debug", "onCreate, no user");
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    }
                }
            });
        }
        else{
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        }
        finish();
    }
}
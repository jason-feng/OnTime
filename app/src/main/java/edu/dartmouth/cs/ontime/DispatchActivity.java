package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser.getCurrentUser().fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                ParseUser currUser = (ParseUser) object;
                // Check if there is current user info
                if (currUser != null) {
                    // Start an intent for the logged in activity
                    Log.d("F8Debug", "onCreate, got user,  "
                            + ParseUser.getCurrentUser().getUsername());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    // Start and intent for the logged out activity
                    Log.d("F8Debug", "onCreate, no user");
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        });
    }
}
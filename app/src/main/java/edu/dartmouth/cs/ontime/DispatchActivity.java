package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            Log.d("F8Debug", "onCreate, got user,  "
                    + ParseUser.getCurrentUser().getUsername());
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Start and intent for the logged out activity
            Log.d("F8Debug", "onCreate, no user");
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

}
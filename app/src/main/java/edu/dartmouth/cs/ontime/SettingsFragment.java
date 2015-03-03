package edu.dartmouth.cs.ontime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by garygreene on 2/17/15.
 */
public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    private UiLifecycleHelper uiHelper;
    private Context mContext;
    public ArrayList<Friend> friendsArray;
    Friend me = null;
    ParseObject meParse;
    TextView name;
    ImageView propic;
    String myfriends = "";


    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsArray = new ArrayList<Friend>();

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        authButton.setReadPermissions(Arrays.asList("user_friends"));
        authButton.setFragment(this);
        name = (TextView) view.findViewById(R.id.name);
        propic = (ImageView) view.findViewById(R.id.propic);

        return view;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            new Request(
                    session,
                    "/me",
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            GraphObject graphObject = response.getGraphObject();
                            JSONObject object = graphObject.getInnerJSONObject();
                            try {
                                URL imgUrl = new URL("https://graph.facebook.com/"
                                        + object.getString("id") + "/picture?type=large");
                                Log.d("url",imgUrl.toString());
                                me = new Friend(object.getString("name"),object.getString("id"),imgUrl);
                                new DownloadImageTask(propic)
                                        .execute(imgUrl.toString());

                                name.setText("Logged in as: " +object.getString("name"));
                                meParse = new ParseObject("Friend");
                                meParse.put("name",object.getString("name"));
                                meParse.put("idNum",object.getString("id"));

//                                meParse.put("photo",imgUrl.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("me",me.name);

                        }
                    }
            ).executeAsync();

            new Request(
                    session,
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                                /* handle the result */
                            GraphObject graphObject = response.getGraphObject();
                            JSONObject json = graphObject.getInnerJSONObject();
                            JSONArray friends = null;
                            try {
                                friends = json.getJSONArray("data");
                                Log.d("hello",friends.toString());
                                if (friends.length() > 0) {

                                    // Ensure the user has at least one friend ...
                                    for (int i = 0; i < friends.length(); i++) {
                                        JSONObject friend = friends.getJSONObject(i);
                                        String name = friend.getString("name");
                                        String id = friend.getString("id");
                                        Log.d("name",name);
                                        URL imgUrl = new URL("http://graph.facebook.com/"
                                                + friend.getString("id") + "/picture?type=large");
                                        friendsArray.add(new Friend(friend.getString("name"),friend.getString("id"),imgUrl));
                                        myfriends += friend.getString("name") + ", ";
                                        ParseObject parseFriend = new ParseObject("Friend");
                                        parseFriend.put("name",friend.getString("name"));
                                        parseFriend.put("idNum",friend.getString("id"));
                                        parseFriend.put("friends",me.getName());
                                        parseFriend.saveInBackground();
                                    }
                                    me.add_friends(friendsArray);
                                    meParse.put("friends",myfriends);
//                                    meParse.put("friends",friendsArray);

                                }
                               meParse.saveInBackground();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }


                        }
                    }
            ).executeAsync();

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        /* make the API call */

    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


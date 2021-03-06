package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * This is a class to query the current user's friends from facebook and display them in a listview where
 * you can check next to or click on the friends that you want to invite to the event
 */

public class FriendList extends Activity {
    MyCustomAdapter dataAdapter = null;
    Friend newfriend;
    Button ok;
    private UiLifecycleHelper uiHelper;
    public ArrayList<Friend> friendList;
    public ArrayList<String> selectedFriends;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        friendList = new ArrayList<Friend>();
        selectedFriends = new ArrayList<String>();

        // add selected friends to the event by sending an intent back
        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int i;
               for (i=0;i<friendList.size();i++){
                   if (friendList.get(i).isSelected()) {
                       // add fb IDs to selected list
                       selectedFriends.add(friendList.get(i).getId());
                       Log.d("friend added",friendList.get(i).getName());
                   }
               }
               Intent intent = new Intent();
               intent.putStringArrayListExtra("selected_friends", selectedFriends);
               setResult(RESULT_OK, intent);
               finish();
           }
        });

        // make sure session is opened
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
        getFBinfo();
    }

    /*
    Get a list of the user's friends who have registered with OnTime using a session request
     */
    private void getFBinfo() {
        Log.d("got", "here");
        Session session = Session.getActiveSession();

        if(session==null) {
            // try to restore from cache
            session = Session.openActiveSessionFromCache(getApplicationContext());
        }
        // query the user's friends from facebook using their interface
        new Request(session,
                "/me/friends",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response) {
                                /* handle the result */
                        Log.d("here","got");
                        GraphObject graphObject = response.getGraphObject();
                        if (graphObject != null) {
                            JSONObject json = graphObject.getInnerJSONObject();
                            JSONArray friends = null;
                            try {
                                friends = json.getJSONArray("data");
                                Log.d("hello", friends.toString());

                                // Ensure the user has at least one friend ...
                                if (friends.length() > 0) {
                                    // create a friend object for each friend and add each to the list of friends
                                    for (int i = 0; i < friends.length(); i++) {
                                        JSONObject friend = friends.getJSONObject(i);
                                        String name = friend.getString("name");
                                        Log.d("name", name);
                                        URL imgUrl = new URL("http://graph.facebook.com/"
                                                + friend.getString("id") + "/picture?type=large");
                                        newfriend = new Friend(friend.getString("name"), friend.getString("id"), imgUrl);
                                        friendList.add(newfriend);
                                    }
                                    displayListView();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            // session was not opened correctly, request a retry
                            Toast.makeText(getApplicationContext(), "Trying to get friends!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("retry", true);
                            setResult(14, intent);
                            finish();
                        }
                    }
                }).executeAsync();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("fb", "Logged in...");

        } else if (state.isClosed()) {
            Log.i("fb", "Logged out...");
        }
    }

    private void displayListView() {

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.friend_info, friendList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        Log.d("we","here");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Friend friend = (Friend) parent.getItemAtPosition(position);
                friend.setSelected(true);
                Toast.makeText(getApplicationContext(),
                        friend.getName() + " selected!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Custom lisview adapter that contains checkboxes and friends' names
     */

    private class MyCustomAdapter extends ArrayAdapter<Friend> {

        private ArrayList<Friend> friendsList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Friend> friendsList) {
            super(context, textViewResourceId, friendsList);
            this.friendsList = new ArrayList<Friend>();
            this.friendsList.addAll(friendsList);

            Log.d("friends",this.friendsList.toString());
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        // custom view for the checkbox and name combination
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.friend_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.friendName);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();

                        friendsList.get(getPosition).setSelected(buttonView.isChecked());

                    }
                });

                holder.name.setTag(position);

            }

            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Friend friend = friendsList.get(position);
            holder.code.setText(friend.getName());
            holder.name.setChecked(friend.isSelected());

            return convertView;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


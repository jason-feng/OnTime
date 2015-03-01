package edu.dartmouth.cs.ontime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;


public class CreateEvent extends ListActivity {

        static final String[] FACULTY = new String[] { "Title","Date", "Time", "Location", "Invitees"};
        private static final String TAG = "CreateEvent";

        public ArrayList mSelectedItems;
        public ListView list;
        public static int[] intCal;
        public String actType;
        private Context context;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_event);

            context = this;

            intCal = new int[6];
            list = (ListView)this.findViewById(android.R.id.list);

            //setContentView(R.layout.activity_manual_entry);
            ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, FACULTY);
            setListAdapter(mAdapter);

            // Define the listener interface
            //when each item is clicked, show the respective fragment
            AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    DialogFragment newFragment;
                    switch (position) {
                        case 0: // Title
                            newFragment = CommentDialogFragment.newInstance(R.string.title);
                            newFragment.show(getFragmentManager(), "dialog");
                            break;
                        case 1: //date
                            newFragment = new DatePickerFragment();
                            newFragment.show(getFragmentManager(), "datePicker");
                            break;
                        case 2: // time
                            newFragment = new TimePickerFragment();
                            newFragment.show(getFragmentManager(), "timePicker");
                            break;
                        case 3: //location
//                            MapFragment fragment = new MapFragment();
//                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            startActivity(new Intent(context, MapDialogFragment.class));
                            break;
                        case 4: //Invites
                            newFragment = new NewListUsersDialogFragment()
                                    .newInstance(R.string.pick_friends);
                            newFragment.show(getFragmentManager(), "dialog");
                            break;
                    }
                }
            };

            // Get the ListView and wired the listener
            //ListView listView = getListView();
            list.setOnItemClickListener(mListener);
            // mHelper.close();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_create_event, menu);
            return true;
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

        // ******************** Fragment Classes *************************//

    public class MapDialogFragment extends FragmentActivity
            implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

        private GoogleMap mMap;
        private TextView mTapTextView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            setContentView(R.layout.events_demo);

//            mTapTextView = (TextView) findViewById(R.id.tap_text);

            setUpMapIfNeeded();
        }

        /**
         * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
         * installed) and the map has not already been instantiated.. This will ensure that we only ever
         * call {@link #setUpMap()} once when {@link #mMap} is not null.
         * <p/>
         * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
         * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
         * install/update the Google Play services APK on their device.
         * <p/>
         * A user can return to this FragmentActivity after following the prompt and correctly
         * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
         * have been completely destroyed during this process (it is likely that it would only be
         * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
         * method in {@link #onResume()} to guarantee that it will be called.
         */
        private void setUpMapIfNeeded() {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        }


        private void setUpMap() //If the setUpMapIfNeeded(); is needed then...
        {
            mMap.setOnMapClickListener(this);
            mMap.setOnMapLongClickListener(this);
        }

        @Override
        public void onMapClick(LatLng point) {
            mTapTextView.setText("tapped, point=" + point);
        }

        @Override
        public void onMapLongClick(LatLng point) {
            mTapTextView.setText("long pressed, point=" + point);
        }
    }

        //provided by Android Developers for Date Picker Fragment
        public static class DatePickerFragment extends DialogFragment
                implements DatePickerDialog.OnDateSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), this, year, month, day);
            }

            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Do something with the date chosen by the user
                intCal[3] = month;
                intCal[4] = day;
                intCal[5] = year;
                // newEntry.setDate(year, month, day);
            }

        }

        //provided by Android studio developers for TimePickerFragment
        public static class TimePickerFragment extends DialogFragment
                implements TimePickerDialog.OnTimeSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute,
//                    DateFormat.is24HourFormat(getActivity()));
                return new TimePickerDialog(getActivity(), this, hour, minute,
                        false);
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                intCal[0] = hourOfDay;
                intCal[1] = minute;
                intCal[2] = 0;
            }
        }

        //Adapted from FragmentDialogAlarmActivity presented in class notes
        public static class MyAlertDialogFragment extends DialogFragment {

            private static final String TAG = "tryThis";

            public static MyAlertDialogFragment newInstance(int title) {
                MyAlertDialogFragment frag = new MyAlertDialogFragment();
                Bundle args = new Bundle();
                args.putInt("title", title);
                frag.setArguments(args);
                return frag;
            }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final int title = getArguments().getInt("title");
                // final EditText servername = new EditText(getActivity());
                final EditText input = new EditText(getActivity().getApplicationContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                return new AlertDialog.Builder(getActivity())
                        //.setIcon(R.drawable.alert_dialog_dart_icon)
                        .setView(input)
                        .setTitle(title)
                                // Set an EditText view to get user input
                        .setPositiveButton(R.string.alert_dialog_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        ((CreateEvent) getActivity())
                                                .doPositiveClick(input, title);
                                    }
                                })
                        .setNegativeButton(R.string.alert_dialog_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        ((CreateEvent) getActivity())
                                                .doNegativeClick();
                                    }
                                }).create();
            }
        }


    //Adapted from FragmentDialogAlarmActivity presented in class notes
    public static class NewListUsersDialogFragment extends DialogFragment {

        private static final String TAG = "tryThis";

        public static NewListUsersDialogFragment newInstance(int title) {
            NewListUsersDialogFragment frag = new NewListUsersDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //mSelectedItems = new ArrayList();  // Where we track the selected items
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Set the dialog title
            builder.setTitle("Choose Friends")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener through which to receive callbacks when items are selected
                    .setMultiChoiceItems(R.array.names_array, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    if (isChecked) {
                                        // If the user checked the item, add it to the selected items
                                        //mSelectedItems.add(which);
                                    } //else if (mSelectedItems.contains(which)) {
                                        // Else, if the item is already in the array, remove it
                                       // mSelectedItems.remove(Integer.valueOf(which));
                                    //}
                                }
                            })
                            // Set the action buttons
                    .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog

                        }
                    })
                    .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            return builder.create();
        }
    }


        //for comment dialog
        public static class CommentDialogFragment extends DialogFragment {

            public static CommentDialogFragment newInstance(int title) {
                CommentDialogFragment frag = new CommentDialogFragment();
                Bundle args = new Bundle();
                args.putInt("title", title);
                frag.setArguments(args);
                return frag;
            }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final int title = getArguments().getInt("title");
                // final EditText servername = new EditText(getActivity());
                final EditText input = new EditText(getActivity().getApplicationContext());

                return new AlertDialog.Builder(getActivity())
                        .setView(input)
                        .setTitle(title)
                        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                            ((CreateEvent) getActivity()).doPositiveClick(input, title);
                                    }
                                })
                        .setNegativeButton(R.string.alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    ((CreateEvent) getActivity())
                                            .doNegativeClick();
                                }
                        }).create();
            }
        }

        // *********** Helper functions to show fragments ****************//

        void commentDialog() {
            DialogFragment newFragment = CommentDialogFragment
                    .newInstance(R.string.comment_prompt);
            newFragment.show(getFragmentManager(), "dialog");
        }

        //these are empty for now; will be sending data soon
        public void doPositiveClick(EditText input, int title) {

            Log.i("FragmentAlertDialog", "Positive click!");

        }


        public void doNegativeClick() {
            Log.i("FragmentAlertDialog", "Negative click!");
        }

        //if user clicks save, exit the view
        public void onSaveClicked(View v) {

            Toast.makeText(getApplicationContext(), getString(R.string.save_message), Toast.LENGTH_SHORT).show();
            this.finish();
        }

        //if user clicks cancel, do not save the profile; just exit the program.
        public void onCancelClicked(View v) {
            Toast.makeText(getApplicationContext(), getString(R.string.discard_message), Toast.LENGTH_SHORT).show();
//            mHelper.close();
            this.finish();
        }

    }



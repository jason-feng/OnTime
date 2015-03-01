package edu.dartmouth.cs.ontime;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventDialogFragments extends DialogFragment {

    public static final String TAG = "MyRunsFragment.java";
    public static final int DIALOG_ID_ERROR = -1;
    public static final int DIALOG_ID_TITLE = 0;
    public static final int DIALOG_ID_DATE = 1;
    public static final int DIALOG_ID_TIME = 2;
    public static final int DIALOG_ID_LOCATION = 3;
    public static final int DIALOG_ID_INVITES = 4;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        int currentPosition = getArguments().getInt(CreateEvent.INDEX);
        final Activity parent = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        final EditText et = new EditText(parent);
        switch(currentPosition) {
            case DIALOG_ID_LOCATION:
                Intent intent = new Intent(getActivity(), MapFragment.class);
                startActivity(intent);
                break;
            case DIALOG_ID_DATE:
                DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        Log.d(TAG, "onDateSet()");
                        Calendar cal = new GregorianCalendar();
                        cal.set(year, month, day);
                    }
                };

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog and return it
                return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
            case DIALOG_ID_TIME:
                TimePickerDialog.OnTimeSetListener onTimeSet = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        Log.d(TAG, "onTimeSet()");
                        Calendar cal = new GregorianCalendar();
                        cal.set(hour, minute);
                        ((CreateEvent)getActivity()).getEvent().setmDateTime(cal);
                    }
                };
                // Use the current date as the default date in the picker
                final Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                boolean twentyfour = false;
                // Create a new instance of DatePickerDialog and return it
                return new TimePickerDialog(getActivity(), onTimeSet, hour, minute, twentyfour);
            case DIALOG_ID_TITLE:
                Log.d(TAG, "TITLE fragment");
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setHint(R.string.hint_title);
                builder.setTitle(R.string.ui_title);
                builder.setView(et);
                builder.setPositiveButton(R.string.dialog_fragment_positive_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((CreateEvent)getActivity()).getEvent().setmTitle(et.getText().toString());
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton(R.string.dialog_fragment_negative_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
            case DIALOG_ID_INVITES:
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
        return null;
    }
}
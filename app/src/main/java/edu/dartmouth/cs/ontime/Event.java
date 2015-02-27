package edu.dartmouth.cs.ontime;

import android.location.Location;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jasonfeng on 1/27/15.
 */
public class Event {

    private Long id;
    private Calendar mDateTime;
    private Location mLocation;
    private ArrayList<Person> people;

    public Event() {
        this.mDateTime = Calendar.getInstance();
        this.mLocation = null;
        this.people = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Calendar mDateTime) {
        this.mDateTime = mDateTime;
    }

}

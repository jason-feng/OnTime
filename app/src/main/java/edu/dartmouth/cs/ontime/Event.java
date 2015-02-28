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
    private String mTitle;
    private ArrayList<Person> people;

    public Event() {
        this.mDateTime = Calendar.getInstance();
        this.mLocation = null;
        this.mTitle = "";
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

    public String getmTitle() { return mTitle; }

    public void setmTitle(String mTitle) { this.mTitle = mTitle; }

    public void setmDateTime(Calendar mDateTime) {
        this.mDateTime = mDateTime;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }
}

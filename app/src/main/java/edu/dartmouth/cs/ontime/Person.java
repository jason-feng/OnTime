package edu.dartmouth.cs.ontime;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by jasonfeng on 1/27/15.
 */
public class Person {

    private Long id;
    private String name;
    private Bitmap image;
    private ArrayList<Event> events;
    private Location mLocation;

    public Person() {
        name = "";
        image = null;
        events = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}

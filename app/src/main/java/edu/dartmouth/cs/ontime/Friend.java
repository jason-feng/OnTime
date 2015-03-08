package edu.dartmouth.cs.ontime;

import java.net.URL;
import java.util.ArrayList;

/**
 * This is a class to hold the name, facebook id, and url locally (not in the cloud) to facilitate accessing user info
 */
public class Friend {
    String name;
    String id;
    URL url;
    ArrayList<Friend> friend = null;
    boolean selected = false;

    public Friend(String name, String id,URL url) {
        this.name = name;
        this.id = id;
        this.url = url;
    }

    public void add_friends(ArrayList<Friend> friends) {
        this.friend = friends;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

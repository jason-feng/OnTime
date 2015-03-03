package edu.dartmouth.cs.ontime;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by garygreene on 3/2/15.
 */
public class Friend {
    String name;
    String id;
    URL url;
    ArrayList<Friend> friend = null;

    public Friend(String name, String id,URL url) {
        this.name = name;
        this.id = id;
        this.url = url;
    }

    public void add_friends(ArrayList<Friend> friends) {
        this.friend = friends;
    }
}

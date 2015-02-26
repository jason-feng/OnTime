package edu.dartmouth.cs.ontime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ActionTabsViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public static final int HOME = 0;
    public static final int CREATE = 1;
    public static final int SETTINGS = 2;

    public static final String UI_TAB_HOME = "HOME";
    public static final String UI_TAB_CREATE = "CREATE";
    public static final String UI_TAB_SETTINGS = "SETTINGS";

    public ActionTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int position){
        return fragments.get(position);
    }

    public int getCount(){
        return fragments.size();
    }

    public CharSequence getPageTitle(int position){
        switch (position) {
            case HOME:
                return UI_TAB_HOME;
            case CREATE:
                return UI_TAB_CREATE;
            case SETTINGS:
                return UI_TAB_SETTINGS;
            default:
                break;
        }
        return null;
    }
}

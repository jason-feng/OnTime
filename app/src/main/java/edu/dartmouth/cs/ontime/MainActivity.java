package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import edu.dartmouth.cs.ontime.view.SlidingTabLayout;


public class MainActivity extends Activity {

    //OnTime demo 1
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPagerAdapter;
    private HomeFragment homeFrag;
    private CreateFragment createFrag;
    private Settings settingsFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // startService(new Intent(MainActivity.this, TrackingService.class));

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (savedInstanceState != null){
            homeFrag = (HomeFragment) getFragmentManager().getFragment(savedInstanceState, HomeFragment.class.getName());
            createFrag = (CreateFragment) getFragmentManager().getFragment(savedInstanceState, CreateFragment.class.getName());
            settingsFrag = (Settings) getFragmentManager().getFragment(savedInstanceState, Settings.class.getName());
        }

        if (homeFrag == null){
            homeFrag = new HomeFragment();
        }
        if (createFrag == null){
            createFrag = new CreateFragment();
        }
        if (settingsFrag == null){
            settingsFrag = new Settings();
        }

        // create a fragment list in order.
        fragments = new ArrayList<Fragment>();
        fragments.add(homeFrag);
        fragments.add(createFrag);
        fragments.add(settingsFrag);

        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles) and ViewPager (different pages of fragment) together.
        myViewPagerAdapter =new ActionTabsViewPagerAdapter(getFragmentManager(),
                fragments);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        // make tabs the appropriate design
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, HomeFragment.class.getName(), homeFrag);
        getFragmentManager().putFragment(outState, CreateFragment.class.getName(), createFrag);
        getFragmentManager().putFragment(outState, Settings.class.getName(), settingsFrag);
    }
}

package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by garygreene on 2/17/15.
 */
public class Settings extends PreferenceFragment {

    private Context myContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.bokeh1copy3);

        final ActionBar actionBar = getActivity().getActionBar();

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.hide();



//        ListPreference listPreference = (ListPreference) findPreference("list_preference");
//        if (listPreference.getValue() == null) {
//            listPreference.setValueIndex(1);
//        }
//
//        String currValue = listPreference.getValue();
//
//
//        String mKey = getString(R.string.conversion);
//        SharedPreferences mPreference = getActivity().getApplicationContext().getSharedPreferences(mKey, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor mEditor = mPreference.edit();
//        mEditor.putString(mKey,currValue);
//        mEditor.commit();

        addPreferencesFromResource(R.xml.settings);
//        myContext = this.getActivity();
//        myContext.setTheme(R.style.MyPreferenceTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(R.drawable.bokehcheat);
        //view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

//        TextView tv = new TextView (this.getActivity());
//        tv.setTextColor (Color.BLACK);
//        tv.setText("Hi There", TextView.BufferType.NORMAL);
//        LayoutParams layoutParams  = new LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
////        tv.setBackgroundColor(Color.WHITE);
//        pms.setMargins(100, 900, 0, 500);
//        tv.setLayoutParams(pms);

        //view.setTop(50);
//        view.setX(500);
//        view.setY(500);

        return view;
    }
}

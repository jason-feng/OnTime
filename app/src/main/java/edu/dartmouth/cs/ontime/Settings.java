package edu.dartmouth.cs.ontime;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by garygreene on 2/17/15.
 */
public class Settings extends Fragment {

    private Context myContext;
    private ListView mListFB, mListLocations;
    private CheckBox mMe, mOthers;
    public static final String INDEX = "Settings.java";
    private Button mSave, mCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        myContext = getActivity();
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

//        addPreferencesFromResource(R.xml.settings);
//        myContext = this.getActivity();
//        myContext.setTheme(R.style.MyPreferenceTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.settings_listview_layout, container, false);
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
        mSave = (Button) view.findViewById(R.id.save_button_settings);
        mSave.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         String mKey = getString(R.string.preference);
                                         SharedPreferences mPreference = getActivity().getSharedPreferences(mKey,getActivity().MODE_PRIVATE);

                                         SharedPreferences.Editor mEditor = mPreference.edit();
                                         mEditor.clear();
                                         mKey = getString(R.string.me);
                                         boolean me = mMe.isSelected();

                                         mEditor.putBoolean(mKey,me);
                                         mKey = getString(R.string.others);
                                         boolean others = mOthers.isSelected();
                                         Log.d("put",Boolean.toString(others));
                                         mEditor.putBoolean(mKey,others);
                                         mEditor.commit();
                                         Toast.makeText(myContext,
                                                 getString(R.string.pref_saved), Toast.LENGTH_SHORT).show();
                                         getActivity().finish();
                                     }
                                 });
        mCancel = (Button) view.findViewById(R.id.cancel_button_settings);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(myContext,
                        getString(R.string.pref_cancel), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        mListFB = (ListView) view.findViewById(R.id.listFb);
        mListFB.setBackgroundColor(Color.WHITE);
        mListLocations = (ListView) view.findViewById(R.id.listLoc);
        mListLocations.setBackgroundColor(Color.WHITE);
        mMe = (CheckBox) view.findViewById(R.id.checkMe);
        mMe.setBackgroundColor(Color.WHITE);
        mMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mMe.setSelected(true);
            }
        });
        mOthers = (CheckBox) view.findViewById(R.id.checkOthers);
        mOthers.setBackgroundColor(Color.WHITE);
        mOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOthers.setSelected(true);
            }
        });

        String mKey = getString(R.string.preference);
        SharedPreferences mPreference = getActivity().getSharedPreferences(mKey,getActivity().MODE_PRIVATE);

        mKey = getString(R.string.me);
        if (mPreference.contains(mKey)) {
            boolean mValue = mPreference.getBoolean(mKey,false);
            mMe.setSelected(mValue);
        }
        mKey = getString(R.string.others);
        if (mPreference.contains(mKey)) {
            Log.d("does","contain");
            boolean mValue = mPreference.getBoolean(mKey,false);
            mOthers.setSelected(mValue);
        }


        String[] fbArray = {"Login with Facebook"};

        ArrayAdapter<String> fbadapter = new ArrayAdapter<String>(this.myContext,
                android.R.layout.simple_list_item_1, fbArray);


        String[] locArray = new String[2];
        locArray[0] = "Home Address";
        locArray[1] = "Work Address";

        ArrayAdapter<String> locadapter = new ArrayAdapter<String>(this.myContext,
                android.R.layout.simple_list_item_1, locArray);

        mListFB.setAdapter(fbadapter);
        mListLocations.setAdapter(locadapter);

        MainActivity.ListUtils.setDynamicHeight(mListFB);
        MainActivity.ListUtils.setDynamicHeight(mListLocations);

        mListFB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(myContext, SettingsActivity.class));
            }
        });

        mListLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("pos", Integer.toString(position));
                Bundle bundle = new Bundle();
                bundle.putInt(INDEX, position);
                SettingDialogFragments fragment = new SettingDialogFragments();
                fragment.setArguments(bundle);
                fragment.show(getActivity().getFragmentManager(), "dialog");

            }
        });
        return view;

    }


}


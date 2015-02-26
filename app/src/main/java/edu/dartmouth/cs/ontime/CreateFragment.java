package edu.dartmouth.cs.ontime;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

public class CreateFragment extends Fragment {
    Button createButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        createButton = (Button) view.findViewById(R.id.createButton);


        createButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), CreateEvent.class);
                    //put the activity type in an intent

                    startActivity(intent);
                }


        });
        return view;
    }

}

package edu.dartmouth.cs.ontime;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * MapFragment that allows you to search through a map and select a pin corresponding to a location
 * to set as the event location
 *
 * Code credit to George Matthew: wptrafficanalyzer.in
 */
public class MapFragment extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private static final String TAG = "MapFragment";
    private static final int MENU_ID_SEARCH = 0;
    private GoogleMap mMap;
    private Marker mMarker;
    private Context context;
    private boolean markerClicked;
    private Button save_button;
    private Button cancel_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        markerClicked = false;
        setUpMapIfNeeded();
        Intent intent = getIntent();
        if (intent.getAction() != null) {
            handleIntent(intent);
        }
        mMarker = null;
        context = this;
        save_button = (Button)findViewById(R.id.startSaveButton);
        cancel_button = (Button)findViewById(R.id.startCancelButton);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        else {
            Log.d(TAG, "onActivityResult");
            Intent intent = new Intent(this, CreateEvent.class);
            intent.putExtra("MARKER_LATITUDE", data.getDoubleExtra("MARKER_LATITUDE", 0.0));
            intent.putExtra("MARKER_LONGITUDE", data.getDoubleExtra("MARKER_LONGITUDE", 0.0));
            startActivity(intent);
        }
    }

    public void onStartSaveClicked(View v) {
        Log.d(TAG, "onStartSaveClicked");
        if (mMarker != null) {
            Intent intent = new Intent();
            intent.putExtra("LAT", mMarker.getPosition().latitude);
            intent.putExtra("LONG", mMarker.getPosition().longitude);
            setResult(RESULT_OK, intent);
        }
        CreateEvent.setDialogField(3,true);
        finish();
    }

    public void onStartCancelClicked(View v) {
        Log.d(TAG, "onStartCancelClicked");
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add(Menu.NONE, MENU_ID_SEARCH, MENU_ID_SEARCH, "Search");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        String searchText = ((App)this.getApplicationContext()).getSearchText();
        if (searchText != null) {
            doSearch(searchText);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == MENU_ID_SEARCH) {
            onSearchRequested();
            return true;
        }

        finish();
        return false;
    }

   /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() //If the setUpMapIfNeeded(); is needed then...
    {
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                mMarker = marker;
                return true;
            }
        });
    }

    private void handleIntent(Intent intent){
        Log.d(TAG, "handleIntent");
        if(intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String searchText = intent.getStringExtra(SearchManager.QUERY);
            ((App)this.getApplicationContext()).setSearchText(searchText);
            this.finish();
        }
        else if(intent.getAction().equals(Intent.ACTION_VIEW)){
            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        setIntent(intent);
        handleIntent(intent);
    }

    private void doSearch(String query){
        Log.d(TAG, "doSearch");
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(1, data, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        Log.d(TAG, "onCreateLoader");
        CursorLoader cLoader = null;
        if(arg0==0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
        else if(arg0==1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {
        Log.d(TAG, "onLoadFinished");
        showLocations(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }

    private void showLocations(Cursor c){
        Log.d(TAG, "showLocations");
        MarkerOptions markerOptions = null;
        LatLng position = null;
        mMap.clear();
        while(c.moveToNext()){
            markerOptions = new MarkerOptions();
            position = new LatLng(Double.parseDouble(c.getString(1)),Double.parseDouble(c.getString(2)));
            markerOptions.position(position);
            markerOptions.title(c.getString(0));
            mMap.addMarker(markerOptions);
        }
        if(position!=null){
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
            mMap.animateCamera(cameraPosition);
        }
    }
}
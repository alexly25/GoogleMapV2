package com.alex.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 14.12.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class MapActivity extends FragmentActivity {
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

}
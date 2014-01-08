package com.alex.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import ru.shem.services.Variables;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 14.12.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class MapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String LOG = "mylogMapActivity";
    private GoogleMap map;
    private ArrayList<Location> locationArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        LatLng defaultCoordinatesMap = new LatLng(53.217482, 50.112419);
        locationArrayList = Variables.getInstance().getLocationArrayList(); // Получаем маркеры

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)) // Получаем карту
                .getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCoordinatesMap, 11)); // Фокусируемся на Самаре
        map.setOnMarkerClickListener(this);
        map.setOnInfoWindowClickListener(this);

        Log.d(LOG,"Добавление маркеров станций на карту");

        // Добавление маркеров станций на карту
        int size = locationArrayList.size();
        for (int i = 0; i < size; i++) {
            addBoathouse(locationArrayList.get(i));
        }

        Log.d(LOG,"Добавление маркеров станций на карту закончено");
    }

    /**
     * Метод добавляет маркер на карту
     *
     * @param location Имя и координаты мркера
     */
    private void addBoathouse(Location location) {
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(location.getName()));
    }

    /**
     * Метод отправляет данные OrderActivity
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(LOG, "onMarkerClick");

        if (!marker.isInfoWindowShown()) {

            marker.showInfoWindow();
        }

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(LOG,"onInfoWindowClick()");

        Location location;

        // Если координата найдена
        if((location = Variables.getInstance().getLocation(marker.getTitle())) != null){
            Intent intent = new Intent();
            intent.putExtra("location", location);
            intent.putExtra("id", marker.getId().substring(1));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Log.d(LOG,"onInfoWindowClick(): !!!!!location is not");
        }

    }
}
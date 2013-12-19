package com.alex.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 14.12.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class MapActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {

    private final String LOG = "logMapActivity";
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        LatLng defaultCoordinatesMap = new LatLng(53.217482, 50.112419);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)) // Получаем карту
                .getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultCoordinatesMap, 11)); // Фокусируемся на Самаре
        map.setOnMarkerClickListener(this);

        // Добавление маркеров на левый берег волги

        addBoathouse("8-я просека", 53.261874, 50.181009);//id "m0" - все id прописываются сами, число id - номер добавления маркера на карту
        addBoathouse("3-я просека", 53.241320, 50.167147);//id "m1"
        addBoathouse("Осипенко", 53.214176, 50.126560);//id "m2"
        addBoathouse("Вилоновский сп.", 53.200887, 50.096519);//id "m3"
        addBoathouse("Речной вокзал", 53.185758, 50.076960);//id "m4"

        // Добавление маркеров на правый берег волги

        addBoathouse("Ближний пляж", 53.232343, 50.115069);//id "m5"
        addBoathouse("Рождественно", 53.226423, 50.064595);//id "m6"
    }

    /**
     * Метод добавляет маркер на карту
     *
     * @param name      Имя маркера
     * @param latitude  Широта
     * @param longitude Долгота
     */
    private void addBoathouse(String name, double latitude, double longitude) {
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .position(new LatLng(latitude, longitude))
                .title(name));
    }

    /**
     * Метод отправляет данные MainActivity
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(LOG, "onMarkerClick");

        Intent intent = new Intent();
        intent.putExtra("nameBoathouse", marker.getTitle());
        intent.putExtra("id", marker.getId().substring(1));
        setResult(RESULT_OK, intent);
        finish();

        return false;
    }
}
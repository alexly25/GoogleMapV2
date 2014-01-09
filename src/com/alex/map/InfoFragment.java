package com.alex.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import ru.shem.services.Variables;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 14.12.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class InfoFragment extends FragmentActivity  {

    private static final String LOG = "mylogInfoFragment";
    private static final String statusActual = "actual";
    private Booking booking;
    private GoogleMap map;
    private ArrayList<Location> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        this.booking = (Booking) getIntent().getSerializableExtra("booking"); // Получаем выбранный в listview заказ

        setTextInfo();

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.info_map)) // Получаем карту
                .getMap();

        //locationArrayList = Variables.getInstance().getLocationArrayList(); // Получаем маркеры

        addMarkers();

        addLine();

    }

    /**
     * Метод добавляет маркеры на карту
     */
    private void addMarkers() {

        // Нужны ли нам другие маркеры или оставим только два(Откуда и Куда)???

        // Добавление маркеров станций на карту
        /*int size = locationArrayList.size();
        for (int i = 0; i < size; i++) {
            addBoathouse(locationArrayList.get(i));
        }*/


        Location fromLocation = booking.getFromLocation();
        Marker fromMarker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .position(new LatLng(fromLocation.getLatitude(), fromLocation.getLongitude()))
                .title(fromLocation.getName())
                .snippet("Отплываем: 15:00"));
        fromMarker.showInfoWindow();

        Location toLocation = booking.getToLocation();
        Marker toMarker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .position(new LatLng(toLocation.getLatitude(), toLocation.getLongitude()))
                .title(toLocation.getName()).snippet("Прибытие: 15:30"));
    }

    /**
     * Метод добавляет линию соединения точек(отправки и прибытия) на карту. Фокусируется на этой линии и делает zoom
     */
    private void addLine() {

        // Добавление линии соединяющую точку отправки и точки прибытия

        map.addPolyline(new PolylineOptions().geodesic(false)
                .color(Color.RED)
                .add(booking.getFromLocation().getLatLng())
                .add(booking.getToLocation().getLatLng()));


        // Расчитываем середину отрезка пути и зумируемся на нем

        double x1 = booking.getFromLocation().getLatitude();
        double x2 = booking.getToLocation().getLatitude();
        double y1 = booking.getFromLocation().getLongitude();
        double y2 = booking.getToLocation().getLongitude();

        double latMidpoint = x1 - (x1 - x2) / 2;
        double lonMidpoint = y1 - (y1 - y2) / 2;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latMidpoint, lonMidpoint), 12));

    }

    /**
     * Метод записывает информацию о выбраном заказе в View компоненты
     */
    private void setTextInfo() {
        ((TextView) findViewById(R.id.tvFromInfo)).setText(booking.getFromLocation().getName());
        ((TextView) findViewById(R.id.tvToInfo)).setText(booking.getToLocation().getName());
        ((TextView) findViewById(R.id.tvDateInfo)).setText("Время пути: 30 мин.");
        ((TextView) findViewById(R.id.tvCostInfo)).setText(booking.getCostToString());
    }

    /**
     * Метод добавляет маркер на карту
     *
     * //@param location Имя и координаты мркера
     */
    /*private void addBoathouse(Location location) {
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2))
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(location.getName()));
    }*/
}
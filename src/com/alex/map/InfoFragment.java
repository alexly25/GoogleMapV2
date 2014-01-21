package com.alex.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 14.12.13
 * Time: 2:50
 * To change this template use File | Settings | File Templates.
 */
public class InfoFragment extends FragmentActivity implements View.OnClickListener {

    private static final String LOG = "mylogInfoFragment";
    private static final String statusActual = "actual";
    private Booking booking;
    private GoogleMap map;
    private ArrayList<Location> locationArrayList;
    private LatLng coordinateSamara;
    private int zoomSamara = 11;
    private int zoomPoints = 21;
    private int tiltSamara = 0;
    private int tiltPoints = 90;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        Log.d(LOG, "onCreate()");
        this.booking = (Booking) getIntent().getSerializableExtra("booking"); // Получаем выбранный в listview заказ
        Log.d(LOG, "onCreate()");
        setTextInfo();
        Log.d(LOG, "onCreate()");
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.info_map)) // Получаем карту
                .getMap();
        Log.d(LOG, "onCreate() " + booking.toString() );
        addMarkers(booking.getFromLocation(), booking.getFromTime());
        addMarkers(booking.getToLocation(), booking.getToTime());
        Log.d(LOG, "onCreate()");
        addLine();
        Log.d(LOG, "onCreate()");
        // Фокусируемся на Самаре
        coordinateSamara = new LatLng(53.217482, 50.112419);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinateSamara, zoomSamara));
        Log.d(LOG, "onCreate()");
    }

    /**
     * Метод добавляет маркеры на карту
     */
    private void addMarkers(Location location, String snippet) {
        Log.d(LOG, "addMarker()");

        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(location.getName())
                .snippet(snippet));
        Log.d(LOG, "addMarker()end");
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

    }

    /**
     * Метод записывает информацию о выбраном заказе в View компоненты
     */
    private void setTextInfo() {
        ((TextView) findViewById(R.id.tvFromInfo)).setText("Точка отправки: " + booking.getFromLocation().getName());
        ((TextView) findViewById(R.id.tvToInfo)).setText("Точка прибытия: " + booking.getToLocation().getName());
        ((TextView) findViewById(R.id.tvDateInfo)).setText(booking.getTravelTime());
        ((TextView) findViewById(R.id.tvCostInfo)).setText("Стоимость: " + booking.getCostToString());
    }

    GoogleMap.CancelableCallback MyCancelableCallback =
            new GoogleMap.CancelableCallback() {

                @Override
                public void onFinish() {

                    Log.d(LOG, "onFinish()");

                    if (i == 0) {

                        LatLng latLng = new LatLng(booking.getFromLocation().getLatitude() + 0.00002,
                                booking.getFromLocation().getLongitude() + 0.00002);
                        nextPoint(latLng, zoomSamara, tiltSamara, 5000);

                    } else if (i == 1) {

                        LatLng latLng = new LatLng(booking.getFromLocation().getLatitude() + 0.00002,
                                booking.getFromLocation().getLongitude() + 0.00002);
                        nextPoint(latLng, zoomPoints, tiltPoints, 2000);

                    } else if (i == 2) {

                        nextPoint(booking.getFromLocation().getLatLng(), zoomPoints, tiltPoints, 2000);

                    } else if (i == 3) {

                        nextPoint(booking.getToLocation().getLatLng(), zoomPoints, tiltPoints, 20000);

                    } else if (i == 4) {


                        LatLng latLng = new LatLng(coordinateSamara.latitude - 0.0002,
                                coordinateSamara.longitude);
                        nextPoint(latLng, zoomSamara, tiltSamara, 2000);

                    } else if (i == 5) {

                        nextPoint(coordinateSamara, zoomSamara, tiltSamara, 2000);
                    }
                }

                @Override
                public void onCancel() {

                    Log.d(LOG, "onCancel()");
                }

                private void nextPoint(LatLng latLng, int zoom, int tilt, int time) {

                    Log.d(LOG, "nextPoint() ");

                    i++;

                    //Get the current location
                    android.location.Location startingLocation = new android.location.Location("starting point");
                    startingLocation.setLatitude(map.getCameraPosition().target.latitude);
                    startingLocation.setLongitude(map.getCameraPosition().target.longitude);

                    //Get the target location
                    android.location.Location endingLocation = new android.location.Location("ending point");
                    endingLocation.setLatitude(latLng.latitude);
                    endingLocation.setLongitude(latLng.longitude);

                    //Find the Bearing from current location to next location
                    float targetBearing = startingLocation.bearingTo(endingLocation);

                    //Create a new CameraPosition
                    CameraPosition cameraPosition =
                            new CameraPosition.Builder()
                                    .target(latLng)
                                    .bearing(targetBearing)
                                    .tilt(tilt)
                                    .zoom(zoom)
                                    .build();


                    map.animateCamera(
                            CameraUpdateFactory.newCameraPosition(cameraPosition),
                            time,
                            MyCancelableCallback);

                }
            };

    @Override
    public void onClick(View v) {

        Log.d(LOG, "onClick()");

        switch (v.getId()) {
            case R.id.tvShowPath:

                i = 0;
                MyCancelableCallback.onFinish();

                break;
        }
    }
}
package com.alex.map;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 07.01.14
 * Time: 1:12
 * To change this template use File | Settings | File Templates.
 */
public class Location implements Serializable {

    private static final long serialVersionUID = -1096217523172222471L;
    private String name;
    private double latitude;
    private double longitude;

    public Location(String name, double latitude, double longitude){
        this.name = new String(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }

    public boolean equals(Location location){

        if(latitude == location.getLatitude() && longitude == location.getLongitude()){
            return true;
        }

        return false;
    }
}

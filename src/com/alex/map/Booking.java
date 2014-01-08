package com.alex.map;

import com.google.android.gms.internal.ek;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 21.12.13
 * Time: 23:47
 * To change this template use File | Settings | File Templates.
 */
public class Booking implements Serializable {

    private static final int ADD_MINUTE = 5;
    private static final String statusActual = "actual";
    private static final long serialVersionUID = -1096217523172222472L;

    private Location fromLocation;
    private Location toLocation;
    private Date date;
    private int cost;
    private String status;


    public Booking(Location fromLocation, Location toLocation, Date date, int cost, String status) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.date = date;
        this.cost = cost;
        this.status = status;
    }
    public int getCost() {
        return cost;
    }

    public String getCostToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Оплата: ").append(cost);
        return sb.toString();
    }

    public Date getDate() {
        return date;
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public String getInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("Статус: ").append((getStatus().equals(statusActual)) ? "Актуален" : "Не актуален")
                .append(", Из: ").append(fromLocation.getName())
                .append(", В: ").append(toLocation.getName())
                .append(", Дата: ").append(date.getDate()).append(" ").append(date.getHours())
                .append(":").append((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes());
        return sb.toString();
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        StringBuilder sb = new StringBuilder();
        sb.append("Отплываем  ").append(date.getDate())
                .append(" в ").append(date.getHours())
                .append(":").append((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes());
        return sb.toString();
    }

    public Location getToLocation() {
        return toLocation;
    }

    public boolean isEmpty() {
        return fromLocation == null || toLocation == null || date == null || cost == -1 || status == null;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Из: ").append(fromLocation.getName())
                .append(", в: ").append(toLocation.getName())
                .append(", дата: ").append(date.getDate()).append(" ").append(date.getHours())
                .append(":").append((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes())
                .append(", цена: ").append(cost)
                .append(", статус: ").append(status);
        return sb.toString();
    }

    private Date dateAdded() {
        Date d = new Date();
        return new Date(d.getYear(),
                d.getMonth(),
                d.getDate(),
                d.getHours(),
                d.getMinutes() + ADD_MINUTE);
    }
}

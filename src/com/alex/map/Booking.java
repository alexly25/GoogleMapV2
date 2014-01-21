package com.alex.map;

import com.google.android.gms.internal.ek;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

    /*  Зачем??
    public Booking() { }*/

    public int getCost() {
        return cost;
    }

    public String getCostToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Оплата: ").append(cost);
        return sb.toString();
    }

    public Date getFromDate() {
        return date;
    }

    public Date getToDate() {
        return new Date(date.getTime() + 1800000); // Прибавляем 30 мин ко времени отплытия
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public String getStatus() {
        return status;
    }

    public String getFromTime() {
        return new SimpleDateFormat("'Отплытие:' d MMM 'в' HH:mm").format(date);
    }

    public String getToTime() {
        return new SimpleDateFormat("'Время прибытия:' d MMM 'в' HH:mm").format(getToDate());
    }

    public String getTravelTime() {
        return new SimpleDateFormat("'Время пути:' mm мин.").format(getToDate().getTime() - getFromDate().getTime());
    }

    public Location getToLocation() {
        return toLocation;
    }

    public boolean isEmpty() {

        if (fromLocation != null && toLocation != null && fromLocation.getName().equals(toLocation.getName())) {
            return true;
        }

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
        sb.append(fromLocation.getName())
                .append(" \u2192 ").append(toLocation.getName())
                .append("\n").append(new SimpleDateFormat("d MMM HH:mm").format(date))
                .append("\t\t\t\t\t\t").append(cost).append(" руб.");
        return sb.toString();
    }
}

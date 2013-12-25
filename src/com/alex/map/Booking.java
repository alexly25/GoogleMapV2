package com.alex.map;

import com.google.android.gms.internal.ek;

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
public class Booking {

    private final int ADD_MINUTE = 5;

    private String boathouseFrom;
    private String boathouseTo;
    private Date date;
    private int cost;
    private String status;

    public Booking() {
        this.boathouseFrom = "Откуда";
        this.boathouseTo = "Куда";
        this.date = dateAdded();
        this.status = "";
    }

    public Booking(String boathouseFrom, String boathouseTo, Date date, int cost, String status) {
        this.boathouseFrom = boathouseFrom;
        this.boathouseTo = boathouseTo;
        this.date = date;
        this.cost = cost;
        this.status = status;
    }

    public String getCostToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Оплата: ").append(cost);
        return sb.toString();
    }

    public String getTime() {
        StringBuilder sb = new StringBuilder();
        int minutes = date.getMinutes();

        sb.append("Отплываем  ").append(date.getDate())
                .append(" в ").append(date.getHours())
                .append(":").append((minutes < 10) ? ("0" + minutes) : minutes);
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("From ").append(boathouseFrom)
                .append(", to ").append(boathouseTo)
                .append(", date: ").append(date.getDate()).append(" ").append(date.getHours()).append(":").append(date.getMinutes())
                .append(", cost: ").append(cost)
                .append(", status: ").append(status);
        return sb.toString();
    }

    public boolean isEmpty() {
        return boathouseFrom.equals("") && boathouseTo.equals("") && date == null && cost == 0;
    }

    public void setBoathouseFrom(String boathouseFrom) {
        this.boathouseFrom = boathouseFrom;
    }

    public void setBoathouseTo(String boathouseTo) {
        this.boathouseTo = boathouseTo;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getBoathouseTo() {
        return boathouseTo;
    }

    public String getBoathouseFrom() {
        return boathouseFrom;
    }

    public Date getDate() {
        return date;
    }

    public int getCost() {
        return cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

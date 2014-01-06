package ru.shem.services;

import com.alex.map.HistoryBookings;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 19.12.13
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class Variables {
    private static Variables instance;

    private int dayOrNight = 1;

    private Integer fromId;
    private Integer toId;

    private HistoryBookings historyBookings;

    private boolean isFromChose;

    public static Variables getInstance() {
        if(instance == null) {
            instance = new Variables();
        }
        return instance;
    }

    public Variables() { }

    public int getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(int dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public HistoryBookings getHistoryBookings() {
        return historyBookings;
    }

    public void setHistoryBookings(HistoryBookings historyBookings) {
        this.historyBookings = historyBookings;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public void setFromChose(boolean fromChose) {
        isFromChose = fromChose;
    }

    public boolean isFromChose() {
        return isFromChose;
    }
}

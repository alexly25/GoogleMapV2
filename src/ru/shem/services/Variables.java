package ru.shem.services;

import com.alex.map.HistoryBookings;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 19.12.13
 *
 * Здесь распологаются переменные используемые в более чем одном классе
 */
public class Variables {
    private static Variables instance;

    private int dayOrNight = 1;

    // ID от куда и куда, они обнулялись почему то. Теперь нет.
    private Integer fromId;
    private Integer toId;

    // Переменная используется и в OrderActivity и в HistoryActivity
    private HistoryBookings historyBookings;

    // Тоже обнулялась при выборе "Откуда"/"Куда"
    private boolean isFromChose;

    public static Variables getInstance() {
        if(instance == null) {
            instance = new Variables();
        }
        return instance;
    }

    public Variables() { }

    // всё ниже - Геттеры, сеттеры и проверки булевых значений

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

package ru.shem.services;

import com.alex.map.MainActivity;

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
}

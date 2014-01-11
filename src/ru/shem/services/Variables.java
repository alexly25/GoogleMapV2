package ru.shem.services;

import com.alex.map.Booking;
import com.alex.map.HistoryBookings;
import com.alex.map.Location;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 19.12.13
 * <p/>
 * Здесь распологаются переменные используемые в более чем одном классе
 */
public class Variables {
    private static Variables instance;

    private int dayOrNight = 1;



    // Переменная используется и в OrderActivity и в HistoryActivity
    private HistoryBookings historyBookings;



    private ArrayList<Location> locationArrayList;

    private Booking newBooking;

    public static Variables getInstance() {
        if (instance == null) {
            instance = new Variables();
        }
        return instance;
    }

    private Variables() {

        newBooking = new Booking(null, null, null, -1, null);
        locationArrayList = new ArrayList<Location>();

        // Добавление маркеров на левый берег волги
        locationArrayList.add(new Location("8-я просека", 53.261874, 50.181009));
        locationArrayList.add(new Location("3-я просека", 53.241320, 50.167147));
        locationArrayList.add(new Location("Осипенко", 53.214176, 50.126560));
        locationArrayList.add(new Location("Вилоновский сп.", 53.200887, 50.096519));
        locationArrayList.add(new Location("Речной вокзал", 53.185758, 50.076960));

        // Добавление маркеров на правый берег волги
        locationArrayList.add(new Location("Ближний пляж", 53.232343, 50.115069));
        locationArrayList.add(new Location("Рождественно", 53.226423, 50.064595));
    }

    public Location getLocation(String name) {

        int size = locationArrayList.size();

        for (int i = 0; i < size; i++) {

            Location location = locationArrayList.get(i);

            if (location.getName().equals(name)) {
                return location;
            }
        }

        return null;
    }

    public Booking getNewBooking() {
        return newBooking;
    }

    public void setNewBooking(Booking newBooking) {
        this.newBooking = newBooking;
    }

    public ArrayList<Location> getLocationArrayList() {
        return locationArrayList;
    }

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
}

package com.alex.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.ListView;
import com.alex.data.Data;
import com.alex.data.SQLite;
import ru.shem.services.HistoryAdapter;
import ru.shem.services.Variables;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 22.12.13
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class HistoryBookings {

    private static final String LOG = "mylogHistoryBooking";
    private static final String statusPause = "pause";
    private static final String statusActual = "actual";
    private static final String statusNoActual = "no_actual";
    private static final String tableName = "history";

    private ArrayList<Booking> alHistory;
    private HistoryAdapter arrayAdapter;
    private HashMap<Integer, Booking> bookingHashMap;
    private SQLite sqLite;

    public HistoryBookings(Context context, ListView lvHistory) {

        Log.d(LOG, "HistoryBookings");

        alHistory = new ArrayList<Booking>();
        arrayAdapter = new HistoryAdapter(context, R.layout.history_list_view_style, alHistory);
        sqLite = new SQLite(context);
        bookingHashMap = new HashMap<Integer, Booking>();

        lvHistory.setAdapter(arrayAdapter);
        lvHistory.setDivider(new ColorDrawable(Color.rgb(71, 185, 228)));
        lvHistory.setDividerHeight(1);
    }

    /**
     * Метод эмулирующий добавление заказа. НЕ УДАЛЯТЬ!!!
     *
     * @return
     */
    public int addBookingForTests() {

        Booking booking = new Booking(
                new Location("8-я просека", 53.261874, 50.181009),
                new Location("Ближний пляж", 53.232343, 50.115069),
                new Date(),
                2111,
                statusActual);

        Data data = new Data(sqLite);

        if (data.addBooking(booking) && booking.getStatus().equals(statusActual)) {

            outBookings();

            return 1;
        }

        return 0;
    }

    /**
     * Метод проверяет актуальные заказы. Если время заказа прошедшее, то статус заказа становиться не актуальным.
     */
    public void checkStatus() {

        Log.d(LOG, "checkStatus()");

        Data data = new Data(sqLite);

        try {

            Log.d(LOG, "checkStatus() c.getCount() actual: " + data.getCountLineActual());

            data.updataStatus();

            Log.d(LOG, "checkStatus() c.getCount() actual: " + data.getCountLineActual());

            outBookings();

        } catch (Exception e) {

            Log.d(LOG, "!!!!!checkStatus() catch error: " + e.toString());

        }

    }

    /**
     * Метод возвращает объект класса Booking который находиться в списке на позиции position.
     *
     * @param position Номер запрашиваемого объекта
     * @return Запрашиваемый объект
     */
    public Booking getBooking(int position) {

        Log.d(LOG, "getBookingPause()");

        return bookingHashMap.get(position);
    }

    /**
     * Метод добавляет объект класса Booking в БД. Если статус заказа актуальный, то обновляется лист с заказами.
     *
     * @param booking Записываемый объект
     * @return true - запись данных в БД удалась, false - запись данных в БД не удалась
     */
    public boolean addBooking(Booking booking) {

        Log.d(LOG, "addBooking()");

        if (booking == null) {
            return false;
        }

        Data data = new Data(sqLite);

        if (data.addBooking(booking) && booking.getStatus().equals(statusActual)) {
            outBookings();

            return true;
        }

        return false;
    }

    /**
     * Метод выводит все даные о заказах, находящиеся в БД, в список заказов на экране девайса
     */
    private void outBookings() {

        Log.d(LOG, "outBooking()");

        SQLiteDatabase db = null;
        Cursor c = null;

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            c = db.query(tableName, null, null, null, null, null, null); // делаем запрос всех данных из таблицы tableName

            if (c.moveToFirst()) {

                alHistory.clear();
                bookingHashMap.clear();
                Log.d(LOG, "outBooking() clear list");
                int i = -1;

                do {
                    i++;
                    Booking booking = getBooking(c);
                    Log.d(LOG, "outBookings() Booking booking = getBooking(c);");
                    Log.d(LOG, "outBookings() add into list: " + booking.getFromLocation().getName());

                    alHistory.add(booking);
                    arrayAdapter.sort();
                    arrayAdapter.notifyDataSetChanged();
                    bookingHashMap.put(i, booking);
                    Log.d(LOG, "outBookings() add into hashMap: " + bookingHashMap.size() + " get(" + i + "): " + bookingHashMap.get(0).toString());

                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());

            } else {
                Log.d(LOG, "outBooking() 0 rows");
            }

        } catch (Exception e) {

            Log.d(LOG, "!!!!!outBooking() catch error: " + e.toString());

        } finally {
            c.close();
            db.close();
        }
    }

    /**
     * Метод считывает информацию о заказе с объекта Cursor и возвращает объект Booking
     *
     * @param c Переменная с которой происходит считывание
     */
    private Booking getBooking(Cursor c) {

        Log.d(LOG, "getBooking()");

        return new Booking(
                Variables.getInstance().getLocation(c.getString(c.getColumnIndex("boathouseFrom"))),
                Variables.getInstance().getLocation(c.getString(c.getColumnIndex("boathouseTo"))),
                new Date(Long.valueOf(c.getString(c.getColumnIndex("date")))),
                Integer.valueOf(c.getString(c.getColumnIndex("cost"))),
                c.getString(c.getColumnIndex("status")));
    }


}

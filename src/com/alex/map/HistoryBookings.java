package com.alex.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    private ArrayList<String> alHistory;
    private ArrayAdapter<String> arrayAdapter;
    private SQLite sqLite;
    private HashMap<Integer, Booking> bookingHashMap;

    public HistoryBookings(Context context, ListView lvHistory) {

        Log.d(LOG, "HistoryBookings");

        alHistory = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, alHistory);
        sqLite = new SQLite(context);
        bookingHashMap = new HashMap<Integer, Booking>();

        lvHistory.setAdapter(arrayAdapter);
    }

    /**
     * Метод эмулирующий добавление заказа. НЕ УДАЛЯТЬ!!!
     * @return
     */
    public int addBookingForTests() {

        Booking booking = new Booking(
                new Location("8-я просека", 53.261874, 50.181009),
                new Location("Ближний пляж", 53.232343, 50.115069),
                new Date(),
                2111,
                statusActual);

        if (addingBooking(booking)) {
            if (booking.getStatus().equals(statusActual)) {
                outBookings();
                Log.d(LOG, "addBooking() status actual");
            } else if (booking.getStatus().equals(statusPause)) {
                Log.d(LOG, "addBooking() status pause");
            }
            return 1;
        }
        return 0;
    }

    /**
     * Метод проверяет актуальные заказы. Если время заказа прошедшее, то статус заказа становиться не актуальным.
     */
    public void checkStatus() {

        Log.d(LOG, "checkStatus()");

        SQLiteDatabase db = null;
        Cursor c = null;
        Date date = new Date();

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            c = db.rawQuery("SELECT * FROM " + tableName + " WHERE status = '" + statusActual + "'", null);
            Log.d(LOG, "checkStatus() c.getCount() actual: " + c.getCount());

            db.execSQL("UPDATE history SET status = '" + statusNoActual + "' WHERE date < " + date.getTime() + "");
            Log.d(LOG, "checkStatus() UPDATE history SET status = '" + statusNoActual + "' WHERE date < " + date.getTime() + "");

            c = db.rawQuery("SELECT * FROM " + tableName + " WHERE status = '" + statusActual + "'", null);
            Log.d(LOG, "checkStatus() c.getCount() actual: " + c.getCount());

            outBookings();

        } catch (Exception e) {

            Log.d(LOG, "!!!!!checkStatus() catch error: " + e.toString());

        } finally {
            c.close();
            db.close();
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
     * Метод получает строку из БД со статусом statusPause, записывает ее данные в booking,
     * с которого будет считываться состояние, и удаляет эту строку из БД.
     *
     * @return Возвращает объект класса Booking хранящий состояние перед закрытием программы.
     */
    public Booking getBookingPause() {
        Booking booking = null;

        Log.d(LOG, "getBookingPause()");

        SQLiteDatabase db = null;
        Cursor c = null;

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            c = db.rawQuery("SELECT * FROM " + tableName + " WHERE status = 'pause'", null);

            if (c.getCount() > 1) {
                Log.d(LOG, "!!!!!getBookingPause() c.getCount(): " + c.getCount());
            }

            if (c.moveToFirst()) {

                booking = getBooking(c);
                Log.d(LOG, "getBookingPause() booking.toString() " + booking.toString());

                db.execSQL("DELETE FROM " + tableName + " WHERE status = 'pause'");
                Log.d(LOG, "getBookingPause() Delete FROM " + tableName + " WHERE status = 'pause'");

                outBookings();
            }

        } catch (Exception e) {

            Log.d(LOG, "!!!!!getBookingPause() catch error: " + e.toString());

        } finally {
            c.close();
            db.close();
        }
        return booking;
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

        if (addingBooking(booking)) {
            if (booking.getStatus().equals(statusActual)) {
                outBookings();
                Log.d(LOG, "addBooking() status actual");
            } else if (booking.getStatus().equals(statusPause)) {
                Log.d(LOG, "addBooking() status pause");
            }
            return true;
        }

        Log.d(LOG, "!!!!!addBooking() return false");

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

                    alHistory.add(booking.getInfo());
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

    /**
     * Вспомагательный метод для добавления
     *
     * @param booking
     * @return
     */
    private boolean addingBooking(Booking booking) {

        Log.d(LOG, "addingBooking()");

        boolean isAdded = false;

        if (booking == null) {
            return false;
        }

        SQLiteDatabase db = null;

        try {
            // создаем объект для данных
            ContentValues cv = new ContentValues();

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            cv.put("boathouseFrom", booking.getFromLocation().getName());
            cv.put("boathouseTo", booking.getToLocation().getName());
            cv.put("date", booking.getDate().getTime());
            cv.put("cost", booking.getCost());
            cv.put("status", booking.getStatus());

            if (db.insert(tableName, null, cv) != -1) {
                isAdded = true;
            } else {

                Log.d(LOG, "!!!!!addingBooking() no added " + booking.toString() + ", insert: -1");
            }


        } catch (Exception e) {

            Log.d(LOG, "!!!!!addingBooking() catch error: " + e.toString());
            isAdded = false;

        } finally {
            db.close();

        }
        return isAdded;

    }

    private class SQLite extends SQLiteOpenHelper {

        public SQLite(Context context) {
            super(context, "BoatTaxi", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(LOG, "onCreate() create table");

            db.execSQL("create table history ("
                    + "id integer primary key autoincrement,"
                    + "boathouseFrom text,"
                    + "boathouseTo text,"
                    + "date date,"
                    + "cost text,"
                    + "status text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}

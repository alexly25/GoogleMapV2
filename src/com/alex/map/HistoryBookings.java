package com.alex.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Browser;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 22.12.13
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class HistoryBookings {

    private static final String LOG = "logHistoryBooking";
    private static final String statusPause = "pause";
    private static final String statusActual = "actual";
    private static final String tableName = "history";

    private ArrayList<String> alHistory;
    private ArrayAdapter<String> arrayAdapter;
    private SQLite sqLite;

    public HistoryBookings(Context context, ListView lvHistory) {

        Log.d(LOG, "HistoryBookings");

        alHistory = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, alHistory);
        sqLite = new SQLite(context);

        lvHistory.setAdapter(arrayAdapter);
    }

    /**
     * Метод проверяет актуальные заказы. Если время заказа прошедшее, то статус заказа становиться не актуальным.
     */
    public void checkStatus() {

        // Допишу позже

    }

    /**
     * Метод получает строку из БД со статусом statusPause, записывает ее данные в booking,
     * с которого будет считываться состояние, и удаляет эту строку из БД.
     * @return Возвращает объект класса Booking хранящий состояние перед закрытием программы.
     */
    public Booking getBookingPause() {
        Booking booking = new Booking();

        Log.d(LOG, "getBookingPause()");

        SQLiteDatabase db = null;
        Cursor c = null;

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            c = db.rawQuery("SELECT * FROM "+tableName+" WHERE status = 'pause'", null);

            if (c.getCount() > 1) {
                Log.d(LOG, "!!!!!getBookingPause() c.getCount(): " + c.getCount());
            }

            if (c.moveToFirst()) {

                setBookingValues(c, booking);
                Log.d(LOG, "getBookingPause() booking.toString() " + booking.toString());

                db.execSQL("DELETE FROM "+tableName+" WHERE status = 'pause'");
                Log.d(LOG, "getBookingPause() Delete FROM "+tableName+" WHERE status = 'pause'");

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
                Log.d(LOG, "outBooking() clear list");

                do {

                    Booking booking = new Booking();
                    setBookingValues(c, booking);
                    Log.d(LOG, "outBookings() add into list: " + booking.toString());

                    alHistory.add(booking.toString());
                    arrayAdapter.notifyDataSetChanged();

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
     * Метод записывает значения с объекта типа Cursor, в объект типа Booking
     * @param c Переменная с которой происходит считывание
     * @param booking Переменная в которую происходит запись
     */
    private void setBookingValues(Cursor c, Booking booking) {
        booking.setBoathouseFrom(c.getString(c.getColumnIndex("boathouseFrom")));
        booking.setBoathouseTo(c.getString(c.getColumnIndex("boathouseTo")));
        booking.setDate(new Date(Long.valueOf(c.getString(c.getColumnIndex("date")))));
        booking.setCost(Integer.valueOf(c.getString(c.getColumnIndex("cost"))));
        booking.setStatus(c.getString(c.getColumnIndex("status")));
    }

    /**
     * Вспомагательный метод для добавления
     * @param booking
     * @return
     */
    private boolean addingBooking(Booking booking) {

        Log.d(LOG, "addingBooking()");

        boolean isAdded = false;

        if (booking == null) {
            return false;
        }

        ContentValues cv = null;
        SQLiteDatabase db = null;

        try {
            // создаем объект для данных
            cv = new ContentValues();

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            cv.put("boathouseFrom", booking.getBoathouseFrom());
            cv.put("boathouseTo", booking.getBoathouseTo());
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

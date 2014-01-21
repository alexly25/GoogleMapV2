package com.alex.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.alex.map.Booking;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13.01.14
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class Data {

    private static final String LOG = "mylogHistoryBooking";
    private static final String statusPause = "pause";
    private static final String statusActual = "actual";
    private static final String statusNoActual = "no_actual";
    private static final String tableName = "history";

    SQLiteDatabase db = null;
    Cursor c = null;

    private SQLite sqLite;

    public Data(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    /**
     * Метод возращает количество строк в таблице c именем tableName, поле status, у которых, равно переменной statusActual.
     * @return
     */
    public int getCountLineActual() {

        Log.d(LOG, "getCountLineActual()");
        int count = 0;

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД
            Log.d(LOG, "getCountLineActual()");

            c = db.rawQuery("SELECT * FROM " + tableName + " WHERE status = '" + statusActual + "'", null);
            count = c.getCount();
            Log.d(LOG, "getCountLineActual()");

        } catch (Exception e) {

            Log.d(LOG, "!!!!!getCountLineActual() catch error: " + e.toString());

        } finally {
            c.close();
            db.close();
        }

        return count;
    }

    public void updataStatus() {

        try {

            db = sqLite.getWritableDatabase();

            db.execSQL("UPDATE " + tableName + " SET status = '" + statusActual + "' WHERE date < " + new Date().getTime() + "");

        } catch (Exception e) {

            Log.d(LOG, "!!!!!updataStatus() catch error: " + e.toString());

        } finally {
            db.close();
        }

    }

    public boolean addBooking(Booking booking){
        Log.d(LOG, "addBooking: " + booking.toString());

        boolean isAdded = false;

        try {

            // создаем объект для данных
            ContentValues cv = new ContentValues();

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            cv.put("boathouseFrom", booking.getFromLocation().getName());
            cv.put("boathouseTo", booking.getToLocation().getName());
            cv.put("date", booking.getFromDate().getTime());
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
}

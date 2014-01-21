package com.alex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13.01.14
 * Time: 16:12
 * To change this template use File | Settings | File Templates.
 */
public class SQLite extends SQLiteOpenHelper {

    private static final String LOG = "SQLite";

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

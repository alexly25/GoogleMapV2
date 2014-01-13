package ru.shem.services;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.alex.map.Booking;

import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ирина
 * Date: 13.01.14
 * Time: 8:55
 * To change this template use File | Settings | File Templates.
 */
public class HistoryAdapter extends ArrayAdapter<Booking> {
    private List<Booking> objects;
    private static final String statusActual = "actual";

    public HistoryAdapter(Context context, int textViewResourceId, List<Booking> objects) {
        super(context, textViewResourceId, objects);

        this.objects = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Booking getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(getContext());

        Booking booking = getItem(position);

        tv.setTextSize(16);

        if(booking.getStatus() == statusActual) {
            tv.setTextColor(Color.rgb(89, 230, 129));
            tv.setTextSize(18);
        } else {
            tv.setTextColor(Color.DKGRAY);
        }

        convertView = tv;
        return super.getView(position, convertView, parent);
    }


}

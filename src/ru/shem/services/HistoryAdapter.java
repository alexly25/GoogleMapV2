package ru.shem.services;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.alex.map.Booking;
import com.alex.map.R;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 13.01.14
 */
public class HistoryAdapter extends ArrayAdapter<Booking> {
    private int textViewResourceId;

    private static final String STATUS_ACTUAL = "actual";
    private static final String STATUS_PAUSE = "pause";

    /**
     *  Типы сортировки:
     *  1) THROUGH_SORT_TYPE - сквозная сортировка, внизу не активные заказы, а на верху активные.
     *                                      Та что предложил сделать Лёша.
     *  2) TIMELINE_SORT_TYPE - сортировка по принципу единой временной шкалы.
     *                                      Та что предложил Юра.
     */
    private static final String THROUGH_SORT_TYPE = "through";
    private static final String TIMELINE_SORT_TYPE = "timeline";

    public HistoryAdapter(Context context, int textViewResourceId, List<Booking> objects) {
        super(context, textViewResourceId, objects);

        this.textViewResourceId = textViewResourceId;
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

    public void sort() {
        super.sort(new Comparator(THROUGH_SORT_TYPE));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(textViewResourceId, null);
        }

        Booking booking = getItem(position);

        if(booking.getStatus().equals(STATUS_ACTUAL)) {
            ((TextView) convertView).setTextColor(Color.BLACK);
        } else if(booking.getStatus().equals(STATUS_PAUSE)){
            ((TextView) convertView).setTextColor(R.color.no_actual_order);
        }
        return super.getView(position, convertView, parent);
    }
}

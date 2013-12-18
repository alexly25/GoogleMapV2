package com.alex.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 18.12.13
 * Time: 15:40
 * To change this template use File | Settings | File Templates.
 */
public class TimeDialog extends DialogFragment implements OnClickListener, NumberPicker.OnValueChangeListener {

    private final String LOG = "logDialogTime";
    private final int MAX_HOUR = 23;
    private final int MAX_MINUTE = 59;

    private Button btnTime;
    private NumberPicker npDate;
    private NumberPicker npHour;
    private NumberPicker npMinute;
    private int minDate;
    private int minHour;
    private int minMinute;

    public TimeDialog(Button btnTime) {
        this.btnTime = btnTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = null;

        try {

            builder = new AlertDialog.Builder(getActivity());

            Date d = new Date();
            minDate = d.getDate();
            minHour = d.getHours();
            minMinute = d.getMinutes();

            Log.d(LOG, d.toGMTString());

            // Подключаемся к time_dialog.xml
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.time_dialog, null);

            // определяем количество дней в месяце
            int dayInMonth = ((GregorianCalendar) GregorianCalendar.getInstance()).getActualMaximum(Calendar.DAY_OF_MONTH);

            setValues(npDate = (NumberPicker) v.findViewById(R.id.npDate), minDate, dayInMonth, minDate);
            setValues(npHour = (NumberPicker) v.findViewById(R.id.npHour), minHour, MAX_HOUR, minHour);
            setValues(npMinute = (NumberPicker) v.findViewById(R.id.npMinute), minMinute, MAX_MINUTE, minMinute + MainActivity.ADD_MINUTE);

            builder.setView(v)
                    .setTitle("Время отправки")
                    .setPositiveButton("Задать", this);

        } catch (Exception e) {
            Log.d(LOG, "onCreteDialog " + e.toString());
        }

        return builder.create();
    }

    /**
     * Метод задает свойства компоненту класса NumberPicker
     *
     * @param np       Компонент класса
     * @param minValue минимальное значение которое принимает компонент
     * @param maxValue максимальное знаечение которое принимает компонент
     * @param value    значение которое принимает компонент по умолчанию
     */
    private void setValues(NumberPicker np, int minValue, int maxValue, int value) {
        np.setMinValue(minValue);
        np.setMaxValue(maxValue);
        np.setValue(value);
        np.setOnValueChangedListener(this);
    }

    /**
     * Метод обрабатывает нажатие кнопки "Задать"
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == Dialog.BUTTON_POSITIVE) {

            MainActivity.selectedDate = npDate.getValue();
            MainActivity.selectedHour = npHour.getValue();
            MainActivity.selectedMinute = npMinute.getValue();
            btnTime.setText("Отплываем " + npDate.getValue() + " в " + npHour.getValue() + ":" + npMinute.getValue());
        }
    }

    /**
     * Метод обрабатывает смену значения бегунков
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        int value;

        switch (picker.getId()) {
            case R.id.npDate: // Если изменилось значение бегунка с датой

                Log.d(LOG, "minDate ");

                value = npDate.getValue();

                // Если значение даты больше минимального значения, то обнуляем минимальное значение у бегунков с часами и минутами
                if (value > minDate) {
                    npHour.setMinValue(0);
                    npMinute.setMinValue(0);
                } else if (value == minDate) { // Иначе задаем минимальные значения
                    npHour.setMinValue(minHour);
                    npMinute.setMinValue(minMinute);
                }

                break;
            case R.id.npHour: // Если изменилось значение бегунка с часом

                Log.d(LOG, "minHour ");

                value = npHour.getValue();

                // Если значение часа больше минимального значения, то обнуляем минимальное значение бегунка с минутами
                if (value > minHour) {
                    npMinute.setMinValue(0);
                } else if (value == minHour) { // Иначе задаем минимальное значение
                    npMinute.setMinValue(minMinute);
                }
                break;
        }
    }
}

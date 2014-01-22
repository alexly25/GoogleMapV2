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
import android.widget.NumberPicker;
import ru.shem.services.Variables;

import java.text.SimpleDateFormat;
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
    private final int ADD_MINUTE = 5;

    private Variables var = Variables.getInstance();

    private NumberPicker npDate;
    private NumberPicker npHour;
    private NumberPicker npMinute;
    private int minDate;
    private int minHour;
    private int minMinute;
    private int selectedDate;
    private int selectedHour;
    private int selectedMinute;
    private Booking booking;
    private Date date;
    private int[] displayedValuesDate;

    public TimeDialog(Booking booking) {
        this.booking = booking;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = null;

        try {


            builder = new AlertDialog.Builder(getActivity());

            date = new Date();
            minDate = date.getDate();
            minHour = date.getHours();
            minMinute = date.getMinutes();

            Log.d(LOG, date.toGMTString());

            // Подключаемся к time_dialog.xml
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View v = inflater.inflate(R.layout.time_dialog, null);

            // Задаем значения объектам NumberPicker
            //setValues(npDate = (NumberPicker) v.findViewById(R.id.npDate), minDate, dayInMonth, minDate);
            setValueNPDate(npDate = (NumberPicker) v.findViewById(R.id.npDate));
            setValues(npHour = (NumberPicker) v.findViewById(R.id.npHour), minHour, MAX_HOUR, minHour);
            setValues(npMinute = (NumberPicker) v.findViewById(R.id.npMinute), minMinute, MAX_MINUTE, minMinute + ADD_MINUTE);

            builder.setView(v)
                    .setTitle("Время отправки")
                    .setPositiveButton("OK", this);

        } catch (Exception e) {
            Log.d(LOG, "!!!!!onCreteDialog " + e.toString());
        }

        return builder.create();
    }

    /**
     * Метод задает значение объекту класса NumberPicker, отвечающий за выбор даты
     * @param numberPicker объект, которому задаем значения
     */
    private void setValueNPDate(NumberPicker numberPicker) {

        // определяем количество дней в месяце
        int dayOfMonth = ((GregorianCalendar) GregorianCalendar.getInstance()).getActualMaximum(Calendar.DAY_OF_MONTH);

        // осталось дней в месяце
        int daysLeft = dayOfMonth - date.getDate();

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(daysLeft);

        String[] displayedValuesString = new String[daysLeft + 1];
        displayedValuesDate = new int[daysLeft + 1];
        for (int i = 0; i < displayedValuesString.length; i++) {
            Date d = new Date(date.getTime() + i * 86400000);
            displayedValuesString[i] = new SimpleDateFormat("d MMM").format(d);
            displayedValuesDate[i] = d.getDate();
        }

        numberPicker.setDisplayedValues(displayedValuesString);
        numberPicker.setOnValueChangedListener(this);
    }

    /**
     * Метод задает свойства объекту numberPicker
     *
     * @param numberPicker объект которому задаем значение
     * @param minValue     минимальное значение которое принимает объект
     * @param maxValue     максимальное знаечение которое принимает объект
     * @param value        мначение которое принимает объект по умолчанию
     */
    private void setValues(NumberPicker numberPicker, int minValue, int maxValue, int value) {
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setValue(value);
        numberPicker.setOnValueChangedListener(this);
    }

    /**
     * Метод обрабатывает нажатие кнопки "Задать"
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == Dialog.BUTTON_POSITIVE) {
            selectedDate = displayedValuesDate[npDate.getValue()];
            selectedHour = npHour.getValue();
            selectedMinute = npMinute.getValue();

            booking.setDate(new Date(date.getYear(),
                    date.getMonth(),
                    selectedDate,
                    selectedHour,
                    selectedMinute));

            ((OrderActivity) getActivity()).doCalculation();
            ((OrderActivity) getActivity()).resetViews();
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

                value = displayedValuesDate[npDate.getValue()];

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

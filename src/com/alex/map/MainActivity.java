package com.alex.map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity implements View.OnClickListener {

    private final String LOG = "logMainActivity";
    private final int DIALOG_DATE = 1;
    private final int DIALOG_TIME = 2;

    private EditText etFrom;
    private EditText etTo;
    private Button dateOrder;
    private Button timeOrder;

    private Integer fromId;
    private Integer toId;

    /**
     * Переменная определяет для какого поля был вызван activity
     * true - для поля etFrom
     * false - для поля etTo
     */
    private boolean isFromChose;

    /**
     * Задание нынешней времени и даты, надо автоматизировать..
     */
    private int myYear = 2013;
    private int myMonth = 12;
    private int myDay = 15;
    private int myHour = 4;
    private int myMinute = 37;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(LOG, "onCreate");

        etFrom = (EditText) findViewById(R.id.fromChoice);
        etFrom.setOnClickListener(this);

        etTo = (EditText) findViewById(R.id.toChoice);
        etTo.setOnClickListener(this);

        dateOrder = (Button) findViewById(R.id.dateOrder);
        dateOrder.setOnClickListener(this);

        timeOrder = (Button) findViewById(R.id.timeOrder);
        timeOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d(LOG, "onClick()");

        Intent intent;
        switch (v.getId()) {
            case R.id.fromChoice:

                Log.d(LOG, "Select from");

                isFromChose = true;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);

                break;
            case R.id.toChoice:

                Log.d(LOG, "Select to");

                isFromChose = false;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);

                break;
            case R.id.dateOrder:

                Log.d(LOG, "Pick date");

                showDialog(DIALOG_DATE);
                break;
            case R.id.timeOrder:

                Log.d(LOG, "Pick time");

                showDialog(DIALOG_TIME);
                break;
        }
    }

    /**
     * Метод принимает данный с MapActivity
     *
     * @param requestCode
     * @param resultCode
     * @param data        nameBoathouse - имя лодочной станции, id - номер лодочной станции в матрице расстояний
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isFromChose) {
            etFrom.setText(data.getStringExtra("nameBoathouse"));
            fromId = Integer.valueOf(data.getStringExtra("id"));
        } else {
            etTo.setText(data.getStringExtra("nameBoathouse"));
            toId = Integer.valueOf(data.getStringExtra("id"));
        }

        calculation();
    }

    /**
     * Расчет стоимости
     */
    private void calculation() {

        Log.d(LOG, "toId: " + toId + ", fromId: " + fromId);

        if (toId != null && fromId != null /* && time == good */) {

            // Подключаемся к серверу
        }
    }

    // Относится к выбору даты и времени
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, dateCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, timeCallBack, myHour, myMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener dateCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear + 1;
            myDay = dayOfMonth;

            dateOrder.setText(myDay + "/" + myMonth + "/" + myYear);
        }
    };

    TimePickerDialog.OnTimeSetListener timeCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            timeOrder.setText(myHour + ":" + myMinute);
        }
    };
}

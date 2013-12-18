package com.alex.map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import ru.shem.services.CostCalculating;

public class MainActivity extends Activity implements View.OnClickListener{
    String myLog = "myLog";

    private EditText etFrom;
    private EditText etTo;
    private TextView cost;
    private Button dateOrder;
    private Button timeOrder;
    private Button doOrder;
    private Integer fromId;
    private Integer toId;

    private CostCalculationBroadcastReceiver broadcastReceiver;
    private final String LOG = "logMainActivity";
    private final int DIALOG_DATE = 1;
    private final int DIALOG_TIME = 2;

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
        Log.d(myLog,"onCreate");

        etFrom = (EditText) findViewById(R.id.fromChoice);
        etFrom.setOnClickListener(this);

        etTo = (EditText) findViewById(R.id.toChoice);
        etTo.setOnClickListener(this);

        dateOrder = (Button) findViewById(R.id.dateOrder);
        dateOrder.setOnClickListener(this);

        timeOrder = (Button) findViewById(R.id.timeOrder);
        timeOrder.setOnClickListener(this);

        doOrder = (Button) findViewById(R.id.doOrder);
        doOrder.setOnClickListener(this);

        cost = (TextView) findViewById(R.id.cost);
        cost.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() { // Что делаем при закрытии приложения
        super.onDestroy();
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver); // уначтажаем широковеательный канал при закрытии приложения
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // Метод сохраняет состояния объектов при повороте экрана и при не хватке памяти
        super.onSaveInstanceState(outState);
        outState.putString("date", dateOrder.getText().toString());
        outState.putString("time", timeOrder.getText().toString());
        outState.putString("cost", cost.getText().toString());
        outState.putBoolean("doOrder", doOrder.isEnabled());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { // метод загружает данные и метода onSaveInstanceState(..)
        super.onRestoreInstanceState(savedInstanceState);
        dateOrder.setText(savedInstanceState.getString("date"));
        timeOrder.setText(savedInstanceState.getString("time"));
        cost.setText(savedInstanceState.getString("cost"));
        doOrder.setEnabled(savedInstanceState.getBoolean("doOrder"));
    }

    @Override
    public void onClick(View v) {

        Log.d(myLog,"onClick()");
        Log.d(LOG, "onClick()");

        Intent intent;
        switch (v.getId()){
            case R.id.fromChoice:
                Log.d(myLog,"Select from");
                Log.d(LOG, "Select from");
                isFromChose = true;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.toChoice:
                Log.d(LOG, "Select to");
                Log.d(myLog,"Select to");

                isFromChose = false;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.dateOrder:
                Log.d(LOG, "Pick date");
                Log.d(myLog, "Pick date");

                showDialog(DIALOG_DATE);
                break;
            case R.id.timeOrder:
                Log.d(LOG, "Pick time");
                Log.d(myLog, "Pick time");

                showDialog(DIALOG_TIME);
                break;
        }
    }

    /**
     -     * Метод принимает данный с MapActivity
     -     *
     -     * @param requestCode
     -     * @param resultCode
     -     * @param data        nameBoathouse - имя лодочной станции, id - номер лодочной станции в матрице расстояний
     -     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Модицицировал метод, переместив calculate()
        if (isFromChose) {
            etFrom.setText(data.getStringExtra("nameBoathouse"));
            fromId = Integer.valueOf(data.getStringExtra("id"));
            calculation();
        } else {
            etTo.setText(data.getStringExtra("nameBoathouse"));
            toId = Integer.valueOf(data.getStringExtra("id"));
            calculation();
        }
    }

    /**
    * Расчет стоимости
    */
    private void calculation() {
        Log.d(LOG, "toId: " + toId + ", fromId: " + fromId);

        if (toId != null && fromId != null /* && time == good */) {
            // Создаём исходный поток в IntentService
            Intent intentCostCalculating = new Intent(this, CostCalculating.class);

            startService(intentCostCalculating.putExtra("time", 2).putExtra("i", fromId).putExtra("j", toId));
            startService(intentCostCalculating);

            // Регистрируем широковещательный канал
            broadcastReceiver = new CostCalculationBroadcastReceiver();

            IntentFilter intentFilter = new IntentFilter(CostCalculating.ACTION_OF_MY_SERVICE);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(broadcastReceiver, intentFilter);

            // Активируем кнопку "Заказать"
            doOrder.setEnabled(true);
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

    public class CostCalculationBroadcastReceiver extends BroadcastReceiver { // Широковещательный канал, с его помощью мы получаем стоимсть поездки от сервиса

        @Override
        public void onReceive(Context context, Intent intent) {
            double result = intent
                    .getDoubleExtra(CostCalculating.EXTRA_KEY_OUT, 0.0);
            cost.setText("Оплата: " + result);
        }
    }
}

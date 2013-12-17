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

    EditText etFrom;
    TextView cost;

    CostCalculationBroadcastReceiver broadcastReceiver;
    private final String LOG = "logMainActivity";
    private final int DIALOG_DATE = 1;
    private final int DIALOG_TIME = 2;

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

        Log.d(myLog,"onCreate");

        etFrom = (EditText) findViewById(R.id.fromChoice);
        etFrom.setOnClickListener(this);

        dateOrder = (Button) findViewById(R.id.dateOrder);
        dateOrder.setOnClickListener(this);

        timeOrder = (Button) findViewById(R.id.timeOrder);
        timeOrder.setOnClickListener(this);

        cost = (TextView) findViewById(R.id.cost);
        cost.setOnClickListener(this);

        // Создаём исходный поток в IntentService
        Intent intentCostCalculating = new Intent(this, CostCalculating.class);

        startService(intentCostCalculating.putExtra("time", 2).putExtra("i", 5).putExtra("j", 2));
        startService(intentCostCalculating);

        broadcastReceiver = new CostCalculationBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter(CostCalculating.ACTION_OF_MY_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(View v) {

        Log.d(myLog,"onClick()");

        switch (v.getId()){
            case R.id.fromChoice:

                Log.d(myLog,"Select from");

                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.toChoice:

                Log.d(myLog,"Select from");

                intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.dateOrder:

                Log.d(myLog, "Pick date");

                showDialog(DIALOG_DATE);
                break;
            case R.id.timeOrder:

                Log.d(myLog, "Pick time");

                showDialog(DIALOG_TIME);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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

    public class CostCalculationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            double result = intent
                    .getDoubleExtra(CostCalculating.EXTRA_KEY_OUT, 0.0);
            cost.setText("Оплата: " + result);
        }
    }
}

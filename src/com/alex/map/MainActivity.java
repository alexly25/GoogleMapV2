package com.alex.map;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import ru.shem.services.CostCalculating;

import java.util.Date;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private final String LOG = "logMainActivity";
    private final int DIALOG_TIME = 2;
    protected static final int ADD_MINUTE = 5;

    protected static int selectedDate;
    protected static int selectedHour;
    protected static int selectedMinute;

    private CostCalculationBroadcastReceiver broadcastReceiver;

    /**
     * Переменная определяет для какого поля был вызван activity
     * true - для поля tvFrom
     * false - для поля tvTo
     */
    private boolean isFromChose;

    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvCost;
    private Button btnTime;
    private Button btnToOrder;
    private Integer fromId;
    private Integer toId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(LOG, "onCreate");

        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvFrom.setOnClickListener(this);

        tvTo = (TextView) findViewById(R.id.tvTo);
        tvTo.setOnClickListener(this);

        btnTime = (Button) findViewById(R.id.btnTime);
        btnTime.setOnClickListener(this);

        btnToOrder = (Button) findViewById(R.id.btnToOrder);
        btnToOrder.setOnClickListener(this);

        tvCost = (TextView) findViewById(R.id.tvCost);
        tvCost.setOnClickListener(this);

        // Автоматически задаем время отправки
        Date date = new Date();
        selectedDate = date.getDate();
        selectedHour = date.getHours();
        selectedMinute = date.getMinutes() + ADD_MINUTE;
        btnTime.setText("Отплываем " + selectedDate + " в " + selectedHour + ":" + selectedMinute);
    }

    @Override
    protected void onDestroy() { // Что делаем при закрытии приложения
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver); // уначтажаем широковеательный канал при закрытии приложения
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // Метод сохраняет состояния объектов при повороте экрана и при не хватке памяти
        super.onSaveInstanceState(outState);
        outState.putString("time", btnTime.getText().toString());
        outState.putString("cost", tvCost.getText().toString());
        outState.putBoolean("btnToOrder", btnToOrder.isEnabled());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { // метод загружает данные и метода onSaveInstanceState(..)
        super.onRestoreInstanceState(savedInstanceState);
        btnTime.setText(savedInstanceState.getString("time"));
        tvCost.setText(savedInstanceState.getString("cost"));
        btnToOrder.setEnabled(savedInstanceState.getBoolean("btnToOrder"));
    }

    @Override
    public void onClick(View v) {

        Log.d(LOG, "onClick()");

        Intent intent;
        switch (v.getId()) {
            case R.id.tvFrom: // Вызываем карту для выбора пункта отправки

                Log.d(LOG, "Select from");

                isFromChose = true;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tvTo: // Вызываем карту для выбора пункта прибытия

                Log.d(LOG, "Select to");

                isFromChose = false;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btnTime: // Вызываем диалоговое окно выбора времени отправки

                Log.d(LOG, "Pick time");

                new TimeDialog(btnTime).show(getSupportFragmentManager(), null);
                break;
            case R.id.btnToOrder: // Вызывем диалоговое окно с информацией о заказе

                Log.d(LOG, "Pick to order");

                new ToOrderDialog(selectedHour, selectedMinute).show(getSupportFragmentManager(), null);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // Модицицировал метод, переместив calculate()
        if (data != null) {

            if (isFromChose) {
                tvFrom.setText(data.getStringExtra("nameBoathouse"));
                fromId = Integer.valueOf(data.getStringExtra("id"));
            } else {
                tvTo.setText(data.getStringExtra("nameBoathouse"));
                toId = Integer.valueOf(data.getStringExtra("id"));
            }

            calculation();
        }
    }

    /**
     * Расчет стоимости
     */
    private void calculation() {
        Log.d(LOG, "toId: " + toId + ", fromId: " + fromId);

        if (toId != null && fromId != null) {
            // Создаём исходный поток в IntentService
            Intent intentCostCalculating = new Intent(this, CostCalculating.class);

            startService(intentCostCalculating.putExtra("time", 2).putExtra("i", fromId).putExtra("j", toId));

            // Регистрируем широковещательный канал
            broadcastReceiver = new CostCalculationBroadcastReceiver();

            IntentFilter intentFilter = new IntentFilter(CostCalculating.ACTION_OF_MY_SERVICE);
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(broadcastReceiver, intentFilter);

            // Активируем кнопку "Заказать"
            btnToOrder.setEnabled(true);
        } else {

            btnToOrder.setEnabled(false);
        }
    }

    public class CostCalculationBroadcastReceiver extends BroadcastReceiver { // Широковещательный канал, с его помощью мы получаем стоимсть поездки от сервиса

        @Override
        public void onReceive(Context context, Intent intent) {
            double result = intent
                    .getDoubleExtra(CostCalculating.EXTRA_KEY_OUT, 0.0);
            tvCost.setText("Оплата: " + result);
        }
    }

}

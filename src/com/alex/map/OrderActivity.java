package com.alex.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import ru.shem.services.CostCalculating;
import ru.shem.services.Variables;

import java.util.Date;

/**
 * ВЕЩИ ДОЛЖНЫ НЗЫВАТЬСЯ СВОИМИ ИМЕНАМИ!!
 * Бывший MainActivity, больше этот класс не стартовый!!!
 */
public class OrderActivity extends FragmentActivity implements View.OnClickListener {

    private static final String LOG = "mylogOrderActivity";
    private static final String statusPause = "pause";
    private static final String statusActual = "actual";

    private CostCalculationBroadcastReceiver broadcastReceiver;
    private Variables var = Variables.getInstance();
    private Booking newBooking;

    private Integer fromId = null;
    private Integer toId = null;

    private boolean isFromChoice;

    private EditText etFrom;
    private EditText etTo;
    private TextView tvCost;
    private EditText etTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG, "onCreate()");
        setContentView(R.layout.order);

        init();

        // Востанавливаем состояние view компонентов перед выполнением Activity
        newBooking = var.getNewBooking();
        resetViews();
    }

    /**
     * Метод инициализирует View коммпоненты
     */
    private void init() {

        etFrom = (EditText) findViewById(R.id.etFrom);
        etFrom.setFocusable(false);
        etFrom.setOnClickListener(this);

        etTo = (EditText) findViewById(R.id.etTo);
        etTo.setFocusable(false);
        etTo.setOnClickListener(this);

        etTime = (EditText) findViewById(R.id.etTime);
        etTime.setFocusable(false);
        etTime.setOnClickListener(this);

        tvCost = (TextView) findViewById(R.id.tvCost);
        tvCost.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        Log.d(LOG, "onResume()");
        super.onResume();

        // Регистрируем широковещательный канал
        broadcastReceiver = new CostCalculationBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter(CostCalculating.ACTION_OF_MY_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // создание меню для создания заказа Furs
        getMenuInflater().inflate(R.menu.make_booking_item, menu);
        return true;
    }

    /**
     * Метод обрабатывает нажатие кнопки "Заказать"
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(LOG, "onOptionsItemSelected() make_booking_item");

        if(item.getItemId() == R.id.make_booking_item){

                newBooking.setStatus(statusActual);

                if (newBooking.isEmpty() // Если не коректные поля заказа
                        || newBooking.getFromDate().getTime() < new Date().getTime()) { // Если заказывается в прошлом времени

                    Toast.makeText(getBaseContext(), R.string.toast_booking_error, Toast.LENGTH_LONG).show();

                } else if (var.getHistoryBookings().addBooking(newBooking)) { // Если заказ успешно сохранился
                    finish();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG, "onPause()");

        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver); // уначтажаем широковеательный канал при закрытии приложения
        }
    }

    /**
     * Метод перезаписывает значения View элиментов на экране заказа
     */
    protected void resetViews() {

        Log.d(LOG, "resetViews()");

        if (newBooking.getFromLocation() != null) {
            etFrom.setText(newBooking.getFromLocation().getName());
        }
        if (newBooking.getToLocation() != null) {
            etTo.setText(newBooking.getToLocation().getName());
        }
        if (newBooking.getFromDate() != null) {
            etTime.setText(newBooking.getFromTime());
        }
        if (newBooking.getCost() != -1) {
            tvCost.setText(newBooking.getCostToString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // Метод сохраняет состояния объектов при повороте экрана и при не хватке памяти

        Log.d(LOG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);

        outState.putString("from", etFrom.getText().toString());
        outState.putString("to", etTo.getText().toString());
        outState.putString("time", etTime.getText().toString());
        outState.putString("cost", tvCost.getText().toString());
        outState.putBoolean("isFromChoice", isFromChoice);
        if(fromId != null)outState.putInt("fromId", fromId);
        if(toId != null)outState.putInt("toId", toId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { // метод загружает данные и метода onSaveInstanceState(..)

        Log.d(LOG, "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);

        etFrom.setText(savedInstanceState.getString("from"));
        etTo.setText(savedInstanceState.getString("to"));
        etTime.setText(savedInstanceState.getString("time"));
        tvCost.setText(savedInstanceState.getString("cost"));
        isFromChoice = savedInstanceState.getBoolean("isFromChoice");
        fromId = savedInstanceState.getInt("fromId");
        toId = savedInstanceState.getInt("toId");
    }

    @Override
    public void onClick(View v) {

        Log.d(LOG, "onClick()");

        Intent intent;

        switch (v.getId()) {
            case R.id.etFrom: // Вызываем карту для выбора пункта отправки

                isFromChoice = true;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.etTo: // Вызываем карту для выбора пункта прибытия

                isFromChoice = false;
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.etTime: // Вызываем диалоговое окно выбора времени отправки

                new TimeDialog(newBooking).show(getSupportFragmentManager(), null);
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

        Log.d(LOG, "onActivityResult()");

        if (data != null) {

            Location location = (Location) data.getSerializableExtra("location");
            String nameBoathouse = location.getName();

            if (isFromChoice) {
                etFrom.setText(nameBoathouse);
                newBooking.setFromLocation(location);
                fromId = Integer.valueOf(data.getStringExtra("id"));
            } else {
                etTo.setText(nameBoathouse);
                newBooking.setToLocation(location);
                toId = Integer.valueOf(data.getStringExtra("id"));
            }

            calculation();
        }
    }

    protected void doCalculation() {
        calculation();
    }

    /**
     * Метод расчитывает стоимость, если поля "Откуда" и "Куда" введенны корректно
     */
    private void calculation() {

        Log.d(LOG, "calculation()");

        if (toId != null && fromId != null && newBooking.getFromDate() != null) {

            if (toId != fromId) {

                // Создаём исходный поток в IntentService
                Intent intentCostCalculating = new Intent(this, CostCalculating.class);

                startService(intentCostCalculating.putExtra("hour", newBooking.getFromDate().getHours()).putExtra("i", fromId).putExtra("j", toId));
            } else {
                Toast.makeText(getBaseContext(), "Вы выбрали одинаковые станции!", Toast.LENGTH_LONG).show();
            }

        }
    }

    public class CostCalculationBroadcastReceiver extends BroadcastReceiver { // Широковещательный канал, с его помощью мы получаем стоимсть поездки от сервиса

        @Override
        public void onReceive(Context context, Intent intent) {
            double costResult = intent
                    .getDoubleExtra(CostCalculating.EXTRA_KEY_OUT, 0.0);
            newBooking.setCost((int) costResult);
            resetViews();
        }
    }


}

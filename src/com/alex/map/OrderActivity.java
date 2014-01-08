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

    private static final String LOG = "logMainActivity";
    private static final String statusPause = "pause";
    private static final String statusActual = "actual";

    private CostCalculationBroadcastReceiver broadcastReceiver;
    private Variables var = Variables.getInstance();
    private Booking newBooking;

    /**
     * Переменная определяет для какого поля был вызван activity
     * true - для поля tvFrom
     * false - для поля tvTo
     */

    private EditText etFrom;
    private EditText etTo;
    private TextView tvCost;
    private EditText etTime;
    private Button btnToOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG, "onCreate()");
        setContentView(R.layout.order);

        init();

        // Востанавливаем состояние view компонентов перед выполнением Activity
        newBooking = var.getHistoryBookings().getBookingPause();
        resetViews();
    }

    @Override
    protected void onResume() {
        Log.d(LOG, "onResume()");
        super.onResume();

        Log.d(LOG, ", newBooking.getDate().getTime()" + newBooking.getDate().toGMTString()
                + " < new Date().getTime()" + new Date().toGMTString()
                + "=" + (newBooking.getDate().getTime() < new Date().getTime()));

        var.getHistoryBookings().checkStatus();

        // Регистрируем широковещательный канал
        broadcastReceiver = new CostCalculationBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter(CostCalculating.ACTION_OF_MY_SERVICE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver, intentFilter);

    }
    public boolean onCreateOptionsMenu(Menu menu) { // создание меню для создания заказа Furs
        getMenuInflater().inflate(R.menu.make_booking_item, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.make_booking_item:
                Log.d(LOG, "onClick() Pick to order");


                newBooking.setStatus(statusActual);

                if (newBooking.isEmpty() // Если не коректные поля переменных заказа
                        || newBooking.getDate().getTime() < new Date().getTime()) { // Если заказывается в прошлом времени

                    Toast.makeText(getBaseContext(), R.string.toast_booking_error, Toast.LENGTH_LONG).show();

                } else if (var.getHistoryBookings().addBooking(newBooking)) { // Если заказ успешно сохранился
                    finish();
                }
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "onDestroy()");

        // Сохранем состояние view компонентов
        newBooking.setStatus(statusPause);
        var.getHistoryBookings().addBooking(newBooking);
        Log.d(LOG, "onDestroy() historyBookings.addBooking(newBooking) newBooking: " + newBooking.toString());
    }

    /**
     * Метод перезаписывает значения View элиментов на экране заказа
     */
    protected void resetViews() {

        Log.d(LOG, "resetViews()");

        etFrom.setText(newBooking.getBoathouseFrom());
        etTo.setText(newBooking.getBoathouseTo());
        etTime.setText(newBooking.getTime());
        tvCost.setText(newBooking.getCostToString());
    }

    /**
     * Находим View коммпоненты
     */
    private void init() {

        // Находим остольные элименты

        etFrom = (EditText) findViewById(R.id.etFrom);
        etFrom.setFocusable(false);
        etFrom.setOnClickListener(this);

        etTo = (EditText) findViewById(R.id.etTo);
        etTo.setFocusable(false);
        etTo.setOnClickListener(this);

        etTime = (EditText) findViewById(R.id.etTime);
        etTime.setFocusable(false);
        etTime.setOnClickListener(this);

        //btnToOrder = (Button) findViewById(R.id.btnToOrder);
      //  btnToOrder.setOnClickListener(this);

        tvCost = (TextView) findViewById(R.id.tvCost);
        tvCost.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // Метод сохраняет состояния объектов при повороте экрана и при не хватке памяти
        super.onSaveInstanceState(outState);
        outState.putString("from", etFrom.getText().toString());
        outState.putString("to", etTo.getText().toString());
        outState.putString("time", etTime.getText().toString());
        outState.putString("cost", tvCost.getText().toString());
      //  outState.putBoolean("btnToOrder", btnToOrder.isEnabled());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) { // метод загружает данные и метода onSaveInstanceState(..)
        super.onRestoreInstanceState(savedInstanceState);
        etFrom.setText(savedInstanceState.getString("from"));
        etTo.setText(savedInstanceState.getString("to"));
        etTime.setText(savedInstanceState.getString("time"));
        tvCost.setText(savedInstanceState.getString("cost"));
       // btnToOrder.setEnabled(savedInstanceState.getBoolean("btnToOrder"));
    }

    @Override
    public void onClick(View v) {

        Log.d(LOG, "onClick()");

        Intent intent;

        switch (v.getId()) {
            case R.id.etFrom: // Вызываем карту для выбора пункта отправки

                Log.d(LOG, "Select from");

                var.setFromChose(true);
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.etTo: // Вызываем карту для выбора пункта прибытия

                Log.d(LOG, "Select to");

                var.setFromChose(false);
                intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.etTime: // Вызываем диалоговое окно выбора времени отправки

                Log.d(LOG, "Pick time");

                new TimeDialog(newBooking).show(getSupportFragmentManager(), null);
                break;
          /*  case R.id.btnToOrder: //Сохраняем данные заказа

                Log.d(LOG, "onClick() Pick to order");

                newBooking.setStatus(statusActual);

                if (newBooking.isEmpty() // Если не коректные поля переменных заказа
                        || newBooking.getDate().getTime() < new Date().getTime()) { // Если заказывается в прошлом времени

                    Toast.makeText(getBaseContext(), R.string.toast_booking_error, Toast.LENGTH_LONG).show();

                } else if (var.getHistoryBookings().addBooking(newBooking)) { // Если заказ успешно сохранился
                    finish();
                }
                break;  */
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

            String nameBoathouse = data.getStringExtra("nameBoathouse");

            Log.d(LOG, "onActivityResult() " + nameBoathouse);

            if (var.isFromChose()) {
                etFrom.setText(nameBoathouse);
                newBooking.setBoathouseFrom(nameBoathouse);
                var.setFromId(Integer.valueOf(data.getStringExtra("id")));
            } else {
                etTo.setText(nameBoathouse);
                newBooking.setBoathouseTo(nameBoathouse);
                var.setToId(Integer.valueOf(data.getStringExtra("id")));
            }

            calculation();
        }
    }

    /**
     * Расчет стоимости
     */
    private void calculation() {
        Log.d(LOG, "toId: " + var.getToId() + ", fromId: " + var.getFromId());

        if (var.getToId() != null && var.getFromId() != null) {

            if (var.getToId() != var.getFromId()) {

                // Создаём исходный поток в IntentService
                Intent intentCostCalculating = new Intent(this, CostCalculating.class);

                startService(intentCostCalculating.putExtra("time", var.getDayOrNight()).putExtra("i", var.getFromId()).putExtra("j", var.getToId()));

               // btnToOrder.setEnabled(true);

            } else {

                tvCost.setText("Вы выбрали одинаковые станции!");

             //   btnToOrder.setEnabled(false);
            }

        } else {
            if (btnToOrder.isEnabled()) {
                btnToOrder.setEnabled(false);
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

package com.alex.map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ru.shem.services.Variables;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 06.01.14
 * <p/>
 * СТАРТОВЫЙ ЭКРАН!!
 * Экран для просмотра всех заказов с кнопкой "Создать заказ" в ActionBar
 */
public class HistoryActivity extends FragmentActivity implements AdapterView.OnItemClickListener {
    private static final String LOG = "mylogHistiryActivity";

    private static Variables var = Variables.getInstance();

    private ListView lvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(LOG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        lvHistory = (ListView) findViewById(R.id.lvHistory);
        lvHistory.setOnItemClickListener(this);


        // Теперь переменные которые используются больше чем в одном экране хранятся в Variables
        var.setHistoryBookings(new HistoryBookings(this, lvHistory));

        // Эмулятор добавления заказа. НЕ УДАЛЯТЬ!!! т.к. у меня(Алеша) не отображаются MenuItem
        /*if (var.getHistoryBookings().addBookingForTests() == 1) {
            Log.d(LOG, "add booking for test");
        }*/
        var.getHistoryBookings().checkStatus();
    }
/*

    @Override
    protected void onResume() {
        Log.d(LOG, "onResume()");
        super.onResume();

        // Обновление списка истории
        //var.getHistoryBookings().checkStatus();
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // создание ActionBar
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // Обработчик нажатия на кнопки в ActionBar

        Intent intent;

        if (item.getItemId() == R.id.btnAddOrder) {
            intent = new Intent(this, OrderActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(LOG, "onItemClick()");

        try {

            Booking booking = var.getHistoryBookings().getBooking(position); // Получаем кликнутый заказ

            Intent intent = new Intent(this, InfoFragment.class);
            intent.putExtra("booking", booking);
            startActivity(intent);

        } catch (Exception e) {
            Log.d(LOG, "!!!!! onItemClick() catch error: " + e.toString());
        }
    }
}

package com.alex.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ru.shem.services.Variables;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 06.01.14
 */
public class HistoryActivity extends FragmentActivity implements View.OnClickListener,  AdapterView.OnItemClickListener {
    private static final String LOG = "logMainActivity";

    private static Variables var = Variables.getInstance();

    private ListView lvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        init();
        var.setHistoryBookings(new HistoryBookings(this, lvHistory));
    }

    @Override
    protected void onResume() {
        Log.d(LOG, "onResume()");
        super.onResume();

        var.getHistoryBookings().checkStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.btnAddOrder:
                intent = new Intent(this, OrderActivity.class);

                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    private void init() {
        lvHistory = (ListView) findViewById(R.id.lvHistory);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(LOG, "onItemClick()");

        try {

            Booking booking = var.getHistoryBookings().getBooking(position); // Получаем кликнутый заказ
            Log.d(LOG, "onItemClick() booking.toString(): " + booking.toString());

            new ShowInfoDialog(booking).show(getSupportFragmentManager(), null); // Выводим информацию о нем

        } catch (Exception e) {
            Log.d(LOG,"!!!!! onItemClick() catch error: " + e.toString());
        }
    }
}

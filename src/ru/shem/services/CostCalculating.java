package ru.shem.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: Madness
 * Date: 15.12.13
 */
public class CostCalculating extends IntentService {

    public static final String ACTION_OF_MY_SERVICE = "ru.shem.intentservice.response";
    public static final String EXTRA_KEY_OUT = "CostResponse";

    public CostCalculating() {
        super("CostCalculating");
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double cost = 0.0;

        int dayOrNight = intent.getIntExtra("time", 1); // сода передаётся, днём или ночью был создан заказ.
        // Значения: 1 или 2

        double present = 0.5; // Процент от расстояния, наценка.
        double range = intent.getDoubleExtra("range", 0); // передаём расстояние указанное в матрице

        cost = (range * present) * dayOrNight; // какая ни какая формула расчёта, потом поменяем, если что.

        /**
         * Всё что ниже, это возврат стоимости в MainActivity .
         */
        Intent intentResponse = new Intent();
        intentResponse.setAction(ACTION_OF_MY_SERVICE);
        intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
        intentResponse.putExtra(EXTRA_KEY_OUT, cost);
        sendBroadcast(intentResponse);
    }
}

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

    private final double[][] distanceMatrix = new double[7][7];

    public CostCalculating() {
        super("CostCalculating");
    }

    public void onCreate() {
        super.onCreate();

        // Главная диагональ матрицы
        distanceMatrix[0][0] =
                distanceMatrix[1][1] =
                        distanceMatrix[2][2] =
                                distanceMatrix[3][3] =
                                        distanceMatrix[4][4] =
                                                distanceMatrix[5][5] =
                                                        distanceMatrix[6][6] = 0;
        // От Ostrov1 к остальным
        distanceMatrix[0][5] = distanceMatrix[5][0] = 6400;
        distanceMatrix[1][5] = distanceMatrix[5][1] = 3980;
        distanceMatrix[2][5] = distanceMatrix[5][2] = 2170;
        distanceMatrix[3][5] = distanceMatrix[5][3] = 3540;
        distanceMatrix[4][5] = distanceMatrix[5][4] = 5520;

        // От Rozhdestvenno к остальным
        distanceMatrix[0][6] = distanceMatrix[6][0] = 12170;
        distanceMatrix[1][6] = distanceMatrix[6][1] = 9620;
        distanceMatrix[2][6] = distanceMatrix[6][2] = 6260;
        distanceMatrix[3][6] = distanceMatrix[6][3] = 4530;
        distanceMatrix[4][6] = distanceMatrix[6][4] = 4820;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        double cost = 0.0;

        int dayOrNight = intent.getIntExtra("time", 1); // сода передаётся, днём или ночью был создан заказ.
        // Значения: 1 или 2

        double present = 0.25; // Процент от расстояния, наценка.
        double range = distanceMatrix[intent.getIntExtra("i", 2)][intent.getIntExtra("j", 6)]; // передаём расстояние указанное в матрице

        cost = ((range * present) + 50) * dayOrNight; // какая ни какая формула расчёта, потом поменяем, если что.

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

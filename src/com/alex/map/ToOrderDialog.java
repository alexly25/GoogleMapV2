package com.alex.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 18.12.13
 * Time: 5:10
 * To change this template use File | Settings | File Templates.
 */
public class ToOrderDialog extends DialogFragment implements OnClickListener {

    private String LOG = "logDialogFragment";
    private int hour, minute; // Выбранное клиентом время
    private String message;

    ToOrderDialog(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        Date d = new Date();

        String logMsg = "ToOrderTime: " + this.hour + ":" + this.minute + ", nowTime: " + d.getHours() + ":" + d.getMinutes();
        Log.d(LOG, logMsg);

        // Проверка времяни
        if (hour < d.getHours()) {

            Log.d(LOG, "hour < d.getHours()");

            message = "Не верно выбрано время";
        } else if (hour == d.getHours() && minute < d.getMinutes()) {

            Log.d(LOG, "hour == d.getHours() && minute < d.getMinutes()");

            message = "Не верно выбрано время";
        } else {

            Log.d(LOG, "else");

            message = "Ваш заказ принят. Информацию о заказе вы можите посмотреть нигде.";
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(LOG, "onCreate");

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Информация о заказе")
                .setPositiveButton("Ok", this)
                .setMessage(message);
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}

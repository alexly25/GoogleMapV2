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
 *
 * Бывший ShowInfoDialog, были траблы с гитом. Изменил
 */
public class InfoDialog extends DialogFragment implements OnClickListener {

    private static final String LOG = "logDialogFragment";
    private static final String statusActual = "actual";
    private Booking booking;

    InfoDialog(Booking booking) {
        this.booking = booking;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(LOG, "onCreateDialog()");

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Информация о заказе")
                .setPositiveButton("Ok", this)
                .setMessage(getInfo());
        return adb.create();
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        Date date = booking.getDate();
        sb.append("Место отправки: ")/*.append(booking.getBoathouseFrom()).append("\n")
                .append("Место прибытия: ").append(booking.getBoathouseTo()).append("\n")*/
                .append("Дата отравки: ").append(date.getDate())
                .append(" в ").append(date.getHours()).append(":")
                .append((date.getMinutes() < 10) ? ("0" + date.getMinutes()) : date.getMinutes() + "\n")
                .append("Стоимость: ").append(booking.getCost()).append(" руб. \n")
                .append("Статус заказа: ").append((booking.getStatus().equals(statusActual)) ? "Актуален" : "Не актуален");
        return sb.toString();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

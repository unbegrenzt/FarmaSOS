/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-10-17 07:39 PM
 */

package com.example.unbegrenzt.fharmaapp.Thread;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import com.example.unbegrenzt.fharmaapp.R;
import org.aviran.cookiebar2.CookieBar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Conexionstate extends Thread {

    private int interval;
    private Activity activity;

    @Override
    public void run(){

        verifynet();
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.currentThread().run();
        Log.i("asdf","12");
    }

    private void verifynet() {
        if (isNetDisponible() && isOnlineNet()) {
            CookieBar.Build(activity)
                    .setTitle("Conectado")
                    .setIcon(R.drawable.ic_update)
                    .setMessage("Está conectado")
                    .setLayoutGravity(Gravity.BOTTOM)
                    .setBackgroundColor(R.color.primary_dark1)
                    .setTitleColor(R.color.color_no_seleccion)
                    .setMessageColor(R.color.color_de_texto)
                    .show();
        }else{
            CookieBar.Build(activity)
                    .setTitle("Mala conexión detectada")
                    .setIcon(R.drawable.ic_update)
                    .setMessage("Está desconectado de internet")
                    .setLayoutGravity(Gravity.BOTTOM)
                    .setBackgroundColor(R.color.primary_dark1)
                    .setTitleColor(R.color.color_no_seleccion)
                    .setMessageColor(R.color.color_de_texto)
                    .show();
        }
    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    private Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}

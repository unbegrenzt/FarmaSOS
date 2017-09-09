/**
 * Created  by LAB-UOL for Jorge Luis Morales Centeno on 08-26-17 11:34 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 08-26-17 11:34 AM
 */

package com.example.unbegrenzt.fharmaapp.actividades;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import com.example.unbegrenzt.fharmaapp.R;
import com.rubengees.introduction.IntroductionBuilder;
import com.rubengees.introduction.Slide;
import com.rubengees.introduction.interfaces.OnSlideListener;

import java.util.ArrayList;
import java.util.List;

public class Intro extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Actyvity para crearla intro del app
        //cargando fragments

        new IntroductionBuilder(this)
                .withSlides(generateSlides())
                .withOnSlideListener(new OnSlideListener() {
                    @Override
                    public void onSlideChanged(int from, int to) {

                        //generando solicitudes de permisos al usuario
                        if (from == 0 && to == 1) {

                            askforpermissions();
                        }

                    }
                }).introduceMyself();

    }

    public void askforpermissions(){

        /*if (ActivityCompat.checkSelfPermission(Intro.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Intro.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
        }*/

        if (ActivityCompat.checkSelfPermission(Intro.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(Intro.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }

        if (ActivityCompat.checkSelfPermission(Intro.this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(Intro.this,
                    new String[]{Manifest.permission.INTERNET}, 12);
        }

        if (ActivityCompat.checkSelfPermission(Intro.this,
                Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(Intro.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 12);
        }

        /*if (ActivityCompat.checkSelfPermission(Intro.this,
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(Intro.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS}, 12);
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntroductionBuilder.INTRODUCTION_REQUEST_CODE && resultCode == RESULT_OK) {

            Intent intent = new Intent(Intro.this, Principal_map.class);
            startActivity(intent);

        }
    }

    //TODO: cambiar las imágenes que utilizaremos
    private List<Slide> generateSlides() {
        List<Slide> result = new ArrayList<>();

        result.add(new Slide()
                .withDescription(getResources().getString(R.string.intro1)).
                        withColorResource(R.color.color_acentuado)
                .withImage(R.drawable.descarga)
        );

        result.add(new Slide()
                .withDescription(getResources().getString(R.string.intro2))
                .withColorResource(R.color.primary_dark1)
                .withImage(R.drawable.ic_farmaco)
        );

        result.add(new Slide()
                .withDescription(getResources().getString(R.string.intro3))
                .withColorResource(R.color.color_base)
                .withImage(R.drawable.ic_add_location)
        );

        return result;
    }
}

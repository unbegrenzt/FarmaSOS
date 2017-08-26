/**
 * Created  by LAB-UOL for Jorge Luis Morales Centeno on 08-26-17 11:34 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 08-26-17 11:34 AM
 */

package com.example.unbegrenzt.fharmaapp.actividades;


import android.graphics.Color;
import android.os.Bundle;

import com.example.unbegrenzt.fharmaapp.Fragments.PagesAdapter;
import com.example.unbegrenzt.fharmaapp.Fragments.izi;
import com.example.unbegrenzt.fharmaapp.R;
import com.github.paolorotolo.appintro.AppIntro;

public class Intro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Actyvity para crearla intro del app

        //cargando fragments
        addSlide(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                getResources().getIdentifier("descarga" , "drawable", getPackageName())));

        addSlide(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                getResources().getIdentifier("descarga1" , "drawable", getPackageName())));

        addSlide(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                getResources().getIdentifier("descarga2" , "drawable", getPackageName())));

        setBarColor(Color.parseColor(String.valueOf(R.color.primary_dark1)));
        setSeparatorColor(Color.parseColor(String.valueOf(R.color.primary1)));
    }
}

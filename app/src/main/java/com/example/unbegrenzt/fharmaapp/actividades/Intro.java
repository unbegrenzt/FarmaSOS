/**
 * Created  by LAB-UOL for Jorge Luis Morales Centeno on 08-26-17 11:34 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 08-26-17 11:34 AM
 */

package com.example.unbegrenzt.fharmaapp.actividades;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.unbegrenzt.fharmaapp.Fragments.PagesAdapter;
import com.example.unbegrenzt.fharmaapp.Fragments.SampleSlide;
import com.example.unbegrenzt.fharmaapp.R;
import com.github.paolorotolo.appintro.AppIntro;

public class Intro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Actyvity para crearla intro del app

        //cargando fragments
        addSlide(/*getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                getResources().getIdentifier("descarga" , "drawable", getPackageName()))*/
                SampleSlide.newInstance(R.layout.fragment_tienda_frag));

        addSlide(PagesAdapter.newInstance(/*getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),*/
                getResources().getIdentifier("descarga1" , "drawable", getPackageName())));

        addSlide(PagesAdapter.newInstance(/*getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),*/
                getResources().getIdentifier("descarga2" , "drawable", getPackageName())));

        //cambio de color en las barras
        setBarColor(Color.parseColor(String.valueOf(R.color.primary_dark1)));
        setSeparatorColor(Color.parseColor(String.valueOf(R.color.primary1)));

        //cinf
        //setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}

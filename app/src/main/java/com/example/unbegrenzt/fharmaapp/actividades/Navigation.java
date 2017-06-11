/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-10-17 07:42 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-10-17 07:42 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.unbegrenzt.fharmaapp.Fragments.Farmacias;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.Fragments.Perfil;
import com.example.unbegrenzt.fharmaapp.Fragments.farmacos;
import com.example.unbegrenzt.fharmaapp.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Navigation extends AppCompatActivity implements Farmacias.OnFragmentInteractionListener,
        Map.OnFragmentInteractionListener, farmacos.OnFragmentInteractionListener,
            Perfil.OnFragmentInteractionListener{

    BottomBar bottomBar;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                //SE maneja el evento click actual
                if (tabId == R.id.tab_catalogo) {

                    fragment = Farmacias.newInstance("sada","dsada");

                }else if (tabId == R.id.tab_mapa){

                    fragment = Map.newInstance("dasd","rfr");

                }else if (tabId == R.id.tab_farmaco){

                    fragment = farmacos.newInstance("dsda","dagrrg");

                }else if (tabId == R.id.tab_profile){

                    fragment = Perfil.newInstance("drrrf","Ffdff");

                }

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame, fragment);
                transaction.commit();

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-10-17 12:16 AM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.unbegrenzt.fharmaapp.Fragments.*;
import com.example.unbegrenzt.fharmaapp.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class FMap extends AppCompatActivity implements izi.OnFragmentInteractionListener,
    Map.OnFragmentInteractionListener, perfil_off.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                if (String.valueOf(item.getTitle()).contentEquals("Tienda")) {
                    //mwnu.setVisibility(View.GONE);
                    fragment = new izi();
                    //setTitle(getString(R.string.hom));

                } else if (String.valueOf(item.getTitle()).contentEquals("Home")) {
                    //Intent intent = new Intent(this, MapsActivity.class);
                    //startActivity(intent);
                    //mwnu.setVisibility(View.VISIBLE);
                    fragment = new Map();
                    //setTitle("Mapa");
                    //bottomNavigation.setCurrentItem(anterior);

                } else if (String.valueOf(item.getTitle()).contentEquals("Cuenta")) {
                    fragment = new perfil_off();
                }
            /*fragment = new farmacos();
            setTitle(getString(R.string.farm));*/

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frames, fragment);
                transaction.commit();
                return false;
            }
        });
        bnve.setCurrentItem(1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

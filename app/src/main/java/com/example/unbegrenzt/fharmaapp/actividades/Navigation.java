/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-10-17 07:42 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-10-17 07:42 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.unbegrenzt.fharmaapp.Fragments.Farmacias;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.Fragments.Perfil;
import com.example.unbegrenzt.fharmaapp.Fragments.farmacos;
import com.example.unbegrenzt.fharmaapp.R;
import com.facebook.CallbackManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Navigation extends AppCompatActivity implements Farmacias.OnFragmentInteractionListener,
        Map.OnFragmentInteractionListener, farmacos.OnFragmentInteractionListener,
            Perfil.OnFragmentInteractionListener,AHBottomNavigation.OnTabSelectedListener{

    AHBottomNavigation bottomNavigation;
    CallbackManager callbackManager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isLoged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.hom));

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();


        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnTabSelectedListener(this);
        this.CreateItems();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    isLoged = true;
                } else {
                    // User is signed out
                    isLoged = false;
                }
                // ...
            }
        };

    }

    @Override
    public void onStop(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void CreateItems(){

        //creando los items
        AHBottomNavigationItem FarmaItem = new AHBottomNavigationItem(getString(R.string.hom),
                R.drawable.ic_home);

        AHBottomNavigationItem MapItem = new AHBottomNavigationItem(getString(R.string.map)
                ,R.drawable.ic_map);

        AHBottomNavigationItem MedItem = new AHBottomNavigationItem(getString(R.string.farm)
                ,R.drawable.ic_farmaco);

        AHBottomNavigationItem ProfItem = new AHBottomNavigationItem(getString(R.string.per)
                ,R.drawable.ic_profile);

        //añadiendo los items
        bottomNavigation.addItem(FarmaItem);
        bottomNavigation.addItem(MapItem);
        bottomNavigation.addItem(MedItem);
        bottomNavigation.addItem(ProfItem);

        //propiedades
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        Fragment fragment = null;

        if(position == 0){

            fragment = new Farmacias();
            setTitle(getString(R.string.hom));

        }else if (position == 1){

            fragment = new Map();
            setTitle(getString(R.string.map));

        }else if (position == 2){

            fragment = new farmacos();
            setTitle(getString(R.string.farm));

        }else if (position == 3){

            fragment = new Perfil();
            setTitle(getString(R.string.per));

        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag, fragment);
        transaction.commit();
        return true;
    }

    public boolean getloged(){
        return this.isLoged;
    }
}

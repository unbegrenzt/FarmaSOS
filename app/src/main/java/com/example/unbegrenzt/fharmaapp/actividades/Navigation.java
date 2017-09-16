/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-10-17 07:42 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-10-17 07:42 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.Fragments.Tienda_frag;
import com.example.unbegrenzt.fharmaapp.Fragments.izi;
import com.facebook.AccessToken;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.unbegrenzt.fharmaapp.Fragments.Perfil;
import com.example.unbegrenzt.fharmaapp.Fragments.farmacos;
import com.example.unbegrenzt.fharmaapp.Fragments.perfil_off;
import com.example.unbegrenzt.fharmaapp.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.keiferstone.nonet.Configuration;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;


public class Navigation extends AppCompatActivity implements izi.OnFragmentInteractionListener,
        Map.OnFragmentInteractionListener, farmacos.OnFragmentInteractionListener,
            Perfil.OnFragmentInteractionListener,perfil_off.OnFragmentInteractionListener
                ,AHBottomNavigation.OnTabSelectedListener, Tienda_frag.OnFragmentInteractionListener{

    AHBottomNavigation bottomNavigation;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isLoged = false;
    private FrameLayout frame;
    int beforestate = 0;
    private FloatingActionButton buttom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        //se crea el toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.hom));

        //callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();



        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnTabSelectedListener(this);

        //instancia del float buttom
        buttom = (FloatingActionButton) findViewById(R.id.butflot);
        this.CreateItems();

        getWindow().setBackgroundDrawable(null);

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

        //TODO: optimizar bateria
        //metodo verificador de internet
        NoNet.monitor(this)
                .poll()
                .callback(new Monitor.Callback() {
                    @Override
                    public void onConnectionEvent(int connectionStatus) {

                        if (connectionStatus == 101){

                            if (!(beforestate == connectionStatus)) {

                                CookieBar.Build(Navigation.this)
                                        .setTitle("Mala conexión detectada")
                                        .setIcon(R.drawable.ic_wifioff)
                                        .setMessage("Está desconectado de internet")
                                        .setLayoutGravity(Gravity.TOP)
                                        .setBackgroundColor(R.color.primary_dark1)
                                        .setTitleColor(R.color.color_no_seleccion)
                                        .setMessageColor(R.color.color_de_texto)
                                        .setDuration(5000)
                                        .show();
                            }
                        }
                        beforestate = connectionStatus;
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void login(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Navigation.this, "Fallo de conexión",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Navigation.this, ";) bien",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d("asdf", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("asdf", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("asdf", "signInWithCredential", task.getException());
                            Toast.makeText(Navigation.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_farma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

        /*AHBottomNavigationItem MedItem = new AHBottomNavigationItem(getString(R.string.farm)
                ,R.drawable.ic_farmaco);*/

        AHBottomNavigationItem ProfItem = new AHBottomNavigationItem(getString(R.string.per)
                ,R.drawable.ic_profile);

        //añadiendo los items
        bottomNavigation.addItem(FarmaItem);
        bottomNavigation.addItem(MapItem);
        //bottomNavigation.addItem(MedItem);
        bottomNavigation.addItem(ProfItem);

        //propiedades
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setCurrentItem(1);

        bottomNavigation.manageFloatingActionButtonBehavior(buttom);
        // Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(mwnu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        Fragment fragment = null;

        if (position == 0) {
            //mwnu.setVisibility(View.GONE);
            fragment = new izi();
            setTitle(getString(R.string.hom));

        } else if (position == 1) {
            //Intent intent = new Intent(this, MapsActivity.class);
            //startActivity(intent);
            //mwnu.setVisibility(View.VISIBLE);
            fragment = new Map();
            setTitle("Mapa");
            //bottomNavigation.setCurrentItem(anterior);

        } else if (position == 2) {
            //mwnu.setVisibility(View.GONE);
            if (isLoged) {
                fragment = new Perfil();
                setTitle(getString(R.string.per));
            } else {
                fragment = new perfil_off();
                setTitle(getString(R.string.per));
            }
            /*fragment = new farmacos();
            setTitle(getString(R.string.farm));*/

        } else if (position == 3) {
            //si la persona se logea ser presenta su perfil en
            //caso contrario se muestra un sms de que debe conectarse


        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag, fragment);
        transaction.commit();

        return true;
    }

    public boolean isLog(boolean log) {
        return this.isLoged = log;
    }

    public void refresh(){
        bottomNavigation.setCurrentItem(3);
        bottomNavigation.refresh();
    }


    public void showtienda(com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmaciasx) {
        //frame.setVisibility(View.VISIBLE);
        //FragmentManager manager = getSupportFragmentManager();
        //FragmentTransaction transaction = manager.beginTransaction();
        //transaction.replace(R.id.farma, Tienda_frag.newInstance(farmaciasx));
        //transaction.commit();
    }

    public void disposetienda() {
        //if(frame != null)
        //frame.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed(){
        if (frame.getVisibility() == View.VISIBLE){
            disposetienda();
        }else{
            super.onBackPressed();
        }
    }
}

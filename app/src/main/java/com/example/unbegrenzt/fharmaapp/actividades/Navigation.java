/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-30-17 01:34 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.example.unbegrenzt.fharmaapp.Adapter.DemoViewPagerAdapter;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.Fragments.Tienda_frag;
import com.example.unbegrenzt.fharmaapp.Fragments.izi;
import com.example.unbegrenzt.fharmaapp.animaciones.FABehavior;
import com.facebook.AccessToken;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.unbegrenzt.fharmaapp.Fragments.Perfil;
import com.example.unbegrenzt.fharmaapp.Fragments.farmacos;
import com.example.unbegrenzt.fharmaapp.Fragments.perfil_off;
import com.example.unbegrenzt.fharmaapp.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;


public class Navigation extends AppCompatActivity implements izi.OnFragmentInteractionListener,
        Map.OnFragmentInteractionListener, farmacos.OnFragmentInteractionListener,
            Perfil.OnFragmentInteractionListener,perfil_off.OnFragmentInteractionListener
        ,Tienda_frag.OnFragmentInteractionListener{

    AHBottomNavigation bottomNavigation;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean isLoged = false;
    private FrameLayout frame;
    int beforestate = 0;

    // UI
    private AHBottomNavigationViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private Handler handler = new Handler();
    private DemoViewPagerAdapter adapter;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private Fragment currentFragment;
    private FloatingActionButton fab;

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


        initUI();

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

        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);

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

        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
        // Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(mwnu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showOrHideBottomNavigation(boolean show) {
        if (show) {
            bottomNavigation.restoreBottomNavigation(true);
        } else {
            bottomNavigation.hideBottomNavigation(true);
        }
    }

    /**
     * Show or hide selected item background
     */
    public void updateSelectedBackgroundVisibility(boolean isVisible) {
        bottomNavigation.setSelectedBackgroundVisible(isVisible);
    }


    public void setForceTitleHide(boolean forceTitleHide) {
        AHBottomNavigation.TitleState state = forceTitleHide ? AHBottomNavigation.TitleState.ALWAYS_HIDE : AHBottomNavigation.TitleState.ALWAYS_SHOW;
        bottomNavigation.setTitleState(state);
    }


    public int getBottomNavigationNbItems() {
        return bottomNavigation.getItemsCount();
    }

    private void initUI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        //intancias
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        AHBottomNavigationItem tienda = new AHBottomNavigationItem(getString(R.string.hom),
                R.drawable.ic_mall);

        AHBottomNavigationItem mapa = new AHBottomNavigationItem(getString(R.string.map)
                , R.drawable.ic_map);

        AHBottomNavigationItem perfil = new AHBottomNavigationItem(getString(R.string.per)
                , R.drawable.ic_profile);

        bottomNavigationItems.add(tienda);
        bottomNavigationItems.add(mapa);
        bottomNavigationItems.add(perfil);

        bottomNavigation.addItems(bottomNavigationItems);


        bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
        bottomNavigation.setTranslucentNavigationEnabled(true);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewPager.getCurrentItem() == 1) {
                    Fragment fragment = adapter.getItem(1);
                    if (fragment != null && fragment instanceof Map) {
                        ((Map) fragment).move_to_my_pos();
                    }
                }

                if (viewPager.getCurrentItem() == 0) {
                    Fragment fragment = adapter.getItem(0);
                    if (fragment != null && fragment instanceof izi) {
                        Intent intent = new Intent(Navigation.this, ggeasyy.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewPager.getCurrentItem() == 1) {

                    Fragment fragment = adapter.getItem(1);
                    if (fragment != null && fragment instanceof Map) {
                        ((Map) fragment).farm_cercana(200);
                    }

                }

            }
        });



        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                /*if (currentFragment == null) {
                    currentFragment = adapter.getCurrentFragment();
                }

                if (wasSelected) {
                    currentFragment.refresh();
                    return true;
                }

                if (currentFragment != null) {
                    currentFragment.willBeHidden();
                }*/

                if (viewPager.getCurrentItem() != 1){
                    fab.setVisibility(View.VISIBLE);
                }else {
                    fab.setVisibility(View.GONE);
                }

                viewPager.setCurrentItem(position, false);

                if (currentFragment == null) {
                    return true;
                }

                currentFragment = adapter.getCurrentFragment();
                //currentFragment.willBeDisplayed();

                return true;
            }
        });

		/*
		bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
			@Override public void onPositionChange(int y) {
				Log.d("DemoActivity", "BottomNavigation Position: " + y);
			}
		});
		*/

        viewPager.setOffscreenPageLimit(4);
        adapter = new DemoViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        currentFragment = adapter.getCurrentFragment();

        bottomNavigation.setCurrentItem(1);

    }

    //posicionamos el otro floating buttom
    public void manageoveranotherfab(FloatingActionButton fab) {
        if (fab.getParent() instanceof CoordinatorLayout) {
            FABehavior fabBehavior = new FABehavior(fab.getHeight());
            ((CoordinatorLayout.LayoutParams) fab.getLayoutParams())
                    .setBehavior(fabBehavior);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

        /*Fragment fragment = null;

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

        /*} else if (position == 3) {
            //si la persona se logea ser presenta su perfil en
            //caso contrario se muestra un sms de que debe conectarse


        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag, fragment);
        transaction.commit();*/

    public boolean isLog(boolean log) {
        return this.isLoged = log;
    }


    //hacemos que las instancias creadas en el adaptador se actualizen
    /*public void refresh(int pos){

        adapter.refreshpos(pos);
        viewPager.refreshDrawableState();

    }*/

    private void initplacefragmet() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("gg", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("gg", "An error occurred: " + status);
            }
        });

        autocompleteFragment.setText("");

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

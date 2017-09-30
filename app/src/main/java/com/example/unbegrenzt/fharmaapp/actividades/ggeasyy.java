/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 09-24-17 11:20 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-24-17 11:20 AM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.unbegrenzt.fharmaapp.Adapter.ItemPagerAdapter;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.behavior.BottomSheetBehaviorGoogleMapsLike;
import com.example.unbegrenzt.fharmaapp.behavior.MergedAppBarLayoutBehavior;
import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.nipunbirla.boxloader.BoxLoaderView;

public class ggeasyy extends AppCompatActivity implements Map.OnFragmentInteractionListener{


    int[] mDrawables = {
            R.drawable.ic_add_location,
            R.drawable.background_splash,
            R.drawable.catalogo,
            R.drawable.cloud_off,
            R.drawable.ic_exit,
            R.drawable.ic_clock
    };

    private OptionsFabLayout fabWithOptions;
    private BoxLoaderView boxLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ggeasy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*
          If we want to listen for states callback
         */

        initbars();
        loadmap();
        initFabActionMenu();
        initplacefragmet();

    }

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_map, menu);
        return true;
    }



    private void initbars(){

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehaviorGoogleMapsLike behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        Log.d("bottomsheet-", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_SETTLING:
                        Log.d("bottomsheet", "STATE_SETTLING");
                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        AppBarLayout mergedAppBarLayout = (AppBarLayout) findViewById(R.id.merged_appbarlayout);
        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle("Title Dummy");
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
            }
        });

        TextView bottomSheetTextView = (TextView) bottomSheet.findViewById(R.id.bottom_sheet_title);
        bottomSheetTextView.setText("gg izii");
        ItemPagerAdapter adapter = new ItemPagerAdapter(this,mDrawables);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
    }

    private void loadmap(){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.dummy_framelayout_replacing_map, new Map(), "map");
        transaction.commit();
    }

    private void initFabActionMenu() {

        boxLoader = (BoxLoaderView)findViewById(R.id.progress);
        fabWithOptions = (OptionsFabLayout) findViewById(R.id.fab_l);

        //Set mini fab's colors.
        fabWithOptions.setMiniFabsColors(
                R.color.accent2,
                R.color.accent2);

        //Set main fab clicklistener.
        fabWithOptions.setMainFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabWithOptions.isOptionsMenuOpened())
                    fabWithOptions.closeOptionsMenu();
            }
        });

        //Set mini fabs clicklisteners.
        fabWithOptions.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {

                Map fragment;
                switch (fabItem.getItemId()) {
                    case R.id.fab_pos:
                        fragment = (Map)getSupportFragmentManager().findFragmentByTag("map");
                        if (fragment != null) {
                            fragment.move_to_my_pos();
                        }
                        break;
                    case R.id.fab_loc:
                        fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                        if (fragment != null) {
                            fragment.farm_cercana(100);
                            boxLoader.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void disposebox(){
        boxLoader.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Closes menu if its opened.

        if (fabWithOptions.isOptionsMenuOpened())
            fabWithOptions.closeOptionsMenu();
        else if(boxLoader.getVisibility() == View.VISIBLE){
            boxLoader.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

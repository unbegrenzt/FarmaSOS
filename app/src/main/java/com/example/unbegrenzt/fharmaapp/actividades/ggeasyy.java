/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-02-17 02:32 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.unbegrenzt.fharmaapp.Adapter.ItemPagerAdapter;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.Interfaces.RequestType;
import com.example.unbegrenzt.fharmaapp.Interfaces.Respuesta;
import com.example.unbegrenzt.fharmaapp.Objects.Comentario;
import com.example.unbegrenzt.fharmaapp.Objects.Peticion;
import com.example.unbegrenzt.fharmaapp.Objects.Respuestas;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.behavior.BottomSheetBehaviorGoogleMapsLike;
import com.example.unbegrenzt.fharmaapp.behavior.MergedAppBarLayoutBehavior;
import com.example.unbegrenzt.fharmaapp.web_service.clases.ObjDistance.Element;
import com.example.unbegrenzt.fharmaapp.web_service.clases.ObjPlaceWS.PlaceWS;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.ndroid.nadim.sahel.CoolToast;
import com.nipunbirla.boxloader.BoxLoaderView;
import com.squareup.picasso.Picasso;
import me.grantland.widget.AutofitTextView;
import noman.googleplaces.PlaceType;

import java.util.ArrayList;
import java.util.List;

public class ggeasyy extends AppCompatActivity implements
        Map.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

    private static final int PICK_IMAGE = 100;
    private OptionsFabLayout fabWithOptions;
    private BoxLoaderView boxLoader;
    private BottomSheetBehaviorGoogleMapsLike<View> behavior;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView userfoto;
    private TextView username;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private static final int LOGIN_SUCCESS = 64206;
    private static final int OK = -1;
    private View bottomSheet;
    private ImageView userportada;
    private AccessTokenTracker accessTokenTracker;
    private DatabaseReference colaborar;
    private ValueEventListener colaborarlistenr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
          If we want to listen for states callback
         */

        initbars();
        loadmap();
        initFabActionMenu();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initplacefragmet();
                initUIs();
                initFirebase();
            }
        });


        //TODO:verificar los permisos al entrar

    }

    private void initUIs() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //iniciamos las vistas del usuario actual
        userfoto = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.foto_perfil);
        username = (TextView)navigationView.getHeaderView(0).findViewById(R.id.name);
        userportada = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.fondo);

        loginButton = (LoginButton)navigationView.getHeaderView(0).findViewById(R.id.login_button2);
        loginButton.setReadPermissions("email","public_profile");

        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //se pasa el token de facebook a firebase
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code

            }

            @Override
            public void onError(FacebookException exception) {
                // App code

            }
        });

        userfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginButton.performClick();

            }
        });


        //TODO: aqui permiso de escritura y lectura en tarjetas sd

        userportada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();
            }
        });


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {

                if (currentAccessToken == null) {

                    FirebaseAuth.getInstance().signOut();

                    Picasso.with(getApplicationContext()).load(R.drawable.ic_person).placeholder(R.drawable.ic_download)
                            .error(R.drawable.ic_person).resize(40, 40).into(userfoto);

                    username.setText("Usuario anónimo");

                    if (colaborarlistenr != null){

                        colaborar.removeEventListener(colaborarlistenr);

                    }

                    Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                    if (fragment != null) {
                        fragment.photo = Uri.parse("ggsdar");
                    }

                }
            }
        };

    }

    private void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Autenticación fallida.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_SUCCESS){

            if(resultCode == OK){


                new CoolToast(ggeasyy.this)
                        .make("Sesión iniciada",CoolToast.INFO,CoolToast.LONG,true);

            }

        }

        if (requestCode == 12 && resultCode != OK) {

            new CoolToast(ggeasyy.this)
                    .make("Permiso porfavor",CoolToast.DANGER,CoolToast.LONG,true);

        }

        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE && data != null){

            Picasso.with(getApplicationContext()).load(data.getData()).placeholder(R.drawable.cloud_off)
                    .error(R.drawable.ic_person).resize(userportada.getWidth(),userportada.getHeight())
                    .into(userportada);

        }

    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();

        colaborar = FirebaseDatabase.getInstance().getReference("peticion");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    // User is signed in
                    FirebaseUser user =  firebaseAuth.getCurrentUser();

                    username.setText(user.getDisplayName());

                    colaborar.addValueEventListener(colaborarlistenr = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int count = 0;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                List<Peticion> peticion = new ArrayList<>();
                                peticion.add(postSnapshot.getValue(Peticion.class));

                                count ++;

                                if (count == dataSnapshot.getChildrenCount()) {
                                    Log.e("ggizi","last_info");
                                    Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                                    while (fragment == null){
                                        Log.e("ggizi","aqui");
                                    }
                                    fragment.peticion(peticion);

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).placeholder(R.drawable.ic_download)
                            .error(R.drawable.ic_person).resize(80,80).into(userfoto);

                    Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                    if (fragment != null) {
                        fragment.photo = user.getPhotoUrl();
                        fragment.updateLocationUI();
                    }

                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        accessTokenTracker.startTracking();

    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        if (accessTokenTracker != null){

            accessTokenTracker.stopTracking();
        }
        if (colaborarlistenr != null){

            colaborar.removeEventListener(colaborarlistenr);

        }

    }

    private void initplacefragmet() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                if (fragment != null) {
                    Log.e("ggizi","aqui");
                    fragment.get_search(place.getId());
                }
            }

            @Override
            public void onError(Status status) {
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
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
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
                        fabWithOptions.setVisibility(View.VISIBLE);
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
        mergedAppBarLayoutBehavior.setToolbarTitle(" ");
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
            }
        });

        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);
    }

    private void loadmap(){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.dummy_framelayout_replacing_map, new Map(), "map");
        transaction.commit();

    }

    private void askforpermissions(){
        if (ActivityCompat.checkSelfPermission(ggeasyy.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(ggeasyy.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        }

        if (ActivityCompat.checkSelfPermission(ggeasyy.this,
                android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(ggeasyy.this,
                    new String[]{android.Manifest.permission.INTERNET}, 12);
        }

        if (ActivityCompat.checkSelfPermission(ggeasyy.this,
                android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(ggeasyy.this,
                    new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE}, 12);
        }
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

                askforpermissions();

                if (fabWithOptions.isOptionsMenuOpened())
                    fabWithOptions.closeOptionsMenu();
            }
        });

        //Set mini fabs clicklisteners.
        fabWithOptions.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {

                final Map fragment;

                switch (fabItem.getItemId()) {
                    case R.id.fab_pos:
                        fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                        if (fragment != null) {
                            fragment.move_to_my_pos();
                        }
                        break;
                    case R.id.fab_loc:

                        fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                        if (fragment != null) {

                            if (SharedPreferencesManager.getInstance().getValue("no_preguntar",
                                    Boolean.class)){

                                // codigo que borra los markers offline para no
                                //sobreescribir

                                fragment.farm_cercana(500, SharedPreferencesManager.getInstance()
                                        .getValue("recordar_seleccion",String.class));

                            } else {

                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this,
                                        R.style.dialog_formal);
                                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                                mBuilder.setTitle("Seleccione el local a buscar");
                                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
                                final CheckBox checkBox = (CheckBox) mView.findViewById(R.id.recordar_selecc);

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ggeasyy.this,
                                        android.R.layout.simple_spinner_item,
                                        getResources().getStringArray(R.array.local_type));
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                mSpinner.setAdapter(adapter);


                                //para validar checkbox
                                if (!SharedPreferencesManager.getInstance().getValue("no_preguntar",
                                        Boolean.class)) {

                                    checkBox.setChecked(false);

                                }

                                mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //obtenemos el estado del checkbox

                                        //TODO: AQUI LIMPIA INTERFACE

                                        if (checkBox.isChecked()) {

                                            //validamos para tener listo el modo fuera de linea
                                            SharedPreferencesManager.getInstance().putValue("no_preguntar", true);

                                            if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Farmacia")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.PHARMACY);

                                                fragment.farm_cercana(500, PlaceType.PHARMACY);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            } else if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Hospital")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.HOSPITAL);

                                                fragment.farm_cercana(500, PlaceType.HOSPITAL);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            } else if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Doctor")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.HOSPITAL);

                                                fragment.farm_cercana(500, PlaceType.DOCTOR);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            } else if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Dentista")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.DENTIST);

                                                fragment.farm_cercana(500, PlaceType.DENTIST);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            }

                                        } else {

                                            SharedPreferencesManager.getInstance().putValue("no_preguntar", false);

                                            if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Farmacia")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.PHARMACY);

                                                fragment.farm_cercana(500, PlaceType.PHARMACY);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            } else if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Hospital")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.HOSPITAL);

                                                fragment.farm_cercana(500, PlaceType.HOSPITAL);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            } else if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Doctor")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.HOSPITAL);

                                                fragment.farm_cercana(500, PlaceType.DOCTOR);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            } else if (mSpinner.getSelectedItem().toString()
                                                    .equalsIgnoreCase("Dentista")) {

                                                SharedPreferencesManager.getInstance().putValue("recordar_seleccion",
                                                        PlaceType.DENTIST);

                                                fragment.farm_cercana(500, PlaceType.DENTIST);
                                                boxLoader.setVisibility(View.VISIBLE);

                                            }

                                        }
                                    }
                                });

                                mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.dismiss();
                                    }
                                });

                                mBuilder.setView(mView);
                                AlertDialog dialog = mBuilder.create();
                                dialog.show();
                            }

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

    public void showbox(){
        boxLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        //validamos que antes de salir el usuario no
        //desee cerrar algo
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else if (fabWithOptions.isOptionsMenuOpened()) {

            fabWithOptions.closeOptionsMenu();

        } else if (boxLoader.getVisibility() == View.VISIBLE){

            boxLoader.setVisibility(View.GONE);

        } else if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

        } else if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_SETTLING){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

        } else if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

        } else if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

        } else if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

        } else {
            //TODO:Mandar mensaje si deseo salir y presentar la opc de no volver a mostrar
            super.onBackPressed();

        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)  {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Map fragment;

        if (id == R.id.mis_sitios) {

            fragment = (Map)getSupportFragmentManager().findFragmentByTag("map");
            if (fragment != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fragment.mis_sitios();
                    }
                });

            }

        } else if (id == R.id.terreno) {

            fragment = (Map)getSupportFragmentManager().findFragmentByTag("map");
            if (fragment != null) {
                fragment.setMaptype(GoogleMap.MAP_TYPE_TERRAIN);
            }

        } else if (id == R.id.satelite) {

            fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
            if (fragment != null) {
                fragment.setMaptype(GoogleMap.MAP_TYPE_HYBRID);
            }

        } else if (id == R.id.normal) {

            fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
            if (fragment != null) {
                fragment.setMaptype(GoogleMap.MAP_TYPE_NORMAL);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.comentarios) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null){

                comentario_dialog();

            } else {

                face_dialog();

            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void face_dialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this,
                R.style.dialog_formal);
        View mView = getLayoutInflater().inflate(R.layout.dialog_face, null);
        mBuilder.setTitle("Inicia Sesión");


        mBuilder.setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                loginButton.performClick();

            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void comentario_dialog() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this,
                R.style.dialog_formal);
        View mView = getLayoutInflater().inflate(R.layout.sugerencia_dialog, null);
        mBuilder.setTitle("Sugiere una actualización");
        final EditText sugerencia = (EditText) mView.findViewById(R.id.suggest);


        mBuilder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatabaseReference databaseFarma;
                databaseFarma = FirebaseDatabase.getInstance().getReference("comentarios");
                String id = databaseFarma.push().getKey();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                Comentario comentario = new Comentario(user.getUid(), String.valueOf(sugerencia.getText()));

                databaseFarma.child(id).setValue(comentario);

            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    public void behaviordimiss(){

        if (behavior.getState() == BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

        }
    }

    public void peticion_dialog(final LatLng latLng){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this,
                R.style.dialog_formal);
        View mView = getLayoutInflater().inflate(R.layout.peticion_dialog, null);
        mBuilder.setTitle("Hacer una pregunta");

        LinearLayout transito = (LinearLayout)mView.findViewById(R.id.transito);
        final RadioButton rd_transito = (RadioButton)mView.findViewById(R.id.rd_transito);

        LinearLayout clima = (LinearLayout)mView.findViewById(R.id.clima);
        final RadioButton rd_clima = (RadioButton)mView.findViewById(R.id.rd_clima);

        transito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rd_transito.setChecked(true);
                rd_clima.setChecked(false);

            }
        });

        rd_transito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rd_clima.setChecked(false);
            }
        });

        rd_clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rd_transito.setChecked(false);
            }
        });

        clima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rd_transito.setChecked(false);
                rd_clima.setChecked(true);

            }
        });

        mBuilder.setPositiveButton("Preguntar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(rd_transito.isChecked()){

                    Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                    if (fragment != null) {

                        CreateRequest(RequestType.TRANSIT,latLng);

                    }

                }else{

                    Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                    if (fragment != null) {

                            Log.e("ggizi", "aquiii");
                            CreateRequest(RequestType.CLIMA,latLng);

                    }

                }

            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void CreateRequest(int type, LatLng latLng) {

        if (type == RequestType.TRANSIT){


            Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
            if (fragment != null) {

                fragment.verifydistance(type, latLng);


            }

            //TODO:se debe ver que los marker no esten a una distancia menor a 600 mts
            //create_transit(latLng);
            //Falta contador externo

            //TODO:recorda que solo haga una vez a cada usuario la peticion



        } else if (type == RequestType.CLIMA){

            //createclima(latLng);

            Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
            if (fragment != null) {

                Log.e("ggizi","en verificar");
                fragment.verifydistance(type, latLng);

            }

        }

    }

    public void createclima(LatLng latLng) {

        DatabaseReference databasepet;
        databasepet = FirebaseDatabase.getInstance().getReference("peticion");
        String id = databasepet.push().getKey();

        DatabaseReference databaseRespuestas;
        databaseRespuestas = FirebaseDatabase.getInstance().getReference("respuestas");
        String idResp = databaseRespuestas.push().getKey();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        Peticion peticion = new Peticion(RequestType.CLIMA, user.getUid(), latLng.latitude, latLng.longitude,
                idResp);

        List<String> Uid = new ArrayList<>();
        Uid.add(user.getUid());

        List<Integer> respu = new ArrayList<>();
        respu.add(Respuesta.MASTER);

        Respuestas resp = new Respuestas(Uid, respu, id);

        databasepet.child(id).setValue(peticion);
        databaseRespuestas.child(idResp).setValue(resp);

        //TODO:append dialog

    }

    public void create_transit(LatLng latLng) {

        DatabaseReference databasepet;
        databasepet = FirebaseDatabase.getInstance().getReference("peticion");
        String id = databasepet.push().getKey();

        DatabaseReference databaseRespuestas;
        databaseRespuestas = FirebaseDatabase.getInstance().getReference("respuestas");
        String idResp = databaseRespuestas.push().getKey();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        Peticion peticion = new Peticion(RequestType.TRANSIT, user.getUid(), latLng.latitude, latLng.longitude,
                idResp);

        List<String> Uid = new ArrayList<>();
        Uid.add(user.getUid());

        List<Integer> respu = new ArrayList<>();
        respu.add(Respuesta.MASTER);

        Respuestas resp = new Respuestas(Uid, respu, id);

        databasepet.child(id).setValue(peticion);
        databaseRespuestas.child(idResp).setValue(resp);

        //TODO: clima needs dialog

    }

    public void Drawpharma(final PlaceWS body) {

        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
        fabWithOptions.closeOptionsMenu();
        fabWithOptions.setVisibility(View.GONE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //boton para llamar
                ImageButton llamar = (ImageButton) bottomSheet.findViewById(R.id.btn_llamar);
                llamar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (body.getResult().getFormattedPhoneNumber() != null) {

                            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s", body.getResult().getFormattedPhoneNumber())));

                            if (ActivityCompat.checkSelfPermission(ggeasyy.this, android.Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(ggeasyy.this,
                                        new String[]{android.Manifest.permission.CALL_PHONE}, 12);

                            } else {
                                startActivity(i);
                            }

                        } else {

                            new CoolToast(ggeasyy.this)
                                    .make("No se proporcionó", CoolToast.INFO, CoolToast.LONG, true);

                        }
                    }
                });

                //logica para guardar
                final TextView text_guardar = (TextView) bottomSheet.findViewById(R.id.txt_guardar);
                final ImageButton guardar = (ImageButton) bottomSheet.findViewById(R.id.btn_guardar);
                if (SharedPreferencesManager.getInstance().getValue(
                        String.valueOf(body.getResult().getGeometry().getLocation().getLat()) + "," +
                                String.valueOf(body.getResult().getGeometry().getLocation().getLng()),
                        PlaceWS.class) != null) {

                    guardar.setImageResource(R.drawable.ic_bookmark_black_36dp);
                    text_guardar.setText(getString(R.string.olv));

                } else {

                    guardar.setImageResource(R.drawable.ic_bookmark_border_black_36dp);
                    text_guardar.setText(getString(R.string.keep));

                }
                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SharedPreferencesManager.getInstance().getValue(
                                String.valueOf(body.getResult().getGeometry().getLocation().getLat()) + "," +
                                        String.valueOf(body.getResult().getGeometry().getLocation().getLng()),
                                PlaceWS.class) != null) {

                            SharedPreferencesManager.getInstance().remove(
                                    String.valueOf(body.getResult().getGeometry().getLocation().getLat()) + "," +
                                            String.valueOf(body.getResult().getGeometry().getLocation().getLng()));

                            guardar.setImageResource(R.drawable.ic_bookmark_border_black_36dp);
                            text_guardar.setText(getString(R.string.keep));

                        } else {

                            SharedPreferencesManager.getInstance().putValue(
                                    String.valueOf(body.getResult().getGeometry().getLocation().getLat()) + "," +
                                            String.valueOf(body.getResult().getGeometry().getLocation().getLng()),
                                    body);

                            guardar.setImageResource(R.drawable.ic_bookmark_black_36dp);
                            text_guardar.setText(getString(R.string.olv));

                        }
                    }
                });

                //logica para ir al website
                ImageButton website = (ImageButton) bottomSheet.findViewById(R.id.btn_website);
                website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (body.getResult().getWebsite() != null) {

                            Uri uri = Uri.parse(body.getResult().getWebsite());

                            if (ActivityCompat.checkSelfPermission(ggeasyy.this,
                                    android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(ggeasyy.this,
                                    android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(ggeasyy.this,
                                        new String[]{android.Manifest.permission.INTERNET}, 12);

                            } else {

                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            }

                        } else {

                            new CoolToast(ggeasyy.this)
                                    .make("No posee", CoolToast.INFO, CoolToast.LONG, true);

                        }
                    }
                });

                //configuracion del titulo
                AutofitTextView titulo = (AutofitTextView) bottomSheet.findViewById(R.id.titulo);
                titulo.setText(body.getResult().getName());

                //configuracion del subtitulo
                AutofitTextView subtitulo = (AutofitTextView) bottomSheet.findViewById(R.id.subtitulo);
                if (body.getResult().getOpeningHours() != null) {
                    if (body.getResult().getOpeningHours().getOpenNow()) {

                        if (body.getResult().getInternationalPhoneNumber() != null) {

                            subtitulo.setText("Abierto - " + body.getResult().getInternationalPhoneNumber());
                        } else {

                            subtitulo.setText("Abierto - " + body.getResult().getFormattedAddress());
                        }

                    } else {

                        if (body.getResult().getInternationalPhoneNumber() != null) {

                            subtitulo.setText("Cerrado - " + body.getResult().getInternationalPhoneNumber());
                        } else {

                            subtitulo.setText("Cerrado - " + body.getResult().getFormattedAddress());
                        }

                    }
                } else {

                    if (body.getResult().getInternationalPhoneNumber() != null) {

                        subtitulo.setText(body.getResult().getInternationalPhoneNumber() + " - "+
                                body.getResult().getFormattedAddress());
                    }

                }

                //para poner a la vista el telefono
                if (body.getResult().getFormattedPhoneNumber() != null) {

                    AutofitTextView info_cel = (AutofitTextView) bottomSheet.findViewById(R.id.info_tel);
                    info_cel.setText(body.getResult().getFormattedPhoneNumber());
                }

                //para poner a la vista la dirección
                if (body.getResult().getFormattedAddress() !=  null){

                    AutofitTextView info_cel = (AutofitTextView) bottomSheet.findViewById(R.id.info_direc);
                    info_cel.setText(body.getResult().getFormattedAddress());
                }

                //para poner el horario
                if (body.getResult().getOpeningHours() !=  null){

                    TextView info_horario = (TextView) bottomSheet.findViewById(R.id.info_horario);
                    info_horario.setText(body.getResult().getOpeningHours().getWeekdayText().get(0) + "\n" +
                        body.getResult().getOpeningHours().getWeekdayText().get(1) + "\n" +
                            body.getResult().getOpeningHours().getWeekdayText().get(2) + "\n" +
                            body.getResult().getOpeningHours().getWeekdayText().get(3) + "\n" +
                            body.getResult().getOpeningHours().getWeekdayText().get(4) + "\n" +
                            body.getResult().getOpeningHours().getWeekdayText().get(5) + "\n" +
                            body.getResult().getOpeningHours().getWeekdayText().get(6));
                }

                //fab para ir al lugar deseado
                FloatingActionButton ir_a = (FloatingActionButton) findViewById(R.id.ir_a);
                ir_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                        if (fragment != null) {

                            fragment.ir_a(new LatLng(body.getResult().getGeometry().getLocation().getLat(),
                                            body.getResult().getGeometry().getLocation().getLng()));
                        }
                    }
                });

                //logica para obtener la imagenes
                Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                if (fragment != null) {

                    ItemPagerAdapter adapter = new ItemPagerAdapter(getApplicationContext()
                            ,body.getResult().getPlaceId(),fragment.mGoogleApiClient);
                    ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                    viewPager.setAdapter(adapter);

                }

                if (boxLoader.getVisibility() == View.VISIBLE){

                    disposebox();
                }

            }
        });
    }

    public void CreatecoolToast(String texto, int info, int duration, boolean rounded) {
        new CoolToast(ggeasyy.this)
                .make(texto, info, duration, rounded);
    }

    public void create_dialog_peticion(final Peticion aPeticion, Element element) {

        if (aPeticion.getTipo() == RequestType.TRANSIT) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this,
                    R.style.dialog_formal);
            View mView = getLayoutInflater().inflate(R.layout.dialog_transit, null);
            mBuilder.setTitle("Contribuye!");

            LinearLayout nose = (LinearLayout)mView.findViewById(R.id.no_se);
            LinearLayout bueno = (LinearLayout)mView.findViewById(R.id.Bueno);
            final LinearLayout pesimo = (LinearLayout)mView.findViewById(R.id.pesimo);

            final RadioButton rd_nose = (RadioButton)mView.findViewById(R.id.rd_no_se);
            final RadioButton rd_bueno = (RadioButton)mView.findViewById(R.id.rd_bueno);
            final RadioButton rd_pesimo = (RadioButton)mView.findViewById(R.id.rd_pesimo);

            nose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rd_nose.setChecked(true);
                    rd_bueno.setChecked(false);
                    rd_pesimo.setChecked(false);

                }
            });

            bueno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rd_nose.setChecked(false);
                    rd_bueno.setChecked(true);
                    rd_pesimo.setChecked(false);

                }
            });

            pesimo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rd_nose.setChecked(false);
                    rd_bueno.setChecked(false);
                    rd_pesimo.setChecked(true);

                }
            });

            final View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();

                    switch (id){

                        case R.id.rd_no_se:

                            rd_bueno.setChecked(false);
                            rd_pesimo.setChecked(false);

                            break;

                        case R.id.rd_bueno:

                            rd_nose.setChecked(false);
                            rd_pesimo.setChecked(false);

                            break;

                        case R.id.rd_pesimo:

                            rd_nose.setChecked(false);
                            rd_bueno.setChecked(false);

                            break;

                        default:
                            break;

                    }


                }
            };

            rd_nose.setOnClickListener(listener);
            rd_bueno.setOnClickListener(listener);
            rd_pesimo.setOnClickListener(listener);

            mBuilder.setPositiveButton("Aportar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if (rd_bueno.isChecked()){

                        final DatabaseReference respuesta = FirebaseDatabase.getInstance().getReference("respuestas");

                        respuesta.child(aPeticion.getRespuestas()).child("respuesta");

                        ValueEventListener listener2;
                        respuesta.addListenerForSingleValueEvent(listener2 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                respuesta.child(aPeticion.getRespuestas())
                                        .child("respuesta")
                                        .child(String.valueOf(dataSnapshot.getChildrenCount()))
                                        .setValue(Respuesta.YES);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        respuesta.removeEventListener(listener2);

                    } else if (rd_pesimo.isChecked()) {

                        final DatabaseReference respuesta = FirebaseDatabase.getInstance().getReference("respuestas");

                        respuesta.child(aPeticion.getRespuestas()).child("respuesta");

                        ValueEventListener listener2;
                        respuesta.addListenerForSingleValueEvent(listener2 = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                respuesta.child(aPeticion.getRespuestas())
                                        .child("respuesta")
                                        .child(String.valueOf(dataSnapshot.getChildrenCount()))
                                        .setValue(Respuesta.NO);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        respuesta.removeEventListener(listener2);

                    }

                }
            });

            mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();

        } else if (aPeticion.getTipo() == RequestType.CLIMA){

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this,
                    R.style.dialog_formal);
            View mView = getLayoutInflater().inflate(R.layout.dialog_face, null);
            mBuilder.setTitle("Inicia Sesión");


            mBuilder.setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    loginButton.performClick();

                }
            });

            mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });

            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();

        }

    }

    public void dialog_No_more() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this);
        mBuilder.setTitle("No permitido");

        mBuilder.setMessage("No es permitido hacer dos preguntas del mismo tipo en un radio menor a 600 metros");

        mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    public void CreateDialogMaxAsk() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ggeasyy.this);
        mBuilder.setTitle("No permitido");

        mBuilder.setMessage("No es permitido hacer mas de 5 preguntas por instancia");

        mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        mBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }
}

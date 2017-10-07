/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-02-17 02:32 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.unbegrenzt.fharmaapp.Adapter.ItemPagerAdapter;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.behavior.BottomSheetBehaviorGoogleMapsLike;
import com.example.unbegrenzt.fharmaapp.behavior.MergedAppBarLayoutBehavior;
import com.example.unbegrenzt.fharmaapp.web_service.clases.PlaceWS;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;
import com.ndroid.nadim.sahel.CoolToast;
import com.nipunbirla.boxloader.BoxLoaderView;
import com.squareup.picasso.Picasso;
import me.grantland.widget.AutofitTextView;

public class ggeasyy extends AppCompatActivity implements
        Map.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener{

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
    private PlaceWS info;

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
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
                if (AccessToken.getCurrentAccessToken() == null) {

                    username.setText("Usuario anónimo");

                }

            }
        });

        //tienda = (FloatingActionButton)findViewById(R.id.tienda);
    }

    public void setInfo(PlaceWS info) {

        this.info = info;
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

                //((Navigation)getActivity()).refresh(1);
                //aqui se parsea al perfil normal
                //esto por si habia alguien ya en firebase
                FirebaseAuth.getInstance().signOut();
                new CoolToast(ggeasyy.this)
                        .make("Sesión iniciada",CoolToast.INFO,CoolToast.LONG,true);

            }

        }

        if (requestCode == 12 && resultCode != OK) {

            new CoolToast(ggeasyy.this)
                    .make("Permiso porfavor",CoolToast.DANGER,CoolToast.LONG,true);

        }


        Log.e("ggizi",String.valueOf(requestCode) + " + " + String.valueOf(resultCode));


    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    // User is signed in
                    Log.e("ggizi","entre");
                    FirebaseUser user =  firebaseAuth.getCurrentUser();

                    username.setText(user.getDisplayName());

                    Log.e("ggizi", String.valueOf(user.getProviderData()));

                    Picasso.with(getApplicationContext()).load(user.getPhotoUrl()).placeholder(R.drawable.ic_download)
                            .error(R.drawable.ic_person).resize(50,50).into(userfoto);
                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void initplacefragmet() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Map fragment = (Map) getSupportFragmentManager().findFragmentByTag("map");
                if (fragment != null) {
                    Log.e("ggizi","aqui");
                    fragment.get_search(place.getId());
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("gg", "An error occurred: " + status);
            }
        });

        //TODO:cambiar los colores grises de la toolbar

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
                            fragment.farm_cercana(500);
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

        } else if(boxLoader.getVisibility() == View.VISIBLE){

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void behaviordimiss(){

        if (behavior.getState() != BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN){

            behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN);

        }
    }

    public void Drawpharma(final PlaceWS body) {

        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);

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

                            fragment.ir_a(new LatLng(fragment.mCurrentLocation.getLatitude(),
                                    fragment.mCurrentLocation.getLatitude()),
                                    new LatLng(body.getResult().getGeometry().getLocation().getLat(),
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
}

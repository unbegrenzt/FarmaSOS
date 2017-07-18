/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-10-17 11:23 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-10-17 11:23 PM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Adapter.AdapterCnClick;
import com.example.unbegrenzt.fharmaapp.Adapter.AdapterFarma;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.touchlistener.ClicklistenerFarma;
import com.example.unbegrenzt.fharmaapp.touchlistener.ItemClickListener;
import com.example.unbegrenzt.fharmaapp.touchlistener.ItemClickSupport;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.R.attr.id;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Perfil extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE = 100;
    private static final int TIME_DIALOG_ID = 1111;
    private static final int PLACE_PICKER_REQUEST = 1;


    //campos del storage
    // Create a storage reference from our app
    private StorageReference mStorageRef;

    private FirebaseStorage storage;
    // Create a storage reference from our app

    private String mParam1;
    private String mParam2;
    public TextView textName, txtDirecc, txt_numero;
    public LatLng mipos;
    public ImageView logo;
    private Uri imageUri;
    private Button bentrada, bsalida,location;
    private int hora, minutos;

    private OnFragmentInteractionListener mListener;
    private TextView text_horaentrada;
    private TextView text_horasalida;
    private boolean existphoto = false;

    //campos enviados a firebase
    private String name = "";
    private String direcc = "";
    private String numero = "";
    private boolean is24hrs = false;
    private String h_entrada = "";
    private String h_salida = "";
    private Uri photo;

    //variables del Recycler
    List<Farmacia> farmacias;

    //variables para la pos actual
    boolean hay_location = false;
    /**
     * variable que almacena el numero del caso que contiene los resultados a la peticion de enceder
     * gps
     */
    private final static int REQUEST_LOCATION = 199;
    /**
     * Objeto que contiene el mapa de google
     */
    private GoogleMap mMap;
    /**
     * Objeto que contiene la barra de busqueda
     */
    private PlaceAutocompleteFragment autocompleteFragment;
    /**
     * Marker que muestra las actualizaciones de ubicacion del usuario
     */
    private Marker MiUbicacion;
    /**
     * Numero enlazado ala caja de dialogo para configurar el gps.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * El intervalo deseado para actualizaciones de ubicación. Inexacta. Las actualizaciones pueden
     * ser más o menos muy frecuentes.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;
    /**
     * el intervalo de tiempo más rapido para las actualizaciones de ubicación
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    /**
     * variables "clave" para almacenar el estado de la actividad en el bundle
     */
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    /**
     * provee el punto de acceso a la Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Almacena parámetros de peticiones al FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Almacena los tipos de servicios de localización interesados en el cliente que se está
     * utilizando. Se utiliza para determinar la configuración, para comprobar el si el
     * dispositivo tiene una configuración óptima ubicación.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Posee los datos de la ubicacion actual
     */
    protected Location mCurrentLocation;

    /**
     * Realiza un seguimiento del estado de las actualizaciones de solicitud de ubicación
     */
    protected Boolean mRequestingLocationUpdates;
    /**
     * El tiempo en que se actualiza la ubicación y se representada como una cadena.
     */
    protected String mLastUpdateTime;

    //our database reference object
    DatabaseReference databaseFarma;

    /**
     * fin de declaracion de las variables e inicio de los metodos de la activity
     */

    private Switch hrs24;
    private double latitude;
    private double longitud;
    private boolean enabled_gps = false;
    private RecyclerView CardItems;
    private String uid;
    private boolean es_actualizar;
    private int pos;

    public Perfil() {
        // Required empty public constructor
    }

    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.perfil, container, false);

        //se instancian las View de la cuenta_administrador
        Button send = (Button) rootView.findViewById(R.id.valid);
        textName = (TextView) rootView.findViewById(R.id.txt_nombre);
        txtDirecc = (TextView) rootView.findViewById(R.id.txt_Dirección);
        txt_numero = (TextView) rootView.findViewById(R.id.txt_telefono);

        // incializamos variables de localizacion
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        farmacias = new ArrayList<>();

        // actualizamos a partir de un instancia guardada
        updateValuesFromBundle(savedInstanceState);

        // construimos el cliente desde la api en google
        // creamos una peticion de ubicacion
        // se instancia el constructor de las peticiones de ubicacion
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        //capturar location
        location = (Button)rootView.findViewById(R.id.qwe);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enabled_gps){
                    updateLocationUI();
                    Toast.makeText(getApplicationContext(),"Ubicación tomada con exito",
                            Toast.LENGTH_LONG).show();
                    hay_location = true;
                }else{
                    check_gps();
                }
            }
        });

        //getting the reference of farmacias node
        databaseFarma = FirebaseDatabase.getInstance().getReference(getString(R.string.get_ref_farma));

        //captura de fecha y hora entrada
        bentrada = (Button) rootView.findViewById(R.id.Entrada);
        bsalida = (Button) rootView.findViewById(R.id.Salida);
        text_horaentrada = (TextView) rootView.findViewById(R.id.hora_entrada);
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hora = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minutos = c.get(Calendar.MINUTE);

        bentrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(TIME_DIALOG_ID).show();
            }
        });

        //captura de fecha y hora salida
        text_horasalida = (TextView) rootView.findViewById(R.id.hora_salida);
        // Current Hour
        hora = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minutos = c.get(Calendar.MINUTE);

        bsalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog2(TIME_DIALOG_ID).show();
            }
        });

        //verifica si el local es 24 horas
        hrs24 = (Switch) rootView.findViewById(R.id.hrs24);

        // set current time into output textview
        //updateTime(hora, minutos);
        //updateTime2(hora,minutos);

        //logo de la foto de perfil
        logo = (ImageView) rootView.findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_farmacia();
            }
        });

        //updateRecycler();

        //configurar la User interface conforme al usuario actual
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            TextView name = (TextView) rootView.findViewById(R.id.Id);
            name.setText(user.getDisplayName());
            //String name = user.getDisplayName();
            //String email = user.getEmail();

            //se carga ña imagen dentro de un cricular image view
            ImageView profile = (ImageView) rootView.findViewById(R.id.profile);
            Picasso.with(getApplicationContext()).load(user.getPhotoUrl())
                    .error(R.drawable.ic_person).placeholder(R.drawable.ic_person).into(profile);

            //se carga la cover foto
            ImageView banner = (ImageView) rootView.findViewById(R.id.banner);


            //foto de perdil
            Picasso.with(getApplicationContext()).load("https://assets.vg247.it/current//2015/03/" +
                    "video-games-black-broken-sony-console-crash-playstation-destroyed-crush-dualsh" +
                    "ock-gamepad-controller_www.wall321.com_71.jpg").placeholder(R.drawable.load)
                    .error(R.drawable.side_nav_bar).into(banner);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.getUid();
        }
        //storageRef = storage.getReferenceFromUrl("gs://fharmaapp.appspot.com/");
        if(isNetDisponible() && isOnlineNet()){
            //updateRecycler();
        }else{
            Toast.makeText(getApplicationContext(),"Conectese a internet",
                    Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String get_userid(){
        //configurar la User interface conforme al usuario actual
        String uid = "?";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }else{
            Toast.makeText(getApplicationContext(),"Desliza hacia arriba\npara actualizar",Toast.LENGTH_LONG);
            recargar1();
        }
        return uid;
    }

    private void recargar1() {
        //TODO: recarga el usuario actual
    }

    private void recargar() {

    }

    private void updateRecycler() {
        databaseFarma.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                farmacias.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Farmacia farmacia = postSnapshot.getValue(Farmacia.class);
                    farmacia.setID(postSnapshot.getKey());
                    Toast.makeText(getApplicationContext(),farmacia.getProfile(),Toast.LENGTH_SHORT);
                    farmacias.add(farmacia);
                }
                updateRecycler(farmacias);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //TODO: ver cuestiomes de performance en el adapter
    private void updateRecycler(final List<Farmacia> farmacia) {
        CardItems.setHasFixedSize(true);
        CardItems.setItemViewCacheSize(1);
        CardItems.setDrawingCacheEnabled(true);
        ItemClickSupport.addTo(CardItems).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                updatesate(position);
                Toast.makeText(getApplicationContext(),String.valueOf(v.getId()),Toast.LENGTH_LONG)
                        .show();
            }
        });
        CardItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        CardItems.setAdapter(new AdapterFarma(getApplicationContext(), farmacias));
        CardItems.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        CardItems.setLayoutManager(layoutManager);
    }

    private void updatesate(int position) {
        Farmacia farmacia = farmacias.get(position);
        if(farmacia.isDisponible()) {
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("farmacias");
            databaseReference.child(farmacia.getID()).child("disponible").setValue(false);
        }else{
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("farmacias");
            databaseReference.child(farmacia.getID()).child("disponible").setValue(true);
        }
    }

    //creating mi own Dialog box
    public void Dialog(final int position, final String idu){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Información")
                .setMessage("¿Qué deseas hacer?")
                .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        updateFarma(farmacias.get(position));

                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        deleteArtist(idu);

                    }
                })
                .setIcon(R.drawable.ic_web)
                .show();
    }

    private void updateFarma(Farmacia farmacia) {

    }

    //es para preguntar si el usuario está seguro
    private boolean[] Dialogsecure(){
        final boolean[] response = {false};
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Advertencia!")
                .setMessage("Estas seguro de borrar?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        response[0] = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.ic_web)
                .show();
        return response;
    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("farmacias").equalTo(id);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("asdf", "onCancelled", databaseError.toException());
            }
        });

        return true;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Actualiza el valor de mRequestingLocationUpdates de el Bundle, y asegurate de que
            // el inicio de las actualizaciones y termino de actualizaciones en algun boton
            // son habilitadas o deshabilitadas.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Actualiza el valor de mCurrentLocation de el Bundle y actualiza la UI para mostrar la
            // latitude y longitude correcta.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // ya que KEY_LOCATION fue encontrada en el Bundle, nosotros debemos asegurarnos
                // que mCurrentLocation no es null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Actualiza el valor de mLastUpdateTime de el Bundle y actualiza el UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                mLocationSettingsRequest
        ).setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest,Perfil.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                }
                updateUI();
            }
        });

    }

    private void check_gps(){
        LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                mLocationSettingsRequest
        ).setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        enabled_gps = true;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                }
                updateUI();
            }
        });
    }

    private void updateUI() {
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            /*
             * @param mCurrentLocation es la variable que posee todos los datos del
             * usuario y aqui se actualizan
             *
             * @<code>
             *  mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
             *mCurrentLocation.getLatitude()));
             * mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
             *      mCurrentLocation.getLongitude()));
             * mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
             *      mLastUpdateTime));
             * </code>
             * eso seria un buen ejemplo
             */
            latitude = mCurrentLocation.getLatitude();
            longitud = mCurrentLocation.getLongitude();
        }
    }

    //secrea el dialogo para la salida
    private Dialog createDialog2(int timeDialogId) {
        switch (timeDialogId) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(getActivity(), timePickerListener2, hora, minutos, false);

        }
        return null;
    }

    //Listener de la salida
    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hora   = hourOfDay;
            minutos = minutes;

            updateTime2(hora,minutos);

        }

    };

    private void updateTime2(int hora, int mins) {
        String timeSet = "";
        if (hora > 12) {
            hora -= 12;
            timeSet = "PM";
        } else if (hora == 0) {
            hora += 12;
            timeSet = "AM";
        } else if (hora == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hora).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        text_horasalida.setText(aTime);
    }

    //listener de la hora
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hora   = hourOfDay;
            minutos = minutes;

            updateTime(hora,minutos);

        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        text_horaentrada.setText(aTime);
    }

    public Dialog createDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(getActivity(), timePickerListener, hora, minutos, false);

        }
        return null;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE && data != null){
            imageUri = data.getData();
            Picasso.with(getApplicationContext()).load(imageUri).placeholder(R.drawable.ic_add)
                    .error(R.drawable.ic_add).into(logo);
            existphoto = true;
        }else{
            Toast.makeText(getApplicationContext(),"Debe seleccionar una imagen",Toast.LENGTH_LONG)
                    .show();
        }

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // El usuario nos concedio el permiso
                        Toast.makeText(getApplicationContext(),
                                "Operación exitosa", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // El usuario no otorgo el permiso
                        Toast.makeText(getApplicationContext(),
                                "Operación no otorgada", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }

    }

    private void OpenGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //TODO: aqui se sube una farmacia
    public void send_farmacia(){
        if (!validate()) {
            return;
        }
        String uid;
        if(is24hrs){

        }else {
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseFarma.push().getKey();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                uid = user.getUid();
            }else{
                Toast.makeText(getApplicationContext(),"Inicie sesion Con facebook"
                        ,Toast.LENGTH_LONG).show();
                return;
            }

            //creating an Artist Object
            List<String> eString = new ArrayList<>();
            eString.add("hola");
            eString.add("adios :v ");
            Farmacia nueva_farmacia = new Farmacia(id, uid, name,direcc,numero,h_entrada,h_salida
            ,String.valueOf(latitude),String.valueOf(longitud),"https://assets.vg247.it/current//2015/03/" +
                    "video-games-black-broken-sony-console-crash-playstation-destroyed-crush-dualsh" +
                    "ock-gamepad-controller_www.wall321.com_71.jpg",1,"https://assets.vg247.it/current//2015/03/" +
                    "video-games-black-broken-sony-console-crash-playstation-destroyed-crush-dualsh" +
                    "ock-gamepad-controller_www.wall321.com_71.jpg",false,"lunes,jueves",
                    "www.google.com", eString,true);

            //Saving the Artist
            databaseFarma.child(id).setValue(nueva_farmacia);

            /*
            id_farma
            id_tendero
            name = textName.getText().toString();
            direcc = txtDirecc.getText().toString();
            numero = txt_numero.getText().toString();
            is24hrs = hrs24.isChecked();
            h_entrada = text_horaentrada.getText().toString();
            h_salida = text_horasalida.getText().toString();
            latitude;
            longitud;*/

            //displaying a success toast
            Toast.makeText(getApplicationContext(), "Farmacia subida", Toast.LENGTH_LONG).show();
        }

    }

    public boolean validate() {
        boolean valid = true;

        name = textName.getText().toString();
        direcc = txtDirecc.getText().toString();
        numero = txt_numero.getText().toString();
        is24hrs = hrs24.isChecked();
        h_entrada = text_horaentrada.getText().toString();
        h_salida = text_horasalida.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            textName.setError(geterrorColor(getString(R.string.least), R.color.icons1));
            valid = false;
        } else {
            textName.setError(null);
        }

        //!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (direcc.isEmpty() || direcc.length() < 3) {
            txtDirecc.setError(geterrorColor("Este campo es obligatorio",R.color.icons1));
            valid = false;
        } else {
            txtDirecc.setError(null);
        }

        if (numero.isEmpty() || numero.length() < 3) {
            txt_numero.setError(geterrorColor("Este campo es obligatorio",R.color.icons1));
            valid = false;
        } else {
            txt_numero.setError(null);
        }

        //si no es 24 se pider el turno
        if((h_entrada.compareTo("00:00") == 0)
                || (h_salida.compareTo("00:00") == 0)){

            Toast.makeText(getApplicationContext(),"no puede quedar vacio el turno"
                    ,Toast.LENGTH_LONG).show();
            valid = false;
        }
        if(!(h_entrada.contains("AM"))){
            Toast.makeText(getApplicationContext(),"primero AM luego PM en el turno"
                    ,Toast.LENGTH_LONG).show();
            valid = false;
        }

        if(!existphoto){
            Toast.makeText(getApplicationContext(),"Ingrese un logo"
                    ,Toast.LENGTH_LONG).show();
            valid = false;
        }else{

            /*// Get the data from an ImageView as bytes
            logo.setDrawingCacheEnabled(true);
            logo.buildDrawingCache();
            Bitmap bitmap = logo.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

            // File or Blob
            file = Uri.fromFile(new File("path/to/mountains.jpg"));

            // Create the file metadata
            metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();

            // Upload file and metadata to the path 'images/mountains.jpg'
            uploadTask = storageRef.child("images/"+file.getLastPathSegment()).putFile(file, metadata);

            // Listen for state changes, errors, and completion of the upload.
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Handle successful uploads on complete
                    Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                }
            });*/
        }

        if(!hay_location){
            Toast.makeText(getApplicationContext(),"Envíenos la ubicación del local"
                    ,Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    public SpannableStringBuilder geterrorColor(String estring, int ecolor){
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(ecolor));
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);

        return ssbuilder;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
        }

        /*
         * cuando se conecte comienzo a recibir actualizaciones
         */
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Sin conexión", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

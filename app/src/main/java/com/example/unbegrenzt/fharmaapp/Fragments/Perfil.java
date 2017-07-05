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
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.R;
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
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Perfil extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE = 100;
    private static final int TIME_DIALOG_ID = 1111;


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
    DatabaseReference databaseArtists;

    /**
     * fin de declaracion de las variables e inicio de los metodos de la activity
     */

    private Switch hrs24;
    private double latitude;
    private double longitud;
    private boolean enabled_gps = false;

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

            Picasso.with(getApplicationContext()).load("https://assets.vg247.it/current//2015/03/" +
                    "video-games-black-broken-sony-console-crash-playstation-destroyed-crush-dualsh" +
                    "ock-gamepad-controller_www.wall321.com_71.jpg").placeholder(R.drawable.load)
                    .error(R.drawable.side_nav_bar).into(banner);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

        return rootView;
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
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
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

    public void send_farmacia(){
        if (!validate()) {
            return;
        }
        if(is24hrs){

        }else{
            /*DatabaseReference databaseArtists;
            databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
            Farmacia = new*/
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

    /*private List<Farmacia> getApps(){

        List<Farmacia> apps = new ArrayList<>();
        apps.add(new Farmacia("Gooogle",R.drawable.cloud_off,4.5f));
        apps.add(new Farmacia("Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_star,1.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_exit,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.ic_info,3.5f));
        apps.add(new Farmacia("Gooogle",R.drawable.cloud_off,4.5f));
        apps.add(new Farmacia(" Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_star,1.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_exit,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.ic_info,3.5f));

        return apps;

    }*/

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

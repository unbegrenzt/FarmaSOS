/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-04-17 11:47 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-04-17 11:46 AM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.actividades.Navigation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.api.services.gmail.model.Thread;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Map extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,GoogleMap.OnMarkerClickListener{

    private OnFragmentInteractionListener mListener;
    private SupportMapFragment fragment;
    private static final int PLACE_PICKER_REQUEST = 1;

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
     * Lista de Markers de los locales
     */
    private List<Marker> locales;
    /**
     * Numero enlazado ala caja de dialogo para configurar el gps.
     */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * El intervalo deseado para actualizaciones de ubicación. Inexacta. Las actualizaciones pueden
     * ser más o menos muy frecuentes.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    /**
     * el intervalo de tiempo más rapido para las actualizaciones de ubicación
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
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
    private boolean primeravez = true;
    private LatLng pos;
    private Uri photo;
    private DatabaseReference databaseFarma;
    private ValueEventListener listener;
    private Marker places;
    private int x = 0;
    private LatLng posloc;
    private boolean primeravez_token = true;
    private ValueEventListener latlistener;
    private boolean query = false;
    private Marker markerplace;
    private Thread conexion;

    /**
     * fin de declaracion de las variables e inicio de los metodos de la activity
     */

    public Map() {
        // Required empty public constructor
    }

    public static Map newInstance() {
        return new Map();
    }

    //se carga el mapa al fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //primero verificamos el acceso a internet
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
        fragment.getMapAsync(this);

        databaseFarma = FirebaseDatabase.getInstance().getReference
                (getString(R.string.get_ref_farma));

        // incializamos variables de localizacion
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        locales = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            photo = user.getPhotoUrl();
        }

        //TODO: falta mover camara del floating action button
        // actualizamos a partir de un instancia guardada
        updateValuesFromBundle(savedInstanceState);

        // construimos el cliente desde la api en google
        // creamos una peticion de ubicacion
        // se instancia el constructor de las pReticiones de ubicacion
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        //updatelistener();
        //AddPlace();
    }

    private void AddPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            Log.i("asdf",e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.i("asdf",e.getMessage());
        }
    }



    //pedimos si hay algun cambio en la base de datos
    private void updatelistener() {
        databaseFarma.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: lo comentado sirve para la logica de los markers con imagenes
                int contador = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmaciasx = postSnapshot.getValue(com.example.unbegrenzt.fharmaapp.Objects.Farmacia.class);
                    if(primeravez_token){
                        Marker addtostack;
                        if (farmaciasx.isDisponible()) {
                            addtostack = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(farmaciasx.getLat()),
                                            Double.parseDouble(farmaciasx.getLong())))
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView
                                            (farmaciasx.getProfile()))));
                        } else {
                            addtostack = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(farmaciasx.getLat()),
                                            Double.parseDouble(farmaciasx.getLong())))
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView
                                        (farmaciasx.getProfile()))));
                        }
                        locales.add(addtostack);
                    }else{

                        /*Marker izi = locales.get(contador);

                        if (farmaciasx.isDisponible() &&
                                (izi.getSnippet().compareTo("Cerrada") == 0)){

                            izi.setSnippet("Abierta");

                        }else if (!farmaciasx.isDisponible() &&
                                (izi.getSnippet().compareTo("Abierta") == 0)){

                            izi.setSnippet("Cerrada");
                        }*/
                    }
                    contador++;
                }
                primeravez_token = false;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * cuando reinicia o inicia el Activity nos conectamos al api para ahorrar recursos y energía
     */
    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();

    }

    /**
     * cuando el app esta en curso se deben de iniciar las actualizaciones de ubicacion
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        updateUI();
    }

    /**
     * si estamos pausados solo detenemos las actualizaciones para ahorrar recursos
     */
    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    /**
     * si la app se detiene nos desconectamos del api
     */
    @Override
    public void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();

        if(listener != null)databaseFarma.removeEventListener(listener);
    }
    /*
     * terminan los metodos de la activity e inician los recurrentes a las api y demas
     */
    private double redondear(double valor){
        String val = valor+"";
        BigDecimal big = new BigDecimal(val);
        big = big.setScale(7, RoundingMode.HALF_UP);
        return big.doubleValue();
    }

    /**
     * Creamos el @param GoogleApiClient. usamos el metodo {@code #addApi} para pedir
     * acceso a las API's requeridas.
     */
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

    /**
     * Use un {@link com.google.android.gms.location.LocationSettingsRequest.Builder} para crear
     * un {@link com.google.android.gms.location.LocationSettingsRequest} es usado para revisar
     * si un dispositivo posee las configuraciones de ubicacion requeridas.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * configuramos el location request. Android posee dos configuraciones de peticion de ubicacion:
     * {@code ACCESS_COARSE_LOCATION} y {@code ACCESS_FINE_LOCATION}. Estas configuraciones de
     * gestion en la presicion de la actual ubicacion. Este ejemplo usa ACCESS_FINE_LOCATION, como
     * se define en el AndroidManifest.xml.
     * <p/>
     * cuando el @param ACCESS_FINE_LOCATION es especificado en la configuracion, combinado con un
     * intervalo de actualizacion rapido (5 seconds), la fusion provista a la Location API retorna
     * actualizaciones de ubicacion que son precisas dentro de unos pocos pies.
     * <p/>
     * Esas configuraciones son apropiadas para applicaciones cartograficas que muestran
     * actualizaciones de ubicacion en tiempo real.
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * actualizamos los campos basados en los datos guardados en el bundle.
     *
     * @param savedInstanceState guarda el estado de la activity en el Bundle.
     */
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

    /**
     * pide actualizaciones de ubicacion de la FusedLocationApi.
     */
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
                                mGoogleApiClient, mLocationRequest, Map.this);
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getApplicationContext(),data);
                String id = databaseFarma.push().getKey();
                LatLng coord = place.getLatLng();
                int x = 0;
                boolean valid = true;
                String ID, address, latitud, longitud, name, telefono, website;
                List<Integer> placetypes;

                ID = place.getId();
                address = String.valueOf(place.getAddress());
                latitud = String.valueOf(redondear(coord.latitude));
                longitud = String.valueOf(redondear(coord.longitude));
                name = String.valueOf(place.getName());
                telefono = String.valueOf(place.getPhoneNumber());
                placetypes = place.getPlaceTypes();
                website = String.valueOf(place.getWebsiteUri());

                if(place.getId() == null) {
                    ID = "";
                    valid = false;
                }
                if(place.getAddress() == null){
                    address = "";
                }
                if(place.getName() == null){
                    name = "";
                    valid = false;
                }
                if(place.getPhoneNumber() == null){
                    telefono = "";
                }
                //TODO:acuerdate que si el placetype.get(0) es -1200 es un valor null
                if(place.getPlaceTypes() == null){
                    placetypes.add(-1200);
                }
                if(place.getWebsiteUri() == null){
                    website = "";
                }

                if(valid){
                    Farmacia local = new Farmacia(ID, address, latitud, longitud, name, telefono,
                            placetypes,false,website);
                    databaseFarma.child(id).setValue(local);
                }else{
                    //TODO:enviar al request manual
                    valid = false;
                }

            }
        }

    }

    /**
     * actualiza todos campos de la UI.
     */
    private void updateUI() {
        updateLocationUI();
    }

    /**
     * configura el valor de los campos UI para la ubicacion  latitude, longitude y el ultimo
     * momento de actualizacion.
     */
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
            MarkerMiUbicacion(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        }
    }

    /**
     * es una marker que se mueve con tu ubicación
     */
    private void MarkerMiUbicacion(double latitude, double longitude) {
        pos = new LatLng(latitude,longitude);
        if(primeravez){
            move_to_my_pos();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                photo = user.getPhotoUrl();
            }
            primeravez = false;
        }
        if(MiUbicacion != null)MiUbicacion.remove();
        MiUbicacion = mMap.addMarker(new MarkerOptions()
                .position(pos).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(photo))));

    }

    //TODO:cambiar logica para todos los markers
    private void addMarker(Farmacia farmacia){
        posloc = new LatLng(Double.parseDouble(farmacia.getLat()),Double.parseDouble(
                farmacia.getLong()));

        if(x == 1){
            places.remove();
            x = 0;
        }
        if(farmacia.isDisponible()){
            places = mMap.addMarker(new MarkerOptions()
                    .position(posloc).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView
                            (farmacia.getProfile())))
                    .title(farmacia.getmName()).snippet("Abierta"));
        }else {
            places = mMap.addMarker(new MarkerOptions()
                    .position(posloc).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView
                            (farmacia.getProfile())))
                    .title(farmacia.getmName()).snippet("Cerrada"));
        }
        x = 1;
    }

    //creamos un custom marker
    private Bitmap getMarkerBitmapFromView(String url) {

        View customMarkerView = ((LayoutInflater)getActivity().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_custom_marker, null);

        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker);
        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.load)
                .error(R.drawable.ic_person).into(markerImageView);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(),
                customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(),
                customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;

    }

    private Bitmap getMarkerBitmapFromView(Uri url) {

        View customMarkerView = ((LayoutInflater)getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);

        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker);
        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person).into(markerImageView);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(),
                customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();

        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(),
                customMarkerView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    //mueve la camara a tu ubicación
    public void move_to_my_pos() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    /**
     * remueve las actualizaciones de ubicacion de la FusedLocationApi.
     */
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

    /**
     * @param savedInstanceState sirve para recuperar instancias guardadas
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        Field childFragmentManager = null;
        try {
            childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (childFragmentManager != null) {
            childFragmentManager.setAccessible(true);
        }
        try {
            if (childFragmentManager != null) {
                childFragmentManager.set(this, null);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //agregar barra de utilidades al evento click de los marcadores
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //se quita la brujula
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*query = false;
                if(markerplace != null)markerplace.remove();
                markerplace = mMap.addMarker(new MarkerOptions()
                        .position(latLng));
                doQuery(redondear(latLng.latitude),redondear(latLng.longitude));*/
            }
        });
        Markerspdate();

    }

    private void Markerspdate(){

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        databaseFarma.addValueEventListener(listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmaciasx = postSnapshot.getValue(com.example.unbegrenzt.fharmaapp.Objects.Farmacia.class);
                                    double lat_2 = Double.parseDouble(farmaciasx.getLat());
                                    double longitud_2 = Double.parseDouble(farmaciasx.getLong());

                                    LatLng gg = new LatLng(lat_2,longitud_2);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(gg).title(farmaciasx.getmName()).snippet(farmaciasx.getID()));

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                }
                , 100);

    }



    private void doQuery(final double latitud, final double longitud) {
        //Query querylat = databaseFarma.orderByChild("lat").equalTo(latitud);
        //Query querylong = databaseFarma.orderByChild("long").equalTo(longitud);

        databaseFarma.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("entro","entro");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmaciasx = postSnapshot.getValue(com.example.unbegrenzt.fharmaapp.Objects.Farmacia.class);
                    double lat_2 = Double.parseDouble(farmaciasx.getLat());
                    double longitud_2 = Double.parseDouble(farmaciasx.getLong());
                    double x = lat_2 - latitud;
                    x = x * x;
                    double y = longitud_2 - longitud;
                    y = y * y;

                    double distancia = Math.sqrt((x + y));
                    Log.i("entro",String.valueOf(distancia));
                    if(distancia <= 0.0000490){
                        Toast.makeText(getApplicationContext(),"aquiii",Toast.LENGTH_LONG)
                                .show();
                        ((Navigation)getActivity()).showtienda(farmaciasx);
                        break;
                    }else{
                        Toast.makeText(getApplicationContext(),"aquiiiz112",Toast.LENGTH_LONG)
                                .show();
                        ((Navigation)getActivity()).disposetienda();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*querylat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //TODO:query sabe si existe esa pos o no
                    query = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        querylong.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //TODO:query sabe si existe esa pos o no
                    query = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Cuando cambie el gps se actualiza
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
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

    /**
     * si la ubicacion cambia se actualizan los parametros
     */
    @Override
    public void onLocationChanged(Location location) {
        //actualizamos los datos si la ubicacion cambia
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateLocationUI();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(getApplicationContext(),
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

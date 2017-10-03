/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.Manifest;
import android.app.Activity;
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
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Objects.*;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import static com.example.unbegrenzt.fharmaapp.R.layout.farmacias;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

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

    /**
     * fin de declaracion de las variables e inicio de los metodos de la activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // @param SupportMapFragment nos notifica si el mapa está listo para usarse
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseFarma = FirebaseDatabase.getInstance().getReference(getString(R.string.get_ref_farma));

        // Instanciamos la barra de busqueda
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // incializamos variables de localizacion
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            photo = user.getPhotoUrl();
        }

        FloatingActionButton mi_ub = (FloatingActionButton)findViewById(R.id.mi_ub);

        mi_ub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_to_my_pos();
            }
        });

         // actualizamos a partir de un instancia guardada
        updateValuesFromBundle(savedInstanceState);

         // construimos el cliente desde la api en google
         // creamos una peticion de ubicacion
         // se instancia el constructor de las peticiones de ubicacion
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
        updatelistener();
    }

    private void updatelistener() {
        databaseFarma.addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmacia = postSnapshot.getValue(com.example.unbegrenzt.fharmaapp.Objects.Farmacia.class);
                    addMarker(farmacia);
                }

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
    protected void onStart() {
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
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    /**
     * si la app se detiene nos desconectamos del api
     */
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
        databaseFarma.removeEventListener(listener);
    }

    /*
     * terminan los metodos de la activity e inician los recurrentes a las api y demas
     */

    /**
     * Creamos el @param GoogleApiClient. usamos el metodo {@code #addApi} para pedir
     * acceso a las API's requeridas.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
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
                                mGoogleApiClient, mLocationRequest, MapsActivity.this);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException ignored) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        mRequestingLocationUpdates = false;
                }
                updateUI();
            }
        });

    }

    /**
     * actualiza todos campos de la UI.
     */
    private void updateUI() {
        setButtonsEnabledState();
        updateLocationUI();
    }

    /**
     * sirve a la logica de un switch por que no se puede habilitar y deshabilitar
     * las actualizaciones al mismo tiempo
     */
    private void setButtonsEnabledState() {
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
        .position(pos).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(photo)))
        .title("mi ubicación"));

    }

    private void addMarker(Farmacia farmacia){
        pos = new LatLng(Double.parseDouble(farmacia.getLat()),Double.parseDouble(
                farmacia.getLong()));

        if(x == 1){
            places.remove();
            x = 0;
        }
        if(farmacia.isDisponible()){
            places = mMap.addMarker(new MarkerOptions()
                    .position(pos).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView
                            (farmacia.getProfile())))
                    .title(farmacia.getmName()).snippet("Abierta"));
        }else {
            places = mMap.addMarker(new MarkerOptions()
                    .position(pos).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView
                            (farmacia.getProfile())))
                    .title(farmacia.getmName()).snippet("Cerrada"));
        }
        x = 1;
    }

    private Bitmap getMarkerBitmapFromView(String url) {

        View customMarkerView = ((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker);
        Picasso.with(this).load(url).placeholder(R.drawable.load).error(R.drawable.ic_person)
                .into(markerImageView);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromView(Uri url) {

        View customMarkerView = ((LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.marker);
        Picasso.with(this).load(url).placeholder(R.drawable.ic_person).error(R.drawable.ic_person)
                .into(markerImageView);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    private void move_to_my_pos() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos).zoom(15).build();
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
                setButtonsEnabledState();
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

    /**
     * @param googleMap se activa cuando el mapa está listo
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //agregar barra de utilidades al evento click de los marcadores
        mMap.getUiSettings().setMapToolbarEnabled(true);

        //se quita la brujula
        mMap.getUiSettings().setCompassEnabled(false);

        //aqui se reciben los lugares de la barra de busqueda
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //TODO:configurar las entradas a conveniencia
                Toast toast = Toast.makeText(getApplicationContext(), "permiso denegado ", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(Status status) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.Máx_preg, Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    /**
     * @param view recibe el click de una vista
     */
    public void click(View view) {


    }

    /**
     * si la conexion falla se envia un sms
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed

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

    /**
     * funcion que se llama al conectarse ala api
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Cuando cambie el gps se actualiza
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            setButtonsEnabledState();
            startLocationUpdates();
        }
        /* @comentario esto es si se activo con anterioridad el gps
           if (mRequestingLocationUpdates) {
            startLocationUpdates();
            Toast.makeText(getApplicationContext(),"seguimiento",Toast.LENGTH_LONG).show();
        }
         */

    }

    /**
     * aqui se reciben las repuestas al pedirle al usuario que nos
     * de el permiso de localizacion y ejecuto una respuesta
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // El usuario nos concedio el permiso
                        Toast.makeText(MapsActivity.this, "Operación exitosa", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // El usuario no otorgo el permiso
                        Toast.makeText(MapsActivity.this, "Operación no otorgada", Toast.LENGTH_LONG).show();
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

    /**
     * ocurre si se suspende la conexión
     */
    @Override
    public void onConnectionSuspended(int i) {

    }
}

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
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
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

import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.Services.GPSTracker;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Perfil extends Fragment{

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
    int latitude; // latitude
    int longitude; // longitude
    boolean hay_location = false;
    private LocationManager locationManager;
    private LocationListener listener;

    private Switch hrs24;
    private GPSTracker gps;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.perfil, container, false);

        //se instancian las View de la cuenta_administrador
        Button send = (Button) rootView.findViewById(R.id.valid);
        textName = (TextView) rootView.findViewById(R.id.txt_nombre);
        txtDirecc = (TextView) rootView.findViewById(R.id.txt_Dirección);
        txt_numero = (TextView) rootView.findViewById(R.id.txt_telefono);


        //capturar location
        location = (Button)rootView.findViewById(R.id.qwe);

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(),
                        "\n " + location.getLongitude() + " " + location.getLatitude(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });
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
            Toast.makeText(getApplicationContext(),"Envienos su ubicación"
                    ,Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;
    }

    @Override
    public void onStop(){
        super.onStop();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

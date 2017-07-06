/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-28-17 10:30 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-28-17 10:30 AM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

import java.net.URI;
import java.util.List;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */

public class Farmacia {

    private int mDrawable;
    private String mName;
    private float mRating;
    private String profile;
    private String Lat;
    private String Long;
    private boolean isVerified;
    private String dias;
    private List<URI> fotos_local;
    private String telefono;
    private String Dirección;
    private String SitioWeb;
    private String hora_apertura;
    private String hora_cierre;
    private String ID;
    private String Uid;
    private boolean disponible = true;
    private String acepted = "true";

    public Farmacia(){

    }

    public Farmacia(String id, String uid, String name, String direcc, String numero,
                    String h_entrada, String h_salida, String s, String s1, String Uri) {
        ID = id;
        Uid = uid;
        mName = name;
        Dirección = direcc;
        telefono = numero;
        hora_apertura = h_entrada;
        hora_cierre = h_salida;
        Lat = s;
        Long = s1;
        profile = Uri;
    }
            /*id_farma
            id_tendero
            name = textName.getText().toString();
            direcc = txtDirecc.getText().toString();
            numero = txt_numero.getText().toString();
            is24hrs = hrs24.isChecked();
            h_entrada = text_horaentrada.getText().toString();
            h_salida = text_horasalida.getText().toString();
            latitude;
            longitud;
            String path to drawables*/

    public String getmName() {
        return mName;
    }

    public float getmRating() {
        return mRating;
    }

    public String getProfile() {
        return profile;
    }

    public String getLat() {
        return Lat;
    }

    public String getLong() {
        return Long;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public String getDias() {
        return dias;
    }

    public List<URI> getFotos_local() {
        return fotos_local;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDirección() {
        return Dirección;
    }

    public String getSitioWeb() {
        return SitioWeb;
    }

    public String getHora_apertura() {
        return hora_apertura;
    }

    public String getHora_cierre() {
        return hora_cierre;
    }

    public String getID() {
        return ID;
    }

    public String getUid() {
        return Uid;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public String getAcepted() {
        return acepted;
    }
}

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
    private String Latlong;
    private boolean isVerified;
    private String dias;
    private List<URI> fotos_local;
    private int telefono;
    private String Dirección;
    private String SitioWeb;
    private String hora_apertura;
    private String hora_cierre;
    private String ID;

    public Farmacia(){

    }

    public Farmacia(String name, int drawable , float rating){

        mName = name;
        mDrawable = drawable;
        mRating = rating;

    }

    public Farmacia(String mName, float mRating, String profile, String latlong, boolean isVerified,
                    String dias, List<URI> fotos_local, int telefono,
                    String dirección, String SitioWeb, String hora_apertura,
                    String hora_cierre, String ID) {
        this.mName = mName;
        this.mRating = mRating;
        this.profile = profile;
        Latlong = latlong;
        this.isVerified = isVerified;
        this.dias = dias;
        this.fotos_local = fotos_local;
        this.telefono = telefono;
        Dirección = dirección;
        this.SitioWeb = SitioWeb;
        this.hora_apertura = hora_apertura;
        this.hora_cierre = hora_cierre;
        this.ID = ID;
    }

    public Farmacia(int mDrawable, String mName, float mRating,
                    String latlong, boolean isVerified, String dias,
                    List<URI> fotos_local, int telefono, String dirección, String SitioWeb,
                    String hora_apertura, String hora_cierre, String ID) {
        this.mDrawable = mDrawable;
        this.mName = mName;
        this.mRating = mRating;
        Latlong = latlong;
        this.isVerified = isVerified;
        this.dias = dias;
        this.fotos_local = fotos_local;
        this.telefono = telefono;
        Dirección = dirección;
        this.SitioWeb = SitioWeb;
        this.hora_apertura = hora_apertura;
        this.hora_cierre = hora_cierre;
        this.ID = ID;
    }

    public int getmDrawable() {
        return mDrawable;
    }

    public String getmName() {
        return mName;
    }

    public float getmRating() {
        return mRating;
    }

}

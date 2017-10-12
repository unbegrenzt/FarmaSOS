/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-11-17 02:09 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-11-17 02:09 PM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

public class Peticion {

    private int tipo;
    private String Uid;
    private double latitud;
    private double longitud;
    private String respuestas;

    public Peticion(int tipo, String uid, double latitud, double longitud, String respuestas) {
        this.tipo = tipo;
        this.Uid = uid;
        this.latitud = latitud;
        this.longitud = longitud;
        this.respuestas = respuestas;
    }

    public Peticion() {


    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(String respuestas) {
        this.respuestas = respuestas;
    }
}

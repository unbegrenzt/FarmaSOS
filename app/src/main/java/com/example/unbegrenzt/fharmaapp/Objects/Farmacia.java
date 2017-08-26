/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-28-17 10:30 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-28-17 10:30 AM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */

public class Farmacia implements Serializable {

    private String mName;
    private float mRating;
    private String portada;
    private String profile;
    private String pricelevel;
    private List<Integer> placetypes;
    private String Lat;
    private String Long;
    private boolean isVerified;
    private String dias;
    private List<String> fotos_local;
    private String telefono;
    private String direccion;
    private String SitioWeb;
    private String hora_apertura;
    private String hora_cierre;
    private String ID;
    private String Uid;
    private boolean disponible;

    public Farmacia(String ID,  String address,
                    String latitud, String longitud, String name, String telefono,
                    List<Integer> placetypes,boolean disponible,String sitioWeb){
        this.ID = ID;
        this.direccion = address;
        this.Lat = latitud;
        this.Long = longitud;
        this.mName = name;
        this.telefono = telefono;
        this.placetypes = placetypes;
        this.disponible = disponible;
        this.SitioWeb = sitioWeb;

    }

    public Farmacia(){

    }

    public Farmacia(String id, String uid, String name, String direcc, String numero,
                    String h_entrada, String h_salida, String s, String s1, String Uri,
                    float mRating, String portada, boolean isVerified, String dias,
                    String sitioWeb, List<String> fotos_local, boolean disponible) {
        this.ID = id;
        this.Uid = uid;
        this.mName = name;
        this.direccion = direcc;
        this.telefono = numero;
        this.hora_apertura = h_entrada;
        this.hora_cierre = h_salida;
        this.Lat = s;
        this.Long = s1;
        this.profile = Uri;
        this.mRating = mRating;
        this.portada = portada;
        this.isVerified = isVerified;
        this.dias = dias;
        this.SitioWeb = sitioWeb;
        this.fotos_local = fotos_local;
        this.disponible = disponible;
    }

    public Farmacia(String id, String uid, String name, String direcc, String numero,
                    String h_entrada, String h_salida, String s, String s1, String Uri,
                    List<String> fotos_local) {
        ID = id;
        Uid = uid;
        mName = name;
        direccion = direcc;
        telefono = numero;
        hora_apertura = h_entrada;
        hora_cierre = h_salida;
        Lat = s;
        Long = s1;
        profile = Uri;
        this.fotos_local = fotos_local;

    }

    public Farmacia(String id, String uid, String name, String direcc, String numero,
                    String h_entrada, String h_salida, String s, String s1, String Uri,boolean disp) {
        ID = id;
        Uid = uid;
        mName = name;
        direccion = direcc;
        telefono = numero;
        hora_apertura = h_entrada;
        hora_cierre = h_salida;
        Lat = s;
        Long = s1;
        profile = Uri;
        this.disponible = disp;
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

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public void setFotos_local(List<String> fotos_local) {
        this.fotos_local = fotos_local;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setSitioWeb(String sitioWeb) {
        SitioWeb = sitioWeb;
    }

    public void setHora_apertura(String hora_apertura) {
        this.hora_apertura = hora_apertura;
    }

    public void setHora_cierre(String hora_cierre) {
        this.hora_cierre = hora_cierre;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getmName() {
        return mName;
    }

    public float getmRating() {
        return mRating;
    }

    public String getPortada() {
        return portada;
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

    public List<String> getFotos_local() {
        return fotos_local;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
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

    public String getPricelevel() {
        return pricelevel;
    }

    public void setPricelevel(String pricelevel) {
        this.pricelevel = pricelevel;
    }

    public List<Integer> getPlacetypes() {
        return placetypes;
    }

    public void setPlacetypes(List<Integer> placetypes) {
        this.placetypes = placetypes;
    }


}

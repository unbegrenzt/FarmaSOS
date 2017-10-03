/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

import java.util.List;

/**
 * Created by Unbegrenzt on 12/7/2017.
 */

public class Local {
    private String ID;
    private String Website;
    private String Address;
    private String Atributions;
    private double Latitud;
    private double Longitud;
    private String Name;
    private String Telefono;
    private List<Integer> Placetypes;

    public Local(String ID,  String address,
                 double latitud, double longitud, String name, String telefono,
                 List<Integer> placetypes) {
        this.ID = ID;
        Address = address;
        Latitud = latitud;
        Longitud = longitud;
        Name = name;
        Telefono = telefono;
        Placetypes = placetypes;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAtributions() {
        return Atributions;
    }

    public void setAtributions(String atributions) {
        Atributions = atributions;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public List<Integer> getPlacetypes() {
        return Placetypes;
    }

    public void setPlacetypes(List<Integer> placetypes) {
        Placetypes = placetypes;
    }
}

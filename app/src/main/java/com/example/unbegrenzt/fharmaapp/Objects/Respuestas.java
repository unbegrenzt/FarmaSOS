/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-12-17 11:19 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-11-17 06:00 PM
 */

package com.example.unbegrenzt.fharmaapp.Objects;


import java.util.List;

public class Respuestas {

    private List<String> uid;
    private List<Integer> respuesta;
    private String id_peticion;

    public Respuestas(List<String> uid, List<Integer> respuesta, String id_peticion) {
        this.uid = uid;
        this.respuesta = respuesta;
        this.id_peticion = id_peticion;
    }

    public Respuestas(){



    }

    public List<String> getUid() {
        return uid;
    }

    public void setUid(List<String> uid) {
        this.uid = uid;
    }

    public List<Integer> getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(List<Integer> respuesta) {
        this.respuesta = respuesta;
    }

    public String getId_peticion() {
        return id_peticion;
    }

    public void setId_peticion(String id_peticion) {
        this.id_peticion = id_peticion;
    }
}

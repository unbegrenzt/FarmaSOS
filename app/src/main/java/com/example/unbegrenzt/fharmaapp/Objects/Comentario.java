/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-10-17 07:06 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-10-17 07:06 PM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

public class Comentario {

    private String User_id;
    private String comentario;

    public Comentario(String user_id, String comentario) {
        User_id = user_id;
        this.comentario = comentario;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}

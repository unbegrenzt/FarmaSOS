/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-28-17 09:56 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-26-17 11:58 PM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

import java.util.List;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */

public class Card_Farmacias {

    private int mGravity;
    private String mText;
    private List<Farmacia> mApps;

    public Card_Farmacias(int gravity, String text, List<Farmacia> farmacias){
        mGravity = gravity;
        mText = text;
        mApps = farmacias;
    }

    public int getmGravity() {
        return mGravity;
    }

    public String getmText() {
        return mText;
    }

    public List<Farmacia> getmApps() {
        return mApps;
    }
}

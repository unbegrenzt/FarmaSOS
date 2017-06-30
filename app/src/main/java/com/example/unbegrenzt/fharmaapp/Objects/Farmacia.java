/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-28-17 10:30 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-28-17 10:30 AM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */

public class Farmacia {

    private int mDrawable;
    private String mName;
    private float mRating;

    public Farmacia(String name, int drawable , float rating){

        mName = name;
        mDrawable = drawable;
        mRating = rating;

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

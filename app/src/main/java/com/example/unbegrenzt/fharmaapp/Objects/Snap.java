/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

import java.util.List;

/**
 * Created by Unbegrenzt on 8/7/2017.
 */

public class Snap {

    private int mGravity;
    private String mText;
    private List<Farmacia> mApps;

    public Snap(int gravity, String text, List<Farmacia> apps){
        mGravity = gravity;
        mText = text;
        mApps = apps;
    }

    public  String getText() { return mText; }
    public int getGravity() { return  mGravity; }
    public List<Farmacia> getApps() { return mApps; }
}

/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-01-17 03:58 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-28-17 10:38 AM
 */

package com.example.unbegrenzt.fharmaapp.Objects;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Unbegrenzt on 28/6/2017.
 */

public class Banners {
    private String Title;
    private RecyclerView Items;

    public Banners(String title, RecyclerView items){

        Title = title;
        Items = items;

    }

    public String getTitle() {
        return Title;
    }

    public RecyclerView getItems() {
        return Items;
    }
}

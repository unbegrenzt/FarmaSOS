/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
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

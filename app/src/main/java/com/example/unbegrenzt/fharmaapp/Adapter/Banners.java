/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-28-17 09:54 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-28-17 09:54 AM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

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

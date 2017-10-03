/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.touchlistener.ItemClickListener;

/**
 * Created by Unbegrenzt on 7/7/2017.
 */

public class HolderFarma extends RecyclerView.ViewHolder implements View.OnClickListener {
    //VIEWS
    public ImageView imageView;
    public TextView nameFarma;
    public ImageView update;
    public ImageView acepted;
    ItemClickListener itemClickListener;

    public HolderFarma(View itemView) {
        super(itemView);

        //ASSIGNING VIEWS
        imageView = (ImageView)itemView.findViewById(R.id.ic_farma);
        nameFarma = (TextView)itemView.findViewById(R.id.namefarma);
        update = (ImageView) itemView.findViewById(R.id.update);
        acepted = (ImageView) itemView.findViewById(R.id.acepted);

        itemView.setOnClickListener(this);
    }

    //WHEN CLICKED
    @Override
    public void onClick(View v) {

        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }


    //SHALL BE CALLED OUTSIDE
    public void serItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}

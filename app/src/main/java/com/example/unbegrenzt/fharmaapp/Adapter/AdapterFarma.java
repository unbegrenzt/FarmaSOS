/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-26-17 11:08 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-26-17 11:08 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;

import java.util.List;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */
//Adapter Farma se encarga de la tarjetas de tienda
public class AdapterFarma  extends RecyclerView.Adapter<AdapterFarma.ViewHolder> {

    private List<Farmacia> mApps;
    private boolean mPager;

    public AdapterFarma(boolean pager, List<Farmacia> Farmacias){
        mApps = Farmacias;
        mPager = pager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        if(mPager){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_farma, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_farma_horizontal, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Farmacia app = mApps.get(position);
        holder.imageView.setImageResource(app.getmDrawable());
        holder.nameFarma.setText(app.getmName());
        holder.ratingText.setText(String.valueOf(app.getmRating()));
    }

    @Override
    public int getItemViewType(int position){
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameFarma;
        public TextView ratingText;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.photofarma);
            nameFarma = (TextView)itemView.findViewById(R.id.FarmName);
            ratingText = (TextView)itemView.findViewById(R.id.ratingText);
        }

    }

}

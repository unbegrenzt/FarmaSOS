/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */


//Adapter Farma se encarga de la tarjetas del admin
public class AdapterFarma  extends RecyclerView.Adapter<AdapterFarma.ViewHolder> {

    private List<Farmacia> mApps;
    private Context context;

    public AdapterFarma(Context context, List<Farmacia> Farmacias){
        mApps = Farmacias;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vertical, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Farmacia app = mApps.get(position);
        Picasso.with(context).load(app.getProfile()).placeholder(R.color.transparent)
                .error(R.color.transparent).resize(50,50).centerCrop().into(holder.perfil);

        holder.nombre.setText(app.getmName());

        if(app.isDisponible()){
            holder.disp.setText("Abierto");
            holder.disp_ic.setImageResource(R.drawable.ic_local_green);
        }else{
            holder.disp.setText("Cerrado");
            holder.disp_ic.setImageResource(R.drawable.ic_local_red);
        }

        holder.rating.setText(String.valueOf(app.getmRating()));
    }

    @Override
    public int getItemViewType(int position){
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public void add(Farmacia farmacia) {
        mApps.add(farmacia);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView perfil;
        public TextView nombre;
        public TextView disp;
        public ImageView disp_ic;
        public TextView rating;

        public ViewHolder(View itemView){
            super(itemView);
            perfil = (ImageView) itemView.findViewById(R.id.imageView);
            nombre =  (TextView) itemView.findViewById(R.id.nameTextView);
            disp = (TextView) itemView.findViewById(R.id.disponible);
            disp_ic = (ImageView) itemView.findViewById(R.id.disp_ic);
            rating = (TextView) itemView.findViewById(R.id.rating);
        }

    }

}

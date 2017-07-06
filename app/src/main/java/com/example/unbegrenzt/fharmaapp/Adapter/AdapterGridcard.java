/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-05-17 09:52 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-05-17 09:52 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unbegrenzt.fharmaapp.Objects.Banners;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Unbegrenzt on 5/7/2017.
 */

public class AdapterGridcard extends RecyclerView.Adapter<AdapterGridcard.ViewHolder> {

    private List<Farmacia> mApps;
    private Context context;

    public AdapterGridcard(List<Farmacia> Farmacias,
                           Context context){
        mApps = Farmacias;
        this.context = context;
    }

    @Override
    public AdapterGridcard.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_farma_horizontal, parent, false);
        AdapterGridcard.ViewHolder vh = new AdapterGridcard.ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(AdapterGridcard.ViewHolder holder, int position){
        Farmacia app = mApps.get(position);
        Picasso.with(context).load(app.getProfile()).placeholder(R.drawable.load)
                .error(R.drawable.ic_person).resize(50,50).centerCrop().into(holder.imageView);

        holder.nameFarma.setText(app.getmName());

        if(app.isDisponible()){
            holder.turno.setText("Abierta");
        }else{
            holder.turno.setText("Cerrada");
        }
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
        public TextView turno;
        public ImageView acepted;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.photofarma);
            nameFarma = (TextView)itemView.findViewById(R.id.FarmName);
            turno = (TextView) itemView.findViewById(R.id.turno);
        }

    }
}

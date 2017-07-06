/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-26-17 11:08 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-26-17 11:08 PM
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

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Unbegrenzt on 26/6/2017.
 */


//Adapter Farma se encarga de la tarjetas del admin
public class AdapterFarma  extends RecyclerView.Adapter<AdapterFarma.ViewHolder> {

    private List<Farmacia> mApps;
    private boolean mPager;
    private Context context;
    private View.OnClickListener clickListener;

    public AdapterFarma(boolean pager, List<Farmacia> Farmacias, Context context,View.OnClickListener clickListener){
        mApps = Farmacias;
        mPager = pager;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        if(mPager){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmacias, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_farmacias, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Farmacia app = mApps.get(position);
        Picasso.with(context).load(app.getProfile()).placeholder(R.drawable.load)
                .error(R.drawable.ic_person).resize(50,50).centerCrop().into(holder.imageView);

        holder.nameFarma.setText(app.getmName());

        holder.update.setImageResource(R.drawable.ic_update);

        if(app.getAcepted().compareTo("true") == 0){
            holder.acepted.setImageResource(R.drawable.ic_info_green);
        }else{
            holder.acepted.setImageResource(R.drawable.ic_info_red);
        }

        holder.imageView.setOnClickListener(clickListener);
        holder.acepted.setOnClickListener(clickListener);
        holder.update.setOnClickListener(clickListener);
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
        public ImageView update;
        public ImageView acepted;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.ic_farma);
            nameFarma = (TextView)itemView.findViewById(R.id.namefarma);
            update = (ImageView) itemView.findViewById(R.id.update);
            acepted = (ImageView) itemView.findViewById(R.id.acepted);
        }

    }

}

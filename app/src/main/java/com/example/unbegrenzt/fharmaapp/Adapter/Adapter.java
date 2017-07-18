/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-08-17 01:52 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-08-17 01:52 PM
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

/**
 * Created by Unbegrenzt on 8/7/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Context context;
    private List<Farmacia> mApps;
    private boolean mHorizontal;
    private boolean mPager;

    public Adapter(Context context, boolean horizontal, boolean pager, List<Farmacia> apps) {
        mHorizontal = horizontal;
        mApps = apps;
        mPager = pager;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mPager) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_pager, parent, false));
        } else {
            return mHorizontal ? new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter, parent, false)) :
                    new ViewHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.adapter_vertical, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Farmacia app = mApps.get(position);
        Picasso.with(context).load(app.getProfile()).placeholder(R.drawable.load)
                .error(R.drawable.ic_person).into(holder.imageView);

        holder.nameTextView.setText(app.getmName());

        if(app.isDisponible()){
            holder.ratingTextView.setText("Abierto");
        }else{
            holder.ratingTextView.setText("cerrado");
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameTextView;
        public TextView ratingTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            ratingTextView = (TextView) itemView.findViewById(R.id.ratingTextView);
        }

    }

}

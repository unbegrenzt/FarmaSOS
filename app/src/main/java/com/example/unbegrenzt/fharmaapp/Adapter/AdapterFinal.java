/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.unbegrenzt.fharmaapp.Objects.Banners;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unbegrenzt on 28/6/2017.
 */

/*public class AdapterFinal extends RecyclerView.Adapter<AdapterFinal.ViewHolder> {

    private List<Farmacia> mApps;
    private boolean mPager;
    private Context context;

    public AdapterFinal(Context context, List<Farmacia> banners){
        mApps = banners;
        this.context = context;
    }

    @Override
    public AdapterFinal.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vertical, parent, false);
        AdapterFinal.ViewHolder vh = new AdapterFinal.ViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Farmacia app = mApps.get(position);
        holder.Title.setText(app.getTitle());
        holder.recycler.setHasFixedSize(true);
        holder.recycler.setItemViewCacheSize(10);
        holder.recycler.setDrawingCacheEnabled(true);
        holder.recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //MyAdapter adapter = new MyAdapter(new String[]{"Example One", "Example Two", "Example Three", "Example Four", "Example Five" , "Example Six" , "Example Seven"});
        holder.recycler.setAdapter(new AdapterFarma(true,getApps()));
        holder.recycler.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        holder.recycler.setLayoutManager(layoutManager);
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

        public TextView Title;
        public RecyclerView recycler;

        public ViewHolder(View itemView){
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.Cardtitle1);
            recycler = (RecyclerView) itemView.findViewById(R.id.recycler_cards);
        }

    }

}*/

/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-28-17 09:49 AM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-28-17 09:49 AM
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

public class AdapterBannerCards extends RecyclerView.Adapter<AdapterBannerCards.ViewHolder> {

    private List<Banners> mApps;
    private boolean mPager;
    private Context context;

    public AdapterBannerCards(Context context, boolean pager, List<Banners> banners){
        mApps = banners;
        mPager = pager;
        this.context = context;
    }

    @Override
    public AdapterBannerCards.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        if(mPager){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farma_item, parent, false);
            AdapterBannerCards.ViewHolder vh = new AdapterBannerCards.ViewHolder(v);
            return vh;
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.farma_item, parent, false);
            AdapterBannerCards.ViewHolder vh = new AdapterBannerCards.ViewHolder(v);
            return vh;
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Banners app = mApps.get(position);
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

    public List<Farmacia> getApps(){

        List<Farmacia> apps = new ArrayList<>();
        apps.add(new Farmacia("Gooogle",R.drawable.cloud_off,4.5f));
        apps.add(new Farmacia("Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_star,1.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_exit,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.ic_info,3.5f));
        apps.add(new Farmacia("Gooogle",R.drawable.cloud_off,4.5f));
        apps.add(new Farmacia(" Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_star,1.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_profile,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.common_google_signin_btn_icon_dark_focused,3.5f));
        apps.add(new Farmacia("Mail",R.drawable.ic_exit,2.5f));
        apps.add(new Farmacia("Gmail",R.drawable.ic_info,3.5f));

        return apps;
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

}

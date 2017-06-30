/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 09:53 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 05:05 PM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unbegrenzt.fharmaapp.Adapter.AdapterBannerCards;
import com.example.unbegrenzt.fharmaapp.Adapter.Banners;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;

import java.util.ArrayList;
import java.util.List;


public class Farmacias extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Farmacias() {
        // Required empty public constructor
    }

    public static Farmacias newInstance(String param1, String param2) {
        Farmacias fragment = new Farmacias();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.farmacias, container, false);

        RecyclerView CardItems = (RecyclerView) rootView.findViewById(R.id.recyclerCards);
        CardItems.setHasFixedSize(true);
        CardItems.setItemViewCacheSize(1);
        CardItems.setDrawingCacheEnabled(true);
        CardItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //MyAdapter adapter = new MyAdapter(new String[]{"Example One", "Example Two", "Example Three", "Example Four", "Example Five" , "Example Six" , "Example Seven"});
        CardItems.setAdapter(new AdapterBannerCards(getContext(),true,getCards()));
        CardItems.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        CardItems.setLayoutManager(layoutManager);

        return rootView;
    }

    private List<Banners> getCards(){

        List<Banners> apps = new ArrayList<>();
        apps.add(new Banners("Lo mas buscado", new RecyclerView(getContext())));
        apps.add(new Banners("Lo mejor", new RecyclerView(getContext())));
        apps.add(new Banners("Lo nuevo", new RecyclerView(getContext())));
        return apps;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-10-17 11:23 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-10-17 11:23 PM
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
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Adapter.AdapterFarma;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.touchlistener.ClicklistenerFarma;

import java.util.ArrayList;
import java.util.List;


public class Perfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Perfil() {
        // Required empty public constructor
    }

    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.perfil, container, false);

        RecyclerView Farma_recycler = (RecyclerView) rootView.findViewById(R.id.recycler_profes);
        Farma_recycler.addOnItemTouchListener(new ClicklistenerFarma(getContext(),Farma_recycler,
                new ClicklistenerFarma.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        Farma_recycler.setHasFixedSize(true);
        Farma_recycler.setItemViewCacheSize(10);
        Farma_recycler.setDrawingCacheEnabled(true);
        Farma_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //MyAdapter adapter = new MyAdapter(new String[]{"Example One", "Example Two", "Example Three", "Example Four", "Example Five" , "Example Six" , "Example Seven"});
        Farma_recycler.setAdapter(new AdapterFarma(true,getApps()));
        Farma_recycler.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        Farma_recycler.setLayoutManager(layoutManager);

        RecyclerView recycler2 = (RecyclerView) rootView.findViewById(R.id.recycler_profs);
        recycler2.addOnItemTouchListener(new ClicklistenerFarma(getContext(),recycler2,
                new ClicklistenerFarma.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        recycler2.setHasFixedSize(true);
        recycler2.setItemViewCacheSize(10);
        recycler2.setDrawingCacheEnabled(true);
        recycler2.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //MyAdapter adapter = new MyAdapter(new String[]{"Example One", "Example Two", "Example Three", "Example Four", "Example Five" , "Example Six" , "Example Seven"});
        recycler2.setAdapter(new AdapterFarma(true,getApps()));
        recycler2.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recycler2.setLayoutManager(layoutManagers);

        return rootView;
    }

    private List<Farmacia> getApps(){

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

    // TODO: Rename method, update argument and hook method into UI event
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

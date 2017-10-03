/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-30-17 12:45 PM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unbegrenzt.fharmaapp.Adapter.AdapterGridcard;
import com.example.unbegrenzt.fharmaapp.Objects.Banners;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.unbegrenzt.fharmaapp.actividades.Navigation;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class Farmacias extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView CardItems;
    private DatabaseReference databaseFarma;
    private List<Farmacia> farmacias;

    public Farmacias() {
        // Required empty public constructor
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

        CardItems = (RecyclerView) rootView.findViewById(R.id.recyclerCards);
        databaseFarma = FirebaseDatabase.getInstance().getReference(getString(R.string.get_ref_farma));
        farmacias = new ArrayList<>();
        update_cards();

        return rootView;
    }

    private void update_cards(){
        databaseFarma.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                farmacias.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Farmacia farmacia = postSnapshot.getValue(Farmacia.class);
                    farmacias.add(farmacia);
                }
                RecyclerCards(farmacias);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void RecyclerCards(List<Farmacia> farmacias){
        CardItems.setHasFixedSize(true);
        CardItems.setItemViewCacheSize(1);
        CardItems.setDrawingCacheEnabled(true);
        CardItems.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        //MyAdapter adapter = new MyAdapter(new String[]{"Example One", "Example Two", "Example Three", "Example Four", "Example Five" , "Example Six" , "Example Seven"});
        CardItems.setAdapter(new AdapterGridcard(farmacias,getApplicationContext()));
        CardItems.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        CardItems.setLayoutManager(layoutManager);
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
                    + " must implement OnFragmentInt eractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}

/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-05-17 11:17 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-05-17 11:17 PM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unbegrenzt.fharmaapp.Adapter.AdapterGridcard;
import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link izi.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link izi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class izi extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView CardItems;
    private DatabaseReference databaseFarma;
    private List<Farmacia> farmacias;
    private ValueEventListener listener;

    public izi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment izi.
     */
    // TODO: Rename and change types and number of parameters
    public static izi newInstance(String param1, String param2) {
        izi fragment = new izi();
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
        View rootView = inflater.inflate(R.layout.izi, container, false);

        CardItems = (RecyclerView) rootView.findViewById(R.id.Cards);
        databaseFarma = FirebaseDatabase.getInstance().getReference(getString(R.string.get_ref_farma));
        farmacias = new ArrayList<>();
        update_cards();

        return rootView;
    }

    private void update_cards(){
        databaseFarma.addValueEventListener(listener = new ValueEventListener() {
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        CardItems.setLayoutManager(layoutManager);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

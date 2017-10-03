/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 08-27-17 05:22 PM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import com.example.unbegrenzt.fharmaapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * PagesAdapter.OnFragmentInteractionListener interface
 * to handle interaction events.
 * Use the {@link PagesAdapter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PagesAdapter extends Fragment {

    /**
     * Key to insert the background color into the mapping of a Bundle.
     */
    private static final String IMAGE = "image";
    private static final String DESCRIP = "descrip";

    private ImageView imagen;
    private TextView texto;

    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param image
     */
    public static PagesAdapter newInstance(String descripsion, int image) {

        // Instantiate a new fragment
        PagesAdapter fragment = new PagesAdapter();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putString(DESCRIP, descripsion);
        bundle.putInt(IMAGE, image);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        //this.color = (getArguments() != null) ? getArguments().getInt(
          //      BACKGROUND_COLOR) : Color.BLUE;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.intro_layout, container, false);

        //Top_Color = (ImageView)rootView.findViewById(R.id.backtop);
        //Top_Color.setBackgroundColor(getArguments().getInt(BACKGROUND_COLOR));

        //Bottom_Color = (ImageView)rootView.findViewById(R.id.backbottom);
        //Bottom_Color.setBackgroundColor(getArguments()
                //.getInt(BACKGROUND_COLOR_BOT));

            imagen = (ImageView)rootView.findViewById(R.id.imagen);
            imagen.setBackgroundResource(getArguments().getInt(IMAGE));

            texto = (TextView)rootView.findViewById(R.id.texto);
            texto.setText(getArguments().getString(DESCRIP));

        return rootView;

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

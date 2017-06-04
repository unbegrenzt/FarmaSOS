/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 12:29 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 11:56 AM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    private static final String BACKGROUND_COLOR = "color_top";
    private static final String BACKGROUND_COLOR_BOT = "color_bot";
    private static final String IMAGE = "image";

    /**
     * Key to insert the index page into the mapping of a Bundle.
     */
    private static final String INDEX = "index";

    private int color_top;
    private int color_bot;
    private int index;
    private int image;

    private ImageView Top_Color, Bottom_Color, imagen;

    /**
     * Instances a new fragment with a background color and an index page.
     *
     * @param color
     *            background color
     * @param image
     *@param index
     *            index page  @return a new page
     */
    public static PagesAdapter newInstance(int color, int color_bot, int image, int index) {

        // Instantiate a new fragment
        PagesAdapter fragment = new PagesAdapter();

        // Save the parameters
        Bundle bundle = new Bundle();
        bundle.putInt(BACKGROUND_COLOR, color);
        bundle.putInt(BACKGROUND_COLOR_BOT,color_bot);
        bundle.putInt(IMAGE, image);
        bundle.putInt(INDEX, index);
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

        this.index = (getArguments() != null) ? getArguments().getInt(INDEX)
                : -1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.pages_adapter, container, false);

        Top_Color = (ImageView)rootView.findViewById(R.id.backtop);
        Top_Color.setBackgroundColor(getArguments().getInt(BACKGROUND_COLOR));

        Bottom_Color = (ImageView)rootView.findViewById(R.id.backbottom);
        Bottom_Color.setBackgroundColor(getArguments()
                .getInt(BACKGROUND_COLOR_BOT));

        imagen = (ImageView)rootView.findViewById(R.id.imagen);
        imagen.setBackgroundResource(getArguments().getInt(IMAGE));

        return rootView;

    }

}

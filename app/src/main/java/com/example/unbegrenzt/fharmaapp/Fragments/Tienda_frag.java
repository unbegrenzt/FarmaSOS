/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-13-17 10:46 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-13-17 10:41 PM
 */

package com.example.unbegrenzt.fharmaapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Objects.Farmacia;
import com.example.unbegrenzt.fharmaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tienda_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tienda_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tienda_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String OBJ_FARMA = "param1";


    private Farmacia farmacia;

    private ImageView perfil;
    private TextView nombre;
    private TextView disp;
    private ImageView disp_ic;
    private TextView direccion;
    private TextView telefono;
    private TextView h_entrada;
    private TextView h_salida;
    private DatabaseReference mDatabase;
    private Query myTopPostsQuery;
    private ValueEventListener listenr;

    private OnFragmentInteractionListener mListener;

    public Tienda_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Tienda_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Tienda_frag newInstance(Farmacia param1) {
        Tienda_frag fragment = new Tienda_frag();
        Bundle args = new Bundle();
        args.putSerializable(OBJ_FARMA, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            farmacia = (Farmacia) getArguments().getSerializable(OBJ_FARMA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tienda_frag, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.get_ref_farma));

        //configura el toolbar
        //setTitle(farmacia.getmName());

        //pedimos foto de perfil
        perfil = (ImageView)v.findViewById(R.id.photoperfil);

        Picasso.with(getApplicationContext()).load(farmacia.getProfile()).placeholder(R.color.transparent)
                .error(R.color.transparent).into(perfil);

        //ponemos nombre en el texto
        nombre = (TextView)v.findViewById(R.id.nameTextView);

        //configuramos el nombre de la iglesia
        nombre.setText(farmacia.getmName());

        //configuramos si está abierta o no
        disp = (TextView)v.findViewById(R.id.disp);
        disp_ic = (ImageView)v.findViewById(R.id.ic_disp);
        if(farmacia.isDisponible()){
            disp.setText("Abierto");
            disp_ic.setImageResource(R.drawable.ic_local_green);
        }else{
            disp.setText("Cerrado");
            disp_ic.setImageResource(R.drawable.ic_local_red);
        }

        //configuramos la dirección
        direccion = (TextView)v.findViewById(R.id.direccion);
        direccion.setText(farmacia.getDireccion());

        //configuramos el telefono
        telefono = (TextView)v.findViewById(R.id.telefono);
        telefono.setText(farmacia.getTelefono());

        //configurar hora entrada y salida
        h_entrada = (TextView)v.findViewById(R.id.hora_entrada);
        h_entrada.setText(farmacia.getHora_apertura());

        h_salida = (TextView)v.findViewById(R.id.hora_salida);
        h_salida.setText(farmacia.getHora_cierre());

        return v;
    }

    private void updatedata(com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmacia) {
        //setTitle(farmacia.getmName());
        Picasso.with(getApplicationContext()).load(farmacia.getProfile()).placeholder(R.color.transparent)
                .error(R.color.transparent).into(perfil);
        nombre.setText(farmacia.getmName());
        if(farmacia.isDisponible()){
            disp.setText("Abierto");
            disp_ic.setImageResource(R.drawable.ic_local_green);
        }else{
            disp.setText("Cerrado");
            disp_ic.setImageResource(R.drawable.ic_local_red);
        }
        direccion.setText(farmacia.getDireccion());
        telefono.setText(farmacia.getTelefono());
        h_entrada.setText(farmacia.getHora_apertura());
        h_salida.setText(farmacia.getHora_cierre());
    }

    @Override
    public void onPause(){
        super.onPause();
        mDatabase.removeEventListener(listenr);
    }

    @Override
    public void onResume(){
        super.onResume();
        mDatabase.addValueEventListener(listenr = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    com.example.unbegrenzt.fharmaapp.Objects.Farmacia farma =
                            postSnapshot.getValue(com.example.unbegrenzt.fharmaapp
                                    .Objects.Farmacia.class);
                    if(farmacia.getID().compareTo(postSnapshot.getKey()) == 0){
                        farmacia = farma;
                    }
                }
                updatedata(farmacia);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

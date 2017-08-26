/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 07-09-17 02:12 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 07-09-17 02:12 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Farmacia extends AppCompatActivity {

    private com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmacia;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tienda);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            farmacia = (com.example.unbegrenzt.fharmaapp.Objects.Farmacia)
                    getIntent().getSerializableExtra("farmaciaValue"); //Obtaining data
        }else{
            Toast.makeText(getApplicationContext(),"Recarge la pagina",Toast.LENGTH_LONG).show();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference(getString(R.string.get_ref_farma));

        getWindow().setBackgroundDrawable(null);

        //configura el toolbar
        setTitle(farmacia.getmName());

        //pedimos foto de perfil
        perfil = (ImageView)findViewById(R.id.photoperfil);

        Picasso.with(this).load(farmacia.getProfile()).placeholder(R.color.transparent)
                .error(R.color.transparent).into(perfil);

        //ponemos nombre en el texto
        nombre = (TextView)findViewById(R.id.nameTextView);

        //configuramos el nombre de la iglesia
        nombre.setText(farmacia.getmName());

        //configuramos si está abierta o no
        disp = (TextView)findViewById(R.id.disp);
        disp_ic = (ImageView)findViewById(R.id.ic_disp);
        if(farmacia.isDisponible()){
            disp.setText("Abierto");
            disp_ic.setImageResource(R.drawable.ic_local_green);
        }else{
            disp.setText("Cerrado");
            disp_ic.setImageResource(R.drawable.ic_local_red);
        }

        //configuramos la dirección
        direccion = (TextView)findViewById(R.id.direccion);
        direccion.setText(farmacia.getDireccion());

        //configuramos el telefono
        telefono = (TextView)findViewById(R.id.telefono);
        telefono.setText(farmacia.getTelefono());

        //configurar hora entrada y salida
        h_entrada = (TextView)findViewById(R.id.hora_entrada);
        h_entrada.setText(farmacia.getHora_apertura());

        h_salida = (TextView)findViewById(R.id.hora_salida);
        h_salida.setText(farmacia.getHora_cierre());
    }

    private void updatedata(com.example.unbegrenzt.fharmaapp.Objects.Farmacia farmacia) {
        setTitle(farmacia.getmName());
        Picasso.with(this).load(farmacia.getProfile()).placeholder(R.color.transparent)
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
                        Log.e("asdf","y aqui");
                    }
                    Log.e("asdf","aqui");
                }
                updatedata(farmacia);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

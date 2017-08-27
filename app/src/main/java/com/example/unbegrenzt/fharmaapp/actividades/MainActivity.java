/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 09:58 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 09:54 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    //Clase que presenta el SplashScreen si es primera vez nos dirigimos a una
    //vista guiada sino al menu principal
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream fin = openFileInput("momento.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                    String texto = "";
                    String cadena = "";
                    if (fin != null) {

                        while ((cadena = reader.readLine()) != null) {
                            texto = cadena;
                        }
                        if (texto.compareTo("no es primera vez") == 0) {
                            fin.close();
                            Intent intent = new Intent(MainActivity.this, Principal_map.class);
                            startActivity(intent);
                            finish();
                        }else{
                            try {
                                File fichero = new File(getFilesDir().getPath() + "momento.txt");
                            } catch (Exception e) {

                            }

                            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("momento.txt", Context.MODE_PRIVATE));
                            String cad = "no es primera vez";
                            fout.write(cad);
                            fout.close();

                            Intent intent = new Intent(MainActivity.this, Vista.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }catch (Exception ex) {
                    try {
                        try {
                            File fichero = new File(getFilesDir().getPath() + "momento.txt");
                        } catch (Exception e) {

                        }

                        OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("momento.txt", Context.MODE_PRIVATE));
                        String cadena = "no es primera vez";
                        fout.write(cadena);
                        fout.close();

                        Intent intent = new Intent(MainActivity.this, Intro.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception edx) {
                        edx.printStackTrace();
                        Intent intent = new Intent(MainActivity.this, Intro.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        },500);
        //tiempo en ms en que se presenta el SplashScreen
    }

}




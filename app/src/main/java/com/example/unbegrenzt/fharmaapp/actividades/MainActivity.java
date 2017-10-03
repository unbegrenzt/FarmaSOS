/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-30-17 01:10 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.kingfisher.easy_sharedpreference_library.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    //Clase que presenta el SplashScreen si es primera vez nos dirigimos a una
    //vista guiada sino al menu principal
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        SharedPreferencesManager.init(getApplicationContext(), true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int text = SharedPreferencesManager.getInstance().getValue("primera_vez",
                        Integer.class);

                if (text == 3){

                    Intent intent = new Intent(MainActivity.this, ggeasyy.class);
                    startActivity(intent);
                    finish();

                }else{

                    SharedPreferencesManager.getInstance().putValue("primera_vez", 3);

                    Intent intent = new Intent(MainActivity.this, Intro.class);
                    startActivity(intent);
                    finish();
                }


                /*try {
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

                            Intent intent = new Intent(MainActivity.this, Intro.class);
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
                }*/
            }
        },200);
        //tiempo en ms en que se presenta el SplashScreen
    }

}




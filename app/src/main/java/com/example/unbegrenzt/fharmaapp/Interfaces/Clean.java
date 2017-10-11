/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-10-17 12:59 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-10-17 12:59 PM
 */

package com.example.unbegrenzt.fharmaapp.Interfaces;

/**
 * Esta interface nos sive para visualizar de mejor forma como se  limpia
 * la UI de forma amigable al usuario por ejemplo
 * case: Clean.FRAGMENT_SEARCH
 * en este caso por ejemplo se limpian todos los marcadores
 * de otros metodos generadores de marker ademas se debe limpiar la
 * polyline por si acaso el usuario generó una ruta  entre dos puntos
 */

public interface Clean {

    int FRAGMENT_SEARCH = 0;
    int FARM_CERCANA = 1;
    int MY_SITES = 2;
    int BEFORE_ROUTE = 3;
}

/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:56 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-02-17 05:56 PM
 */

package com.example.unbegrenzt.fharmaapp.web_service.api;

import com.example.unbegrenzt.fharmaapp.web_service.clases.ObjPlaceWS.PlaceWS;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceInterface {

    @GET("place/details/json?")
    Call<PlaceWS> doPlaces(@Query(value = "placeid", encoded = true) String placeid,
                           @Query(value = "language", encoded = true) String language,
                           @Query(value = "key", encoded = true)  String key);

    //Call doPlaces(@Query(value = "type", encoded = true) String type, @Query(value = "location", encoded = true) String location, @Query(value = "name", encoded = true) String name, @Query(value = "opennow", encoded = true) boolean opennow, @Query(value = "rankby", encoded = true) String rankby, @Query(value = "key", encoded = true) String key);

}

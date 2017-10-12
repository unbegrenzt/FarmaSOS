/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-11-17 01:17 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-11-17 01:17 PM
 */

package com.example.unbegrenzt.fharmaapp.web_service.api;

import com.example.unbegrenzt.fharmaapp.web_service.clases.ObjDistance.DistanceWS;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DistanceMatrix {

    @GET("distancematrix/json?")
    Call<DistanceWS> getdistance(@Query(value = "units", encoded = true) String units,
                                 @Query(value = "origins", encoded = true) String origins,
                                 @Query(value = "destinations", encoded = true)  String destinations,
                                 @Query(value = "language", encoded = true)  String language,
                                 @Query(value = "key", encoded = true)  String key);

}

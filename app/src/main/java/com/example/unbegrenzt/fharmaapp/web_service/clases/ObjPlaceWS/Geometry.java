
/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-11-17 01:16 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-03-17 05:25 AM
 */

package com.example.unbegrenzt.fharmaapp.web_service.clases.ObjPlaceWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}


/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-11-17 01:16 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-03-17 05:25 AM
 */

package com.example.unbegrenzt.fharmaapp.web_service.clases.ObjPlaceWS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Open {

    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("time")
    @Expose
    private String time;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

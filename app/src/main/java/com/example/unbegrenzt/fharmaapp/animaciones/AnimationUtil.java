/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 09:43 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 05:14 PM
 */

package com.example.unbegrenzt.fharmaapp.animaciones;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Unbegrenzt on 23/5/2017.
 */

public class AnimationUtil {


    public static void animate(RecyclerView.ViewHolder holder , boolean goesDown){

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown==true ? 200 : -200, 0);
        animatorTranslateY.setDuration(1000);

        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView,"translationX",-50,50,-30,30,-20,20,-5,5,0);
        animatorTranslateX.setDuration(1000);

        //animatorSet.playTogether(animatorTranslateX,animatorTranslateY);

        animatorSet.playTogether(animatorTranslateY);

        //animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();

    }
}


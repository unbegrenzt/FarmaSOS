/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-27-17 09:04 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-27-17 09:04 PM
 */

package com.example.unbegrenzt.fharmaapp.touchlistener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Unbegrenzt on 27/6/2017.
 */

public class ClicklistenerFarma implements RecyclerView.OnItemTouchListener {
    public interface OnItemClickListener{
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mListener;

    private GestureDetector mGestureDetector;

    public ClicklistenerFarma(Context context, final RecyclerView recyclerView,
                              OnItemClickListener listener){

        mListener = listener;

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){
                   @Override
                    public boolean onSingleTapUp(MotionEvent e){
                       return true;
                   }
                   @Override
                    public void onLongPress(MotionEvent e){
                       View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                       if(childView != null && mListener != null){
                           mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                       }
                   }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){

            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));

        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

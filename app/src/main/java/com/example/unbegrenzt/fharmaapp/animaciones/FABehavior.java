/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 09-16-17 08:45 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-16-17 08:45 PM
 */

package com.example.unbegrenzt.fharmaapp.animaciones;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

public class FABehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    private int navigationBarHeight = 0;
    private long lastSnackbarUpdate = 0;

    public FABehavior(int navigationBarHeight) {
        this.navigationBarHeight = navigationBarHeight;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency != null && dependency instanceof FloatingActionButton || super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        updateFloatingActionButton(child, dependency);
        return super.onDependentViewChanged(parent, child, dependency);
    }

    /**
     * Update floating action button bottom margin
     */
    private void updateFloatingActionButton(FloatingActionButton child, View dependency) {
        if (child != null && dependency != null && dependency instanceof FloatingActionButton) {
            lastSnackbarUpdate = System.currentTimeMillis();
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int fabDefaultBottomMargin = p.bottomMargin;
            child.setY(dependency.getY() - fabDefaultBottomMargin);
        }
    }

}

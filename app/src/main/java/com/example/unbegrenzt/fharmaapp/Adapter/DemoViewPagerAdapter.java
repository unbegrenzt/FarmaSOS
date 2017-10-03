/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-16-17 04:37 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.example.unbegrenzt.fharmaapp.Fragments.Map;
import com.example.unbegrenzt.fharmaapp.Fragments.izi;
import com.example.unbegrenzt.fharmaapp.Fragments.perfil_off;

import java.util.ArrayList;

/**
 *
 */
public class DemoViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public DemoViewPagerAdapter(FragmentManager fm) {
        super(fm);

        //aqui se añaden los fragments a
        //utilizar
        fragments.clear();
        fragments.add(new izi());
        fragments.add(new Map());
        fragments.add(new perfil_off());

    }

    //refrescamos dependiendo de la acción
    public void refreshpos(int pos){

        if (pos == 0){

            fragments.remove(pos);
            fragments.add(pos,new izi());
        }
        if (pos == 1){

            fragments.remove(pos);
            fragments.add(pos,new Map());
        }

        if (pos == 2){

            fragments.remove(pos);
            fragments.add(pos,new perfil_off());
        }

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);

    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}

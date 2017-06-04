/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 10:00 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 05:05 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

/*
  Created by Unbegrenzt on 1/5/2017.
 */
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 * @author unbegrenzt
 *
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    // List of fragments which are going to set in the view pager widget
    List<Fragment> fragments;

    /**
     * Constructor
     *
     * @param fm
     *            interface for interacting with Fragment objects inside of an
     *            Activity
     */
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }

    /**
     * Add a new fragment in the list.
     *
     * @param fragment
     *            a new fragment
     */
    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int arg0) {
        return this.fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

}


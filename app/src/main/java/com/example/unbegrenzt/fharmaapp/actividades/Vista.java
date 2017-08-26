/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 09:54 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 05:05 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unbegrenzt.fharmaapp.Adapter.PagesAdapter;
import com.example.unbegrenzt.fharmaapp.Adapter.ViewPagerAdapter;
import com.example.unbegrenzt.fharmaapp.R;

public class Vista extends FragmentActivity implements ViewPager.OnPageChangeListener{

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    private ViewPager pager = null;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    //ViewPagerAdapter pagerAdapter;
    private ImageView dot1,dot2,dot3;
    private TextView header, subheader;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.vista);

        dot1 = (ImageView)this.findViewById(R.id.dot1);
        dot2 = (ImageView)this.findViewById(R.id.dot2);
        dot3 = (ImageView)this.findViewById(R.id.dot3);

        header = (TextView)this.findViewById(R.id.heaader);
        subheader = (TextView)this.findViewById(R.id.subheader);

        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pager);

        // Create an adapter with the fragments we show on the ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        /*adapter.addFragment(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                    getResources().getIdentifier("descarga" , "drawable", getPackageName())));

        adapter.addFragment(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                    getResources().getIdentifier("descarga1" , "drawable", getPackageName())
                        ,1));

        adapter.addFragment(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                    getResources().getIdentifier("descarga2" , "drawable", getPackageName())
                        ,2));*/

        this.pager.setAdapter(adapter);
        this.pager.addOnPageChangeListener(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // se trata de cambiar los circulos con las imagenes
        if (this.pager.getCurrentItem() == 0) {

            dot1.setBackgroundResource(R.drawable.selecteditem_dot);
            dot2.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot3.setBackgroundResource(R.drawable.nonselecteditem_dot);
            header.setText(R.string.explor_n_l_map);
            subheader.setText(R.string.encuentra);

        } else if (this.pager.getCurrentItem() == 1) {

            dot1.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot2.setBackgroundResource(R.drawable.selecteditem_dot);
            dot3.setBackgroundResource(R.drawable.nonselecteditem_dot);
            header.setText(R.string.farma_q_busca);
            subheader.setText(R.string.farmaco);

        } else if (this.pager.getCurrentItem() == 2) {

            dot1.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot2.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot3.setBackgroundResource(R.drawable.selecteditem_dot);
            header.setText(R.string.no_conex);
            subheader.setText(R.string.local_disp);

        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void start(View view) {
        //FaceButton.performClick();
        Intent intent = new Intent(Vista.this, Navigation.class);
        startActivity(intent);
        finish();
    }
}


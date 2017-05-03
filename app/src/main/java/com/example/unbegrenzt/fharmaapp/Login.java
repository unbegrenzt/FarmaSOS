package com.example.unbegrenzt.fharmaapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

public class Login extends FragmentActivity implements ViewPager.OnPageChangeListener{

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    ViewPager pager = null;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    //ViewPagerAdapter pagerAdapter;
    ImageView dot1,dot2,dot3;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.login);

        dot1 = (ImageView)this.findViewById(R.id.dot1);
        dot2 = (ImageView)this.findViewById(R.id.dot2);
        dot3 = (ImageView)this.findViewById(R.id.dot3);

        // Instantiate a ViewPager
        this.pager = (ViewPager) this.findViewById(R.id.pager);

        // Create an adapter with the fragments we show on the ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        adapter.addFragment(PagesAdapter.newInstance(getResources()
                .getColor(R.color.atenuante), 0));
        adapter.addFragment(PagesAdapter.newInstance(getResources()
                .getColor(R.color.color_acentuado), 1));
        adapter.addFragment(PagesAdapter.newInstance(getResources()
                .getColor(R.color.color_base), 4));
        this.pager.setAdapter(adapter);
        this.pager.addOnPageChangeListener(this);

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // se trata de cambiar los circulos con las imagenes
        if (this.pager.getCurrentItem() == 0) {

            dot1.setBackgroundResource(R.drawable.selecteditem_dot);
            dot2.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot3.setBackgroundResource(R.drawable.nonselecteditem_dot);

        } else if (this.pager.getCurrentItem() == 1) {

            dot1.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot2.setBackgroundResource(R.drawable.selecteditem_dot);
            dot3.setBackgroundResource(R.drawable.nonselecteditem_dot);

        } else if (this.pager.getCurrentItem() == 2) {

            dot1.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot2.setBackgroundResource(R.drawable.nonselecteditem_dot);
            dot3.setBackgroundResource(R.drawable.selecteditem_dot);

        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}


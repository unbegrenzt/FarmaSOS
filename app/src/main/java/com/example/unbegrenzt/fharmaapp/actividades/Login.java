/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 09:54 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 05:05 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Adapter.PagesAdapter;
import com.example.unbegrenzt.fharmaapp.R;
import com.example.unbegrenzt.fharmaapp.Adapter.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends FragmentActivity implements ViewPager.OnPageChangeListener{

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    ViewPager pager = null;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    //ViewPagerAdapter pagerAdapter;
    ImageView dot1,dot2,dot3;
    TextView header, subheader;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("info", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("info", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
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

        adapter.addFragment(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                    getResources().getIdentifier("descarga" , "drawable", getPackageName())
                        ,0));

        adapter.addFragment(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                    getResources().getIdentifier("descarga1" , "drawable", getPackageName())
                        ,1));

        adapter.addFragment(PagesAdapter.newInstance(getResources().getColor(R.color.color_acentuado)
                ,getResources().getColor(R.color.acentuado_oscuro),
                    getResources().getIdentifier("descarga2" , "drawable", getPackageName())
                        ,2));

        this.pager.setAdapter(adapter);
        this.pager.addOnPageChangeListener(this);
        Log.i("info",getIntent().getExtras().getString("parametro"));
    }

    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }

    public void singup(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                    //Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "signInWithEmail:failed", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                    //Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
        try {
            Intent intent = new Intent(Login.this, menu.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
    }
}


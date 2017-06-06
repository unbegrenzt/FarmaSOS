/*
 * Created  by Unbegrenzt for Jorge Luis Morales Centeno on 06-03-17 09:54 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 06-03-17 05:05 PM
 */

package com.example.unbegrenzt.fharmaapp.actividades;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unbegrenzt.fharmaapp.Adapter.PagesAdapter;
import com.example.unbegrenzt.fharmaapp.Adapter.ViewPagerAdapter;
import com.example.unbegrenzt.fharmaapp.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends FragmentActivity implements ViewPager.OnPageChangeListener{

    /**
     * The pager widget, which handles animation and allows swiping horizontally
     * to access previous and next pages.
     */
    private ViewPager pager = null;

    CallbackManager callbackManager;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    //ViewPagerAdapter pagerAdapter;
    private ImageView dot1,dot2,dot3;
    private TextView header, subheader;

    //face boton
    private LoginButton FaceButton;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        this.setContentView(R.layout.login);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.unbegrenzt.fharmaapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

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

        FaceButton = (LoginButton) findViewById(R.id.login_button);
        FaceButton.setReadPermissions("email","public_profile");
        // If using in a fragment
        //FaceButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        FaceButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

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

    }

    @Override
    public void onDestroy(){
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("asdf", String.valueOf(requestCode) + "  " + String.valueOf(resultCode));
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Login.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
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
        FaceButton.performClick();
    }
}


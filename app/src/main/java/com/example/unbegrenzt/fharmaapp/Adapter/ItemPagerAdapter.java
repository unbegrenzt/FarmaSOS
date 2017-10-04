/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 05:40 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 09-25-17 03:28 PM
 */

package com.example.unbegrenzt.fharmaapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.unbegrenzt.fharmaapp.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

public class ItemPagerAdapter extends android.support.v4.view.PagerAdapter {

    private LayoutInflater mLayoutInflater;
    private GoogleApiClient mGoogleApiClient;
    private int count;
    private String placeId;

    public ItemPagerAdapter(Context context, String placeId, GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.placeId = placeId;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        final ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
                = new ResultCallback<PlacePhotoResult>() {
            @Override
            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                if (!placePhotoResult.getStatus().isSuccess()) {
                    return;
                }
                imageView.setImageBitmap(placePhotoResult.getBitmap());
            }
        };

        // Australian Cruise Group
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {


                    @Override
                    public void onResult(@NonNull PlacePhotoMetadataResult photos) {

                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        count = photoMetadataBuffer.getCount();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            for (int i = 0; i < photoMetadataBuffer.getCount(); i++)
                            photoMetadataBuffer.get(i)
                                    .getScaledPhoto(mGoogleApiClient, imageView.getWidth(),
                                            imageView.getHeight())
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }
                        photoMetadataBuffer.release();
                    }
                });

        container.addView(itemView);
        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
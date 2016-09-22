package com.greek303g.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.greek303g.movieapp.data.TrailerResult;
import com.greek303g.movieapp.fragments.TrailersFragment;

import java.util.List;

/**
 * Created by ahmedabobakr on 9/16/16.
 */
public class TrailersAdapter extends android.support.v13.app.FragmentPagerAdapter {

    List<TrailerResult> urls;
    Context context;




    public TrailersAdapter(android.app.FragmentManager fragmentManager, List<TrailerResult> urls, Context context){
        super(fragmentManager);
        this.urls= urls;
        this.context = context;

    }

    @Override
    public android.app.Fragment getItem(int position) {
        TrailersFragment fragment = new TrailersFragment();
        Bundle args = new Bundle();
        args.putInt(Intent.EXTRA_TEXT, position);
        args.putString(Intent.EXTRA_BCC, urls.get(position).getKey());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return urls.size();
    }
}

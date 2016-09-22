package com.greek303g.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.greek303g.movieapp.data.Result;
import com.greek303g.movieapp.fragments.MovieDetailsFragment;

public class MoviesDetialsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detials);

        if(getIntent() != null) {
            Result result = (Result) getIntent().getExtras().get("movie");
            MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
            Bundle args = new Bundle();
            args.putSerializable("movie", result);
            detailsFragment.setArguments(args);

            getFragmentManager().beginTransaction().add(R.id.container, detailsFragment)
                    .commit();
        }
    }
}

package com.greek303g.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.greek303g.movieapp.data.Result;
import com.greek303g.movieapp.fragments.MovieDetailsFragment;
import com.greek303g.movieapp.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity implements MoviesFragment.SelectedMovieListener{

    private final String DETAILS_TAG = "DETAILS_TAG";
    private final String TAG = MainActivity.class.getName();
    public static final  String BASE_URL = "http://api.themoviedb.org/3/movie";
    public static final String API_KEY_PARAM = "api_key";
    private final String Movies_DETAILS_TAG = "movies_tag";
    boolean mTwoPane, setDefaultMovie = false;
    Result selectedMovie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.movies_fragment, new MoviesFragment())
                .commit();

        if(findViewById(R.id.container1) != null){
            mTwoPane = true;

            if(savedInstanceState == null){
                getFragmentManager().beginTransaction()
                        .replace(R.id.container1, new MovieDetailsFragment(), DETAILS_TAG)
                        .commit();
            }
        }else {
            mTwoPane = false;
        }

        if(savedInstanceState == null)
            setDefaultMovie = true;

        if(savedInstanceState == null){
            getFragmentManager().beginTransaction().replace(R.id.movies_fragment, new MoviesFragment())
                    .commit();
        }else {
            MovieDetailsFragment detailsFragment = (MovieDetailsFragment) getFragmentManager().findFragmentByTag(Movies_DETAILS_TAG);
            //if(savedInstanceState.getString("current_fragment") != null) {
                if (detailsFragment == null)
                    getFragmentManager().beginTransaction().replace(R.id.movies_fragment, new MoviesFragment())
                            .commit();
                else {
                    //MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
                    //Bundle args = new Bundle();
                    //args.putSerializable("movie", selectedMovie);
                    //detailsFragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.movies_fragment, detailsFragment, Movies_DETAILS_TAG)
                            .commit();
                }
            //}
        }

        if(findViewById(R.id.container1) != null){
            mTwoPane = true;
            if(savedInstanceState == null){

                    MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
                if(selectedMovie != null) {
                    Bundle args = new Bundle();
                    args.putSerializable("movie", selectedMovie);
                    detailsFragment.setArguments(args);
                }
                    getFragmentManager().beginTransaction().replace(R.id.container1, detailsFragment)
                            .commit();

            }
        }else {
            mTwoPane = false;
        }


    }










    @Override
    public void onSelectedMovie(Result result) {
        Log.i("Test", "Item Clicked");
        MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", result);
        detailsFragment.setArguments(args);
        selectedMovie = result;

        if(!mTwoPane){

            getFragmentManager().beginTransaction().replace(R.id.movies_fragment, detailsFragment, Movies_DETAILS_TAG)
                    .addToBackStack("Movies").commit();
        }else {

            getFragmentManager().beginTransaction().replace(R.id.container1, detailsFragment)
                    .commit();
        }
    }

    @Override
    public void setSelectedMovie(Result movie) {
        selectedMovie = movie;
        if(findViewById(R.id.container1) != null){
            mTwoPane = true;

                if(selectedMovie != null && setDefaultMovie){
                    MovieDetailsFragment detailsFragment = new MovieDetailsFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("movie", selectedMovie);
                    detailsFragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.container1, detailsFragment)
                            .commit();
                }

        }else {
            mTwoPane = false;
        }
    }
}

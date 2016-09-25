package com.greek303g.movieapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.greek303g.movieapp.BuildConfig;
import com.greek303g.movieapp.MainActivity;
import com.greek303g.movieapp.R;
import com.greek303g.movieapp.SettingsActivity;
import com.greek303g.movieapp.adapters.MoviesAdapter;
import com.greek303g.movieapp.async.Downloader;
import com.greek303g.movieapp.data.MoviesData;
import com.greek303g.movieapp.data.ParserJson;
import com.greek303g.movieapp.data.Result;
import com.greek303g.movieapp.database.MoviesContract;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ahmed-abobakr on 10/08/16.
 */
public class MoviesFragment extends Fragment {

    RecyclerView recyclerView;

    private String selectionParam = "/popular";

    MoviesData mData;
    List<Result> lstMovies;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movies_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_moives);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }






    @Override
    public void onStart() {
        super.onStart();
        if(!selectionParam.equals(com.greek303g.movieapp.utils.Utils.getSelectedCriteria(getActivity()))) {
            selectionParam = "/" + com.greek303g.movieapp.utils.Utils.getSelectedCriteria(getActivity());
            updateMovies();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putString("current_fragment", "movies");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(ParserJson parserJson){
        if(parserJson instanceof  MoviesData) {
            mData = (com.greek303g.movieapp.data.MoviesData) parserJson;
            lstMovies = mData.getResults();
            MoviesAdapter adapter = new MoviesAdapter(lstMovies, getActivity(), ((SelectedMovieListener) getActivity()));
            recyclerView.setAdapter(adapter);

            ((SelectedMovieListener) getActivity()).setSelectedMovie(lstMovies.get(0));
        }
    }

    private void updateMovies(){
        if(selectionParam.equals("/" + getString(R.string.favourite_value))){
            Cursor cursor = getActivity().getContentResolver().query(MoviesContract.FavouriteMoviesEntry.CONTENT_URI,
                    new String[] {MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID, MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_POSTER},
                    null,
                    null,
                    null);
            lstMovies = new ArrayList<Result>();
            if(cursor .moveToFirst()){
                do{
                    Result movie = new Result();
                    movie.setId(cursor.getInt(0));
                    movie.setPosterPath(cursor.getString(1));
                    lstMovies.add(movie);
                }while (cursor.moveToNext());

                MoviesAdapter adapter = new MoviesAdapter(lstMovies, getActivity(), ((SelectedMovieListener) getActivity()));
                recyclerView.setAdapter(adapter);

                ((SelectedMovieListener) getActivity()).setSelectedMovie(lstMovies.get(0));
            }
        }else{
            Uri uri = Uri.parse(MainActivity.BASE_URL + selectionParam)
                    .buildUpon().appendQueryParameter(MainActivity.API_KEY_PARAM, BuildConfig.API_KEY).build();
            if (com.greek303g.movieapp.utils.Utils.checkConnectivity(getActivity())) {
                new Downloader(MoviesData.class.getName(), getActivity()).execute(uri.toString());
            } else {
                Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface  SelectedMovieListener {
        void onSelectedMovie(Result result);
        void setSelectedMovie(Result movie);
    }
}

package com.greek303g.movieapp.fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.greek303g.movieapp.BuildConfig;
import com.greek303g.movieapp.MainActivity;
import com.greek303g.movieapp.R;
import com.greek303g.movieapp.adapters.ReviewsAdapter;
import com.greek303g.movieapp.adapters.TrailersAdapter;
import com.greek303g.movieapp.async.Downloader;
import com.greek303g.movieapp.data.MovieReview;
import com.greek303g.movieapp.data.MovieTrailers;
import com.greek303g.movieapp.data.MoviesData;
import com.greek303g.movieapp.data.ParserJson;
import com.greek303g.movieapp.data.Result;
import com.greek303g.movieapp.data.ReviewResult;
import com.greek303g.movieapp.database.MoviesContract;
import com.greek303g.movieapp.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.greenrobot.event.EventBus;

/**
 * Created by ahmed-abobakr on 10/08/16.
 */
public class MovieDetailsFragment extends Fragment {

    Result movie;
    ImageView imgPoster;
    TextView txtTitle, txtRelease, txtOverview;
    TextView ratingMoview;
    RecyclerView recyclerView;
    ViewPager pager;
    ReviewsAdapter adapter;
    List<ReviewResult> mReviewData;
    Button btnFav;

    private MovieReview mData;
    private final String BACK_SLASH = "/";
    private final String REVIEWS = "reviews";
    private final String VIDEOS = "videos";
    private final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movies_details_fragment, container, false);

        imgPoster = (ImageView) view.findViewById(R.id.img_movie_poster);
        txtTitle = (TextView) view.findViewById(R.id.txt_movie_title);
        txtRelease = (TextView) view.findViewById(R.id.txt_release_date);
        txtOverview = (TextView) view.findViewById(R.id.txt_overView);
        ratingMoview = (TextView) view.findViewById(R.id.rating_movie);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_reviews);
        pager = (ViewPager) view.findViewById(R.id.trailers_lst);
        btnFav = (Button) view.findViewById(R.id.fav_btn_movie);

        mReviewData = new ArrayList<ReviewResult>();
        adapter = new ReviewsAdapter(getActivity(), mReviewData);

        LinearLayoutManager ll = new LinearLayoutManager(getActivity());
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ll);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null){
            movie = (Result) getArguments().get("movie");
        }

        if(movie != null){
            Picasso.with(getActivity()).load(IMAGE_URL + movie.getPosterPath())
                    .into(imgPoster);
            if(movie.getTitle() != null) {
                txtTitle.setText(movie.getTitle());
                txtRelease.setText(movie.getReleaseDate());
                txtOverview.setText(movie.getOverview());
                ratingMoview.setText(movie.getVoteAverage().intValue() + "/" + movie.getVoteCount());
            }else {
                Uri uri = Uri.parse(MainActivity.BASE_URL + BACK_SLASH + movie.getId())
                        .buildUpon().appendQueryParameter(MainActivity.API_KEY_PARAM, BuildConfig.API_KEY).build();
                new Downloader(Result.class.getName(), getActivity()).execute(uri.toString());
            }

            if(isMovieFavourite(movie))
                btnFav.setText("UnFavourite Movie");

            if(checkConnectivity())
                getReviews();


            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isMovieFavourite(movie)) {
                        btnFav.setText("Favourite Movie");
                        deleteFavMovieDB(movie);
                    }else{
                        btnFav.setText("UnFavourite Movie");
                        insertMovieDB(movie);
                    }

                }
            });
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

            outState.putString("current_fragment", "moviesDetails");
            outState.putSerializable("movie", movie);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.revies_item:
                if(checkConnectivity())
                    getReviews();
                return true;
            case R.id.trailers_item:
                if(checkConnectivity())
                    getTrailers();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(ParserJson json){
        if(json instanceof MovieReview) {
            recyclerView.setVisibility(View.VISIBLE);
            pager.setVisibility(View.GONE);
            mData = (MovieReview) json;
            mReviewData.clear();
            mReviewData.addAll(mData.getResults());

            adapter.notifyDataSetChanged();


        }else if(json instanceof MovieTrailers){
            pager.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            List trailersList = ((MovieTrailers) json).getResults();
            TrailersAdapter adapterTrailers = new TrailersAdapter(getActivity().getFragmentManager(), trailersList, getActivity());
            pager.setAdapter(adapterTrailers);
        }else  if(json instanceof Result){
            movie = (Result) json;
            txtTitle.setText(movie.getTitle());
            txtRelease.setText(movie.getReleaseDate());
            txtOverview.setText(movie.getOverview());
            ratingMoview.setText(movie.getVoteAverage().intValue() + "/" + movie.getVoteCount());
        }
    }

    private ContentValues insertMovieDB(Result movie){
        ContentValues values = new ContentValues();
        values.put(MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID, String.valueOf(movie.getId().intValue()));
        values.put(MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_POSTER, movie.getPosterPath());
        Vector<ContentValues> cVValues = new Vector<ContentValues>(1);
        cVValues.add(values);
        if(cVValues.size() > 0){
            ContentValues[] cvArray = new ContentValues[cVValues.size()];
            cVValues.toArray(cvArray);
            getActivity().getContentResolver().bulkInsert(MoviesContract.FavouriteMoviesEntry.CONTENT_URI, cvArray);
        }


        return values;
    }

    private void deleteFavMovieDB(Result movie){
         int rows = getActivity().getContentResolver().delete(MoviesContract.FavouriteMoviesEntry.CONTENT_URI, MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID + " = " + movie.getId().intValue() , null);
        Log.i("Provider", rows  + "");
    }

    private boolean isMovieFavourite(Result movie){

        Cursor cur = getActivity().getContentResolver().query(MoviesContract.FavouriteMoviesEntry.CONTENT_URI, new String[] {MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID},
                MoviesContract.FavouriteMoviesEntry.COLUMN_Favourite_Moies_ID + " = " + movie.getId(), null, null);
        Log.i("Provider", "isFav " + cur.getCount());
        if(cur.moveToFirst())
            return true;
        else
            return false;
    }

    private void getReviews(){
        if(movie != null) {
            Uri uri = Uri.parse(MainActivity.BASE_URL + BACK_SLASH + movie.getId() + BACK_SLASH + REVIEWS)
                    .buildUpon().appendQueryParameter(MainActivity.API_KEY_PARAM, BuildConfig.API_KEY).build();
            new Downloader(MovieReview.class.getName(), getActivity()).execute(uri.toString());
        }
    }

    private void getTrailers(){
        if(movie != null){
            Uri uriTrailers = Uri.parse(MainActivity.BASE_URL + BACK_SLASH + movie.getId() + BACK_SLASH + VIDEOS)
                    .buildUpon().appendQueryParameter(MainActivity.API_KEY_PARAM, BuildConfig.API_KEY).build();
            new Downloader(MovieTrailers.class.getName(), getActivity()).execute(uriTrailers.toString());
        }
    }

    private boolean checkConnectivity(){
        if(Utils.checkConnectivity(getActivity())) {
            return true;
        }else {
            Toast.makeText(getActivity(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}

package com.greek303g.movieapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.greek303g.movieapp.R;
import com.greek303g.movieapp.data.Result;
import com.greek303g.movieapp.fragments.MoviesFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmed-abobakr on 03/08/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    List<Result> moviesList;
    Context context;
    MoviesFragment.SelectedMovieListener movieListener = null;
    private final String BASE_URL = "http://image.tmdb.org/t/p/w500/";

    public MoviesAdapter(List<Result> moviesList, Context context, MoviesFragment.SelectedMovieListener movieListener){
        this.moviesList = moviesList;
        this.context = context;
        this.movieListener = movieListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = moviesList.get(position);
        Picasso.with(context).load(BASE_URL + result.getPosterPath()).into(holder.imgPoster);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgPoster;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movieListener != null)
                        movieListener.onSelectedMovie(moviesList.get(getAdapterPosition()));
                }
            });
        }
    }
}

package com.greek303g.movieapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greek303g.movieapp.R;
import com.greek303g.movieapp.data.ReviewResult;

import java.util.List;

/**
 * Created by ahmedabobakr on 9/16/16.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    Context context;
    List<ReviewResult> lst;

    public ReviewsAdapter(Context context, List<ReviewResult> lst){
        this.context = context;
        this.lst = lst;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_adapter, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewResult result = lst.get(position);
        holder.txtAuthor.setText(result.getAuthor());
        holder.txtContent.setText(result.getContent());
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtAuthor, txtContent;

        public ViewHolder(View v){
            super(v);
            txtAuthor = (TextView) v.findViewById(R.id.review_author);
            txtContent = (TextView) v.findViewById(R.id.review_content);
        }
    }
}

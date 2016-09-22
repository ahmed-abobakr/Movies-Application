package com.greek303g.movieapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greek303g.movieapp.R;

/**
 * Created by ahmedabobakr on 9/16/16.
 */
public class TrailersFragment extends Fragment {


    TextView trailerView;
    private String key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trailers_fragment, container, false);

        trailerView = (TextView) rootView.findViewById(R.id.trailers_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null){
            trailerView.setText(getString(R.string.trailer) + getArguments().getInt(Intent.EXTRA_TEXT));
            key = getArguments().getString(Intent.EXTRA_BCC);
            trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "vnd.youtube:" + key;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                        getActivity().startActivity(intent);
                    }else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
                    }
                }
            });
        }

    }
}

package com.codepolitan.s4cataoguemovie.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.model.Movie;
import com.codepolitan.s4cataoguemovie.ui.detail.DetailMovieActivity;
import com.codepolitan.s4cataoguemovie.utils.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.CONTENT_URI;

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.SearchMovieHolder> {
    Context context;
    List<Movie> movies = new ArrayList<>();

    public SearchMovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchMovieHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchMovieHolder holder, int position) {
        final Movie movie = movies.get(position);
        if (!String.valueOf(movie.getPoster_path()).isEmpty() || movie.getPoster_path() != null) {
            Glide.with(context).load(ClientConfig.BACKDROP_URL + movie.getPoster_path()).into(holder.iv_poster);
        }else {
            Glide.with(context).load("http://ppid.lapan.go.id/uploads/images/noimage.png").into(holder.iv_poster);
        }
        holder.movie_name.setText(movie.getTitle());
        if (String.valueOf(movie.getOverview()).isEmpty() || movie.getPoster_path() != null) {
            holder.movie_desc.setText("-");
        }else {
            holder.movie_desc.setText(movie.getOverview());
        }
        try {
            if (!String.valueOf(movie.getRelease_date()).isEmpty() || movie.getRelease_date() != null) {
                Log.i(SearchMovieAdapter.class.getSimpleName(), "Release date : " + movie.getRelease_date());
                holder.movie_date.setText(Util.parseDate(movie.getRelease_date()));
            }else {
                holder.movie_date.setText("-");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
                intent.setData(uri);
                intent.putExtra("id", String.valueOf(movie.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class SearchMovieHolder extends RecyclerView.ViewHolder {
        ImageView iv_poster;
        TextView movie_name, movie_desc, movie_date;

        public SearchMovieHolder(View itemView) {
            super(itemView);
            iv_poster = itemView.findViewById(R.id.iv_poster);
            movie_name = itemView.findViewById(R.id.movie_name);
            movie_desc = itemView.findViewById(R.id.movie_desc);
            movie_date = itemView.findViewById(R.id.movie_date);
        }
    }

    public void replace_data(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
}

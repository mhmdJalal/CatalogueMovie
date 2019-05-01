package com.codepolitan.s4cataoguemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movies = new ArrayList<>();

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_unp_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        if (!String.valueOf(movie.getPoster_path()).isEmpty() || movie.getPoster_path() != null) {
            Glide.with(context).load(ClientConfig.BACKDROP_URL + movie.getPoster_path()).into(holder.imageView);
        }else {
            Glide.with(context).load("http://ppid.lapan.go.id/uploads/images/noimage.png").into(holder.imageView);
        }
        if (!String.valueOf(movie.getTitle()).isEmpty() || movie.getTitle() != null) {
            holder.title.setText(movie.getTitle());
        }else {
            holder.title.setText("-");
        }
        if (!String.valueOf(movie.getOverview()).isEmpty() || movie.getOverview() != null) {
            holder.overview.setText(movie.getOverview());
        }else {
            holder.overview.setText("-");
        }
        try {
            holder.date.setText(Util.parseDate(movie.getRelease_date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
                intent.setData(uri);
                intent.putExtra("id", String.valueOf(movie.getId()));
                context.startActivity(intent);
            }
        });
        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "";
                String desc = "";
                String releaseDate = "";

                if (movie.getTitle() != null){
                    title = "*"+movie.getTitle() + "* \n";
                }
                if (movie.getRelease_date() != null){
                    try {
                        releaseDate = "Release Date : " + Util.parseDate(movie.getRelease_date()) + "\n";
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (movie.getOverview() != null){
                    desc = "\n\nOverview : \n" + movie.getOverview();
                }
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, title + releaseDate + desc);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                context.startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, overview, date;
        Button btn_detail, btn_share;

        ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.iv_poster);
            title = v.findViewById(R.id.title);
            overview = v.findViewById(R.id.overview);
            date = v.findViewById(R.id.date);
            btn_detail = v.findViewById(R.id.btn_detail);
            btn_share = v.findViewById(R.id.btn_share);
        }
    }

    public void replace_date(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
}

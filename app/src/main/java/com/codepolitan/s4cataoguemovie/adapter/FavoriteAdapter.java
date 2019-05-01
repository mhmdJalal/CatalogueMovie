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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context context;
    private List<Movie> movies = new ArrayList<>();
    private Cursor cursor;

    public FavoriteAdapter() {
    }

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_unp_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        Glide.with(context).load(ClientConfig.BACKDROP_URL + movie.getPoster_path()).into(holder.imageView);
        holder.title.setText(movie.getTitle());
        holder.overview.setText(movie.getOverview());
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
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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

    public void  setListFavorite(Cursor cursor) {
        this.cursor = cursor;

        movies.clear();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(0));
                    String title = cursor.getString(1);
                    String description = cursor.getString(2);
                    String poster = cursor.getString(3);
                    String release = cursor.getString(4);

                    movies.add(new Movie(id, title, description, poster, release));
                }while (cursor.moveToNext());
            }
        }

        notifyDataSetChanged();
    }
}

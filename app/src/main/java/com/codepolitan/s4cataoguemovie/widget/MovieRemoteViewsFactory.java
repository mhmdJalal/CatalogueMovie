package com.codepolitan.s4cataoguemovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.model.Movie;
import com.codepolitan.s4cataoguemovie.service.UpcomingNPJobService;
import com.codepolitan.s4cataoguemovie.service.UpcomingService;
import com.codepolitan.s4cataoguemovie.utils.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int widgetId;
    private List<Movie> movies = new ArrayList<>();

    public MovieRemoteViewsFactory() { }

    public MovieRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "onCreate()");
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            movies = UpcomingNPJobService.getList();
            Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "Job_scheduler, ondatesetchanged");
        }else {
            movies = UpcomingService.getList();
            Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "Gcm, ondatesetchanged");
        }

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "onDestroy()");
        if (movies != null) {
            movies.clear();
        }
    }

    @Override
    public int getCount() {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "getCount()");
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "getCount() " + movies.size());
        return movies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "getViewAt()");
        Movie movie = movies.get(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.movie_widget_item);

        Bitmap bmp = null;
        try {
            Log.i("REMOTE", "image " + movie.getBackdrop_path());
            Log.i("REMOTE", "title " + movie.getTitle());
            Log.i("REMOTE", "releaseDate " + Util.parseDate(movie.getRelease_date()));

            /* set movie banner into widget view banner */
            if (movie.getBackdrop_path().isEmpty() || movie.getBackdrop_path() != null){
                bmp = Glide.with(context)
                        .asBitmap()
                        .load(ClientConfig.BACKDROP_URL + movie.getBackdrop_path())
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            }else {
                bmp = Glide.with(context)
                        .asBitmap()
                        .load("http://ppid.lapan.go.id/uploads/images/noimage.png")
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            }

            rv.setImageViewBitmap(R.id.img_widget, bmp);

            /* set movie title into widget view title */
            if (!movie.getTitle().isEmpty() || movie.getTitle() != null) {
                rv.setTextViewText(R.id.tv_movie_title, movie.getTitle());
            }else {
                rv.setTextViewText(R.id.tv_movie_title, "Catalogue Movie");
            }

            /* set movie release date into widget view release date */
            if (!movie.getRelease_date().isEmpty() || movie.getRelease_date() != null) {
                rv.setTextViewText(R.id.release_date, "Release date : " + Util.parseDate(movie.getRelease_date()));
            }else {
                rv.setTextViewText(R.id.release_date, "Release date : -");
            }

        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        extras.putInt(MovieWidget.EXTRA_ITEM, movie.getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.tv_movie_title, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "getLoadingView()");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "getViewTypeCount()");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "getItemId() : " + String.valueOf(movies.get(position).getId()));
        return movies.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        Log.i(MovieRemoteViewsFactory.class.getSimpleName(), "hasStableIds()");
        return false;
    }

    public void setList(List<Movie> movies) {
        this.movies = movies;
    }
}

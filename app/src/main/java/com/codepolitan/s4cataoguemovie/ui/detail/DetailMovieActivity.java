package com.codepolitan.s4cataoguemovie.ui.detail;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.helper.PrefManager;
import com.codepolitan.s4cataoguemovie.database.FavoriteHelper;
import com.codepolitan.s4cataoguemovie.model.Movie;
import com.codepolitan.s4cataoguemovie.response.MovieDetailResponse;
import com.codepolitan.s4cataoguemovie.utils.Util;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.CONTENT_URI;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_DESCRIPTION;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_ID;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_POSTER;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_RELEASE_DATE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_TITLE;

public class DetailMovieActivity extends AppCompatActivity implements DetailView {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_backdrop)
    ImageView iv_backdrop;
    @BindView(R.id.iv_poster)
    ImageView iv_poster;
    @BindView(R.id.movie_name)
    TextView tv_movieName;
    @BindView(R.id.movie_date)
    TextView tv_movieDate;
    @BindView(R.id.movie_desc)
    TextView tv_movieDesc;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    DetailMoviePresenter presenter;
    MovieDetailResponse movieDetailResponse = new MovieDetailResponse();
    Movie movie;
    String id, release_date;
    PrefManager prefManager;
    FavoriteHelper favoriteHelper;
    private boolean isFavorite = false;
    Menu menuItem = null;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Detail Movie");

        favoriteHelper = new FavoriteHelper(this);
        prefManager = new PrefManager(this);
        presenter = new DetailMoviePresenter<>(this);

        favoriteHelper.open();
        id = getIntent().getStringExtra("id");
        presenter.loadDetailMovie(id, prefManager.getLANGUAGE());

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
            }
        }

        favoriteState();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDetailMovie(MovieDetailResponse movieDetailResponse) {
        this.movieDetailResponse = movieDetailResponse;
        tv_movieName.setText(movieDetailResponse.getTitle());

        if (!String.valueOf(movieDetailResponse.getBackdrop_path()).isEmpty()){
            Glide.with(this).load(ClientConfig.BACKDROP_URL + movieDetailResponse.getBackdrop_path()).into(iv_backdrop);
        }else {
            Glide.with(this).load("http://ppid.lapan.go.id/uploads/images/noimage.png").into(iv_backdrop);
        }

        if (!String.valueOf(movieDetailResponse.getPoster_path()).isEmpty()){
            Glide.with(this).load(ClientConfig.IMAGE_URL + movieDetailResponse.getPoster_path()).into(iv_poster);
        }else {
            Glide.with(this).load("http://ppid.lapan.go.id/uploads/images/noimage.png").into(iv_poster);
        }

        if (!String.valueOf(movieDetailResponse.getOverview()).isEmpty()) {
            tv_movieDesc.setText(movieDetailResponse.getOverview());
        }else {
            tv_movieDesc.setText("-");
        }

        try {
            release_date = Util.parseDate(movieDetailResponse.getRelease_date());
            tv_movieDate.setText(release_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void favoriteState() {
        Cursor cursor = getContentResolver().query(uri, null, FIELD_ID + " = ?", new String[]{id}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = !isFavorite;
            cursor.close();
        }
    }

    private void setMenuFavorite() {
        if (isFavorite) {
            menuItem.getItem(0).setIcon(R.drawable.ic_favorite_filled);
        }else {
            menuItem.getItem(0).setIcon(R.drawable.ic_favorite);
        }
    }

    private void removeFavorite() {
        getContentResolver().delete(uri, null, null);
        Snackbar snackbar = Snackbar.make(scrollView, R.string.remove_favorite, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void addFavorite() {
        ContentValues values = new ContentValues();

        values.put(FIELD_ID, movieDetailResponse.getId());
        values.put(FIELD_TITLE, movieDetailResponse.getTitle());
        values.put(FIELD_DESCRIPTION, movieDetailResponse.getOverview());
        values.put(FIELD_POSTER, movieDetailResponse.getPoster_path());
        values.put(FIELD_RELEASE_DATE, movieDetailResponse.getRelease_date());

        getContentResolver().insert(CONTENT_URI, values);

        Snackbar snackbar = Snackbar.make(scrollView, R.string.add_favorite, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        menuItem = menu;
        setMenuFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.add_to_favorite:
                if (isFavorite) {
                    removeFavorite();
                }else {
                    addFavorite();
                }
                isFavorite = !isFavorite;
                setMenuFavorite();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        favoriteHelper.close();
        super.onStop();
    }
}

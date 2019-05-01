package com.codepolitan.s4cataoguemovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_DESCRIPTION;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_ID;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_POSTER;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_RELEASE_DATE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.FIELD_TITLE;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.getColumnInt;
import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable{

    private int vote_count;
    private int id;
    private boolean video;
    private double vote_average;
    private String title;
    private String popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private List<Integer> genre_ids;

    public Movie() {
    }

    public Movie(int id, String title, String overview, String poster_path, String release_date) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public Movie(int vote_count, int id, double vote_average, String title, String popularity, String poster_path, String backdrop_path, String overview, String release_date) {
        this.vote_count = vote_count;
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
        dest.writeString(this.poster_path);
    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, FIELD_ID);
        this.title = getColumnString(cursor, FIELD_TITLE);
        this.overview = getColumnString(cursor, FIELD_DESCRIPTION);
        this.poster_path = getColumnString(cursor, FIELD_POSTER);
        this.release_date = getColumnString(cursor, FIELD_RELEASE_DATE);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
        this.release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}


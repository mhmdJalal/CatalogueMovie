package com.codepolitan.s4cataoguemovie.response;

import com.google.gson.annotations.SerializedName;

public class MovieDetailResponse {

    /**
     * adult : false
     * backdrop_path : /VuukZLgaCrho2Ar8Scl9HtV3yD.jpg
     * belongs_to_collection : null
     * budget : 116000000
     * genres : [{"id":27,"name":"Horror"},{"id":878,"name":"Science Fiction"},{"id":28,"name":"Action"},{"id":53,"name":"Thriller"},{"id":35,"name":"Comedy"}]
     * homepage : http://www.venom.movie/site/
     * id : 335983
     * imdb_id : tt1270797
     * original_language : en
     * original_title : Venom
     * overview : When Eddie Brock acquires the powers of a symbiote, he will have to release his alter-ego “Venom” to save his life.
     * popularity : 619.372
     * poster_path : /2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg
     * production_companies : [{"id":5,"logo_path":"/71BqEFAF4V3qjjMPCpLuyJFB9A.png","name":"Columbia Pictures","origin_country":"US"},{"id":7505,"logo_path":"/837VMM4wOkODc1idNxGT0KQJlej.png","name":"Marvel Entertainment","origin_country":"US"},{"id":34,"logo_path":"/GagSvqWlyPdkFHMfQ3pNq6ix9P.png","name":"Sony Pictures","origin_country":"US"}]
     * production_countries : [{"iso_3166_1":"US","name":"United States of America"}]
     * release_date : 2018-10-03
     * revenue : 205230000
     * runtime : 112
     * spoken_languages : [{"iso_639_1":"en","name":"English"}]
     * status : Released
     * tagline : The world has enough Superheroes.
     * title : Venom
     * video : false
     * vote_average : 6.7
     * vote_count : 1019
     */

    @SerializedName("adult")
    private boolean adult;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("id")
    private int id;
    @SerializedName("overview")
    private String overview;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("revenue")
    private int revenue;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("status")
    private String status;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private double vote_average;
    @SerializedName("vote_count")
    private int vote_count;

    public MovieDetailResponse() { }

    public MovieDetailResponse(boolean adult, String backdrop_path, String homepage, int id, String overview, double popularity, String poster_path, String release_date, int revenue, int runtime, String status, String tagline, String title, double vote_average, int vote_count) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.homepage = homepage;
        this.id = id;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.revenue = revenue;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }
}

package com.codepolitan.s4cataoguemovie.API;

import com.codepolitan.s4cataoguemovie.response.MovieDetailResponse;
import com.codepolitan.s4cataoguemovie.response.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search/movie")
    Call<MovieResponse> searchMovie(@Query("api_key") String api_key,
                                    @Query("language") String language,
                                    @Query("query") String query);

    @GET("movie/{id}")
    Call<MovieDetailResponse> getDetailMovie(@Path("id") String id,
                                             @Query("api_key") String api_key,
                                             @Query("language") String language);

    @GET("movie/upcoming")
    Call<MovieResponse> upcomingMovie(@Query("api_key") String api,
                                      @Query("language") String language);

    @GET("movie/now_playing")
    Call<MovieResponse> nowplayingMovie(@Query("api_key") String api,
                                        @Query("language") String language);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovie(@Query("api_key") String api,
                                        @Query("language") String language);

    @GET("trending/movie/week")
    Call<MovieResponse> getTrendingMovie(@Query("api_key") String api,
                                        @Query("language") String language);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopratedMovie(@Query("api_key") String api,
                                        @Query("language") String language);

}

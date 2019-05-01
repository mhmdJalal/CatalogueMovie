package com.codepolitan.s4cataoguemovie.ui.searchFragment;

import android.util.Log;

import com.codepolitan.s4cataoguemovie.base.BasePresenter;
import com.codepolitan.s4cataoguemovie.response.MovieResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMoviePresenter<Views extends SearchMovieView> extends BasePresenter {

    private Views movie;

    public SearchMoviePresenter(Views movie) {
        this.movie = movie;
    }

    public void loadSearchMovie(String api_key, String language, String query){
        movie.showLoading();
        apiInterface.searchMovie(api_key, language, query).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        movie.showListMovie(response.body().getResults());
                        movie.hideLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("ERROR_SEARCH_RESPONSE", t.getMessage());
                movie.hideLoading();
            }
        });
    }
}

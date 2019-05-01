package com.codepolitan.s4cataoguemovie.ui.detail;

import android.support.annotation.NonNull;
import android.util.Log;


import com.codepolitan.s4cataoguemovie.base.BasePresenter;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.response.MovieDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMoviePresenter<View extends DetailView> extends BasePresenter {

    private View detailView;

    public DetailMoviePresenter(View detailView) {
        this.detailView = detailView;
    }

    public void loadDetailMovie(String id, String lang){
        detailView.showLoading();
        apiInterface.getDetailMovie(id, ClientConfig.API_KEY, lang)
                .enqueue(new Callback<MovieDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieDetailResponse> call, @NonNull Response<MovieDetailResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null){
                                detailView.showDetailMovie(response.body());
                                detailView.hideLoading();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieDetailResponse> call, @NonNull Throwable t) {
                        Log.e("MOVIE_DETAIL_RESPONSE", t.getMessage());
                        detailView.hideLoading();
                    }
                });
    }
}

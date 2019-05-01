package com.codepolitan.s4cataoguemovie.ui.detail;


import com.codepolitan.s4cataoguemovie.response.MovieDetailResponse;

public interface DetailView {
    void showLoading();
    void hideLoading();
    void showDetailMovie(MovieDetailResponse movieDetailResponse);
}

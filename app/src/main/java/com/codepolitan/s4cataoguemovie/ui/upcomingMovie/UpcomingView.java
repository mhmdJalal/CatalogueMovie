package com.codepolitan.s4cataoguemovie.ui.upcomingMovie;

import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.List;

public interface UpcomingView {
    void showLoading();
    void hideLoading();
    void showUpcomingList(List<Movie> movies);
}

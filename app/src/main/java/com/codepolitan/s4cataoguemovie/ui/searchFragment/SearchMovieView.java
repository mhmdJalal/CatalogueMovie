package com.codepolitan.s4cataoguemovie.ui.searchFragment;



import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.List;

public interface SearchMovieView {
    void showLoading();
    void hideLoading();
    void showListMovie(List<Movie> movies);
}

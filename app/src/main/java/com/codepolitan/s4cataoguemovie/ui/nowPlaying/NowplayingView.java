package com.codepolitan.s4cataoguemovie.ui.nowPlaying;


import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.List;

public interface NowplayingView {
    void showLoading();
    void hideLoading();
    void showNowplayingList(List<Movie> movies);
}

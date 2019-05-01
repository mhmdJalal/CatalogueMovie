package com.codepolitan.s4cataoguemovie.ui.nowPlaying;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.codepolitan.s4cataoguemovie.base.BasePresenter;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.model.M_Date;
import com.codepolitan.s4cataoguemovie.model.Movie;
import com.codepolitan.s4cataoguemovie.response.MovieResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NowplayingPresenter<Views extends NowplayingView> extends BasePresenter {

    private Views nowPlaying;

    @SuppressLint("SimpleDateFormat")
    private
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    NowplayingPresenter(Views nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public void loadNowPlaying(String language){
        nowPlaying.showLoading();
        apiInterface.nowplayingMovie(ClientConfig.API_KEY, language)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        for (int i = 0; i < response.body().getResults().size(); i++){
                            M_Date date = response.body().getDates();
                            Movie movie = response.body().getResults().get(i);

                            try {
                                Date min = df.parse(date.getMinimum());
                                Date max = df.parse(date.getMaximum());
                                Date release = df.parse(movie.getRelease_date());
                                if (release.after(min) && release.before(max)) {
                                    nowPlaying.showNowplayingList(response.body().getResults());
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        nowPlaying.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d("NOW_PLAYING_PRESENTER", t.getMessage());
                        nowPlaying.hideLoading();
                    }
                });
    }
}

package com.codepolitan.s4cataoguemovie.ui.upcomingMovie;

import android.annotation.SuppressLint;
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

public class UpcomingPresenter<Views extends UpcomingView> extends BasePresenter {

    private Views upcoming;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public UpcomingPresenter(Views upcoming) {
        this.upcoming = upcoming;
    }

    public void loadUpcoming(String lang){
        upcoming.showLoading();
        apiInterface.upcomingMovie(ClientConfig.API_KEY, lang)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                for (int i = 0; i < response.body().getResults().size(); i++){
                                    M_Date date = response.body().getDates();
                                    Movie movie = response.body().getResults().get(i);

                                    try {
                                        Date min = df.parse(date.getMinimum());
                                        Date max = df.parse(date.getMaximum());
                                        Date release = df.parse(movie.getRelease_date());
                                        if (release.after(min) && release.before(max)) {
                                            upcoming.showUpcomingList(response.body().getResults());
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                upcoming.hideLoading();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Log.d("NOW_PLAYING_PRESENTER", t.getMessage());
                        upcoming.hideLoading();
                    }
                });
    }
}

package com.codepolitan.s4cataoguemovie.base;

import android.content.Context;

import com.codepolitan.s4cataoguemovie.API.ApiClient;
import com.codepolitan.s4cataoguemovie.API.ApiInterface;

import retrofit2.Retrofit;

public class BasePresenter {

    Retrofit retrofit;

    public ApiInterface apiInterface = new ApiClient().getRetrofit().create(ApiInterface.class);
    public Context context;
}

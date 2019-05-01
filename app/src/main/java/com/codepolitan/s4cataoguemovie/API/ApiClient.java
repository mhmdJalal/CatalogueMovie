package com.codepolitan.s4cataoguemovie.API;


import com.codepolitan.s4cataoguemovie.config.ClientConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Retrofit retrofit;

    private static final String BASE_URL = ClientConfig.BASE_URL;

    public Retrofit getRetrofit() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

package com.yosri.mustafa.eng.bakingapp.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BakingClient {
    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

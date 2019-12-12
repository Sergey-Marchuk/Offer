package com.hfad.offer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService sInstance;
    public Retrofit retrofit;

    //Here will be base URL for Asure service
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private NetworkService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static NetworkService getInstance() {
        if ((sInstance == null)) sInstance = new NetworkService();
        return sInstance;
    }

}

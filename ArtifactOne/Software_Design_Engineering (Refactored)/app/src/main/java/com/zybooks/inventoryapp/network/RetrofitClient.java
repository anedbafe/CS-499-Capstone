package com.zybooks.inventoryapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // BASE URL FOR API (USE 10.0.2.2 FOR LOCALHOST IN ANDROID EMULATOR)
    private static final String BASE_URL = "http://10.0.2.2:5000";

    // SINGLETON RETROFIT INSTANCE
    private static Retrofit retrofit;

    // RETURNS API SERVICE INSTANCE
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}

package dev.spinner_tech.admin_chat.services;

import android.content.Context;


import dev.spinner_tech.admin_chat.api.OneShopApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class ServiceGenerator {
    private static final String BASE_URL = "https://oneshopbd.store/one_shop_admin/app/api/" ;
    private static ServiceGenerator mInstance;
    private Retrofit retrofit;
    private Context context ;

    private ServiceGenerator() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ServiceGenerator getInstance() {
        if (mInstance == null) {
            mInstance = new ServiceGenerator();
        }
        return mInstance;
    }

    public OneShopApi getApi() {
        return retrofit.create(OneShopApi.class);
    }
}

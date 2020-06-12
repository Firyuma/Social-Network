package com.example.socialnetwork.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
        private Retrofit retrofit;
        private static RetrofitUtils instance;
        private RetrofitUtils(){
retrofit = new Retrofit.Builder()
        .baseUrl(API.ROOT)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

        }
        public static RetrofitUtils getInstance(){
                if(instance == null){
                        instance = new RetrofitUtils();
                }
                return instance;
        }

        public RetrofitService createService(){
                RetrofitService service = retrofit.create(RetrofitService.class);
                return service;
        }
        public <abc> abc createService(Class<abc> service) {
                return retrofit.create(service);
        }
}

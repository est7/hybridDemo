package com.est7.hybriddemo.net;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/8.
 */

public class Api {

    private static ApiService sApiService;
    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10000;

    public static ApiService getDefautApiService() {

        if (sApiService == null) {
            //手动创建一个OkHttpClient并设置超时时间
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            /**
             * 对所有请求添加请求头
             */
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    okhttp3.Response originalResponse = chain.proceed(request);
                    //可以对所有的请求头加参数
                    //return originalResponse.newBuilder().header("key1", "value1").addHeader("key2", "value2").build();
                    return originalResponse.newBuilder().build();
                }
            });
            sApiService = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(UrlDefine.BASE_URL)
                    .build().create(ApiService.class);
        }
        return sApiService;
    }
}

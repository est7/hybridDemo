package com.est7.hybriddemo.bean;

import android.util.Log;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2017/3/8.
 */

public class HttpResultFunc<T> implements Function<HttpResult<T>,T> {


    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {
        Log.e("error", tHttpResult.getData().toString() + "");
        if (tHttpResult.getCode() != 0) {
            throw new ApiException(tHttpResult.getCode());
        }
        return tHttpResult.getData();
    }
}

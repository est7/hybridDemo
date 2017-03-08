package com.est7.hybriddemo.net;

import com.est7.hybriddemo.bean.HttpResult;
import com.est7.hybriddemo.bean.Subject;
import com.est7.hybriddemo.bean.UserBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/3/8.
 */

public interface  ApiService {
    @GET("/student/mobileRegister")
    Observable<HttpResult<UserBean>> login(@Query("phone") String phone, @Query("password") String psw);


    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Observable<HttpResult<Subject>> getUser( @Query("touken") String touken);

}

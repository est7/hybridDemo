package com.est7.hybriddemo.mvp;



public interface  BasePresenter <V extends BaseView>{
    void attachView(V view);

    void detachView();
}

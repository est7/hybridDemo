package com.est7.hybriddemo.mvp;


public interface BasePresenter<V extends BaseView> {
    void attachModelView(V pView);

    void detachView();

    boolean isAttach();

    V getView();

}

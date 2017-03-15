package com.est7.hybriddemo.mvp;

import android.content.Context;


public interface BaseView {
    Context getContext();

    void showLoading(String msg);

    void hideLoading();

    void showError(String errorMsg);

}

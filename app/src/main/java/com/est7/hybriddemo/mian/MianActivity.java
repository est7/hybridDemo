package com.est7.hybriddemo.mian;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.widget.TextView;

import com.est7.hybriddemo.R;
import com.est7.hybriddemo.mvp.MVPBaseActivity;

public class MianActivity extends MVPBaseActivity<MianContract.View, MianPresenter> implements MianContract.View {

    private BottomNavigationView mNavigation;
    private TextView mTextMessage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTextMessage = (TextView) findViewById(R.id.message);
        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);

    }

    @Override
    protected void initData() {
        mPresenter.loadUserAccontInfo();
    }


    @Override
    public void showLoading(String msg) {
        //empty
    }

    @Override
    public void hideLoading() {
        //empty
    }

    @Override
    public void showError(String errorMsg) {
        //empty
    }


}

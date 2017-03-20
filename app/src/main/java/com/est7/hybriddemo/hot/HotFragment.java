package com.est7.hybriddemo.hot;


import com.est7.hybriddemo.mvp.MVPBaseFragment;


public class HotFragment extends MVPBaseFragment<HotContract.View, HotPresenter> implements HotContract.View {



    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {


    }
}

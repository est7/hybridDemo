package com.est7.hybriddemo.hot;

import com.est7.hybriddemo.mvp.BasePresenterImpl;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class HotPresenter extends BasePresenterImpl<HotContract.View> implements HotContract.Presenter{

    private HotContract.View mView;

    @Override
    public void attachView(HotContract.View view) {
        super.attachView(view);
        mView = view;
    }




}

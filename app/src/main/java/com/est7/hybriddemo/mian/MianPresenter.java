package com.est7.hybriddemo.mian;

import com.est7.hybriddemo.mvp.BasePresenterImpl;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class MianPresenter extends BasePresenterImpl<MianContract.View> implements MianContract.Presenter{

    private MianContract.View mView;

    @Override
    public void attachView(MianContract.View view) {
        super.attachView(view);
        mView = view;
    }

    @Override
    public void loadUserAccontInfo() {


    }


}

package com.est7.hybriddemo.mian;

import com.est7.hybriddemo.mvp.BasePresenter;
import com.est7.hybriddemo.mvp.BaseView;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class MianContract {
    interface View extends BaseView {
        //void loginSuccess();

        //void loginFailed();

    }

    interface Presenter extends BasePresenter<View> {

        void loadUserAccontInfo();

    }
}

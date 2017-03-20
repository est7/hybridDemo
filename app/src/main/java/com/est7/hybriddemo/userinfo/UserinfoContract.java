package com.est7.hybriddemo.userinfo;

import com.est7.hybriddemo.bean.UserBean;
import com.est7.hybriddemo.mvp.BaseModel;
import com.est7.hybriddemo.mvp.BasePresenter;
import com.est7.hybriddemo.mvp.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserinfoContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {

    }

    interface UserInfoModel<S, C> extends BaseModel<S, C> {
        Observable<List<UserBean>> getUsers(int vsId, boolean update);

    }

    interface LoginModel {

    }

}

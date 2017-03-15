package com.est7.hybriddemo.attention;

import android.content.Context;

import com.est7.hybriddemo.mvp.BasePresenter;
import com.est7.hybriddemo.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class AttentionContract {
    interface View extends BaseView {
        
    }

    interface  Presenter extends BasePresenter<View> {
        
    }
}

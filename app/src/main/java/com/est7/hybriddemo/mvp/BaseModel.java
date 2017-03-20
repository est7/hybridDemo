package com.est7.hybriddemo.mvp;

import com.est7.hybriddemo.net.BaseCacheManager;
import com.est7.hybriddemo.net.BaseServiceManager;

/**
 * Created by Administrator on 2017/3/15.
 */

public class BaseModel<S extends BaseServiceManager, C extends BaseCacheManager> implements IModel{
    protected S mServiceManager;//服务管理类,用于网络请求
    protected C mCacheManager;//缓存管理类,用于管理本地或者内存缓存

    public BaseModel(S serviceManager, C cacheManager) {
        this.mServiceManager = serviceManager;
        this.mCacheManager = cacheManager;
    }

    @Override
    public void onDestroy() {
        if (mServiceManager != null) {
            mServiceManager = null;
        }
        if (mCacheManager != null) {
            mCacheManager = null;
        }
    }
}

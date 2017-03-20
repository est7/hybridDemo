package com.est7.hybriddemo.userinfo;

import com.est7.hybriddemo.mvp.BaseModel;
import com.est7.hybriddemo.net.CacheManager;
import com.est7.hybriddemo.net.ServiceManager;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * Created by Administrator on 2017/3/20.
 */

public class UserModel extends BaseModel<ServiceManager, CacheManager> implements UserinfoContract.UserInfoModel {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public UserModel(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }


    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        Observable<List<User>> users = mServiceManager.getUserService()
                .getUsers(lastIdQueried, USERS_PER_PAGE);
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mCacheManager.getCommonCache()
                .getUsers(users
                        , new DynamicKey(lastIdQueried)
                        , new EvictDynamicKey(update))
                .flatMap(new Func1<Reply<List<User>>, Observable<List<User>>>() {
                    @Override
                    public Observable<List<User>> call(Reply<List<User>> listReply) {
                        return Observable.just(listReply.getData());
                    }
                });
    }

}
package com.est7.hybriddemo.net;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/8.
 * 对code进行判断和解析，不同的错误返回不同的错误信息
 */

public class RxHelper<T> {

    /**
     * 对结果进行预处理
     * 因为每次都要从子线程接收，ui线程操作ui
     * 跟compose()配合使用,比如ObservableUtils.wrap(obj).compose(toMain())
     *
     * @param <T>
     * @return
     */
    /*public static <T> ObservableTransformer<T, T> toMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
*/
     /*public static <T> FlowableTransformer<T, T> toMain() {

        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }*/

    private static FlowableTransformer schedulersTransformer = new FlowableTransformer() {
        @Override
        public Publisher apply(Flowable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

    };

    public static <T> FlowableTransformer<T, T> toMain() {
        return schedulersTransformer;
    }


}

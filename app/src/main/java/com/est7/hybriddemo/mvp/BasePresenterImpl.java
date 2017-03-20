package com.est7.hybriddemo.mvp;


import java.lang.ref.WeakReference;

public class BasePresenterImpl< V extends BaseView> implements BasePresenter<V> {

    public WeakReference<V> mViewRef;

    @Override
    public void attachModelView(V pView) {
        mViewRef = new WeakReference<V>(pView);
    }

    @Override
    public void detachView() {
        if (null != mViewRef && mViewRef.get() != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }


    public V getView() {
        if (isAttach()) {
            return mViewRef.get();
        } else {
            throw new DidNotAttachViewExpecton("Did Not Attach View");
        }
    }

    public boolean isAttach() {
        return null != mViewRef && null != mViewRef.get();
    }

}

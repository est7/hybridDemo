package com.est7.hybriddemo.mvp;

/**
 * Created by Administrator on 2017/3/20.
 */

class DidNotAttachViewExpecton extends RuntimeException {

    public DidNotAttachViewExpecton(String msg) {
        super(msg);
    }
}

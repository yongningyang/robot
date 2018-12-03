package com.innsmap.data.http;

import android.content.Context;

import io.reactivex.observers.DisposableObserver;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * 该类仅供参考，实际业务Code, 根据需求来定义
 */
public abstract class BaseSubscriber<T> extends DisposableObserver<T> {
    public abstract void onResult(T t);

    private Context context;
    private boolean isNeedCahe;

    public BaseSubscriber(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ResponseThrowable) {
            onError((ResponseThrowable) e);
        } else {
            onError(new ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!NetworkUtil.isNetworkAvailable(context)) {
            onComplete();
        }
    }

    @Override
    public void onComplete() {

    }


    public abstract void onError(ResponseThrowable e);

    @Override
    public void onNext(Object o) {
        BaseResponse baseResponse = (BaseResponse) o;
        if (baseResponse.getCode() == 200) {
            onResult((T) baseResponse.getData());
        } else if (baseResponse.getCode() == 330) {
        } else if (baseResponse.getCode() == 503) {
        } else {
        }
    }
}
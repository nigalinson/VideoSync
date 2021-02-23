package com.sloth.multidevicesync.utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2021/2/20 16:36
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2021/2/20         Carl            1.0                    1.0
 * Why & What is modified:
 */
public abstract class DoAction<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) { }

    @Override
    public void onNext(T t) {
        onDoing(t);
    }

    @Override
    public void onError(Throwable e) { }

    @Override
    public void onComplete() { }

    protected abstract void onDoing(T t);
}

package com.sloth.multidevicesync.selection;

import com.sloth.multidevicesync.adjust.Config;
import com.sloth.multidevicesync.info.Device;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/31 18:22
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/31         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class ElectionCounter {

    private Device device;
    private CounterCallback counterCallback;
    private Disposable counter;

    public ElectionCounter(Device device, CounterCallback counterCallback) {
        this.device = device;
        this.counterCallback = counterCallback;
    }

    public void reset(){
        stop();
        counter = Observable.interval(Config.DISPATCH_INTERVAL * 5, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                    if(counterCallback != null){
                        counterCallback.reElection();
                    }
                });
    }

    public void stop(){
        if(counter != null && !counter.isDisposed()){
            counter.dispose();
        }

    }

    public interface CounterCallback {
        void reElection();
    }
}

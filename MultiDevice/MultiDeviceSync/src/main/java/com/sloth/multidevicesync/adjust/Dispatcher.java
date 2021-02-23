package com.sloth.multidevicesync.adjust;

import com.sloth.multidevicesync.event.Event;
import com.sloth.multidevicesync.info.Device;
import com.sloth.multidevicesync.pack.PackManager;
import com.sloth.multidevicesync.player.Player;
import com.sloth.multidevicesync.selection.ElectionCounter;
import com.sloth.multidevicesync.utils.JSON;
import com.sloth.multidevicesync.utils.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 15:07
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Dispatcher implements ElectionCounter.CounterCallback {

    private Device device;
    private Player player;
    private DispatchCallback dispatchCallback;

    private Disposable dispatcherLooper;
    private AtomicBoolean dispatching = new AtomicBoolean(false);

    private ElectionCounter electionCounter;

    public Dispatcher(Device device,  Player player, DispatchCallback dispatchCallback) {
        this.device = device;
        this.player = player;
        this.dispatchCallback = dispatchCallback;
        electionCounter = new ElectionCounter(device, this);
    }

    public void startDispatchLooper(){
        stopDispatchLoop();
        dispatcherLooper = Observable.interval(Config.DISPATCH_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> sendDispatch());
    }

    public void stopDispatchLoop(){
        if(dispatcherLooper != null && !dispatcherLooper.isDisposed()){
            dispatcherLooper.dispose();
        }
    }

    private void sendDispatch(){
        if(device.getCharacter() == Device.CHARACTER_MASTER){
            electionCounter.reset();
            dispatching.set(true);
            Event event = new Event(Event.TYPE_CONTROL, device);
            event.setProgress(player.getNowPosition());
            event.setTimestamp(System.currentTimeMillis());
            PackManager.getInstance().send(JSON.get().toJson(event));
        }
    }

    public void handlerDispatch(Event received, long gap){
        if(received.getNumber() < device.getNumber()){
            //弱于我的，无权调度我
            return;
        }

        //强于我的控制指令，刷一下我的slave状态
        device.setCharacter(Device.CHARACTER_SLAVE);
        electionCounter.reset();


        //B - A = GAP
        //B2 - GAP = A2
        //TTL = A2 - A
        //P2 = P + TTL

        if(gap == 0){
            Log.d(this, "尚未校准");
            return;
        }

        long ttl = System.currentTimeMillis() + gap - received.getTimestamp() ;
        long exceptProgress = received.getProgress() + ttl;
        if(Math.abs(exceptProgress - player.getNowPosition()) > Config.MAX_ALLOWED_OFFSET){
            //误差大于100ms，需要校准
            if(exceptProgress >= 0){
                dispatchCallback.seekTo(0, exceptProgress);
            }else{
                dispatchCallback.seekTo(Math.abs(exceptProgress), 0);
            }
        }
        Log.d(this, "误差：" + Math.abs(exceptProgress - player.getNowPosition()) +"ms");
    }

    @Override
    public void reElection() {
        device.setCharacter(Device.CHARACTER_MASTER);
    }

    public interface DispatchCallback{
        void seekTo(long delay, long progress);
    }
}

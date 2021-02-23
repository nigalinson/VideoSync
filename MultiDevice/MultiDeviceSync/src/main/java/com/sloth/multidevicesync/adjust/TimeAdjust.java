package com.sloth.multidevicesync.adjust;

import com.sloth.multidevicesync.event.Event;
import com.sloth.multidevicesync.info.Device;
import com.sloth.multidevicesync.pack.PackManager;
import com.sloth.multidevicesync.utils.Salt;
import com.sloth.multidevicesync.utils.JSON;
import com.sloth.multidevicesync.utils.Log;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 15:22
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class TimeAdjust {

    private Device device;

    private long lastAdjustTime = 0;

    public TimeAdjust(Device device) {
        this.device = device;
    }

    //机器绝对时间差
    private long timeGap = 0L;

    //上次校准请求指令 目标设备
    private String timeGapMaster = null;
    //最后一次请求的标识码 （防止UDP包错乱）
    private String lastRequestKey = null;

    //时间校准counter - 每隔几秒触发，但不一定会真实刷新时间校准
    private Disposable reAdjustCounter;

    //强制时间校准counter - 一大段时间后，强制真实刷新一次时间校准
    private Disposable forceReAdjustCounter;

    public void startAdjust(){

        closeReAdjustCounter();
        reAdjustCounter = Observable.interval(Config.ADJUST_TIME_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if(device.getCharacter() == Device.CHARACTER_MASTER){
                        Event event = new Event(Event.TYPE_ADJUST_TIME_START, device);
                        PackManager.getInstance().send(JSON.get().toJson(event));
                        Log.d(this, "通知开始校准时间！");
                    }
                });

        closeForceReAdjustCounter();
        forceReAdjustCounter = Observable.interval(Config.FORCE_RE_ADJUST_TIME_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    //强制刷新时间
                    timeGap = 0;
                });
    }

    public void receiveStartAdjust(Event received){
        //时间差校准指令
        if(received.getNumber() < device.getNumber()){
            //弱于我的，无权调度我
            return;
        }

        //强于我的控制指令，刷一下自己的slave状态
        device.setCharacter(Device.CHARACTER_SLAVE);

        if(received.getDevice().equals(timeGapMaster) && timeGap != 0){
            //上一次和目标校准过的,不重复校准
            Log.d(this, "校准状态正常..");
            return;
        }
        lastAdjustTime = System.currentTimeMillis();
        lastRequestKey = Salt.salt();
        timeGapMaster = received.getDevice();
        Event event = new Event(Event.TYPE_ADJUST_TIME_REQUEST, device);
        event.setTimestamp(lastAdjustTime);
        event.setIdentity(lastRequestKey);
        PackManager.getInstance().send(JSON.get().toJson(event));
        Log.d(this, "请求校准时间：" + lastAdjustTime);
    }

    public void adjusting(Event received){
        //Master立即返回当前时间
        if(device.getCharacter() == Device.CHARACTER_MASTER){
            Event event = new Event(Event.TYPE_ADJUST_TIME_RESPONSE, device);
            event.setIdentity(received.getIdentity());
            long now = System.currentTimeMillis();
            event.setTimestamp(now);
            PackManager.getInstance().send(JSON.get().toJson(event));
            Log.d(this, "收到校准请求：" + now + ",反馈！");
        }
    }

    public void result(Event received){
        if(!received.getIdentity().equals(lastRequestKey)){
            //非本机有效回函，无视
            return;
        }

        if(!received.getDevice().equals(timeGapMaster)){
            //非最后一次回函目标主机，无视
            return;
        }

        //时间校准回函
        long now = System.currentTimeMillis();
        long ttl = (now - lastAdjustTime) / 2;

        if(ttl <= Config.MAX_ALLOWED_ADJUST_TTL){
            //满足误差要求
            timeGap = received.getTimestamp() + ttl - now;
            Log.d(this, "校准成功：主从误差为：" + timeGap + "ms");
        }else{
            Log.d(this, "校准失败!");
        }
        Log.d(this, "TTL：" + ttl);
    }

    public long getGap(Event received){
        if(received.getDevice().equals(timeGapMaster)){
            return timeGap;
        }

        return 0;
    }

    public void closeReAdjustCounter() {
        if(reAdjustCounter != null && !reAdjustCounter.isDisposed()){
            reAdjustCounter.dispose();
        }
    }

    public void closeForceReAdjustCounter() {
        if(forceReAdjustCounter != null && !forceReAdjustCounter.isDisposed()){
            forceReAdjustCounter.dispose();
        }
    }

    public void reset(){
        timeGap = 0;
    }

}

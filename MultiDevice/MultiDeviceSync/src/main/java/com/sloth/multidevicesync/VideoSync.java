package com.sloth.multidevicesync;

import android.content.Context;
import android.view.ViewGroup;

import com.sloth.multidevicesync.adjust.Dispatcher;
import com.sloth.multidevicesync.adjust.TimeAdjust;
import com.sloth.multidevicesync.event.Event;
import com.sloth.multidevicesync.info.Device;
import com.sloth.multidevicesync.pack.PackManager;
import com.sloth.multidevicesync.pack.PackReceiver;
import com.sloth.multidevicesync.player.Player;
import com.sloth.multidevicesync.utils.DoAction;
import com.sloth.multidevicesync.utils.JSON;
import com.sloth.multidevicesync.utils.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 10:52
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class VideoSync implements PackReceiver.ReceiveCallback, Dispatcher.DispatchCallback {

    private Device device;

    private TimeAdjust timeAdjust;

    private Dispatcher dispatcher;

    private Player player;

    public VideoSync(Context context, Device device) {
        this.device = device;
        player = new Player(context);
        timeAdjust = new TimeAdjust(device);
        dispatcher = new Dispatcher(device, player, this);
        PackManager.initialize(context.getApplicationContext());
        PackManager.getInstance(context.getApplicationContext()).listen(this);
        //开始校准
        timeAdjust.startAdjust();
        //开始调度
        dispatcher.startDispatchLooper();
    }

    public void setMediaSource(String url){
        //开始播放
        player.setMediaUrl(url);
    }

    public void reSetDevice(String groupName, String deviceName){
        device.setSerial(groupName);
        device.setDevice(deviceName);
    }

    public void attach(ViewGroup viewGroup) {
        player.attach(viewGroup);
        player.play();
    }

    public void detach(){
        player.stop();
        player.detach();
    }

    @Override
    public void receive(String data) {
        if (null == data || "".equals(data)) {
            //空数据
            return;
        }

        Event received = JSON.get().fromJson(data, Event.class);

        if (received == null || received.getSerial() == null || !received.getSerial().equals(device.getSerial())) {
            //无意义数据 | 无效数据
            return;
        }

        if (received.getSerial() != null && device != null
                && received.getSerial().equals(device.getSerial())
                && received.getDevice() != null
                && received.getDevice().equals(device.getDevice())
                && received.getNumber() == device.getNumber()) {
            //自己发送的广播，直接忽略
            return;
        }

        Log.d(this, "received: " + JSON.get().toJson(received));

        switch (received.getType()) {
            case Event.TYPE_ADJUST_TIME_START:
                timeAdjust.receiveStartAdjust(received);
                break;
            case Event.TYPE_ADJUST_TIME_REQUEST:
                timeAdjust.adjusting(received);
                break;
            case Event.TYPE_ADJUST_TIME_RESPONSE:
                timeAdjust.result(received);
                break;
            case Event.TYPE_CONTROL:
                dispatcher.handlerDispatch(received, timeAdjust.getGap(received));
                break;
            default:
                break;
        }
    }

    public void reset(){
        timeAdjust.reset();
    }

    public void exit() {
        PackManager.exit();
        dispatcher.stopDispatchLoop();
        timeAdjust.closeReAdjustCounter();
        timeAdjust.closeForceReAdjustCounter();
        detach();
    }

    @Override
    public void seekTo(long delay, long progress) {
        Log.d(this, "延迟：" + delay + "后，调整进度为：" + progress);

        if (player.isPlaying()) {
            if (delay == 0) {
                player.seekTo(progress);
            } else {
                Observable.timer(delay, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DoAction<Long>() {
                            @Override
                            protected void onDoing(Long aLong) {
                                player.seekTo(progress);
                            }
                        });
            }
        } else {
            if (delay == 0) {
                player.play();
            } else {
                Observable.timer(delay, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DoAction<Long>() {
                            @Override
                            protected void onDoing(Long aLong) {
                                player.play();
                            }
                        });
            }
        }
    }
}

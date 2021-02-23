package com.sloth.multidevicesync.pack;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/29 17:59
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/29         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class PackManager implements PackReceiver.ReceiveCallback {
    public static final int PORT = 8092;
    public static final int RECEIVE_PORT = PORT;

    public static final String HOST = "239.0.0.3";

    private static PackManager ins;

    public static PackManager getInstance(Context context){
        if(ins == null){
            ins = new PackManager(context);
        }
        return ins;
    }

    public static void initialize(Context context){
        getInstance(context);
    }

    public static PackManager getInstance(){
        return ins;
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private PackReceiver packReceiver;

    private PackSender packSender;

    private PackReceiver.ReceiveCallback receiveCallback;

    private WifiManager.MulticastLock lock;

    private PackManager(Context context) {
        receiverHaler.setCallback(this);
        packReceiver = new PackReceiver();
        packSender = new PackSender(context);
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        lock = wifiMgr.createMulticastLock(PackManager.class.getSimpleName());
        lock.setReferenceCounted(false);
        lock.acquire();
    }

    public void listen(PackReceiver.ReceiveCallback tmp){
        this.receiveCallback = tmp;

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                packReceiver.receive(new PackReceiver.ReceiveCallback() {
                    @Override
                    public void receive(String data) {
                        Message msg = Message.obtain();
                        msg.obj = data;
                        receiverHaler.sendMessage(msg);
                    }
                });
            }
        });
    }

    @Override
    public void receive(String data) {
        if(this.receiveCallback != null){
            this.receiveCallback.receive(data);
        }
    }

    public void stopListen(){
        if(packReceiver != null){
            packReceiver.stop();
        }
    }

    private static final class ReceiverHandler extends Handler {

        private PackReceiver.ReceiveCallback mCallback;

        public ReceiverHandler(@NonNull Looper looper) {
            super(looper);
        }

        public void setCallback(PackReceiver.ReceiveCallback mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(mCallback != null){
                mCallback.receive((String) msg.obj);
            }
        }
    };

    private final ReceiverHandler receiverHaler = new ReceiverHandler(Looper.myLooper());

    public void send(String data){
        packSender.send(data);
    }

    public void destroy(){
        receiverHaler.setCallback(null);
        receiverHaler.removeCallbacks(null);
        stopListen();

        packReceiver = null;
        packSender = null;
        if(lock != null){
            lock.release();
        }
    }

    public static void exit(){
        if(ins != null){
            ins.destroy();
            ins = null;
        }
    }

}

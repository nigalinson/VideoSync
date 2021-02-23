package com.sloth.multidevicesync.player;

import android.graphics.Bitmap;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/10 11:18
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/10         Carl            1.0                    1.0
 * Why & What is modified:
 */
public abstract class BaseVideoViewController extends AttachViewController {

    private ControllerListener mControllerListener;

    public void setListener(ControllerListener controllerListener){
        this.mControllerListener = controllerListener;
    }

    public abstract void playLocal(String local);

    public abstract void playOnline(String url);

    public abstract void seekTo(long pro);

    public abstract void stop();

    public abstract Bitmap captureScreen();

    public abstract boolean isPlaying();

    public abstract long getNextKeyFramePosition();

    public abstract long getProgress();

    public void destroy(){ }

    protected void onPrepared(){
        if(mControllerListener != null){
            mControllerListener.onPrepared();
        }
    }

    protected void onEnd(){
        if(mControllerListener != null){
            mControllerListener.onEnd();
        }
    }

    protected void onError(String msg){
        if(mControllerListener != null){
            mControllerListener.onError(msg);
        }
    }
}

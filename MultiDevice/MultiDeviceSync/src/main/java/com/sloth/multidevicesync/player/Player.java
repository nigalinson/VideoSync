package com.sloth.multidevicesync.player;

import android.content.Context;
import android.view.ViewGroup;

import com.sloth.multidevicesync.utils.Log;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 15:10
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Player implements ControllerListener {

    private BaseVideoViewController videoController;

    private String url;

    public Player(Context context) {
        videoController = new DefaultTextureVideoController(context);
        videoController.setListener(this);
    }

    public void attach(ViewGroup viewGroup){
        videoController.attach(viewGroup);
    }

    public void detach(){
        videoController.detach();
    }

    public void setMediaUrl(String url){
        this.url = url;
    }

    public void play(){
        if(videoController.isPlaying()){
            log("已经在播放，请勿重复开启");
            return;
        }
        videoController.playLocal(url);

        log("开始播放");
    }

    public void seekTo(final long pro){
        log("调整进度progress=" + pro);

        videoController.seekTo(pro);
    }

    public void stop(){
        videoController.stop();
    }

    public boolean isPlaying(){
        return videoController.isPlaying();
    }

    @Override
    public void onPrepared() { }

    @Override
    public void onEnd() { }

    @Override
    public void onError(String msg) { }

    public long getNowPosition(){
        return videoController.getProgress();
    }

    private void log(String msg){
        Log.d(this, msg);
    }

}

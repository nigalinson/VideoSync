package com.sloth.multidevicesync.player;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.VideoView;

import com.sloth.multidevicesync.utils.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/10 10:49
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/10         Carl            1.0                    1.0
 * Why & What is modified:
 */
public abstract class AttachViewController {

    private static final String TAG = AttachViewController.class.getSimpleName();
    /**
     * 是否已经添加到View
     */
    private AtomicBoolean attached = new AtomicBoolean(false);

    public boolean isAttached(){
        return attached.get();
    }

    public abstract View attachingView();

    public void attach(ViewGroup parent){

        int attemptTimes = 0;
        while(attached.get()){
            //如果准备添加时已经添加到其他布局，先从其他布局去除
            //尝试3次
            if(attemptTimes >= 3){
                //三次尝试后依然无法去除上一次的View-判定失败
                Log.e(TAG, "attach失败，detach上一次的View异常");
                return;
            }
            detach();
            attemptTimes++;
        }

        attemptTimes = 0;
        while(parentAlreadyHasVideoView(parent)){
            //如果准备添加时已经添加到其他布局，先从其他布局去除
            //尝试3次
            if(attemptTimes >= 3){
                //三次尝试后依然无法去除parent中的video
                Log.e(TAG, "remove parent video failed");
                return;
            }
            attemptTimes++;
        }

        parent.addView(attachingView(), 0,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
        attached.set(true);
    }

    public void detach(){
        View v = attachingView();
        ViewParent parent = v.getParent();
        if(parent == null){
            Log.e(TAG, "video已经detach");
            return;
        }
        ((ViewGroup)parent).removeView(v);
        attached.set(false);
    }

    private boolean parentAlreadyHasVideoView(ViewGroup parent) {
        for(int i = 0; i < parent.getChildCount(); i++){
            View v = parent.getChildAt(i);
            if(v instanceof VideoView){
                parent.removeView(v);
                return true;
            }
        }
        return false;
    }
}

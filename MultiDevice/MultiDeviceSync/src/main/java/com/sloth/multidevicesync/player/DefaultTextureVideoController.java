package com.sloth.multidevicesync.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.sloth.multidevicesync.utils.Log;
import java.io.IOException;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/11 10:19
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/11         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class DefaultTextureVideoController extends BaseVideoViewController
        implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnVideoSizeChangedListener {

    private static final String TAG = DefaultTextureVideoController.class.getSimpleName();

    /**
     * 播放等待surface准备完毕进行的最大CAS尝试次数
     */
    private final int MAX_CAS_TIMES = 3;

    private MediaPlayer mMediaPlayer;
    private TextureView mPreview;
    private Surface surface;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public DefaultTextureVideoController(Context context) {
        mPreview = new TextureView(context);
        mPreview.setSurfaceTextureListener(this);
        try {
            if(mMediaPlayer == null){
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setOnCompletionListener(this);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnVideoSizeChangedListener(this);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View attachingView() {
        return mPreview;
    }

    @Override
    public void playLocal(String local) {
        //如果播放的时候 surface尚未初始化完成，CAS
        validateCouldPlay(true, local, MAX_CAS_TIMES);
    }

    @Override
    public void playOnline(String url) {
        //如果播放的时候 surface尚未初始化完成，CAS
        validateCouldPlay(false, url, MAX_CAS_TIMES);
    }

    @Override
    public void seekTo(long pro) {
        mMediaPlayer.seekTo((int) pro);
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public long getNextKeyFramePosition() {
        return 0;
    }

    @Override
    public long getProgress() {
        return mMediaPlayer.getCurrentPosition();
    }

    private void validateCouldPlay(boolean isLocal, String uri, int leftTryingTimes) {
        if(surface == null){
            if(leftTryingTimes > 0){
                mainHandler.postDelayed(() -> validateCouldPlay(isLocal, uri, leftTryingTimes - 1),100);
            }else{
                //已超过限定重试次数
                Log.e(TAG, "三次检测后surface仍未就绪！放弃本次播放！");
            }
            return;
        }

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            onError(e.getMessage());
        } catch (SecurityException e) {
            e.printStackTrace();
            onError(e.getMessage());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            onError(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    @Override
    public void stop() {
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
            mMediaPlayer.stop();
        }
    }

    @Override
    public Bitmap captureScreen() {
        if(mPreview != null){
            return mPreview.getBitmap();
        }
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        if(mMediaPlayer != null){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if(surface != null){
            surface.release();
            surface = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG, "SurfaceTexture重置");
        if(surface == null){
            surface = new Surface(surfaceTexture);
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) { }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "SurfaceTexture回收");
        surface = null;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        //surface 更新
        if(surface == null){
            surface = new Surface(surfaceTexture);
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "准备就绪，开始播放");
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
        onPrepared();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.i(TAG, "播放结束");
        onPrepared(mediaPlayer);
        onEnd();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) { }
}

package com.sloth.multidevicesync.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.sloth.multidevicesync.R;
import com.sloth.multidevicesync.VideoSync;
import com.sloth.multidevicesync.event.SyncExitEv;
import com.sloth.multidevicesync.info.Device;
import com.sloth.multidevicesync.info.SyncSource;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.File;

public class SyncActivity extends AppCompatActivity {

    public static void start(Context context, String groupName, String deviceName, SyncSource syncSource){
        Intent it = new Intent(context, SyncActivity.class);
        it.putExtra("groupName", groupName);
        it.putExtra("deviceName", deviceName);
        it.putExtra("syncSource", syncSource);
        context.startActivity(it);
    }

    private ViewGroup container;
    //视频内容
    private VideoSync videoSync;
    //图片内容
    private ImageView display;

    //数据
    private String groupName;
    private String deviceName;
    private SyncSource syncSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_sync);
        container = findViewById(R.id.container);
        startSync(getIntent(), savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startSync(intent, null);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("groupName", groupName);
        outState.putString("deviceName", deviceName);
        outState.putSerializable("syncSource", syncSource);
    }

    private void startSync(Intent intent, Bundle savedInstanceState) {
        if(intent != null){
            groupName = intent.getStringExtra("groupName");
            deviceName = intent.getStringExtra("deviceName");
            syncSource = (SyncSource) intent.getSerializableExtra("syncSource");
        }

        if(savedInstanceState != null){
            groupName = savedInstanceState.getString("groupName");
            deviceName = savedInstanceState.getString("deviceName");
            syncSource = (SyncSource) savedInstanceState.getSerializable("syncSource");
        }

        container.removeAllViews();
        if(syncSource != null && syncSource.getSourceType() == SyncSource.SourceType.Video.code){
            syncVideo();
        }else {
            syncImage();
        }

    }

    private void syncVideo() {
        if(videoSync == null){
            Device device = new Device(getApplicationContext(), deviceName);
            //局域网组
            device.setSerial(groupName);
            videoSync = new VideoSync(getApplicationContext(), device);
        }else{
            videoSync.reSetDevice(groupName, deviceName);
        }

        videoSync.setMediaSource(syncSource.getLocalUrl());
    }

    private void syncImage() {
        if(display == null){
            display = new ImageView(this);
            container.addView(display);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) display.getLayoutParams();
            layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT;
            display.setLayoutParams(layoutParams);
            display.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        Glide.with(this).load(new File(syncSource.getLocalUrl())).into(display);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(syncSource != null && syncSource.getSourceType() == SyncSource.SourceType.Video.code){
            videoSync.attach(container);
        }
    }

    @Override
    protected void onPause() {
        if(syncSource != null && syncSource.getSourceType() == SyncSource.SourceType.Video.code){
            videoSync.detach();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if(videoSync != null){
            videoSync.detach();
            videoSync.exit();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SyncExitEv exitEv){
        finish();
    }
}
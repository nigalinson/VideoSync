package com.sloth.multidevice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.sloth.multidevicesync.SyncCenter;
import com.sloth.multidevicesync.info.SyncSource;
import com.sloth.multidevicesync.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        syncYadi(null);
    }

    @Override
    protected void onDestroy() {
        SyncCenter.stopSync();
        super.onDestroy();
    }

    public void syncNana(View view) {
        SyncCenter.startSync(
                this,
                "9998877",
                "dev-" + Utils.random().nextInt(127),
                new SyncSource(SyncSource.SourceType.Image.code, Test.getNana())
        );
    }

    public void syncYadi(View view) {
        SyncCenter.startSync(
                this,
                "9998877",
                "dev-" + Utils.random().nextInt(127),
                new SyncSource(SyncSource.SourceType.Video.code, Test.getYadi())
        );
    }

    public void syncLady(View view) {
        SyncCenter.startSync(
                this,
                "9998877",
                "dev-" + Utils.random().nextInt(127),
                new SyncSource(SyncSource.SourceType.Video.code, Test.getLadyKeyFrame())
        );
    }
}
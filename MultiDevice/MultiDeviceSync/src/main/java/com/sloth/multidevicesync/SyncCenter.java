package com.sloth.multidevicesync;

import android.content.Context;

import com.sloth.multidevicesync.adjust.Config;
import com.sloth.multidevicesync.event.SyncExitEv;
import com.sloth.multidevicesync.info.SyncSource;
import com.sloth.multidevicesync.ui.SyncActivity;
import org.greenrobot.eventbus.EventBus;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2021/2/23 11:58
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2021/2/23         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class SyncCenter {

    public static void startSync(Context context, String groupName, String deviceName, SyncSource syncSource){
        SyncActivity.start(context, groupName, deviceName, syncSource);
    }

    public static void stopSync(){
        EventBus.getDefault().post(new SyncExitEv());
    }

    /**
     * 最大允许的网速延迟
     * @param offset
     */
    public static void maxAllowedNetWorkDelay(long offset){
        Config.MAX_ALLOWED_ADJUST_TTL = offset;
    }

    /**
     * 最大允许调度误差
     * @param offset
     */
    public static void maxAllowedSyncOffset(long offset){
        Config.MAX_ALLOWED_OFFSET = offset;
    }

}

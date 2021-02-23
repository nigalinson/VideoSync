package com.sloth.multidevicesync.player;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/10 15:37
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/10         Carl            1.0                    1.0
 * Why & What is modified:
 */
public interface ControllerListener {

    void onPrepared();

    void onEnd();

    void onError(String msg);

}

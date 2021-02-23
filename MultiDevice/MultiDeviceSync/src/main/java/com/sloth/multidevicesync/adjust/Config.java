package com.sloth.multidevicesync.adjust;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/31 14:02
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/31         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Config {

    //时间校准间隔
    public static final long ADJUST_TIME_INTERVAL = 5000L;

    //强制重新同步时间间隔（长时间摆放后机器绝对时间会有误差）
    public static final long FORCE_RE_ADJUST_TIME_INTERVAL = 20 * 60 * 1000;

    //调度间隔
    public static final long DISPATCH_INTERVAL = 5000L;

    //时间校准时，允许的最大误差
    //（网速极好 - 10ms 、 网速一般 - 50ms 、 能上网 - 100ms）
    public static long MAX_ALLOWED_ADJUST_TTL = 100;

    //误差大于多少时，需要调整进度 （值越小，同步性越高，但可能造成画面频繁卡顿）
    public static long MAX_ALLOWED_OFFSET = 100;

    //使用什么播放器 1-原生 2-IJK
    //IJK ffmpeg目前不支持seek，具体原因可能需要调试C++层
    public static final int PLAYER = 1;
}

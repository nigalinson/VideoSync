package com.sloth.multidevice;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/31 17:02
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/31         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Test {

    private static String[] lady = new String[]{
            "/sdcard/rongyi/resource/video/lady_left.mp4",
            "",
            "/sdcard/rongyi/resource/video/lady_right.mp4"
    };
    private static String[] ladyKeyFrame = new String[]{
            "/sdcard/rongyi/resource/video/lady_left_keyframe.mp4",
            "",
            "/sdcard/rongyi/resource/video/lady_right_keyframe.mp4"
    };
    private static String[] science = new String[]{
            "/sdcard/rongyi/resource/video/video_left.mp4",
            "",
            "/sdcard/rongyi/resource/video/video_right.mp4"
    };
    private static String[] yadi = new String[]{
            "/sdcard/rongyi/resource/video/yadi/yadi_left.mp4",
            "/sdcard/rongyi/resource/video/yadi/yadi_middle.mp4",
            "/sdcard/rongyi/resource/video/yadi/yadi_right.mp4"
    };
    private static String[] nana = new String[]{
            "/sdcard/rongyi/resource/picture/nana/nana_left.png",
            "/sdcard/rongyi/resource/picture/nana/nana_middle.png",
            "/sdcard/rongyi/resource/picture/nana/nana_right.png"
    };

    public static String getLady(){
        return lady[BuildConfig.INDEX];
    }

    public static String getLadyKeyFrame(){
        return ladyKeyFrame[BuildConfig.INDEX];
    }

    public static String getScience(){
        return science[BuildConfig.INDEX];
    }

    public static String getYadi(){
        return yadi[BuildConfig.INDEX];
    }

    public static String getNana(){
        return nana[BuildConfig.INDEX];
    }

}

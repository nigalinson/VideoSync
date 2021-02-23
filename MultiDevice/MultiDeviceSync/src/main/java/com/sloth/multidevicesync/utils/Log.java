package com.sloth.multidevicesync.utils;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 11:10
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Log {
    public static void d(Object aim, String msg){
        System.out.println(aim.getClass().getSimpleName() + ":" + msg);
    }

    public static void e(Object aim, String msg){
        System.out.println(aim.getClass().getSimpleName() + ":" + msg);
    }

    public static void i(Object aim, String msg){
        System.out.println(aim.getClass().getSimpleName() + ":" + msg);
    }

    public static void v(Object aim, String msg){
        System.out.println(aim.getClass().getSimpleName() + ":" + msg);
    }
}

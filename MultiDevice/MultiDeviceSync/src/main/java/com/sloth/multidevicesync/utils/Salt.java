package com.sloth.multidevicesync.utils;

import java.util.Random;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2021/2/22 10:24
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2021/2/22         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Salt {

    private static final char[] keys = new char[]{'h', 'e', 'l', 'l', 'o', '-', 't', 'h', 'i', 's', '-', 'i', 's', '-', 'r', 'o', 'n', 'g', 'y', 'i', '-', 'c', 'o', 'm', '-', '1', '2', '3'};

    public static String salt() {
        int len = 5;
        Random rd = Utils.random();
        char[] res = new char[len];
        for(int i = 0; i < 5; i++){
            res[i] = keys[Math.max((Math.abs(rd.nextInt(keys.length)) - 1), 0)];
        }
        return new String(res);
    }
}

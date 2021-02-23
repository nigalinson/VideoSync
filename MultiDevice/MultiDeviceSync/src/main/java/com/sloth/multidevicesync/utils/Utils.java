package com.sloth.multidevicesync.utils;

import java.util.Random;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 10:55
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Utils {

    public static Random random(){
        return new Random(System.currentTimeMillis());
    }

}

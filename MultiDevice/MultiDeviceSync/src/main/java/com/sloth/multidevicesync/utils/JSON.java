package com.sloth.multidevicesync.utils;

import com.google.gson.Gson;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 11:12
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class JSON {

    private static Gson gson = new Gson();

    public static Gson get(){
        return gson;
    }
}

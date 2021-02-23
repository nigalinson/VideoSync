package com.sloth.multidevicesync.info;

import android.content.Context;
import com.sloth.multidevicesync.utils.Utils;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 10:53
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class Device {

    //角色类型
    public static final int CHARACTER_SLAVE = 0;
    public static final int CHARACTER_MASTER = 2;

    private String serial;
    private int number;
    private String device;
    private int character;

    public Device(Context context, String name) {
        number = Math.abs(Utils.random().nextInt(Byte.MAX_VALUE));
        device = name;
        character = CHARACTER_MASTER;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }
}

package com.sloth.multidevicesync.event;

import com.sloth.multidevicesync.info.Device;

import java.io.Serializable;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2020/12/30 10:37
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2020/12/30         Carl            1.0                    1.0
 * Why & What is modified:
 *
 * 指令集：
 * h：指令类型 1-选举指令  2调度指令
 * s：分屏事务序列
 * n：事务号牌(纯数字，决定选举算法优先级)
 * d：设备码
 * c：角色(2-master，1-预选Master，0-slave)
 * p：当前播放进度
 * t：指令发送时间戳
 *
 */
public class Event implements Serializable {

    //时间校准指令
    public static final int TYPE_ADJUST_TIME_START = 20;
    public static final int TYPE_ADJUST_TIME_REQUEST = 21;
    public static final int TYPE_ADJUST_TIME_RESPONSE = 22;
    //调度指令
    public static final int TYPE_CONTROL = 30;

    private int type;
    private String serial;
    private int number;
    private String device;
    private int character;
    private long progress;
    private long timestamp;
    //口令 （用于区分 - 这条指令是不是针对我的，或者我只是收到了别人群发的消息，不需要处理）
    private String identity;

    public Event(int type) {
        this.type = type;
    }

    public Event(int type, Device device) {
        this.type = type;
        this.serial = device.getSerial();
        this.number = device.getNumber();
        this.device = device.getDevice();
        this.character = device.getCharacter();
        this.timestamp = System.currentTimeMillis();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}

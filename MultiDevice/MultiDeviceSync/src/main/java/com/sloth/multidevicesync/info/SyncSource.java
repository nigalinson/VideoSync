package com.sloth.multidevicesync.info;

import java.io.Serializable;

/**
 * Author:    Carl
 * Version    V1.0
 * Date:      2021/2/23 11:54
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2021/2/23         Carl            1.0                    1.0
 * Why & What is modified:
 */
public class SyncSource implements Serializable {

    public enum SourceType {
        /**
         * 图片
         */
        Image(1),
        /**
         * 视频
         */
        Video(2);

        SourceType(int code) {
            this.code = code;
        }

        public int code;
    }

    private int sourceType;
    private String localUrl;
    private String onLineUrl;

    public SyncSource() {
    }

    public SyncSource(int sourceType) {
        this.sourceType = sourceType;
    }

    public SyncSource(int sourceType, String localUrl) {
        this.sourceType = sourceType;
        this.localUrl = localUrl;
    }

    public SyncSource(int sourceType, String localUrl, String onLineUrl) {
        this.sourceType = sourceType;
        this.localUrl = localUrl;
        this.onLineUrl = onLineUrl;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getOnLineUrl() {
        return onLineUrl;
    }

    public void setOnLineUrl(String onLineUrl) {
        this.onLineUrl = onLineUrl;
    }
}

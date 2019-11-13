package com.example.implanthearts;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2019/5/26.
 */

public class flower extends BmobObject {
    private String name;
    private String wendu;

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    private String light;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getTrshidu() {
        return trshidu;
    }

    public void setTrshidu(String trshidu) {
        this.trshidu = trshidu;
    }

    private String trshidu;
}

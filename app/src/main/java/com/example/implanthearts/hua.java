package com.example.implanthearts;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2019/5/26.
 */

public class hua extends BmobObject {
    private String name;
    private String username;
    private String weather;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}

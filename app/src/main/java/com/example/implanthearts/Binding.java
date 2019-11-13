package com.example.implanthearts;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2019/5/10.
 */

public class Binding extends BmobObject {

    private String initiative_username;//自己的用户名
    private String passive_username;//绑定对方的用户名

//    private String flower;//花名
//    private String weather;//天气
//    private String deviceId;//设备id
//    private String serviceId;//服务名


//    private String initiative_deviceId = "777070b2-ae5a-403a-972e-e8ad4c005324";//自己的设备id
//    private String initiative_serviceId="Light_LED";//自己的服务名
//    private String passive_deviceId = "fb5c4522-4788-45aa-a33f-60724e6d75d0";//对方的设备id
//    private String passive_serviceId="Light_LED";//对方的服务名

//    public String getFlower() {
//        return flower;
//    }
//
//    public void setFlower(String flower) {
//        this.flower = flower;
//    }
//
//    public String getWeather() {
//        return weather;
//    }
//
//    public void setWeather(String weather) {
//        this.weather = weather;
//    }
//
//    public String getDeviceId() {
//        return deviceId;
//    }
//
//    public void setDeviceId(String deviceId) {
//        this.deviceId = deviceId;
//    }
//
//    public String getServiceId() {
//        return serviceId;
//    }
//
//    public void setServiceId(String serviceId) {
//        this.serviceId = serviceId;
//    }


    public String getInitiative_username() {
        return initiative_username;
    }

    public void setInitiative_username(String initiative_username) {
        this.initiative_username = initiative_username;
    }

    public String getPassive_username() {
        return passive_username;
    }

    public void setPassive_username(String passive_username) {
        this.passive_username = passive_username;
    }

}

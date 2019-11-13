package com.example.implanthearts;

/**
 * Created by Administrator on 2018/11/19.
 */

public class JsonBean {

    private String deviceId;
    private Command command;
    private String callbackUrl;
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
    public Command getCommand() {
        return command;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    public String getCallbackUrl() {
        return callbackUrl;
    }

}
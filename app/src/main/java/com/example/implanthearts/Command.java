package com.example.implanthearts;

/**
 * Created by Administrator on 2018/11/19.
 */

public class Command {

    private String serviceId;
    private String method;
    private Paras paras;
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public String getServiceId() {
        return serviceId;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getMethod() {
        return method;
    }

    public void setParas(Paras paras) {
        this.paras = paras;
    }
    public Paras getParas() {
        return paras;
    }

}
package com.example.implanthearts;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2019/5/7.
 */

public class Limit extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}

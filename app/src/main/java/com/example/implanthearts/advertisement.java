package com.example.implanthearts;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2019/5/13.
 */

public class advertisement extends BmobObject {
    private String name;//用户名
    private BmobFile Picture;//用户头像

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getPicture() {
        return Picture;
    }

    public void setPicture(BmobFile icon) {
        Picture = icon;
    }

}

package com.example.implanthearts;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2019/5/12.
 */

public class Publish  extends BmobObject {
    //用户名
    private String name;
    //用户发布帖子的内容
    private String message;
    //用户发布帖子的时间
    private String time;

    //用户最多能发9张图
    private List<BmobFile> picture;

    public List<BmobFile> getPicture() {
        return picture;
    }

    public void setPicture(List<BmobFile> picture) {
        this.picture = picture;
    }

    //上传多少张图片
    private int n;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}

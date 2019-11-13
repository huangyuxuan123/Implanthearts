package com.example.implanthearts;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2019/7/31.
 */

public class Find extends BmobObject {
    private String username;
    private String ifbanding;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIfbanding() {
        return ifbanding;
    }

    public void setIfbanding(String ifbanding) {
        this.ifbanding = ifbanding;
    }


}

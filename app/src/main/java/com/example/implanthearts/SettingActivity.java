package com.example.implanthearts;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class SettingActivity extends Activity {
    private RelativeLayout change_password;
    private RelativeLayout exit;
    private ImageView setting_back;
    private RelativeLayout unbinding_account;
    private RelativeLayout unbinding_zhi;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityManager.addActivity(this);
        change_password = (RelativeLayout)findViewById(R.id.change_password);
        exit = (RelativeLayout)findViewById(R.id.exit);
        setting_back = (ImageView)findViewById(R.id.setting_back);
        unbinding_account = (RelativeLayout)findViewById(R.id.unbinding_account);
        unbinding_zhi = (RelativeLayout)findViewById(R.id.unbinding_zhi);

        //修改密码
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SettingActivity.this,UpdatePwActivity.class);
                startActivity(intent);
            }
        });

        //退出登陆
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录，同时清除缓存用户对象。
                BmobUser.logOut();
                intent = new Intent(SettingActivity.this,LandingActivity.class);
                startActivity(intent);
                ActivityManager.exit();
            }
        });

        //回退到个人中心主页
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent = new Intent(SettingActivity.this,PersonFragment.class);
//                startActivity(intent);
                finish();

            }
        });


        //解绑账号
        unbinding_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查找Binding表，如果有绑定的信息，获取id，然后删除，进行解绑
                BmobQuery<Binding> bmobQuery = new BmobQuery<Binding>();
                bmobQuery.addQueryKeys("initiative_username,passive_username");
                bmobQuery.findObjects(new FindListener<Binding>() {
                String initiative_username = null;
                String passive_username = null;
                    @Override
                    public void done(List<Binding> object, BmobException e) {
                        //Toast.makeText(LandingActivity.this,"查询成功：共" + object.size() + "条数据。",Toast.LENGTH_SHORT).show();
                        try {
                            initiative_username = object.get(0).getInitiative_username();
                            passive_username = object.get(0).getPassive_username();
                            Log.w("zzz", " try : initiative_username==" + initiative_username + " passive_username==" + passive_username);
                        } catch (Exception e2) {
                            initiative_username = null;
                            passive_username = null;
                            Log.w("zzz", e2.getMessage());
                            Log.w("zzz", " catch : initiative_username==" + initiative_username + " passive_username==" + passive_username);
                        }
                        if(initiative_username!=null &&passive_username!=null){
                            String id = object.get(0).getObjectId();
                            //删除一行数据
                            final Binding p2 = new Binding();
                            p2.setObjectId(id);
                            p2.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e== null) {
                                        PersonFragment.user_bangname.setText("未进行情感联系");
                                        Toast.makeText(SettingActivity.this,"解绑成功",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.w("bmob",e.getMessage());
                                        Toast.makeText(SettingActivity.this,"解绑失败",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }
                    }
                });
            }
        });

        unbinding_zhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新bmob上Find表的设备信息
                BmobQuery<Find> query2 = new BmobQuery<Find>();
                query2.addWhereEqualTo("username", LandingActivity.user_name);
                final String[] objectId2 = new String[1];

                query2.findObjects(new FindListener<Find>() {
                    @Override
                    public void done(List<Find> list, BmobException e) {
                        if (e == null){
                            for (Find ad2 : list){
                                objectId2[0] = ad2.getObjectId();
                            }
                            Find ad2 = new Find();
                            ad2.setIfbanding("0");
                            ad2.update(objectId2[0], new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null){
                                        Log.w("device", "更新成功");
                                        Toast.makeText(SettingActivity.this,"解绑成功",Toast.LENGTH_SHORT).show();
                                        intent = new Intent(SettingActivity.this,LandingActivity.class);
                                        startActivity(intent);
                                        ActivityManager.exit();
                                    }else {
                                        Log.w("device", "失败"+e.getErrorCode());
                                    }
                                }
                            });
                        }else {
                            Log.w("device", "获取id失败"+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }
}

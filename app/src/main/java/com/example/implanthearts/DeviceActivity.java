package com.example.implanthearts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DeviceActivity extends AppCompatActivity {
    public static String deviceId1;
    public static String gatewayId1;
    public static String service;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ActivityManager.addActivity(this);


        //根据用户名查找
        //用户有没有绑定种植箱，如果有，直接跳转到主界面
        //如果没有，弹出输入框输入id绑定种植箱
        BmobQuery<Find> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("username", LandingActivity.user_name);
        categoryBmobQuery.findObjects(new FindListener<Find>() {
            @Override
            public void done(List<Find> object, BmobException e) {
                if (e == null) {
                    String ifbanding = object.get(0).getIfbanding();
                    if(ifbanding.equals("1")){
                        //绑定了，直接跳转
                        intent = new Intent(DeviceActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else  if(ifbanding.equals("0")){
                        //没有绑定，弹出输入框
                        a();
                    }
                } else {
                    Log.w("fan","ttttttttttttttttttttttttttttttttt");
                }
            }
        });




    }

    private void a() {
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title)).setIcon(
                R.drawable.zhi).setView(inputServer).setNegativeButton(
                getString(R.string.quxiao), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(DeviceActivity.this,LandingActivity.class);
                        startActivity(intent);
                        ActivityManager.exit();
                    }
                });

        builder.setPositiveButton(getString(R.string.queding),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //用户输入id
                        String inputName = inputServer.getText().toString();
                        //根据id获取设备id和服务名
                        BmobQuery<Device> categoryBmobQuery = new BmobQuery<>();
                        categoryBmobQuery.addWhereEqualTo("number", inputName);
                        categoryBmobQuery.findObjects(new FindListener<Device>() {
                            @Override
                            public void done(List<Device> object, BmobException e) {
                                if (e == null) {
                                    deviceId1 = object.get(0).getDeviceId();
                                    gatewayId1 = object.get(0).getGatewayId();
                                    service = object.get(0).getService();
                                    Log.w("device", object.get(0).getNumber());
                                    Log.w("device", deviceId1);
                                    Log.w("device", gatewayId1);
                                    Log.w("device", service);

                                } else {
                                    Log.w("device", e.toString());
                                }
                            }
                        });

                        //Toast.makeText(DeviceActivity.this,"绑定成功",Toast.LENGTH_SHORT).show();
                        intent = new Intent(DeviceActivity.this,ProgressBarActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }


}

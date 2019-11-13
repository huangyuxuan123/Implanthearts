package com.example.implanthearts;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ProgressBarActivity extends AppCompatActivity {
private ProgressBar progressBar;
private TextView text;
    private int p=0;//当前进度
    private MyHandler myHandler=new MyHandler();//新写的Handler类

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code=msg.what;//接受处理码
            switch (code){
                case 1:
                    p+=20;
                    progressBar.setProgress(p);//给进度条的当前进度赋值
                    text.setText("正在加载中:"+p+"%");//显示当前进度为多少
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        progressBar = (ProgressBar)findViewById(R.id.ProgressBar);
        text = (TextView)findViewById(R.id.text);

        if(0==p){//如果当前进度为0
            new myThread().start();//开启线程
        }
    }

    public class myThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                try {
                    //Thread.sleep(100);//使线程休眠0.1秒

                    //更新bmob上user表的设备信息
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("username", LandingActivity.user_name);
                    final String[] objectId = new String[1];

                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null){
                                for (User ad : list){
                                    objectId[0] = ad.getObjectId();
                                }
                                User ad2 = new User();
                                ad2.setDeviceId(DeviceActivity.deviceId1);
                                ad2.setServiceId(DeviceActivity.service);
                                ad2.update(objectId[0], new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Log.w("device", "更新成功");
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
                                ad2.setIfbanding("1");
                                ad2.update(objectId2[0], new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Log.w("device", "更新成功");
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


                    //获取数据
                    //获取最新采集的数据，根据用户名
                            try
                            {
                                Getshuju getshuju=new Getshuju();
                                getshuju.deviceId =DeviceActivity.deviceId1;
                                getshuju.gatewayId = DeviceActivity.gatewayId1;
                                getshuju.GetaccessToken();
                                getshuju.GetDataandTime();

                            }
                            catch (Exception e)
                            {
                                Log.d("MainActivity", "++++++++++++++++"+e.getMessage());
                            }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(p==100){//当前进度等于总进度时退出循环
                    p=0;
                    Intent intent = new Intent(ProgressBarActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }

                Message msg=new Message();
                msg.what=1;
                myHandler.sendMessage(msg);//发送处理码
            }
        }
    }


}

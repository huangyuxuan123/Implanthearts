package com.example.implanthearts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 用户注册
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button register;

    private Button Return;
    String new_username;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityManager.addActivity(this);
        //初始化Bmob，应用的Application ID
        //Bmob.initialize(this, "应用的Application ID");
        Bmob.initialize(this, "a82e8317486115b0c1c9c16fe3668195");

        register = (Button)findViewById(R.id.register);
        Return = (Button)findViewById(R.id.Return);
        username =(EditText)findViewById(R.id.username);
        password =(EditText)findViewById(R.id.password);

        //注册账号密码
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的账号和密码
                new_username = username.getText().toString();
                String new_password = password.getText().toString();

                Log.w("qqq","register_username="+new_username);
                Log.w("qqq","register_password="+new_password);

                final User user = new User();
                user.setUsername(new_username);
                user.setPassword(new_password);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


                Dingshi p2 = new Dingshi();
                p2.setUsername(new_username);
                p2.setS1("0");
                p2.setS2("0");
                p2.setS3("0");
                p2.setS4("0");
                p2.setS5("0");
                p2.setS6("0");
                p2.setE1("0");
                p2.setE2("0");
                p2.setE3("0");
                p2.setE4("0");
                p2.setE5("0");
                p2.setE6("0");
                p2.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){

                        }else{

                        }
                    }
                });

                Find p = new Find();
                p.setIfbanding("0");
                p.setUsername(new_username);
                p.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){

                        }else{

                        }
                    }
                });


                //在注册的时候顺便把用户的名字，系统默认的头像图片上传到advertisement表中，方便用户查询更改头像
                //Bmob上默认头像的地址
                BmobFile bmobfile =new BmobFile("all.png","","http://bmob-cdn-25638.b0.upaiyun.com/2019/05/14/6356af9b40b740579394fd717fe7eb2e.png");
                bmobfile.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            //下载默认图片并长传到advertisement表中
                                    final BmobFile bmobFile = new BmobFile(new File(s));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    advertisement ad = new advertisement();
                    ad.setName(new_username);//用户名
                    ad.setPicture(bmobFile);//默认头像图片
                    ad.save();
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.w("bbb",bmobFile.getFileUrl());
                    Toast.makeText(RegisterActivity.this,"上传文件成功:" + bmobFile.getFileUrl(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this,"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
                        }
                    }
                });





            }
        });

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




}

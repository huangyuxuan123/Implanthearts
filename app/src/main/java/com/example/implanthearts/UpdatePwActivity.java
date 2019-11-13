package com.example.implanthearts;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePwActivity extends AppCompatActivity {
    private EditText old_password;
    private EditText new_password;
    private Button update_password;
    private ImageView change_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pw);
        ActivityManager.addActivity(this);
        old_password = (EditText)findViewById(R.id.old_password);
        new_password = (EditText)findViewById(R.id.new_password);
        update_password = (Button) findViewById(R.id.update_password);
        change_back = (ImageView) findViewById(R.id.change_back);


        //修改密码
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPwd = new_password.getText().toString();
                String oldPwd = old_password.getText().toString();
                //TODO 此处替换为你的旧密码和新密码
                BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(UpdatePwActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(UpdatePwActivity.this, "修改失败：" + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

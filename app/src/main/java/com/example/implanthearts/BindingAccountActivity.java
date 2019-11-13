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
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BindingAccountActivity extends AppCompatActivity {
    private ImageView binding_back;
    private EditText username;
    private Button binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_account);
        ActivityManager.addActivity(this);
        binding_back  = (ImageView)findViewById(R.id.binding_back);
        username = (EditText)findViewById(R.id.username);
        binding = (Button)findViewById(R.id.binding);

        binding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String binding_username = username.getText().toString();
                Binding p2 = new Binding();
                p2.setInitiative_username(HomeFragment.current_user);//自己的用户名
                p2.setPassive_username(binding_username);//绑定的对方的用户名
                p2.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
                            PersonFragment.id = objectId;
                            PersonFragment.user_bangname.setText("情感联系："+binding_username);
                            Toast.makeText(BindingAccountActivity.this,"绑定成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(BindingAccountActivity.this,"绑定失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        binding_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

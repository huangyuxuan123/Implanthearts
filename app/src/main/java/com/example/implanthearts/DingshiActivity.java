package com.example.implanthearts;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.implanthearts.HomeFragment.deviceId1;

public class DingshiActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView setting_back;
    private EditText FANSETH,FANSETM,SPRINKLERHSET,SPRINKLERMSET,YLIGHTHSET,YLIGHTMSET;
    //private Switch Switch_FAN,Switch_SPRINKLER,Switch_YLIGHT;

    private Button FAN_ON,FAN_OFF,SPRINKLER_ON,SPRINKLER_OFF,YLIGHT_ON,YLIGHT_OFF;
    private String edit_FANSETH,edit_FANSETM,edit_SPRINKLERHSET,edit_SPRINKLERMSET,edit_YLIGHTHSET,edit_YLIGHTMSET;

    private String fanseth,fanseym,sprinklerhset,sprinklermset,ylighthset,ylightmset;

    private String s1,s2,s3,s4,s5,s6;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 2000);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingshi);
        ActivityManager.addActivity(this);
        FANSETH = (EditText)findViewById(R.id.FANSETH);//风扇每过多少小时开一次
        FANSETM = (EditText)findViewById(R.id.FANSETM);//风扇每过多少分钟开一次
        SPRINKLERHSET = (EditText)findViewById(R.id.SPRINKLERHSET);//水泵每过多少小时开一次
        SPRINKLERMSET = (EditText)findViewById(R.id.SPRINKLERMSET);//水泵每过多少分钟开一次
        YLIGHTHSET = (EditText)findViewById(R.id.YLIGHTHSET);//黄灯每过多少小时开一次
        YLIGHTMSET = (EditText)findViewById(R.id.YLIGHTMSET);//黄灯每过多少分钟开一次

//        Switch_FAN = (Switch)findViewById(R.id.Switch_FAN);
//        Switch_SPRINKLER = (Switch)findViewById(R.id.Switch_SPRINKLER);
//        Switch_YLIGHT = (Switch)findViewById(R.id.Switch_YLIGHT);

        FAN_ON = (Button)findViewById(R.id.FAN_ON);
        FAN_OFF = (Button)findViewById(R.id.FAN_OFF);
        SPRINKLER_ON = (Button)findViewById(R.id.SPRINKLER_ON);
        SPRINKLER_OFF = (Button)findViewById(R.id.SPRINKLER_OFF);
        YLIGHT_ON = (Button)findViewById(R.id.YLIGHT_ON);
        YLIGHT_OFF = (Button)findViewById(R.id.YLIGHT_OFF);

        FAN_ON.setOnClickListener(this);
        FAN_OFF.setOnClickListener(this);
        SPRINKLER_ON.setOnClickListener(this);
        SPRINKLER_OFF.setOnClickListener(this);
        YLIGHT_ON.setOnClickListener(this);
        YLIGHT_OFF.setOnClickListener(this);


        setting_back = (ImageView)findViewById(R.id.setting_back);

        //回退到个人中心主页
        setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chazhao();


    }

    private void chazhao() {
        //查找状态
        //根据用户名查找
        BmobQuery<Dingshi> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("username", HomeFragment.current_user);
        categoryBmobQuery.findObjects(new FindListener<Dingshi>() {
            @Override
            public void done(List<Dingshi> object, BmobException e) {
                if (e == null) {
                    FANSETH.setText(object.get(0).getE1());
                    FANSETM.setText(object.get(0).getE2());
                    SPRINKLERHSET.setText(object.get(0).getE3());
                    SPRINKLERMSET.setText(object.get(0).getE4());
                    YLIGHTHSET.setText(object.get(0).getE5());
                    YLIGHTMSET.setText(object.get(0).getE6());

                    if(object.get(0).getS1()!=null && !object.get(0).getS1().equals("0") && object.get(0).getS2()!=null && !object.get(0).getS2().equals("0")){
                        FAN_ON.setEnabled(false);
                        FAN_OFF.setEnabled(true);
                    }else{
                        FAN_ON.setEnabled(true);
                        FAN_OFF.setEnabled(false);
                    }
                    if(object.get(0).getS3()!=null && !object.get(0).getS3().equals("0") && object.get(0).getS4()!=null && !object.get(0).getS4().equals("0")){
                        SPRINKLER_ON.setEnabled(false);
                        SPRINKLER_OFF.setEnabled(true);
                    }else {
                        SPRINKLER_ON.setEnabled(true);
                        SPRINKLER_OFF.setEnabled(false);
                    }
                    if(object.get(0).getS5()!=null && !object.get(0).getS5().equals("0") && object.get(0).getS6()!=null && !object.get(0).getS6().equals("0")){
                        YLIGHT_ON.setEnabled(false);
                        YLIGHT_OFF.setEnabled(true);
                    }else {
                        YLIGHT_ON.setEnabled(true);
                        YLIGHT_OFF.setEnabled(false);
                    }
                } else {
                    Log.w("fan","ttttttttttttttttttttttttttttttttt");
                }
            }
        });
    }


    public void update(String name){
        BmobQuery<Dingshi> query = new BmobQuery<Dingshi>();
        query.addWhereEqualTo("username", name);
        final String[] objectId = new String[1];

        query.findObjects(new FindListener<Dingshi>() {
            @Override
            public void done(List<Dingshi> list, BmobException e) {
                if (e == null){
                    for (Dingshi ad : list){
                        objectId[0] = ad.getObjectId();
                    }
                    Dingshi ad2 = new Dingshi();
                    ad2.setE1(edit_FANSETH);
                    ad2.setE2(edit_FANSETM);
                    ad2.setE3(edit_SPRINKLERHSET);
                    ad2.setE4(edit_SPRINKLERMSET);
                    ad2.setE5(edit_YLIGHTHSET);
                    ad2.setE6(edit_YLIGHTMSET);
                    ad2.setS1(s1);
                    ad2.setS2(s2);
                    ad2.setS3(s3);
                    ad2.setS4(s4);
                    ad2.setS5(s5);
                    ad2.setS6(s6);

                    ad2.update(objectId[0], new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.w("fan", "更新成功");
                            }else {
                                Log.w("fan", "失败"+e.getErrorCode());
                            }
                        }
                    });
                }else {
                    Log.w("fan", "获取id失败"+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.FAN_ON:
                FAN_ON.setEnabled(false);
                FAN_OFF.setEnabled(true);
                    s1 = "1";
                    s2 = "1";
                    //更新
                    update(HomeFragment.current_user);

                    edit_FANSETH = FANSETH.getText().toString().trim();
                    fanseth = "FANHSET"+edit_FANSETH;
                    edit_FANSETM = FANSETM.getText().toString().trim();
                    fanseym = "FANMSET"+edit_FANSETM;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                Getshuju getshuju=new Getshuju();
                                getshuju.GetaccessToken();
                                getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",fanseth);
                                handler.postDelayed(runnable, 2000);
                                getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",fanseym);

                            }
                            catch (Exception e)
                            {
                                Log.d("MainActivity", "++++++++++++++++");
                            }
                        }
                    }).start();
                break;

            case R.id.FAN_OFF:
                FAN_OFF.setEnabled(false);
                FAN_ON.setEnabled(true);
                s1 = "0";
                s2 = "0";
                //更新
                update(HomeFragment.current_user);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANHSET0");
                            handler.postDelayed(runnable, 2000);
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANMSET0");


                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                break;

            case R.id.SPRINKLER_ON:
                SPRINKLER_ON.setEnabled(false);
                SPRINKLER_OFF.setEnabled(true);
                s3 = "1";
                s4 = "1";
                //更新
                update(HomeFragment.current_user);

                edit_SPRINKLERHSET = SPRINKLERHSET.getText().toString().trim();
                sprinklerhset = "SPRINKLERHSET"+edit_SPRINKLERHSET;
                edit_SPRINKLERMSET = SPRINKLERMSET.getText().toString().trim();
                sprinklermset = "SPRINKLERMSET"+edit_SPRINKLERMSET;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",sprinklerhset);
                            handler.postDelayed(runnable, 2000);
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",sprinklermset);

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                break;

            case R.id.SPRINKLER_OFF:
                SPRINKLER_OFF.setEnabled(false);
                SPRINKLER_ON.setEnabled(true);
                s3 = "0";
                s4 = "0";
                //更新
                update(HomeFragment.current_user);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","SPRINKLERHSET0");
                            handler.postDelayed(runnable, 2000);
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","SPRINKLERMSET0");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                break;

            case R.id.YLIGHT_ON:
                YLIGHT_ON.setEnabled(false);
                YLIGHT_OFF.setEnabled(true);
                s5 = "1";
                s6 = "1";
                //更新
                update(HomeFragment.current_user);

                edit_YLIGHTHSET = YLIGHTHSET.getText().toString().trim();
                ylighthset = "YLIGHTHSET"+edit_YLIGHTHSET;
                edit_YLIGHTMSET = YLIGHTMSET.getText().toString().trim();
                ylightmset = "YLIGHTMSET"+edit_YLIGHTMSET;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",ylighthset);
                            handler.postDelayed(runnable, 2000);
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",ylightmset);

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                break;

            case R.id.YLIGHT_OFF:
                YLIGHT_OFF.setEnabled(false);
                YLIGHT_ON.setEnabled(true);
                s5 = "0";
                s6 = "0";
                //更新
                update(HomeFragment.current_user);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","YLIGHTHSET0");
                            handler.postDelayed(runnable, 2000);
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","YLIGHTMSET0");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                break;
        }
    }
}


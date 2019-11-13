package com.example.implanthearts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.implanthearts.HomeFragment.current_user;
import static com.example.implanthearts.WelcomeActivity.city;


/**
 * 用户登陆，获取天气预报,查找有没有被绑定账号并给个人中心赋值
 *///解析天气预报的类型
    public class LandingActivity extends Activity implements View.OnClickListener{
        private Button landing;
        private Button register;
        private EditText username;
        private EditText password;

        public  static  String user_name;
        String pass_word;

        Intent intent;
//    private  LocationClient locationClient;
//    public static StringBuilder currentPosition;
//    public static String city;

        public static Forecast type[] = new Forecast[15];


        public static String user_bang;
        public static String name;

        public static String initiative_username = null;
        public static String passive_username = null;

        //public static String deviceId,gatewayId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_landing);
            ActivityManager.addActivity(this);
            //初始化Bmob，应用的Application ID
            //Bmob.initialize(this, "应用的Application ID");
            Bmob.initialize(LandingActivity.this, "a82e8317486115b0c1c9c16fe3668195");

            landing = (Button) findViewById(R.id.landing);
            register = (Button) findViewById(R.id.register);
            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);

            landing.setOnClickListener(this);
            register.setOnClickListener(this);

            //没有标题栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }

            //得到城市名字之后查找对应城市的代码
            query_code();


        }

        //得到城市名字之后查找对应城市的代码
        private void query_code() {
            BmobQuery<weather> categoryBmobQuery = new BmobQuery<>();
            categoryBmobQuery.addWhereEqualTo("City", city);
            categoryBmobQuery.findObjects(new FindListener<weather>() {
                @Override
                public void done(List<weather> object, BmobException e) {
                    if (e == null) {
                        Log.w("ooo",object.get(0).getCode());

                        //根据城市代码获取天气状况
                        weather(object.get(0).getCode());

                    } else {
                        Log.w("ooo",e.toString());
                    }
                }
            });
        }

        //根据城市代码获取天气状况
        public void weather(final String code){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client =  new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://t.weather.sojson.com/api/weather/city/" + code)
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();

                        //解析天气预报的类型
                        parseJSONWithGSON(responseData);

                        Log.w("ooo",responseData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        private void parseJSONWithGSON( String responseData) {
        int k = 0;
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String data = jsonObject.getString("data");
            JSONObject jsonObject2 = new JSONObject(data);
            String forecast = jsonObject2.getString("forecast");

            Gson gson = new Gson();
            List<Forecast> fore = gson.fromJson(forecast, new TypeToken<List<Forecast>>() {
            }.getType());

            for (Forecast app : fore) {
                type[k] = new Forecast();
                type[k].setType(app.getType());
                k++;
            }

            Log.w("ooo", type[0].getType());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //查找有没有被绑定账号
    public void query() {
        BmobQuery<Binding> bmobQuery = new BmobQuery<Binding>();
        bmobQuery.addQueryKeys("initiative_username,passive_username");
        bmobQuery.findObjects(new FindListener<Binding>() {
//            String initiative_username = null;
//            String passive_username = null;
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
                if (passive_username != null && passive_username.equals(user_name)) {
//                    PersonFragment.user_bang.setText("绑定账号：" + initiative_username);
                    user_bang = "情感联系：" + initiative_username;
                    name = initiative_username;
                    Log.w("zzz", "user_bang=="+user_bang);
                    Log.w("zzz", "name=="+name);
                } else if (passive_username == null && initiative_username == null) {
//                    PersonFragment.user_bang.setText("未绑定账号");
                    user_bang = "未进行情感联系";
                    name = null;
                    Log.w("zzz", "user_bang=="+user_bang);
                    Log.w("zzz", "name=="+name);
                } else if (initiative_username != null && initiative_username.equals(user_name)) {
//                    PersonFragment.user_bang.setText("绑定账号：" + passive_username);
                    user_bang = "情感联系：" + passive_username;
                    name = passive_username;
                    Log.w("zzz", "user_bang=="+user_bang);
                    Log.w("zzz", "name=="+name);
                }

            }


        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.landing:
                //获取用户输入的账号和密码
                user_name = username.getText().toString();
                pass_word = password.getText().toString();

                //查找有没有被绑定账号,放在这里查询是因为要用到用户名
                query();

//                //获取用户的设备id和服务名,放在这里查询是因为要用到用户名
//                BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
//                categoryBmobQuery.addWhereEqualTo("username", user_name);
//                categoryBmobQuery.findObjects(new FindListener<User>() {
//                    @Override
//                    public void done(List<User> object, BmobException e) {
//                        if (e == null) {
//                            deviceId = object.get(0).getDeviceId();
//                            gatewayId = object.get(0).getDeviceId();
//                            Log.w("zzz", object.get(0).getUsername());
//                            Log.w("zzz", object.get(0).getDeviceId());
//                            Log.w("zzz", object.get(0).getServiceId());
//
//                        } else {
//                            Log.w("zzz", e.toString());
//                        }
//                    }
//                });



                //用户登陆
                final User user = new User();
                //此处替换为你的用户名
                user.setUsername(user_name);
                //此处替换为你的密码
                user.setPassword(pass_word);

                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User bmobUser, BmobException e) {
                        if (e == null) {
                            User user = BmobUser.getCurrentUser(User.class);
                            //Toast.makeText(LandingActivity.this, "登录成功：" + user.getUsername(), Toast.LENGTH_LONG).show();
                            //跳转到注册界面
                            //inputTitleDialog();
                            intent = new Intent(LandingActivity.this,DeviceActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LandingActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.w("lll", "登录失败：" + e.getMessage());
                        }
                    }
                });

                break;
            case R.id.register://跳转到注册界面
                intent = new Intent(LandingActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }



    private void inputTitleDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title)).setIcon(
                R.drawable.zhi).setView(inputServer).setNegativeButton(
                getString(R.string.quxiao), null);
        builder.setPositiveButton(getString(R.string.queding),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        intent = new Intent(LandingActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        builder.show();
    }

}

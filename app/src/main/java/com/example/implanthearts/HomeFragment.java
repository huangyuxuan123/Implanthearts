package com.example.implanthearts;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import static com.example.implanthearts.LandingActivity.type;

/**
 * 显示数据、天气状况、地点
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG =HomeFragment.class.getSimpleName();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
    private View view1, view2, view3, view4;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    public static String current_user;

    TextView textView1,textView2;//自己的，对方的

    private EditText Edit;
    private Button chazhao;
    private LinearLayout ll;
    private LinearLayout ll2;
    public  TextView textview;
    private Button jiebang;

    public static String hua_name;
    public static String id;

    //自己的
    private ImageView photo_wendu;
    private ImageView photo_turang;
    private ImageView photo_liaht;

    private TextView text_light;
    private TextView text_turang;
    private TextView text_wendu;

    private LinearLayout toast;
    private LinearLayout aa;

    private TextView text4_1,text4_2,text4_3,text4_4;

    private SwipeRefreshLayout mSwipeLayout;
    private boolean isRefresh = false;//是否刷新中

    private String weather;


    //对方的
    private ImageView photo_wendu2;
    private ImageView photo_turang2;
    private ImageView photo_liaht2;

    private TextView text_light2;
    private TextView text_turang2;
    private TextView text_wendu2;

    private SwipeRefreshLayout mSwipeLayout2;
    private boolean isRefresh2 = false;//是否刷新中

    //被绑定用户的设备id和服务，1是自己，2是对方
    public static String deviceId1,gatewayId1,deviceId2,gatewayId2;

    //用来显示被绑定用户所种植的花
    private TextView text_flowername;
    public static String weather2;
    public static String text_flower_name;


    private LinearLayout cc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_home, null);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mInflater = LayoutInflater.from(getContext());


        view1 = mInflater.inflate(R.layout.fragment_home_view1, null);
        view2 = mInflater.inflate(R.layout.fragment_home_view2, null);
        view3 = mInflater.inflate(R.layout.fragment_home_view3, null);

//        textView1=(TextView)view1.findViewById(R.id.text1);
//        textView1.setText("数据采集最新时间："+ Getshuju.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju.Temp+"℃"+"\n"+"湿度："+Getshuju.Humidity+"%"+"\n"+"土壤湿度："+Getshuju.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju.CO2+"ppm");


        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);

        //添加页卡标题
        mTitleList.add("自己的种植信息");
        mTitleList.add("对方的种植信息");
        mTitleList.add("高级选项");


        //添加tab选项卡，默认第一个选中
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)), true);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);

        //将TabLayout和ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);
        //给Tabs设置适配器
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        ll = (LinearLayout)view1.findViewById(R.id.ll);
        ll2 = (LinearLayout)view1.findViewById(R.id.ll2);
        textview = (TextView)view1.findViewById(R.id.textview);
        Edit = (EditText)view1.findViewById(R.id.Edit);
        chazhao = (Button)view1.findViewById(R.id.chazhao);
        jiebang = (Button)view1.findViewById(R.id.jiebang) ;

        //自己的
        photo_wendu = (ImageView)view1.findViewById(R.id.photo_wendu);
        photo_turang = (ImageView)view1.findViewById(R.id.photo_turang);
        photo_liaht = (ImageView)view1.findViewById(R.id.photo_liaht);

        text_light = (TextView)view1.findViewById(R.id.text_light);
        text_turang = (TextView)view1.findViewById(R.id.text_turang);
        text_wendu = (TextView)view1.findViewById(R.id.text_wendu);

        toast = (LinearLayout)view1.findViewById(R.id.toast);
        aa = (LinearLayout)view1.findViewById(R.id.aa);


        text4_1 = (TextView)view3.findViewById(R.id.text4_1);
        text4_2 = (TextView)view3.findViewById(R.id.text4_2);

        //对方的
        photo_wendu2 = (ImageView)view2.findViewById(R.id.photo_wendu2);
        photo_turang2 = (ImageView)view2.findViewById(R.id.photo_turang2);
        photo_liaht2 = (ImageView)view2.findViewById(R.id.photo_liaht2);

        text_light2 = (TextView)view2.findViewById(R.id.text_light2);
        text_turang2 = (TextView)view2.findViewById(R.id.text_turang2);
        text_wendu2 = (TextView)view2.findViewById(R.id.text_wendu2);

        text_flowername = (TextView)view2.findViewById(R.id.text_flowername);

        text4_3 = (TextView)view3.findViewById(R.id.text4_3);
        text4_4 = (TextView)view3.findViewById(R.id.text4_4);

        //设置SwipeRefreshLayout
        mSwipeLayout = (SwipeRefreshLayout) view1.findViewById(R.id.swipeLayout);
        mSwipeLayout2 = (SwipeRefreshLayout) view2.findViewById(R.id.swipeLayout2);

        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setDistanceToTriggerSync(300);
        mSwipeLayout2.setDistanceToTriggerSync(300);

        // 设定下拉圆圈的背景
        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeLayout2.setProgressBackgroundColorSchemeColor(Color.WHITE);

        // 设置圆圈的大小
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeLayout2.setSize(SwipeRefreshLayout.LARGE);

        //设置下拉刷新的监听
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout2.setOnRefreshListener(this);

        cc = (LinearLayout)view2.findViewById(R.id.cc);

        //初始化Bmob，应用的Application ID
        //Bmob.initialize(this, "应用的Application ID");
        Bmob.initialize(getContext(), "a82e8317486115b0c1c9c16fe3668195");

        //获取当前用户以及用户属性
        getusermessage();


        //用户自己
                //获取用户的设备id和服务名
                BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("username", current_user);
                categoryBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {
                            deviceId1 = object.get(0).getDeviceId();
                            gatewayId1 = object.get(0).getServiceId();
                            Log.w("zzz", object.get(0).getUsername());
                            Log.w("zzz", deviceId1);
                            Log.w("zzz", gatewayId1);

                        } else {
                            Log.w("zzz", e.toString());
                        }
                    }
                });

        //获取最新采集的数据，根据用户名
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Getshuju getshuju=new Getshuju();
                    getshuju.deviceId = deviceId1;
                    getshuju.gatewayId = gatewayId1;
                    getshuju.GetaccessToken();
                    getshuju.GetDataandTime();

                }


                catch (Exception e)
                {
                    Log.d("MainActivity", "++++++++++++++++"+e.getMessage());
                }
            }
        }).start();

        //每次进app的时候，查找hua表，有没有用户绑定的花，进而判断是否显示搜索栏
        query_hua();

        textView1=(TextView)view1.findViewById(R.id.text1);
        textView1.setText("数据采集最新时间："+ Getshuju.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju.Temp+"℃"+"\n"+"湿度："+Getshuju.Humidity+"%"+"\n"+"土壤湿度："+Getshuju.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju.CO2+"ppm");


        //绑定了账号后，根据被绑定的用户名查找其设备id和服务名
        isbinding();


        //被绑定用户
        //获取最新采集的数据，根据用户名
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Getshuju2 getshuju2=new Getshuju2();
                    getshuju2.deviceId = deviceId2;
                    getshuju2.gatewayId = gatewayId2;
                    getshuju2.GetaccessToken();
                    getshuju2.GetDataandTime();

                }


                catch (Exception e)
                {
                    Log.d("MainActivity", "++++++++++++++++"+e.getMessage());
                }
            }
        }).start();

        //被绑定用户的数据具体信息，动态改变显示图片
        query_hua2();

        textView2=(TextView)view2.findViewById(R.id.text2);
        textView2.setText("数据采集最新时间："+ Getshuju2.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju2.Temp+"℃"+"\n"+"湿度："+Getshuju2.Humidity+"%"+"\n"+"土壤湿度："+Getshuju2.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju2.CO2+"ppm");
        Log.w("zzz","22222222"+"数据采集最新时间："+ Getshuju2.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju2.Temp+"℃"+"\n"+"湿度："+Getshuju2.Humidity+"%"+"\n"+"土壤湿度："+Getshuju2.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju2.CO2+"ppm");


        //查找
        chazhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //绑定花
                hua_name=Edit.getText().toString();

                //绑定花之前要先查找表中有没有这个花
                BmobQuery<flower> bmobQuery = new BmobQuery<flower>();
                bmobQuery.addQueryKeys("name");
                bmobQuery.findObjects(new FindListener<flower>() {
                    @Override
                    public void done(List<flower> object, BmobException e) {
                        int n = 0;
                        for(flower f :object){
                            if(f.getName() != null && f.getName().equals(hua_name)){
                                //如果找得到用户想绑定的花就隐藏查找菜单和查找的按钮
                                ll.setVisibility(View.GONE);
                                //显示用户查找的花名和解绑的按钮
                                textview.setText(hua_name);
                                ll2.setVisibility(View.VISIBLE);

                                toast.setVisibility(View.GONE);
                                aa.setVisibility(View.VISIBLE);

                                //用户绑定该花
                                hua p2 = new hua();
                                p2.setUsername(current_user);//用户名
                                p2.setName(hua_name);//花名
                                p2.setWeather(type[0].getType());//天气状况
                                p2.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String objectId,BmobException e) {
                                        id = objectId;
                                        if(e==null){
                                            //Toast.makeText(getContext(),"添加数据成功，返回objectId为：",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getContext(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                break;
                            }
                            n++;

                        }
                        if (n == object.size()){
                            Toast.makeText(getContext(),"抱歉，找不到该花！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                //查找关于该花的参数信息（固定阈值）
                BmobQuery<flower> categoryBmobQuery = new BmobQuery<>();
                categoryBmobQuery.addWhereEqualTo("name", hua_name);
                categoryBmobQuery.findObjects(new FindListener<flower>() {
                    @Override
                    public void done(List<flower> object, BmobException e) {
                        if (e == null) {
                            aa.setVisibility(View.VISIBLE);
                            String wendu = object.get(0).getWendu();
                            String trshidu = object.get(0).getTrshidu();
                            String light = object.get(0).getLight();
                            Log.w("yyy",wendu);
                            Log.w("yyy",trshidu);
                            Log.w("yyy",light);
                        } else {
                            Log.w("yyy",e.toString());
                        }
                    }
                });


            }
        });

        //解绑
        //根据用户名查找id然后解绑
        jiebang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll2.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                toast.setVisibility(View.VISIBLE);
                aa.setVisibility(View.GONE);

                BmobQuery<hua> query = new BmobQuery<hua>();
                query.addWhereEqualTo("username",current_user);
                final String[] objectId = new String[1];

                query.findObjects(new FindListener<hua>() {
                    @Override
                    public void done(List<hua> list, BmobException e) {
                        if (e == null){
                            for (hua ad : list){
                                objectId[0] = ad.getObjectId();
                                Log.w("yyy", "获取id成功"+ objectId[0]);
                            }
                            final hua p2 = new hua();
                            p2.setObjectId(objectId[0]);
                            p2.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(getContext(),"删除成功:"+p2.getUpdatedAt(),Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(),"删除失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {
                            Log.w("yyy", "获取id失败"+e.getErrorCode());
                        }
                    }
                });

            }
        });



        return view;
    }

    public void getusermessage() {
        User user = BmobUser.getCurrentUser(User.class);
        current_user = user.getUsername();
        Log.w("ooo","当前用户："+user.getUsername());
    }



    private void query_hua() {
        //每次进app的时候，查找hua表，有没有用户绑定的花，进而判断是否显示搜索栏
        BmobQuery<hua> bmobQuery = new BmobQuery<hua>();
        bmobQuery.addQueryKeys("username,name,weather");//查找用户名
        bmobQuery.findObjects(new FindListener<hua>() {
            @Override
            public void done(List<hua> object, BmobException e) {
                int n = 0;
                for(hua f :object){
                    //测试
//                    try {
//                        Log.w("hhh", f.getUsername());
//                        Log.w("hhh", f.getName());
//                        Log.w("hhh", f.getWeather());
//                    }catch (Exception r){
//                        Log.w("hhh",r.getMessage());
//                    }
                    if(f.getUsername() != null && f.getUsername().equals(current_user)){
                        Log.w("hhh","f.getUsername()=="+f.getUsername());
                        Log.w("hhh","current_user=="+current_user);
                        //如果找得到
                        ll.setVisibility(View.GONE);
                        //显示用户查找的花名和解绑的按钮
                        textview.setText(f.getName());
                        Log.w("hhh","f.getName=="+f.getName());
                        weather = f.getWeather();
                        ll2.setVisibility(View.VISIBLE);

                        toast.setVisibility(View.GONE);
                        aa.setVisibility(View.VISIBLE);

                        break;
                    }

                    n++;
                }


                if(n!=object.size()) {
                    //查找关于该花的参数信息（固定阈值）
                    BmobQuery<flower> categoryBmobQuery = new BmobQuery<>();
                    categoryBmobQuery.addWhereEqualTo("name",textview.getText().toString());
                    categoryBmobQuery.findObjects(new FindListener<flower>() {
                        @Override
                        public void done(List<flower> object, BmobException e) {
                            if (e == null) {
                                String wendu = object.get(0).getWendu();
                                String trshidu = object.get(0).getTrshidu();
                                String light = object.get(0).getLight();
                                Log.w("yyy", wendu);
                                Log.w("yyy", trshidu);
                                Log.w("yyy", light);

                                toast.setVisibility(View.GONE);
                                aa.setVisibility(View.VISIBLE);

                                //根据天气预报（动态阈值）判断种植环境参数
                                //温度
                                String min_temp = wendu.substring(0, 2);
                                Log.w("yyy", "最小温度=" + min_temp);
                                String max_temp = wendu.substring(3, 5);
                                Log.w("yyy", "最大温度=" + max_temp);

                                //土壤湿度
                                String min_humidity = trshidu.substring(0, 2);
                                Log.w("yyy", "最小土壤温度=" + min_humidity);
                                String max_humidity = trshidu.substring(3, 5);
                                Log.w("yyy", "最小土壤温度=" + max_humidity);

                                //光照强度
                                String min_light = light.substring(0, 2);
                                Log.w("yyy", "最小光照强度=" + min_light);
                                String max_light = light.substring(3, 5);
                                Log.w("yyy", "最大光照强度=" + max_light);

                                //显示原始阈值
                                text4_2.setText("温度：" + "\n" + min_temp + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + min_humidity + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + max_light + "lx之间");

                                //根据天气情况设置动态阈值
                                if (weather.contains("晴")) {
                                    //温度下限-3 湿度上下限+5%  光下限-10，土壤上下限+5%

                                    //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(min_temp) - 3 && Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(max_temp)) {
                                        //温度正常
                                        photo_wendu.setBackgroundResource(R.drawable.san_griy);
                                        text_wendu.setText("温度正常");
                                    } else if (Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(min_temp) - 3) {
                                        //温度太低
                                        photo_wendu.setBackgroundResource(R.drawable.san_blue);
                                        text_wendu.setText("温度太低");
                                    } else if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(max_temp)) {
                                        //温度太高
                                        photo_wendu.setBackgroundResource(R.drawable.san_red);
                                        text_wendu.setText("温度太高");
                                    }

                                    //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(min_humidity) + 5 && Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(max_humidity) + 5) {
                                        //土壤湿度正常
                                        photo_turang.setBackgroundResource(R.drawable.water_griy);
                                        text_turang.setText("土壤湿度正常");
                                    } else if (Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(min_humidity) + 5) {
                                        //土壤湿度太低
                                        photo_turang.setBackgroundResource(R.drawable.water_blue);
                                        text_turang.setText("土壤湿度太低");
                                    } else if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(max_humidity) + 5) {
                                        //土壤湿度太高
                                        photo_turang.setBackgroundResource(R.drawable.water_red);
                                        text_turang.setText("土壤湿度太高");
                                    }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(min_light) - 10 && Integer.parseInt(Getshuju.Light) <= Integer.parseInt(max_light)) {
                                        //土壤湿度正常
                                        photo_liaht.setBackgroundResource(R.drawable.light_griy);
                                        text_light.setText("光照强度正常");
                                    } else if (Integer.parseInt(Getshuju.Light) <= Integer.parseInt(min_light) - 10) {
                                        //土壤湿度太低
                                        photo_liaht.setBackgroundResource(R.drawable.light_yellow);
                                        text_light.setText("光照强度太低");
                                    } else if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(max_light)) {
                                        //土壤湿度太高
                                        photo_liaht.setBackgroundResource(R.drawable.light_red);
                                        text_light.setText("光照强度太高");
                                    }

                                    //显示动态阈值
                                    text4_1.setText("温度：" + "\n" + (Integer.parseInt(min_temp) - 3) + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) + 5) + "% ~ " + (Integer.parseInt(max_humidity) + 5) + "%之间" + "\n" + "光照强度：" + "\n" + (Integer.parseInt(min_light) - 10) + "lx ~ " + max_light + "lx之间");

                                } else if (weather.contains("雨")) {
                                    //温度上下限+3 湿度下限-10%  光上限+15，土壤下限-10%
                                    Log.w("yyy", "温度变" + (Integer.parseInt(min_temp) + 3));

                                    //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(min_temp) + 3 && Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(max_temp) + 3) {
                                        //温度正常
                                        photo_wendu.setBackgroundResource(R.drawable.san_griy);
                                        text_wendu.setText("温度正常");
                                    } else if (Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(min_temp) + 3) {
                                        //温度太低
                                        photo_wendu.setBackgroundResource(R.drawable.san_blue);
                                        text_wendu.setText("温度太低");
                                    } else if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(max_temp) + 3) {
                                        //温度太高
                                        photo_wendu.setBackgroundResource(R.drawable.san_red);
                                        text_wendu.setText("温度太高");
                                    }

                                    //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(min_humidity) - 10 && Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(max_humidity)) {
                                        //土壤湿度正常
                                        photo_turang.setBackgroundResource(R.drawable.water_griy);
                                        text_turang.setText("土壤湿度正常");
                                    } else if (Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(min_humidity) - 10) {
                                        //土壤湿度太低
                                        photo_turang.setBackgroundResource(R.drawable.water_blue);
                                        text_turang.setText("土壤湿度太低");
                                    } else if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(max_humidity)) {
                                        //土壤湿度太高
                                        photo_turang.setBackgroundResource(R.drawable.water_red);
                                        text_turang.setText("土壤湿度太高");
                                    }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju.Light) <= Integer.parseInt(max_light) + 15) {
                                        //土壤湿度正常
                                        photo_liaht.setBackgroundResource(R.drawable.light_griy);
                                        text_light.setText("光照强度正常");
                                    } else if (Integer.parseInt(Getshuju.Light) <= Integer.parseInt(min_light)) {
                                        //土壤湿度太低
                                        photo_liaht.setBackgroundResource(R.drawable.light_yellow);
                                        text_light.setText("光照强度太低");
                                    } else if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(max_light) + 15) {
                                        //土壤湿度太高
                                        photo_liaht.setBackgroundResource(R.drawable.light_red);
                                        text_light.setText("光照强度太高");
                                    }

                                    text4_1.setText("温度：" + "\n" + (Integer.parseInt(min_temp) + 3) + "℃ ~ " + (Integer.parseInt(max_temp) + 3) + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) - 10) + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + (Integer.parseInt(max_light) + 15) + "lx之间");

                                } else if (weather.equals("多云")) {
                                    //温度不变 湿度下限—3%  光上限+10，土壤下限-3%

                                    //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(min_temp) && Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(max_temp)) {
                                        //温度正常
                                        photo_wendu.setBackgroundResource(R.drawable.san_griy);
                                        text_wendu.setText("温度正常");
                                    } else if (Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(min_temp)) {
                                        //温度太低
                                        photo_wendu.setBackgroundResource(R.drawable.san_blue);
                                        text_wendu.setText("温度太低");
                                    } else if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(max_temp)) {
                                        //温度太高
                                        photo_wendu.setBackgroundResource(R.drawable.san_red);
                                        text_wendu.setText("温度太高");
                                    }

                                    //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(min_humidity) - 3 && Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(max_humidity)) {
                                        //土壤湿度正常
                                        photo_turang.setBackgroundResource(R.drawable.water_griy);
                                        text_turang.setText("土壤湿度正常");
                                    } else if (Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(min_humidity) - 3) {
                                        //土壤湿度太低
                                        photo_turang.setBackgroundResource(R.drawable.water_blue);
                                        text_turang.setText("土壤湿度太低");
                                    } else if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(max_humidity)) {
                                        //土壤湿度太高
                                        photo_turang.setBackgroundResource(R.drawable.water_red);
                                        text_turang.setText("土壤湿度太高");
                                    }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju.Light) <= Integer.parseInt(max_light) + 10) {
                                        //土壤湿度正常
                                        photo_liaht.setBackgroundResource(R.drawable.light_griy);
                                        text_light.setText("光照强度正常");
                                    } else if (Integer.parseInt(Getshuju.Light) <= Integer.parseInt(min_light)) {
                                        //土壤湿度太低
                                        photo_liaht.setBackgroundResource(R.drawable.light_yellow);
                                        text_light.setText("光照强度太低");
                                    } else if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(max_light) + 10) {
                                        //土壤湿度太高
                                        photo_liaht.setBackgroundResource(R.drawable.light_red);
                                        text_light.setText("光照强度太高");
                                    }

                                    text4_1.setText("温度：" + "\n" + min_temp + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) - 3) + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + (Integer.parseInt(max_light) + 10) + "lx之间");

                                } else if (weather.contains("阴")) {
                                    //温度上下限+2 湿度下限-5%  光上限+10，土壤下限-5%

                                    //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(min_temp) + 2 && Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(max_temp) + 2) {
                                        //温度正常
                                        photo_wendu.setBackgroundResource(R.drawable.san_griy);
                                        text_wendu.setText("温度正常");
                                    } else if (Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(min_temp) + 2) {
                                        //温度太低
                                        photo_wendu.setBackgroundResource(R.drawable.san_blue);
                                        text_wendu.setText("温度太低");
                                    } else if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(max_temp) + 2) {
                                        //温度太高
                                        photo_wendu.setBackgroundResource(R.drawable.san_red);
                                        text_wendu.setText("温度太高");
                                    }

                                    //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(min_humidity) - 5 && Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(max_humidity)) {
                                        //土壤湿度正常
                                        photo_turang.setBackgroundResource(R.drawable.water_griy);
                                        text_turang.setText("土壤湿度正常");
                                    } else if (Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(min_humidity) - 5) {
                                        //土壤湿度太低
                                        photo_turang.setBackgroundResource(R.drawable.water_blue);
                                        text_turang.setText("土壤湿度太低");
                                        Log.w("turang", Getshuju.Soil);
                                    } else if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(max_humidity)) {
                                        //土壤湿度太高
                                        photo_turang.setBackgroundResource(R.drawable.water_red);
                                        text_turang.setText("土壤湿度太高");
                                    }


//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju.Light) <= Integer.parseInt(max_light) + 10) {
                                        //土壤湿度正常
                                        photo_liaht.setBackgroundResource(R.drawable.light_griy);
                                        text_light.setText("光照强度正常");
                                    } else if (Integer.parseInt(Getshuju.Light) <= Integer.parseInt(min_light)) {
                                        //土壤湿度太低
                                        photo_liaht.setBackgroundResource(R.drawable.light_yellow);
                                        text_light.setText("光照强度太低");
                                    } else if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(max_light) + 10) {
                                        //土壤湿度太高
                                        photo_liaht.setBackgroundResource(R.drawable.light_red);
                                        text_light.setText("光照强度太高");
                                    }

                                    text4_1.setText("温度：" + "\n" + (Integer.parseInt(min_temp) + 2) + "℃ ~ " + (Integer.parseInt(max_temp) + 2) + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) - 5) + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + (Integer.parseInt(max_light) + 10) + "lx之间");


                                } else {
                                    //不变
                                    //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(min_temp) && Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(max_temp)) {
                                        //温度正常
                                        photo_wendu.setBackgroundResource(R.drawable.san_griy);
                                        text_wendu.setText("温度正常");
                                    } else if (Integer.parseInt(Getshuju.Temp) <= Integer.parseInt(min_temp)) {
                                        //温度太低
                                        photo_wendu.setBackgroundResource(R.drawable.san_blue);
                                        text_wendu.setText("温度太低");
                                    } else if (Integer.parseInt(Getshuju.Temp) >= Integer.parseInt(max_temp)) {
                                        //温度太高
                                        photo_wendu.setBackgroundResource(R.drawable.san_red);
                                        text_wendu.setText("温度太高");
                                    }

                                    //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(min_humidity) && Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(max_humidity)) {
                                        //土壤湿度正常
                                        photo_turang.setBackgroundResource(R.drawable.water_griy);
                                        text_turang.setText("土壤湿度正常");
                                    } else if (Integer.parseInt(Getshuju.Soil) <= Integer.parseInt(min_humidity)) {
                                        //土壤湿度太低
                                        photo_turang.setBackgroundResource(R.drawable.water_blue);
                                        text_turang.setText("土壤湿度太低");
                                    } else if (Integer.parseInt(Getshuju.Soil) >= Integer.parseInt(max_humidity)) {
                                        //土壤湿度太高
                                        photo_turang.setBackgroundResource(R.drawable.water_red);
                                        text_turang.setText("土壤湿度太高");
                                    }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                                    if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju.Light) <= Integer.parseInt(max_light)) {
                                        //土壤湿度正常
                                        photo_liaht.setBackgroundResource(R.drawable.light_griy);
                                        text_light.setText("光照强度正常");
                                    } else if (Integer.parseInt(Getshuju.Light) <= Integer.parseInt(min_light)) {
                                        //土壤湿度太低
                                        photo_liaht.setBackgroundResource(R.drawable.light_yellow);
                                        text_light.setText("光照强度太低");
                                    } else if (Integer.parseInt(Getshuju.Light) >= Integer.parseInt(max_light)) {
                                        //土壤湿度太高
                                        photo_liaht.setBackgroundResource(R.drawable.light_red);
                                        text_light.setText("光照强度太高");
                                    }

                                    text4_1.setText("温度：" + "\n" + min_temp + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + min_humidity + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + max_light + "lx之间");


                                }


                            } else {
                                Log.w("yyy", e.toString());
                            }
                        }
                    });


                }

                //}else {
                //    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                //}
            }
        });
    }



    //绑定了账号后，根据被绑定的用户名查找其设备id和服务名
    private void isbinding() {
        if (LandingActivity.name != null) {
            BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
            categoryBmobQuery.addWhereEqualTo("username", LandingActivity.name);
            categoryBmobQuery.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if (e == null) {
                        deviceId2 = object.get(0).getDeviceId();
                        Log.w("zzz","deviceId2"+deviceId2);
                        gatewayId2 = object.get(0).getServiceId();
                        Log.w("zzz", "gatewayId2"+gatewayId2);
                        Log.w("zzz", object.get(0).getUsername());
                        Log.w("zzz", object.get(0).getDeviceId());
                        Log.w("zzz", object.get(0).getServiceId());

                    } else {
                        Log.w("zzz", e.toString());
                    }
                }
            });

            //查找被绑定用户的花名，天气
            BmobQuery<hua> BmobQuery = new BmobQuery<>();
            BmobQuery.addWhereEqualTo("username", LandingActivity.name);
            BmobQuery.findObjects(new FindListener<hua>() {
                @Override
                public void done(List<hua> object, BmobException e) {
                    if (e == null) {
                        text_flowername.setText(object.get(0).getName());
                        weather2 =  object.get(0).getWeather();
                        text_flower_name  = object.get(0).getName();
                        Log.w("zzz", object.get(0).getUsername());//用户名
                        Log.w("zzz", object.get(0).getWeather());//天气状况
                        Log.w("zzz", object.get(0).getName());//花名

                    } else {
                        Log.w("zzz", e.toString());
                    }
                }
            });


        } else if (LandingActivity.name == null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("温馨提示")
                    .setMessage("还未与其他账号进行情感联系！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getContext(), "请先绑定账号！", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    }



    //被绑定用户的数据具体信息，动态改变显示图片
    private void query_hua2() {
        cc.setVisibility(View.VISIBLE);
        //查找关于该花的参数信息（固定阈值）
        Log.w("yyy","text_flower_name=="+text_flower_name);
        Log.w("yyy","weather2=="+weather2);
        BmobQuery<flower> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("name",text_flower_name);
        categoryBmobQuery.findObjects(new FindListener<flower>() {
            @Override
            public void done(List<flower> object, BmobException e) {
                if (e == null) {
                    String wendu = object.get(0).getWendu();
                    String trshidu = object.get(0).getTrshidu();
                    String light = object.get(0).getLight();
                    Log.w("yyy", wendu);
                    Log.w("yyy", trshidu);
                    Log.w("yyy", light);

//                    toast.setVisibility(View.GONE);
//                    aa.setVisibility(View.VISIBLE);

                    //根据天气预报（动态阈值）判断种植环境参数
                    //温度
                    String min_temp = wendu.substring(0, 2);
                    Log.w("yyy", "最小温度=" + min_temp);
                    String max_temp = wendu.substring(3, 5);
                    Log.w("yyy", "最大温度=" + max_temp);

                    //土壤湿度
                    String min_humidity = trshidu.substring(0, 2);
                    Log.w("yyy", "最小土壤温度=" + min_humidity);
                    String max_humidity = trshidu.substring(3, 5);
                    Log.w("yyy", "最小土壤温度=" + max_humidity);

                    //光照强度
                    String min_light = light.substring(0, 2);
                    Log.w("yyy", "最小光照强度=" + min_light);
                    String max_light = light.substring(3, 5);
                    Log.w("yyy", "最大光照强度=" + max_light);

                    //显示原始阈值
                    text4_4.setText("温度：" + "\n" + min_temp + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + min_humidity + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + max_light + "lx之间");

                    //根据天气情况设置动态阈值
                    if (weather2.contains("晴")) {
                        //温度下限-3 湿度上下限+5%  光下限-10，土壤上下限+5%

                        //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(min_temp) - 3 && Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(max_temp)) {
                            //温度正常
                            photo_wendu2.setBackgroundResource(R.drawable.san_griy);
                            text_wendu2.setText("温度正常");
                        } else if (Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(min_temp) - 3) {
                            //温度太低
                            photo_wendu2.setBackgroundResource(R.drawable.san_blue);
                            text_wendu2.setText("温度太低");
                        } else if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(max_temp)) {
                            //温度太高
                            photo_wendu2.setBackgroundResource(R.drawable.san_red);
                            text_wendu2.setText("温度太高");
                        }

                        //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(min_humidity) + 5 && Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(max_humidity) + 5) {
                            //土壤湿度正常
                            photo_turang2.setBackgroundResource(R.drawable.water_griy);
                            text_turang2.setText("土壤湿度正常");
                        } else if (Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(min_humidity) + 5) {
                            //土壤湿度太低
                            photo_turang2.setBackgroundResource(R.drawable.water_blue);
                            text_turang2.setText("土壤湿度太低");
                        } else if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(max_humidity) + 5) {
                            //土壤湿度太高
                            photo_turang2.setBackgroundResource(R.drawable.water_red);
                            text_turang2.setText("土壤湿度太高");
                        }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(min_light) - 10 && Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(max_light)) {
                            //土壤湿度正常
                            photo_liaht2.setBackgroundResource(R.drawable.light_griy);
                            text_light2.setText("光照强度正常");
                        } else if (Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(min_light) - 10) {
                            //土壤湿度太低
                            photo_liaht2.setBackgroundResource(R.drawable.light_yellow);
                            text_light2.setText("光照强度太低");
                        } else if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(max_light)) {
                            //土壤湿度太高
                            photo_liaht2.setBackgroundResource(R.drawable.light_red);
                            text_light2.setText("光照强度太高");
                        }

                        //显示动态阈值
                        text4_3.setText("温度：" + "\n" + (Integer.parseInt(min_temp) - 3) + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) + 5) + "% ~ " + (Integer.parseInt(max_humidity) + 5) + "%之间" + "\n" + "光照强度：" + "\n" + (Integer.parseInt(min_light) - 10) + "lx ~ " + max_light + "lx之间");

                    } else if (weather2.contains("雨")) {
                        //温度上下限+3 湿度下限-10%  光上限+15，土壤下限-10%
                        Log.w("yyy", "温度变" + (Integer.parseInt(min_temp) + 3));

                        //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(min_temp) + 3 && Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(max_temp) + 3) {
                            //温度正常
                            photo_wendu2.setBackgroundResource(R.drawable.san_griy);
                            text_wendu2.setText("温度正常");
                        } else if (Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(min_temp) + 3) {
                            //温度太低
                            photo_wendu2.setBackgroundResource(R.drawable.san_blue);
                            text_wendu2.setText("温度太低");
                        } else if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(max_temp) + 3) {
                            //温度太高
                            photo_wendu2.setBackgroundResource(R.drawable.san_red);
                            text_wendu2.setText("温度太高");
                        }

                        //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(min_humidity) - 10 && Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(max_humidity)) {
                            //土壤湿度正常
                            photo_turang2.setBackgroundResource(R.drawable.water_griy);
                            text_turang2.setText("土壤湿度正常");
                        } else if (Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(min_humidity) - 10) {
                            //土壤湿度太低
                            photo_turang2.setBackgroundResource(R.drawable.water_blue);
                            text_turang2.setText("土壤湿度太低");
                        } else if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(max_humidity)) {
                            //土壤湿度太高
                            photo_turang2.setBackgroundResource(R.drawable.water_red);
                            text_turang2.setText("土壤湿度太高");
                        }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(max_light) + 15) {
                            //土壤湿度正常
                            photo_liaht2.setBackgroundResource(R.drawable.light_griy);
                            text_light2.setText("光照强度正常");
                        } else if (Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(min_light)) {
                            //土壤湿度太低
                            photo_liaht2.setBackgroundResource(R.drawable.light_yellow);
                            text_light2.setText("光照强度太低");
                        } else if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(max_light) + 15) {
                            //土壤湿度太高
                            photo_liaht2.setBackgroundResource(R.drawable.light_red);
                            text_light2.setText("光照强度太高");
                        }

                        text4_3.setText("温度：" + "\n" + (Integer.parseInt(min_temp) + 3) + "℃ ~ " + (Integer.parseInt(max_temp) + 3) + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) - 10) + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + (Integer.parseInt(max_light) + 15) + "lx之间");

                    } else if (weather2.equals("多云")) {
                        //温度不变 湿度下限—3%  光上限+10，土壤下限-3%

                        //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(min_temp) && Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(max_temp)) {
                            //温度正常
                            photo_wendu2.setBackgroundResource(R.drawable.san_griy);
                            text_wendu2.setText("温度正常");
                        } else if (Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(min_temp)) {
                            //温度太低
                            photo_wendu2.setBackgroundResource(R.drawable.san_blue);
                            text_wendu2.setText("温度太低");
                        } else if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(max_temp)) {
                            //温度太高
                            photo_wendu2.setBackgroundResource(R.drawable.san_red);
                            text_wendu2.setText("温度太高");
                        }

                        //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(min_humidity) - 3 && Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(max_humidity)) {
                            //土壤湿度正常
                            photo_turang2.setBackgroundResource(R.drawable.water_griy);
                            text_turang2.setText("土壤湿度正常");
                        } else if (Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(min_humidity) - 3) {
                            //土壤湿度太低
                            photo_turang2.setBackgroundResource(R.drawable.water_blue);
                            text_turang2.setText("土壤湿度太低");
                        } else if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(max_humidity)) {
                            //土壤湿度太高
                            photo_turang2.setBackgroundResource(R.drawable.water_red);
                            text_turang2.setText("土壤湿度太高");
                        }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(max_light) + 10) {
                            //土壤湿度正常
                            photo_liaht2.setBackgroundResource(R.drawable.light_griy);
                            text_light2.setText("光照强度正常");
                        } else if (Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(min_light)) {
                            //土壤湿度太低
                            photo_liaht2.setBackgroundResource(R.drawable.light_yellow);
                            text_light2.setText("光照强度太低");
                        } else if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(max_light) + 10) {
                            //土壤湿度太高
                            photo_liaht2.setBackgroundResource(R.drawable.light_red);
                            text_light2.setText("光照强度太高");
                        }

                        text4_3.setText("温度：" + "\n" + min_temp + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) - 3) + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + (Integer.parseInt(max_light) + 10) + "lx之间");

                    } else if (weather2.contains("阴")) {
                        //温度上下限+2 湿度下限-5%  光上限+10，土壤下限-5%

                        //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(min_temp) + 2 && Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(max_temp) + 2) {
                            //温度正常
                            photo_wendu2.setBackgroundResource(R.drawable.san_griy);
                            text_wendu2.setText("温度正常");
                        } else if (Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(min_temp) + 2) {
                            //温度太低
                            photo_wendu2.setBackgroundResource(R.drawable.san_blue);
                            text_wendu2.setText("温度太低");
                        } else if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(max_temp) + 2) {
                            //温度太高
                            photo_wendu2.setBackgroundResource(R.drawable.san_red);
                            text_wendu2.setText("温度太高");
                        }

                        //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(min_humidity) - 5 && Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(max_humidity)) {
                            //土壤湿度正常
                            photo_turang2.setBackgroundResource(R.drawable.water_griy);
                            text_turang2.setText("土壤湿度正常");
                        } else if (Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(min_humidity) - 5) {
                            //土壤湿度太低
                            photo_turang2.setBackgroundResource(R.drawable.water_blue);
                            text_turang2.setText("土壤湿度太低");
                            Log.w("turang", Getshuju2.Soil);
                        } else if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(max_humidity)) {
                            //土壤湿度太高
                            photo_turang2.setBackgroundResource(R.drawable.water_red);
                            text_turang2.setText("土壤湿度太高");
                        }


//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(max_light) + 10) {
                            //土壤湿度正常
                            photo_liaht2.setBackgroundResource(R.drawable.light_griy);
                            text_light2.setText("光照强度正常");
                        } else if (Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(min_light)) {
                            //土壤湿度太低
                            photo_liaht2.setBackgroundResource(R.drawable.light_yellow);
                            text_light2.setText("光照强度太低");
                        } else if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(max_light) + 10) {
                            //土壤湿度太高
                            photo_liaht2.setBackgroundResource(R.drawable.light_red);
                            text_light2.setText("光照强度太高");
                        }

                        text4_3.setText("温度：" + "\n" + (Integer.parseInt(min_temp) + 2) + "℃ ~ " + (Integer.parseInt(max_temp) + 2) + "℃之间" + "\n" + "土壤湿度：" + "\n" + (Integer.parseInt(min_humidity) - 5) + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + (Integer.parseInt(max_light) + 10) + "lx之间");


                    } else {
                        //不变
                        //如果当前采集到的温度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(min_temp) && Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(max_temp)) {
                            //温度正常
                            photo_wendu2.setBackgroundResource(R.drawable.san_griy);
                            text_wendu2.setText("温度正常");
                        } else if (Integer.parseInt(Getshuju2.Temp) <= Integer.parseInt(min_temp)) {
                            //温度太低
                            photo_wendu2.setBackgroundResource(R.drawable.san_blue);
                            text_wendu2.setText("温度太低");
                        } else if (Integer.parseInt(Getshuju2.Temp) >= Integer.parseInt(max_temp)) {
                            //温度太高
                            photo_wendu2.setBackgroundResource(R.drawable.san_red);
                            text_wendu2.setText("温度太高");
                        }

                        //如果当前采集到的土壤湿度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(min_humidity) && Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(max_humidity)) {
                            //土壤湿度正常
                            photo_turang2.setBackgroundResource(R.drawable.water_griy);
                            text_turang2.setText("土壤湿度正常");
                        } else if (Integer.parseInt(Getshuju2.Soil) <= Integer.parseInt(min_humidity)) {
                            //土壤湿度太低
                            photo_turang2.setBackgroundResource(R.drawable.water_blue);
                            text_turang2.setText("土壤湿度太低");
                        } else if (Integer.parseInt(Getshuju2.Soil) >= Integer.parseInt(max_humidity)) {
                            //土壤湿度太高
                            photo_turang2.setBackgroundResource(R.drawable.water_red);
                            text_turang2.setText("土壤湿度太高");
                        }

//									//如果当前采集到的光照强度不在花的适宜范围内,更换图片颜色
                        if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(min_light) && Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(max_light)) {
                            //土壤湿度正常
                            photo_liaht2.setBackgroundResource(R.drawable.light_griy);
                            text_light2.setText("光照强度正常");
                        } else if (Integer.parseInt(Getshuju2.Light) <= Integer.parseInt(min_light)) {
                            //土壤湿度太低
                            photo_liaht2.setBackgroundResource(R.drawable.light_yellow);
                            text_light2.setText("光照强度太低");
                        } else if (Integer.parseInt(Getshuju2.Light) >= Integer.parseInt(max_light)) {
                            //土壤湿度太高
                            photo_liaht2.setBackgroundResource(R.drawable.light_red);
                            text_light2.setText("光照强度太高");
                        }

                        text4_3.setText("温度：" + "\n" + min_temp + "℃ ~ " + max_temp + "℃之间" + "\n" + "土壤湿度：" + "\n" + min_humidity + "% ~ " + max_humidity + "%之间" + "\n" + "光照强度：" + "\n" + min_light + "lx ~ " + max_light + "lx之间");


                    }


                } else {
                    Log.w("yyy", e.toString());
                }
            }
        });
    }

    @Override
    protected View initView() {
        return null;
    }

    @Override
    protected void initData() {
    }

    public  String initiative_username = null;
    public  String passive_username = null;

    @Override
    public void onRefresh() {
        //检查是否处于刷新状态
        if (!isRefresh) {
            isRefresh = true;
            //模拟加载网络数据，这里设置4秒，正好能看到4色进度条
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //显示或隐藏刷新进度条
                    mSwipeLayout.setRefreshing(false);
                    //修改数据

                    //获取采集数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                Getshuju getshuju=new Getshuju();
                                getshuju.deviceId = deviceId1;
                                getshuju.gatewayId = gatewayId1;
                                getshuju.GetaccessToken();
                                getshuju.GetDataandTime();

                            }

                            catch (Exception e)
                            {
                                Log.d("MainActivity", "++++++++++++++++");
                            }
                        }
                    }).start();

                    //更新首页显示的采集的数据
                    textView1.setText("数据采集最新时间："+ Getshuju.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju.Temp+"℃"+"\n"+"湿度："+Getshuju.Humidity+"%"+"\n"+"土壤湿度："+Getshuju.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju.CO2+"ppm");
                    Log.w("zzz","111111"+"数据采集最新时间："+ Getshuju.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju.Temp+"℃"+"\n"+"湿度："+Getshuju.Humidity+"%"+"\n"+"土壤湿度："+Getshuju.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju.CO2+"ppm");


                    query_hua();

                    //更新hua表中的天气状况
                    //从hua表中找到用户名的id，根据id更新weather天气状况
                    update_weather(current_user);

                    isRefresh = false;
                }
            }, 2000);
        }

        //检查是否处于刷新状态
        if (!isRefresh2) {
            isRefresh2 = true;
            //模拟加载网络数据，这里设置4秒，正好能看到4色进度条
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //显示或隐藏刷新进度条
                    mSwipeLayout2.setRefreshing(false);
                    //修改数据

                    //是否绑定了账号
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
                            if (passive_username != null && passive_username.equals(current_user)) {
                                BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                                categoryBmobQuery.addWhereEqualTo("username",initiative_username);
                                categoryBmobQuery.findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(final List<User> object, BmobException e) {
                                        if (e == null) {
                                            deviceId2 = object.get(0).getDeviceId();
                                            Log.w("jjj","deviceId2"+deviceId2);
                                            gatewayId2 = object.get(0).getServiceId();
                                            Log.w("jjj", "gatewayId2"+gatewayId2);
                                            Log.w("jjj", object.get(0).getUsername());
                                            Log.w("jjj", object.get(0).getDeviceId());
                                            Log.w("jjj", object.get(0).getServiceId());

                                            //绑定了账号后获取数据
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try
                                                    {
                                                        Getshuju2 getshuju2=new Getshuju2();
                                                        getshuju2.deviceId = object.get(0).getDeviceId();//deviceId2;
                                                        getshuju2.gatewayId = object.get(0).getServiceId();//gatewayId2;
                                                        getshuju2.GetaccessToken();
                                                        getshuju2.GetDataandTime();

                                                    }

                                                    catch (Exception e)
                                                    {
                                                        Log.d("MainActivity", "++++++++++++++++");
                                                    }
                                                }
                                            }).start();

                                        } else {
                                            Log.w("zzz", e.toString());
                                        }
                                    }
                                });



                                textView2.setText("数据采集最新时间："+ Getshuju2.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju2.Temp+"℃"+"\n"+"湿度："+Getshuju2.Humidity+"%"+"\n"+"土壤湿度："+Getshuju2.Soil+"%"+"\n"+"光照强度："+Getshuju2.Light+"lx"+"\n"+"CO2浓度："+Getshuju2.CO2+"ppm");
                                Log.w("zzz","22222222"+"数据采集最新时间："+ Getshuju2.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju2.Temp+"℃"+"\n"+"湿度："+Getshuju2.Humidity+"%"+"\n"+"土壤湿度："+Getshuju2.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju2.CO2+"ppm");

                                //查找被绑定用户的花名，天气
                                BmobQuery<hua> BmobQuery = new BmobQuery<>();
                                BmobQuery.addWhereEqualTo("username", passive_username);
                                BmobQuery.findObjects(new FindListener<hua>() {
                                    @Override
                                    public void done(List<hua> object, BmobException e) {
                                        if (e == null) {
                                            text_flowername.setText(object.get(0).getName());
                                            weather2 =  object.get(0).getWeather();
                                            text_flower_name  = object.get(0).getName();
                                            Log.w("zzz", object.get(0).getUsername());//用户名
                                            Log.w("zzz", object.get(0).getWeather());//天气状况
                                            Log.w("zzz", object.get(0).getName());//花名

                                        } else {
                                            Log.w("zzz", e.toString());
                                        }
                                    }
                                });

                                query_hua2();
                            } else if (passive_username == null && initiative_username == null) {
                                text_flowername.setText("未进行情感联系");
                                cc.setVisibility(View.INVISIBLE);
                                textView2.setText("数据采集最新时间："+ null+"\n"+"天气状况："+null+"\n"+"温度："+ null+"℃"+"\n"+"湿度："+null+"%"+"\n"+"土壤湿度："+null+"%"+"\n"+"光照强度："+null+"lx"+"\n"+"CO2浓度："+null+"ppm");

                            } else if (initiative_username != null && initiative_username.equals(current_user)) {
                                BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                                categoryBmobQuery.addWhereEqualTo("username",passive_username);
                                categoryBmobQuery.findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(final List<User> object, BmobException e) {
                                        if (e == null) {
                                            deviceId2 = object.get(0).getDeviceId();
                                            Log.w("jjj","deviceId2"+deviceId2);
                                            gatewayId2 = object.get(0).getServiceId();
                                            Log.w("jjj", "gatewayId2"+gatewayId2);
                                            Log.w("jjj", object.get(0).getUsername());
                                            Log.w("jjj", object.get(0).getDeviceId());
                                            Log.w("jjj", object.get(0).getServiceId());

                                            //绑定了账号后获取数据
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try
                                                    {
                                                        Getshuju2 getshuju2=new Getshuju2();
                                                        getshuju2.deviceId = object.get(0).getDeviceId();//deviceId2;
                                                        getshuju2.gatewayId = object.get(0).getServiceId();//gatewayId2;
                                                        getshuju2.GetaccessToken();
                                                        getshuju2.GetDataandTime();

                                                    }

                                                    catch (Exception e)
                                                    {
                                                        Log.d("MainActivity", "++++++++++++++++");
                                                    }
                                                }
                                            }).start();

                                        } else {
                                            Log.w("zzz", e.toString());
                                        }
                                    }
                                });



                                textView2.setText("数据采集最新时间："+ Getshuju2.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju2.Temp+"℃"+"\n"+"湿度："+Getshuju2.Humidity+"%"+"\n"+"土壤湿度："+Getshuju2.Soil+"%"+"\n"+"光照强度："+Getshuju2.Light+"lx"+"\n"+"CO2浓度："+Getshuju2.CO2+"ppm");
                                Log.w("zzz","22222222"+"数据采集最新时间："+ Getshuju2.time4+"\n"+"天气状况："+type[0].getType()+"\n"+"温度："+ Getshuju2.Temp+"℃"+"\n"+"湿度："+Getshuju2.Humidity+"%"+"\n"+"土壤湿度："+Getshuju2.Soil+"%"+"\n"+"光照强度："+Getshuju.Light+"lx"+"\n"+"CO2浓度："+Getshuju2.CO2+"ppm");

                                //查找被绑定用户的花名，天气
                                BmobQuery<hua> BmobQuery = new BmobQuery<>();
                                BmobQuery.addWhereEqualTo("username", passive_username);
                                BmobQuery.findObjects(new FindListener<hua>() {
                                    @Override
                                    public void done(List<hua> object, BmobException e) {
                                        if (e == null) {
                                            text_flowername.setText(object.get(0).getName());
                                            weather2 =  object.get(0).getWeather();
                                            text_flower_name  = object.get(0).getName();
                                            Log.w("zzz", object.get(0).getUsername());//用户名
                                            Log.w("zzz", object.get(0).getWeather());//天气状况
                                            Log.w("zzz", object.get(0).getName());//花名

                                        } else {
                                            Log.w("zzz", e.toString());
                                        }
                                    }
                                });

                                query_hua2();

                            }

                        }

                    });





                    isRefresh2 = false;
                }
            }, 2000);
        }

    }


    //从hua表中找到用户名的id，根据id更新weather天气状况
    public void update_weather(String name){
                    BmobQuery<hua> query = new BmobQuery<hua>();
                    query.addWhereEqualTo("username", name);
                    final String[] objectId = new String[1];

                    query.findObjects(new FindListener<hua>() {
                        @Override
                        public void done(List<hua> list, BmobException e) {
                            if (e == null){
                                for (hua ad : list){
                                    objectId[0] = ad.getObjectId();
                                    Log.w(TAG, "获取id成功"+ objectId[0]);
                                }
                                hua ad2 = new hua();
                                ad2.setWeather(type[0].getType());//更新天气状况
                                ad2.update(objectId[0], new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            //Toast.makeText(getContext(), "信息天气成功", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Log.w(TAG, "失败"+e.getErrorCode());
                                        }
                                    }
                                });
                            }else {
                                Log.w(TAG, "获取id失败"+e.getErrorCode());
                            }
                        }
                    });
        }




    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }




}

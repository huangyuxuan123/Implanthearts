package com.example.implanthearts;

import android.graphics.Color;
import android.graphics.YuvImage;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
import retrofit2.http.GET;

import static com.example.implanthearts.HomeFragment.deviceId1;
import static com.example.implanthearts.HomeFragment.deviceId2;
import static com.example.implanthearts.HomeFragment.gatewayId1;
import static com.example.implanthearts.HomeFragment.gatewayId2;

/**
 * Created by Administrator on 2019/5/8.
 */

public class OperationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = OperationFragment.class.getSimpleName();

    //tab标题的id
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
    private View view1, view2, view3;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合


    //默认模式的id
    private ImageView water;//水的图片
    private ImageView other_water;//对方水的图片
    //自己的喷雾
    private EditText edit_oneself_water;
    private Button oneself_sprayon;
    private String oneself_m;//多少秒数
    private String SPRAYON = null;//用在下发命令最后那个字符串

    //对方的喷雾
    private EditText edit_the_other_side_water;
    private Button the_other_side_sprayon;
    private String other_m;//多少秒数
    private String other_SPRAYON = null;//用在下发命令最后那个字符串

    private ImageView wind;//风扇的图片
    private ImageView other_wind;//对方风扇的图片

    private Button oneself_wind_on,oneself_wind_off;//自己的风扇
    private Button the_other_side_wind_on,the_other_side_wind_off; //对方的风扇



    //同步模式的id
    private ImageView water2;//水的图片
    private Button tong_water;//同步浇水
    private EditText edit_tong_water;
    private ImageView wind2;//风扇的图片

    private Button tong_wind_on,tong_wind_off;//同步开风扇

    private ImageView light2_white;//灯的图片
    //同时开灯的下拉列表
    private Spinner tong_light_white = null;
    private ArrayAdapter<String> tong_adapter = null;
    private String [] tong_langurage ={"","全部关","白灯开","黄灯开","全部开","白灯关","黄灯关"};
    private String tong_light;
    private String tong_LIGHT;

    //喷雾
    private String tong_m;//多少秒数
    private String tong_SPRAYON = null;//用在下发命令最后那个字符串

    //限制模式的id
    private Switch limit;


    String objectId1;
    String current_user;

    //自己
    Handler handler2 = new Handler();//进APP
    private int time = 0;//进APP

    //对方
    Handler handler22 = new Handler();//进APP
    private int time2 = 0;//进APP

    //同步
    Handler handler23 = new Handler();//进APP
    private int time3 = 0;//进APP

    //自己
    //自动刷新
    Handler handler0 = new Handler();//灯全部开
    Handler handler1 = new Handler();//全部关
    Handler handler3 = new Handler();//白灯开
    Handler handler4 = new Handler();//白灯关
    Handler handler5 = new Handler();//黄灯开
    Handler handler6 = new Handler();//黄灯关
    Handler handler7 = new Handler();//喷雾开
    Handler handler8 = new Handler();//风扇开
    Handler handler9 = new Handler();//风扇关

    //对方
    //自动刷新
    Handler handler02 = new Handler();//灯全部开
    Handler handler12 = new Handler();//全部关
    Handler handler32 = new Handler();//白灯开
    Handler handler42 = new Handler();//白灯关
    Handler handler52 = new Handler();//黄灯开
    Handler handler62 = new Handler();//黄灯关
    Handler handler72 = new Handler();//喷雾开
    Handler handler82 = new Handler();//风扇开
    Handler handler92 = new Handler();//风扇关


    //同时
    Handler handler03 = new Handler();//灯全部开
    Handler handler13 = new Handler();//全部关
    Handler handler33 = new Handler();//白灯开
    Handler handler43 = new Handler();//白灯关
    Handler handler53 = new Handler();//黄灯开
    Handler handler63 = new Handler();//黄灯关
    Handler handler73 = new Handler();//喷雾开
    Handler handler83 = new Handler();//风扇开
    Handler handler93 = new Handler();//风扇关

    //限制模式开启的时候,控制板变灰色
    LinearLayout duifang;

    private ImageView light_white;//白灯的图片
    private ImageView other_light_white;//对方灯的图片

    //自己灯的下拉列表
    private Spinner oneself_light = null;
    private ArrayAdapter<String> adapter = null;
    private String []langurage ={"","全部关","白灯开","黄灯开","全部开","白灯关","黄灯关"};
    private String light;
    private String LIGHT;

    //对方灯的下拉列表
    private Spinner the_other_side_light = null;
    private ArrayAdapter<String> adapter2 = null;
    private String []langurage2 ={"","全部关","白灯开","黄灯开","全部开","白灯关","黄灯关"};
    private String other_light;
    private String other_LIGHT;

    //自己
    //计数：自动刷新了多少次
    private int light_time0 = 0;//全部开
    private int light_time1 = 0;//全部关
    private int light_time3 = 0;//白灯开
    private int light_time4 = 0;//白灯关
    private int light_time5 = 0;//黄灯开
    private int light_time6 = 0;//黄灯关
    private int light_time7 = 0;//喷雾开
    private int light_time8 = 0;//风扇开
    private int light_time9 = 0;//风扇关


    //对方
    //计数：自动刷新了多少次
    private int light_time02 = 0;//全部开
    private int light_time12 = 0;//全部关
    private int light_time32 = 0;//白灯开
    private int light_time42 = 0;//白灯关
    private int light_time52 = 0;//黄灯开
    private int light_time62 = 0;//黄灯关
    private int light_time72 = 0;//喷雾开
    private int light_time82 = 0;//风扇开
    private int light_time92 = 0;//风扇关


    //同时
    private int light_time03 = 0;//全部开
    private int light_time13 = 0;//全部关
    private int light_time33 = 0;//白灯开
    private int light_time43 = 0;//白灯关
    private int light_time53 = 0;//黄灯开
    private int light_time63 = 0;//黄灯关
    private int light_time73 = 0;//喷雾开
    private int light_time83 = 0;//风扇开
    private int light_time93 = 0;//风扇关


    private String count;
    private String count2;
    private String tong_count;

    //默认模式的下拉刷新
    private SwipeRefreshLayout mSwipeLayout;
    private boolean isRefresh = false;//是否刷新中

    //同步模式的下拉刷新
    private SwipeRefreshLayout mSwipeLayout2;
    private boolean isRefresh2 = false;//是否刷新中

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_operation, null);

        mViewPager = (ViewPager) view.findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mInflater = LayoutInflater.from(getContext());

        view1 = mInflater.inflate(R.layout.fragment_operation_view1, null);
        view2 = mInflater.inflate(R.layout.fragment_operation_view2, null);
        view3 = mInflater.inflate(R.layout.fragment_operation_view3, null);


        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);


        //添加页卡标题
        mTitleList.add("默认模式");
        mTitleList.add("同步模式");
        mTitleList.add("限制模式");


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

        //默认模式
        //喷雾的图片
        water = (ImageView) view1.findViewById(R.id.water);
        other_water = (ImageView) view1.findViewById(R.id.other_water);
        //灯的图片
        light_white = (ImageView) view1.findViewById(R.id.light_white);
        other_light_white = (ImageView) view1.findViewById(R.id.other_light_white);
        //自己灯的下拉列表
        oneself_light = (Spinner)view1.findViewById(R.id.oneself_light);
        //对方灯的下拉列表
        the_other_side_light = (Spinner)view1.findViewById(R.id.the_other_side_light);
        //风扇的图片
        wind = (ImageView) view1.findViewById(R.id.wind);
        other_wind = (ImageView) view1.findViewById(R.id.other_wind);
        //自己风扇的开关
        oneself_wind_on = (Button) view1.findViewById(R.id.oneself_wind_on);
        oneself_wind_off = (Button)view1.findViewById(R.id.oneself_wind_off);
        //对方风扇的开关
        the_other_side_wind_on = (Button)view1.findViewById(R.id.the_other_side_wind_on);
        the_other_side_wind_off = (Button)view1.findViewById(R.id.the_other_side_wind_off);


        //同步模式
        water2 = (ImageView) view2.findViewById(R.id.water2);
        light2_white = (ImageView) view2.findViewById(R.id.light2_white);
        wind2 = (ImageView) view2.findViewById(R.id.wind2);
        tong_wind_on = (Button)view2.findViewById(R.id.tong_wind_on);
        tong_wind_off = (Button)view2.findViewById(R.id.tong_wind_off);
        tong_wind_off.setEnabled(false);
        tong_light_white = (Spinner)view2.findViewById(R.id.tong_light_white);

        //限制模式
        limit = (Switch) view3.findViewById(R.id.limit);
        duifang = (LinearLayout)view1.findViewById(R.id.duifang);


       //自己每次进去APP的时候获取状态，获取5次
        handler2.postDelayed(runnable,1000);

        //对方每次进去APP的时候获取状态，获取5次
        handler22.postDelayed(runnable2,1000);

        //设置SwipeRefreshLayout
        mSwipeLayout = (SwipeRefreshLayout) view1.findViewById(R.id.SwipeRefreshLayout);
        mSwipeLayout2 =  (SwipeRefreshLayout) view2.findViewById(R.id.SwipeRefreshLayout2);

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



        //自己的喷雾
        edit_oneself_water = (EditText)view1.findViewById(R.id.edit_oneself_water);
        oneself_sprayon = (Button)view1.findViewById(R.id.oneself_sprayon);

        oneself_sprayon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneself_m = edit_oneself_water.getText().toString().trim();
                SPRAYON = "SPRINKLEROPEN"+oneself_m;
                Log.w("sss",SPRAYON);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",SPRAYON);

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                light_time7 = 0;
                handler7.postDelayed(on_spray,2000);
            }
        });

        //对方的喷雾
        edit_the_other_side_water = (EditText)view1.findViewById(R.id.edit_the_other_side_water);
        the_other_side_sprayon = (Button)view1.findViewById(R.id.the_other_side_sprayon);
        the_other_side_sprayon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other_m = edit_the_other_side_water.getText().toString().trim();
                other_SPRAYON = "SPRINKLEROPEN"+other_m;
                Log.w("sss",other_SPRAYON);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",other_SPRAYON);

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                light_time72 = 0;
                handler72.postDelayed(on_spray2,2000);
            }
        });


        //同时喷雾
        edit_tong_water = (EditText)view2.findViewById(R.id.edit_tong_water);
        tong_water = (Button)view2.findViewById(R.id.tong_water);
        tong_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tong_m = edit_tong_water.getText().toString().trim();
                tong_SPRAYON = "SPRINKLEROPEN"+tong_m;
                Log.w("sss",tong_SPRAYON);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            //自己
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",tong_SPRAYON);

                            //对方
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",tong_SPRAYON);

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                light_time73 = 0;
                handler73.postDelayed(on_spray3,2000);
            }
        });

        //自己的灯的下拉列表
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,langurage);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        oneself_light.setAdapter(adapter);
        oneself_light.setVisibility(View.VISIBLE);//设置默认显示
        oneself_light.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        light = ((TextView)arg1).getText().toString().trim();
                        //Toast.makeText(getContext(),"选中="+light,Toast.LENGTH_SHORT).show();
                        if(light.equals("全部关")){
                            LIGHT = "ALIGHTOFF";
                            handler1.postDelayed(off_light_double,2000);
                        }else if(light.equals("白灯开")){
                            LIGHT = "WLIGHTON";
                            handler3.postDelayed(on_light_white,2000);
                        }else if(light.equals("黄灯开")){
                            LIGHT = "YLIGHTON";
                            handler5.postDelayed(on_light_yellow,2000);
                        }else if(light.equals("全部开")){
                            LIGHT = "ALIGHTON";
                            handler0.postDelayed(on_light_double,2000);
                        }else if(light.equals("白灯关")){
                            LIGHT = "WLIGHTOFF";
                            handler4.postDelayed(off_light_white,2000);
                        }else if(light.equals("黄灯关")){
                            LIGHT = "YLIGHTOFF";
                            handler6.postDelayed(off_light_yellow,2000);
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {
                                    Getshuju getshuju=new Getshuju();
                                    getshuju.GetaccessToken();
                                    getshuju.post(deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",LIGHT);

                                }
                                catch (Exception e)
                                {
                                    Log.w("rrr", e.getMessage());
                                }
                            }
                        }).start();


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        //对方的灯的下拉列表
        adapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,langurage2);
        //设置下拉列表风格
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        the_other_side_light.setAdapter(adapter2);
        the_other_side_light.setVisibility(View.VISIBLE);//设置默认显示
        the_other_side_light.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                other_light = ((TextView)arg1).getText().toString().trim();
                Log.w("sss",other_light);
                if(other_light.equals("全部关")){
                    other_LIGHT = "ALIGHTOFF";
                    handler12.postDelayed(off_light_double2,2000);
                }else if(other_light.equals("白灯开")){
                    other_LIGHT = "WLIGHTON";
                    handler32.postDelayed(on_light_white2,2000);
                }else if(other_light.equals("黄灯开")){
                    other_LIGHT = "YLIGHTON";
                    handler52.postDelayed(on_light_yellow2,2000);
                }else if(other_light.equals("全部开")){
                    other_LIGHT = "ALIGHTON";
                    handler02.postDelayed(on_light_double2,2000);
                }else if(other_light.equals("白灯关")){
                    other_LIGHT = "WLIGHTOFF";
                    handler42.postDelayed(off_light_white2,2000);
                }else if(other_light.equals("黄灯关")){
                    other_LIGHT = "YLIGHTOFF";
                    handler62.postDelayed(off_light_yellow2,2000);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",other_LIGHT);
                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        //同时开灯的下拉列表
        tong_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,tong_langurage);
        //设置下拉列表风格
        tong_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        tong_light_white.setAdapter(tong_adapter);
        tong_light_white.setVisibility(View.VISIBLE);//设置默认显示
        tong_light_white.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                tong_light = ((TextView)arg1).getText().toString().trim();
                Log.w("sss",tong_light);
                if(tong_light.equals("全部关")){
                    tong_LIGHT = "ALIGHTOFF";
                    handler13.postDelayed(off_light_double3,2000);
                }else if(tong_light.equals("白灯开")){
                    tong_LIGHT = "WLIGHTON";
                    handler33.postDelayed(on_light_white3,2000);
                }else if(tong_light.equals("黄灯开")){
                    tong_LIGHT = "YLIGHTON";
                    handler53.postDelayed(on_light_yellow3,2000);
                }else if(tong_light.equals("全部开")){
                    tong_LIGHT = "ALIGHTON";
                    handler03.postDelayed(on_light_double3,2000);
                }else if(tong_light.equals("白灯关")){
                    tong_LIGHT = "WLIGHTOFF";
                    handler43.postDelayed(off_light_white3,2000);
                }else if(tong_light.equals("黄灯关")){
                    tong_LIGHT = "YLIGHTOFF";
                    handler63.postDelayed(off_light_yellow3,2000);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            //自己
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(HomeFragment.deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",tong_LIGHT);

                            //对方
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral",tong_LIGHT);
                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });




        //初始化Bmob，应用的Application ID
        //Bmob.initialize(this, "应用的Application ID");
        Bmob.initialize(getContext(), "a82e8317486115b0c1c9c16fe3668195");

        //获取当前用户以及用户属性
        getusermessage();



        //限制模式
        //开启限制模式的按钮
        limit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    send();
                    Toast.makeText(getContext(), "开启限制模式", Toast.LENGTH_SHORT).show();
                } else {
                    delete();
                    Toast.makeText(getContext(), "取消限制模式", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //查找有没有被对方限制
//        view3.findViewById(R.id.can).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                equal();
//            }
//        });



//        open2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub\
////                if(isopen == true) {
//                if (isChecked) {
//                    Toast.makeText(getContext(), "打开开关", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "关闭开关", Toast.LENGTH_SHORT).show();
//                }
////                }
//            }
//        });


        oneself_wind_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(HomeFragment.deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANON");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                oneself_wind_on.setEnabled(false);
                oneself_wind_off.setEnabled(true);
                handler8.postDelayed(on_fan, 2000);
            }
        });

        oneself_wind_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(HomeFragment.deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANOFF");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                oneself_wind_on.setEnabled(true);
                oneself_wind_off.setEnabled(false);
                handler9.postDelayed(off_fan, 2000);
            }
        });


        the_other_side_wind_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANON");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                the_other_side_wind_on.setEnabled(false);
                the_other_side_wind_off.setEnabled(true);
                handler82.postDelayed(on_fan2, 2000);
            }
        });

        the_other_side_wind_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANOFF");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                the_other_side_wind_on.setEnabled(true);
                the_other_side_wind_off.setEnabled(false);
                handler92.postDelayed(off_fan2, 2000);
            }
        });

        tong_wind_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            //自己
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(HomeFragment.deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANON");

                            //对方
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANON");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                tong_wind_on.setEnabled(false);
                tong_wind_off.setEnabled(true);
                handler83.postDelayed(on_fan3, 2000);
            }
        });

        tong_wind_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            //自己
                            Getshuju getshuju=new Getshuju();
                            getshuju.GetaccessToken();
                            getshuju.post(HomeFragment.deviceId1,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANOFF");

                            //对方
                            Getshuju2 getshuju2=new Getshuju2();
                            getshuju2.GetaccessToken();
                            getshuju2.post(deviceId2,"PlantBoxData_ControlCommand","Control_Command","Control_Peripheral","FANOFF");

                        }
                        catch (Exception e)
                        {
                            Log.d("MainActivity", "++++++++++++++++");
                        }
                    }
                }).start();
                tong_wind_on.setEnabled(true);
                tong_wind_off.setEnabled(false);
                handler92.postDelayed(off_fan3, 2000);
            }
        });



        return view;
    }








    //自动刷新，每秒更新一次
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Start_State();
            time++;
            Log.w("time","总次数==="+time);
            if(time==5){
                handler2.removeCallbacksAndMessages(null);
//                time = 0;
            }else {
                handler2.postDelayed(this, 1000);
            }
        }
    };

    //自动刷新，每秒更新一次
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Start_State2();
            time2++;
            Log.w("time","总次数==="+time2);
            if(time2==5){
                handler22.removeCallbacksAndMessages(null);
                time2 = 0;
            }else {
                handler22.postDelayed(this, 1000);
            }
        }
    };

    //同步模式的下拉刷新
    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            Start_State3();
            time3++;
            Log.w("time","总次数==="+time3);
            if(time3==5){
                handler23.removeCallbacksAndMessages(null);
                time3 = 0;
            }else {
                handler23.postDelayed(this, 1000);
            }
        }
    };



    private void Start_State() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Getshuju getshuju = new Getshuju();
                    getshuju.deviceId = deviceId1;
                    getshuju.gatewayId = gatewayId1;
                    getshuju.GetaccessToken();
                    getshuju.GetDataandTime();

                    //灯的状态
                    //黄灯、白灯开
                    if(Getshuju.White_Light_State!=null && Getshuju.White_Light_State.equals("1")&& Getshuju.Yellow_Light_State!=null &&Getshuju.Yellow_Light_State.equals("1")){
                        //回到主线程更新视图
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light_white.setBackgroundResource(R.drawable.light_double);
                            }
                        });
                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //黄灯开
                    else if(Getshuju.White_Light_State!=null && Getshuju.White_Light_State.equals("0")&&Getshuju.Yellow_Light_State!=null &&Getshuju.Yellow_Light_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light_white.setBackgroundResource(R.drawable.light_yellow2);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //白灯开
                    else if(Getshuju.White_Light_State!=null &&Getshuju.White_Light_State.equals("1")&&Getshuju.Yellow_Light_State!=null && Getshuju.Yellow_Light_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light_white.setBackgroundResource(R.drawable.light_white);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //白灯、黄灯关
                    else if(Getshuju.White_Light_State !=null && Getshuju.White_Light_State.equals("0")&&Getshuju.Yellow_Light_State!=null && Getshuju.Yellow_Light_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light_white.setBackgroundResource(R.drawable.light_gray);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //风扇的状态
                    //风扇开
                    if(Getshuju.Fan_State!=null&&Getshuju.Fan_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wind.setBackgroundResource(R.drawable.fan_green);
                                //oneself_wind.setChecked(true);
                                oneself_wind_on.setEnabled(false);
                                oneself_wind_off.setEnabled(true);
                            }
                        });

                        Log.w("rrr","风扇的状态="+Getshuju.Fan_State);
                    }
                    //风扇关
                    if(Getshuju.Fan_State!=null&&Getshuju.Fan_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wind.setBackgroundResource(R.drawable.fan_gray);
                                //oneself_wind.setChecked(false);
                                oneself_wind_on.setEnabled(true);
                                oneself_wind_off.setEnabled(false);
                            }
                        });

                        Log.w("rrr","风扇的状态="+Getshuju.Fan_State);
                    }
                    //喷雾的状态
                    //喷雾开
                    if(Getshuju.Spray_State!=null &&Getshuju.Spray_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                water.setBackgroundResource(R.drawable.water_blue2);
                            }
                        });

                        Log.w("rrr","喷雾的状态="+Getshuju.Spray_State);
                    }
                    //喷雾关
                    if(Getshuju.Spray_State!=null && Getshuju.Spray_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                water.setBackgroundResource(R.drawable.water_gray);
                            }
                        });

                        Log.w("rrr","喷雾的状态="+Getshuju.Spray_State);
                    }
                }
                catch (Exception e)
                {
                    Log.d("MainActivity", "++++++++++++++++");
                }
            }
        }).start();
    }

    private void Start_State2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Getshuju2 getshuju2 = new Getshuju2();
                    getshuju2.deviceId = deviceId2;
                    getshuju2.gatewayId = gatewayId2;
                    getshuju2.GetaccessToken();
                    getshuju2.GetDataandTime();

                    //灯的状态
                    //黄灯、白灯开
                    if(Getshuju2.White_Light_State!=null && Getshuju2.White_Light_State.equals("1")&& Getshuju2.Yellow_Light_State!=null &&Getshuju2.Yellow_Light_State.equals("1")){
                        //回到主线程更新视图
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_light_white.setBackgroundResource(R.drawable.light_double);
                            }
                        });
                        Log.w("rrr","白灯的状态="+Getshuju2.White_Light_State+"黄灯的状态="+Getshuju2.Yellow_Light_State);
                    }
                    //黄灯开
                    else if(Getshuju2.White_Light_State!=null && Getshuju2.White_Light_State.equals("0")&&Getshuju2.Yellow_Light_State!=null &&Getshuju2.Yellow_Light_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_light_white.setBackgroundResource(R.drawable.light_yellow2);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju2.White_Light_State+"黄灯的状态="+Getshuju2.Yellow_Light_State);
                    }
                    //白灯开
                    else if(Getshuju2.White_Light_State!=null &&Getshuju2.White_Light_State.equals("1")&&Getshuju2.Yellow_Light_State!=null && Getshuju2.Yellow_Light_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_light_white.setBackgroundResource(R.drawable.light_white);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju2.White_Light_State+"黄灯的状态="+Getshuju2.Yellow_Light_State);
                    }
                    //白灯、黄灯关
                    else if(Getshuju2.White_Light_State !=null && Getshuju2.White_Light_State.equals("0")&&Getshuju2.Yellow_Light_State!=null && Getshuju2.Yellow_Light_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_light_white.setBackgroundResource(R.drawable.light_gray);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju2.White_Light_State+"黄灯的状态="+Getshuju2.Yellow_Light_State);
                    }
                    //风扇的状态
                    //风扇开
                    if(Getshuju2.Fan_State!=null&&Getshuju2.Fan_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_wind.setBackgroundResource(R.drawable.fan_green);
                                //the_other_side_wind.setChecked(true);
                                the_other_side_wind_on.setEnabled(false);
                                the_other_side_wind_off.setEnabled(true);
                            }
                        });

                        Log.w("rrr","风扇的状态="+Getshuju2.Fan_State);
                    }
                    //风扇关
                    if(Getshuju2.Fan_State!=null&&Getshuju2.Fan_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_wind.setBackgroundResource(R.drawable.fan_gray);
                                //the_other_side_wind.setChecked(false);
                                the_other_side_wind_on.setEnabled(true);
                                the_other_side_wind_off.setEnabled(false);
                            }
                        });

                        Log.w("rrr","风扇的状态="+Getshuju2.Fan_State);
                    }
                    //喷雾的状态
                    //喷雾开
                    if(Getshuju2.Spray_State!=null &&Getshuju2.Spray_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_water.setBackgroundResource(R.drawable.water_blue2);
                            }
                        });

                        Log.w("rrr","喷雾的状态="+Getshuju2.Spray_State);
                    }
                    //喷雾关
                    if(Getshuju2.Spray_State!=null && Getshuju2.Spray_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                other_water.setBackgroundResource(R.drawable.water_gray);
                            }
                        });

                        Log.w("rrr","喷雾的状态="+Getshuju2.Spray_State);
                    }
                }
                catch (Exception e)
                {
                    Log.d("MainActivity", "++++++++++++++++");
                }
            }
        }).start();
    }


    private void Start_State3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //自己
                    Getshuju getshuju = new Getshuju();
                    getshuju.deviceId = deviceId1;
                    getshuju.gatewayId = gatewayId1;
                    getshuju.GetaccessToken();
                    getshuju.GetDataandTime();

                    //对方
                    Getshuju2 getshuju2 = new Getshuju2();
                    getshuju2.deviceId = deviceId2;
                    getshuju2.gatewayId = gatewayId2;
                    getshuju2.GetaccessToken();
                    getshuju2.GetDataandTime();

                    //灯的状态
                    //黄灯、白灯开
                    if(Getshuju.White_Light_State!=null && Getshuju.White_Light_State.equals("1")&& Getshuju.Yellow_Light_State!=null &&Getshuju.Yellow_Light_State.equals("1") && Getshuju2.White_Light_State!=null && Getshuju2.White_Light_State.equals("1")&& Getshuju2.Yellow_Light_State!=null &&Getshuju2.Yellow_Light_State.equals("1")){
                        //回到主线程更新视图
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light2_white.setBackgroundResource(R.drawable.light_double);
                            }
                        });
                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //黄灯开
                    else if(Getshuju.White_Light_State!=null && Getshuju.White_Light_State.equals("0")&&Getshuju.Yellow_Light_State!=null &&Getshuju.Yellow_Light_State.equals("1") && Getshuju2.White_Light_State!=null && Getshuju2.White_Light_State.equals("0")&&Getshuju2.Yellow_Light_State!=null &&Getshuju2.Yellow_Light_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light2_white.setBackgroundResource(R.drawable.light_yellow2);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //白灯开
                    else if(Getshuju.White_Light_State!=null &&Getshuju.White_Light_State.equals("1")&&Getshuju.Yellow_Light_State!=null && Getshuju.Yellow_Light_State.equals("0") && Getshuju2.White_Light_State!=null &&Getshuju2.White_Light_State.equals("1")&&Getshuju2.Yellow_Light_State!=null && Getshuju2.Yellow_Light_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light2_white.setBackgroundResource(R.drawable.light_white);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //白灯、黄灯关
                    else if(Getshuju.White_Light_State !=null && Getshuju.White_Light_State.equals("0")&&Getshuju.Yellow_Light_State!=null && Getshuju.Yellow_Light_State.equals("0") && Getshuju2.White_Light_State !=null && Getshuju2.White_Light_State.equals("0")&&Getshuju2.Yellow_Light_State!=null && Getshuju2.Yellow_Light_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                light2_white.setBackgroundResource(R.drawable.light_gray);
                            }
                        });

                        Log.w("rrr","白灯的状态="+Getshuju.White_Light_State+"黄灯的状态="+Getshuju.Yellow_Light_State);
                    }
                    //风扇的状态
                    //风扇开
                    if(Getshuju.Fan_State!=null&&Getshuju.Fan_State.equals("1") && Getshuju2.Fan_State!=null&&Getshuju2.Fan_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wind2.setBackgroundResource(R.drawable.fan_green);
                                //tong_wind.setChecked(true);
                                tong_wind_on.setEnabled(false);
                                tong_wind_off.setEnabled(true);
                            }
                        });

                        Log.w("rrr","风扇的状态="+Getshuju.Fan_State);
                    }
                    //风扇关
                    if(Getshuju.Fan_State!=null&&Getshuju.Fan_State.equals("0") && Getshuju2.Fan_State!=null&&Getshuju2.Fan_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wind2.setBackgroundResource(R.drawable.fan_gray);
                                //tong_wind.setChecked(false);
                                tong_wind_on.setEnabled(true);
                                tong_wind_off.setEnabled(false);
                            }
                        });

                        Log.w("rrr","风扇的状态="+Getshuju.Fan_State);
                    }
                    //喷雾的状态
                    //喷雾开
                    if(Getshuju.Spray_State!=null &&Getshuju.Spray_State.equals("1") && Getshuju2.Spray_State!=null &&Getshuju2.Spray_State.equals("1")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                water2.setBackgroundResource(R.drawable.water_blue2);
                            }
                        });

                        Log.w("rrr","喷雾的状态="+Getshuju.Spray_State);
                    }
                    //喷雾关
                    if(Getshuju.Spray_State!=null && Getshuju.Spray_State.equals("0") && Getshuju2.Spray_State!=null && Getshuju2.Spray_State.equals("0")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                water2.setBackgroundResource(R.drawable.water_gray);
                            }
                        });

                        Log.w("rrr","喷雾的状态="+Getshuju.Spray_State);
                    }
                }
                catch (Exception e)
                {
                    Log.d("MainActivity", "++++++++++++++++");
                }
            }
        }).start();
    }

    //自动刷新，每秒更新一次
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            //查找有没有被对方限制
//            equal();
//            handler.postDelayed(this, 1000);
//        }
//    };

    //关风扇
    Runnable off_fan = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);
            if (Getshuju.Fan_State != null && Getshuju.Fan_State.equals("0")) {
                Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);

                    wind.setBackgroundResource(R.drawable.fan_gray);
                //oneself_wind.setChecked(false);
                oneself_wind_on.setEnabled(true);
                oneself_wind_off.setEnabled(false);
                //Toast.makeText(getContext(),"给自己停止通风",Toast.LENGTH_SHORT).show();

                handler9.removeCallbacksAndMessages(null);
                light_time9 = 0;
            } else {
                handler9.postDelayed(this, 2000);
            }
            light_time9++;
            Log.w("time","次数="+light_time9);

            if(light_time9 == 20){
                Log.w("time","异常");
                handler9.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable off_fan2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.Fan_State=="+Getshuju2.Fan_State);
            if (Getshuju2.Fan_State != null && Getshuju2.Fan_State.equals("0")) {
                Log.w("time","Getshuju2.Fan_State=="+Getshuju2.Fan_State);

                other_wind.setBackgroundResource(R.drawable.fan_gray);
                //the_other_side_wind.setChecked(false);
                the_other_side_wind_on.setEnabled(true);
                the_other_side_wind_off.setEnabled(false);
                //Toast.makeText(getContext(),"给对方停止通风",Toast.LENGTH_SHORT).show();
                handler92.removeCallbacksAndMessages(null);
                light_time92 = 0;
            } else {
                handler92.postDelayed(this, 2000);
            }
            light_time92++;
            Log.w("time","次数="+light_time92);

            if(light_time92 == 20){
                Log.w("time","异常");
                handler92.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable off_fan3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);
            if (Getshuju.Fan_State != null && Getshuju.Fan_State.equals("0")) {
                Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);

                wind2.setBackgroundResource(R.drawable.fan_gray);
                //tong_wind.setChecked(false);
                tong_wind_on.setEnabled(true);
                tong_wind_off.setEnabled(false);

                //Toast.makeText(getContext(),"同时停止通风",Toast.LENGTH_SHORT).show();

                handler93.removeCallbacksAndMessages(null);
                light_time93 = 0;
            } else {
                handler93.postDelayed(this, 2000);
            }
            light_time93++;
            Log.w("time","次数="+light_time93);

            if(light_time93 == 20){
                Log.w("time","异常");
                handler93.removeCallbacksAndMessages(null);
            }

        }
    };

    //开风扇
    Runnable on_fan = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);
            if (Getshuju.Fan_State != null && Getshuju.Fan_State.equals("1")) {
                Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);

                wind.setBackgroundResource(R.drawable.fan_green);
                //oneself_wind.setChecked(true);
                oneself_wind_on.setEnabled(false);
                oneself_wind_off.setEnabled(true);
                //Toast.makeText(getContext(),"给自己进行通风",Toast.LENGTH_SHORT).show();

                handler8.removeCallbacksAndMessages(null);
                light_time8 = 0;
            } else {
                handler8.postDelayed(this, 2000);
            }
            light_time8++;
            Log.w("time","次数="+light_time8);

            if(light_time8 == 20){
                Log.w("time","异常");
                handler8.removeCallbacksAndMessages(null);
            }

        }
    };
    Runnable on_fan2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.Fan_State=="+Getshuju2.Fan_State);
            if (Getshuju2.Fan_State != null && Getshuju2.Fan_State.equals("1")) {
                Log.w("time","Getshuju2.Fan_State=="+Getshuju2.Fan_State);
                //the_other_side_wind.setChecked(true);
                the_other_side_wind_on.setEnabled(false);
                the_other_side_wind_off.setEnabled(true);
                other_wind.setBackgroundResource(R.drawable.fan_green);

                //Toast.makeText(getContext(),"给对方进行通风",Toast.LENGTH_SHORT).show();

                handler82.removeCallbacksAndMessages(null);
                light_time82 = 0;
            } else {
                handler82.postDelayed(this, 2000);
            }
            light_time82++;
            Log.w("time","次数="+light_time82);

            if(light_time82 == 20){
                Log.w("time","异常");
                handler82.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable on_fan3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);
            if (Getshuju.Fan_State != null && Getshuju.Fan_State.equals("1")) {
                Log.w("time","Getshuju.Fan_State=="+Getshuju.Fan_State);

                wind2.setBackgroundResource(R.drawable.fan_green);
                //tong_wind.setChecked(true);
                tong_wind_on.setEnabled(false);
                tong_wind_off.setEnabled(true);

                //Toast.makeText(getContext(),"同时进行通风",Toast.LENGTH_SHORT).show();

                handler83.removeCallbacksAndMessages(null);
                light_time83 = 0;
            } else {
                handler83.postDelayed(this, 2000);
            }
            light_time83++;
            Log.w("time","次数="+light_time83);

            if(light_time83 == 20){
                Log.w("time","异常");
                handler83.removeCallbacksAndMessages(null);
            }

        }
    };


    //开喷雾
    Runnable on_spray = new Runnable() {
        @Override
        public void run() {
            count = edit_oneself_water.getText().toString().trim();
            Log.w("time","浇水浇多少秒"+count);
            State();
//            Log.w("time","Getshuju.Spray_State=="+Getshuju.Spray_State);
            if (Getshuju.Spray_State != null) {
                if (Getshuju.Spray_State.equals("1")) {
                    Log.w("time", "Getshuju.Spray_State==" + Getshuju.Spray_State);
                    water.setBackgroundResource(R.drawable.water_blue2);
                    //Toast.makeText(getContext(),"给自己进行喷雾",Toast.LENGTH_SHORT).show();
                } else if (Getshuju.Spray_State.equals("0")) {
                    Log.w("time", "Getshuju.Spray_State==" + Getshuju.Spray_State);
                    water.setBackgroundResource(R.drawable.water_gray);
                    //Toast.makeText(getContext(),"给自己停止喷雾",Toast.LENGTH_SHORT).show();
                }
            }

            light_time7++;
            Log.w("time","次数="+light_time7);

            if(light_time7 < (Integer.parseInt(count)+10)){
                Log.w("time","正常");
                handler7.postDelayed(this, 2000);
            }else if(light_time7 == (Integer.parseInt(count)+10)){
                Log.w("time","正常结束");
                handler7.removeCallbacksAndMessages(null);
            }

//            if(){
//                Log.w("time","异常");
//                handler7.removeCallbacksAndMessages(null);
//            }

        }
    };

    Runnable on_spray2 = new Runnable() {
        @Override
        public void run() {
            count2 = edit_the_other_side_water.getText().toString().trim();
            Log.w("time","浇水浇多少秒"+count2);
            State2();
//            Log.w("time","Getshuju.Spray_State=="+Getshuju.Spray_State);
            if (Getshuju2.Spray_State != null) {
                if (Getshuju2.Spray_State.equals("1")) {
                    Log.w("time", "Getshuju2.Spray_State==" + Getshuju2.Spray_State);
                    other_water.setBackgroundResource(R.drawable.water_blue2);
                    //Toast.makeText(getContext(),"给对方进行喷雾",Toast.LENGTH_SHORT).show();
                } else if (Getshuju2.Spray_State.equals("0")) {
                    Log.w("time", "Getshuju.Spray_State==" + Getshuju2.Spray_State);
                    other_water.setBackgroundResource(R.drawable.water_gray);
                    //Toast.makeText(getContext(),"给对方停止喷雾",Toast.LENGTH_SHORT).show();
                }
            }

            light_time72++;
            Log.w("time","次数="+light_time72);

            if(light_time72 < (Integer.parseInt(count2)+10)){
                Log.w("time","正常");
                handler72.postDelayed(this, 2000);
            }else if(light_time72 == (Integer.parseInt(count2)+10)){
                Log.w("time","正常结束");
                handler72.removeCallbacksAndMessages(null);
            }

//            if(){
//                Log.w("time","异常");
//                handler7.removeCallbacksAndMessages(null);
//            }

        }
    };

    //同时开喷雾
    Runnable on_spray3 = new Runnable() {
        @Override
        public void run() {
            tong_count = edit_tong_water.getText().toString().trim();
            Log.w("time","浇水浇多少秒"+tong_count);
            State();
//            Log.w("time","Getshuju.Spray_State=="+Getshuju.Spray_State);
            if (Getshuju.Spray_State != null) {
                if (Getshuju.Spray_State.equals("1")) {
                    Log.w("time", "Getshuju.Spray_State==" + Getshuju.Spray_State);
                    water2.setBackgroundResource(R.drawable.water_blue2);
                    //Toast.makeText(getContext(),"同时进行喷雾",Toast.LENGTH_SHORT).show();
                } else if (Getshuju.Spray_State.equals("0")) {
                    Log.w("time", "Getshuju.Spray_State==" + Getshuju.Spray_State);
                    water2.setBackgroundResource(R.drawable.water_gray);
                    //Toast.makeText(getContext(),"同时停止喷雾",Toast.LENGTH_SHORT).show();
                }
            }

            light_time73++;
            Log.w("time","次数="+light_time73);

            if(light_time73 < (Integer.parseInt(tong_count)+10)){
                Log.w("time","正常");
                handler73.postDelayed(this, 2000);
            }else if(light_time73 == (Integer.parseInt(tong_count)+10)){
                Log.w("time","正常结束");
                handler73.removeCallbacksAndMessages(null);
            }

//            if(){
//                Log.w("time","异常");
//                handler7.removeCallbacksAndMessages(null);
//            }

        }
    };

    //黄灯关
    Runnable off_light_yellow = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("0")) {
                Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
                if(Getshuju.White_Light_State!=null && Getshuju.White_Light_State.equals("1")){
                    light_white.setBackgroundResource(R.drawable.light_white);

                }else {
                    light_white.setBackgroundResource(R.drawable.light_gray);

                }
                handler6.removeCallbacksAndMessages(null);
                light_time6 = 0;
                //Toast.makeText(getContext(),"给自己关黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler6.postDelayed(this, 2000);
            }
            light_time6++;
            Log.w("time","次数="+light_time6);

            if(light_time6 == 20){
                Log.w("time","异常");
                handler6.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable off_light_yellow2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
            if (Getshuju2.Yellow_Light_State != null && Getshuju2.Yellow_Light_State.equals("0")) {
                Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
                if(Getshuju2.White_Light_State!=null && Getshuju2.White_Light_State.equals("1")){
                    other_light_white.setBackgroundResource(R.drawable.light_white);
                }else {

                    other_light_white.setBackgroundResource(R.drawable.light_gray);
                }
                handler62.removeCallbacksAndMessages(null);
                light_time62 = 0;
                //Toast.makeText(getContext(),"给对方关黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler62.postDelayed(this, 2000);
            }

            light_time62++;
            Log.w("time","次数="+light_time62);

            if(light_time62 == 20){
                Log.w("time","异常");
                handler62.removeCallbacksAndMessages(null);
            }

        }
    };


    Runnable off_light_yellow3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("0")) {
                Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
                if(Getshuju.White_Light_State!=null && Getshuju.White_Light_State.equals("1")){
                    light2_white.setBackgroundResource(R.drawable.light_white);
                }else {
                    light2_white.setBackgroundResource(R.drawable.light_gray);
                }
                handler63.removeCallbacksAndMessages(null);
                light_time63 = 0;
                //Toast.makeText(getContext(),"同时关黄灯",Toast.LENGTH_SHORT).show();

            } else {
                handler63.postDelayed(this, 2000);
            }

            light_time63++;
            Log.w("time","次数="+light_time63);

            if(light_time63 == 20){
                Log.w("time","异常");
                handler63.removeCallbacksAndMessages(null);
            }

        }
    };

    //黄灯开
    Runnable on_light_yellow = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("1")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.Yellow_Light_State);
                if(Getshuju.White_Light_State!= null && Getshuju.White_Light_State.equals("1")){
                    light_white.setBackgroundResource(R.drawable.light_double);
                }else {
                    light_white.setBackgroundResource(R.drawable.light_yellow2);
                }
                handler5.removeCallbacksAndMessages(null);
                light_time5 = 0;
                //Toast.makeText(getContext(),"给自己开黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler5.postDelayed(this, 2000);
            }

            light_time5++;
            Log.w("time","次数="+light_time5);

            if(light_time5 == 20){
                Log.w("time","异常");
                handler5.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable on_light_yellow2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
            if (Getshuju2.Yellow_Light_State != null && Getshuju2.Yellow_Light_State.equals("1")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.Yellow_Light_State);
                if(Getshuju2.White_Light_State!= null && Getshuju2.White_Light_State.equals("1")){

                    other_light_white.setBackgroundResource(R.drawable.light_double);
                }else {
                    //light_white.setBackgroundResource(R.drawable.light_yellow2);
                    other_light_white.setBackgroundResource(R.drawable.light_yellow2);
                }
                handler52.removeCallbacksAndMessages(null);
                light_time52 = 0;
                //Toast.makeText(getContext(),"给对方开黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler52.postDelayed(this, 2000);
            }

            light_time52++;
            Log.w("time","次数="+light_time52);

            if(light_time52 == 20){
                Log.w("time","异常");
                handler52.removeCallbacksAndMessages(null);
            }

        }
    };


    Runnable on_light_yellow3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("1")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.Yellow_Light_State);
                if(Getshuju.White_Light_State!= null && Getshuju.White_Light_State.equals("1")){
                    light2_white.setBackgroundResource(R.drawable.light_double);
                }else {
                    light2_white.setBackgroundResource(R.drawable.light_yellow2);
                }
                handler53.removeCallbacksAndMessages(null);
                light_time53 = 0;
                //Toast.makeText(getContext(),"同时开黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler53.postDelayed(this, 2000);
            }

            light_time53++;
            Log.w("time","次数="+light_time53);

            if(light_time53 == 20){
                Log.w("time","异常");
                handler53.removeCallbacksAndMessages(null);
            }

        }
    };

    //白灯开
    Runnable on_light_white = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("1")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State);

                if(Getshuju.Yellow_Light_State!= null && Getshuju.Yellow_Light_State.equals("1")){
                    light_white.setBackgroundResource(R.drawable.light_double);
                }else {
                    light_white.setBackgroundResource(R.drawable.light_white);
                }

                handler3.removeCallbacksAndMessages(null);
                light_time3 = 0;
                //Toast.makeText(getContext(),"给自己开白灯",Toast.LENGTH_SHORT).show();

            } else {
                handler3.postDelayed(this, 2000);
            }

            light_time3++;
            Log.w("time","次数="+light_time3);

            if(light_time3 == 20){
                Log.w("time","异常");
                handler3.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable on_light_white2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State);
            if (Getshuju2.White_Light_State != null && Getshuju2.White_Light_State.equals("1")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State);

                if(Getshuju2.Yellow_Light_State!= null && Getshuju2.Yellow_Light_State.equals("1")){
                    other_light_white.setBackgroundResource(R.drawable.light_double);
                }else {
                    other_light_white.setBackgroundResource(R.drawable.light_white);
                }
                handler32.removeCallbacksAndMessages(null);
                light_time32 = 0;
                //Toast.makeText(getContext(),"给对方开白灯",Toast.LENGTH_SHORT).show();

            } else {
                handler32.postDelayed(this, 2000);
            }

            light_time32++;
            Log.w("time","次数="+light_time32);

            if(light_time32 == 20){
                Log.w("time","异常");
                handler32.removeCallbacksAndMessages(null);
            }

        }
    };


    Runnable on_light_white3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("1")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State);

                if(Getshuju.Yellow_Light_State!= null && Getshuju.Yellow_Light_State.equals("1")){
                    light2_white.setBackgroundResource(R.drawable.light_double);
                }else {
                    light2_white.setBackgroundResource(R.drawable.light_white);
                }

                handler33.removeCallbacksAndMessages(null);
                light_time33 = 0;
                //Toast.makeText(getContext(),"同时开白灯",Toast.LENGTH_SHORT).show();
            } else {
                handler33.postDelayed(this, 2000);
            }
            light_time33++;
            Log.w("time","次数="+light_time33);

            if(light_time33 == 20){
                Log.w("time","异常");
                handler33.removeCallbacksAndMessages(null);
            }

        }
    };


    //白灯关
    Runnable off_light_white = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("0")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State);
                if(Getshuju.Yellow_Light_State!=null && Getshuju.Yellow_Light_State.equals("1")){
                    light_white.setBackgroundResource(R.drawable.light_yellow2);
                }else {
                    light_white.setBackgroundResource(R.drawable.light_gray);
                }
                handler4.removeCallbacksAndMessages(null);
                light_time4 = 0;
                //Toast.makeText(getContext(),"给自己关白灯",Toast.LENGTH_SHORT).show();
            } else {
                handler4.postDelayed(this, 2000);
            }
            light_time4++;
            Log.w("time","次数="+light_time4);

            if(light_time4 == 20){
                Log.w("time","异常");
                handler4.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable off_light_white2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State);
            if (Getshuju2.White_Light_State != null && Getshuju2.White_Light_State.equals("0")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State);
                if(Getshuju2.Yellow_Light_State!=null && Getshuju2.Yellow_Light_State.equals("1")){
                    other_light_white.setBackgroundResource(R.drawable.light_yellow2);
                }else {
                    other_light_white.setBackgroundResource(R.drawable.light_gray);
                }
                handler42.removeCallbacksAndMessages(null);
                light_time42 = 0;
                //Toast.makeText(getContext(),"给对方关白灯",Toast.LENGTH_SHORT).show();
            } else {
                handler42.postDelayed(this, 2000);
            }
            light_time42++;
            Log.w("time","次数="+light_time42);

            if(light_time42 == 20){
                Log.w("time","异常");
                handler42.removeCallbacksAndMessages(null);
            }

        }
    };


    Runnable off_light_white3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("0")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State);
                if(Getshuju.Yellow_Light_State!=null && Getshuju.Yellow_Light_State.equals("1")){
                    light2_white.setBackgroundResource(R.drawable.light_yellow2);
                }else {
                    light2_white.setBackgroundResource(R.drawable.light_gray);
                }
                handler43.removeCallbacksAndMessages(null);
                light_time43 = 0;
                //Toast.makeText(getContext(),"同时关白灯",Toast.LENGTH_SHORT).show();
            } else {
                handler43.postDelayed(this, 2000);
            }
            light_time43++;
            Log.w("time","次数="+light_time43);

            if(light_time43 == 20){
                Log.w("time","异常");
                handler43.removeCallbacksAndMessages(null);
            }

        }
    };


        //全部开
        Runnable on_light_double = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("1") && Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("1")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
                light_white.setBackgroundResource(R.drawable.light_double);
                handler0.removeCallbacksAndMessages(null);
                light_time0 = 0;
                //Toast.makeText(getContext(),"给自己开白灯、黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler0.postDelayed(this, 2000);
            }
            light_time0++;
            Log.w("time","次数="+light_time0);

            if(light_time0 == 20){
                Log.w("time","异常");
                handler0.removeCallbacksAndMessages(null);
            }

        }
    };

    //
    Runnable on_light_double2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State+"Getshuju2.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
            if (Getshuju2.White_Light_State != null && Getshuju2.White_Light_State.equals("1") && Getshuju2.Yellow_Light_State != null && Getshuju2.Yellow_Light_State.equals("1")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State+"Getshuju2.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
                other_light_white.setBackgroundResource(R.drawable.light_double);
                handler02.removeCallbacksAndMessages(null);
                light_time02 = 0;
                //Toast.makeText(getContext(),"给对方开白灯、黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler02.postDelayed(this, 2000);
            }
            light_time02++;
            Log.w("time","次数="+light_time02);

            if(light_time02 == 20){
                Log.w("time","异常");
                handler02.removeCallbacksAndMessages(null);
            }

        }
    };

    Runnable on_light_double3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("1") && Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("1")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
                light2_white.setBackgroundResource(R.drawable.light_double);
                handler03.removeCallbacksAndMessages(null);
                light_time03 = 0;
                //Toast.makeText(getContext(),"同时开白灯、黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler03.postDelayed(this, 2000);
            }
            light_time03++;
            Log.w("time","次数="+light_time03);

            if(light_time03 == 20){
                Log.w("time","异常");
                handler03.removeCallbacksAndMessages(null);
            }

        }
    };

        //全部关
    Runnable off_light_double = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("0") && Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("0")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
                light_white.setBackgroundResource(R.drawable.light_gray);

                handler1.removeCallbacksAndMessages(null);
                light_time1 = 0;
                //Toast.makeText(getContext(),"给自己关白灯、黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler1.postDelayed(this, 2000);
            }
            light_time1++;
            Log.w("time","次数="+light_time1);

            if(light_time1 == 20){
                Log.w("time","异常");
                handler1.removeCallbacksAndMessages(null);
            }

        }
    };

    //全部关
    Runnable off_light_double2 = new Runnable() {
        @Override
        public void run() {
            State2();
            Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State+"Getshuju2.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
            if (Getshuju2.White_Light_State != null && Getshuju2.White_Light_State.equals("0") && Getshuju2.Yellow_Light_State != null && Getshuju2.Yellow_Light_State.equals("0")) {
                Log.w("time","Getshuju2.White_Light_State=="+Getshuju2.White_Light_State+"Getshuju2.Yellow_Light_State=="+Getshuju2.Yellow_Light_State);
                other_light_white.setBackgroundResource(R.drawable.light_gray);
                handler12.removeCallbacksAndMessages(null);
                light_time12 = 0;
                //Toast.makeText(getContext(),"给对方关白灯、黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler12.postDelayed(this, 2000);
            }
            light_time12++;
            Log.w("time","次数="+light_time12);

            if(light_time12 == 20){
                Log.w("time","异常");
                handler12.removeCallbacksAndMessages(null);
            }

        }
    };


    Runnable off_light_double3 = new Runnable() {
        @Override
        public void run() {
            State();
            Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
            if (Getshuju.White_Light_State != null && Getshuju.White_Light_State.equals("0") && Getshuju.Yellow_Light_State != null && Getshuju.Yellow_Light_State.equals("0")) {
                Log.w("time","Getshuju.White_Light_State=="+Getshuju.White_Light_State+"Getshuju.Yellow_Light_State=="+Getshuju.Yellow_Light_State);
                light2_white.setBackgroundResource(R.drawable.light_gray);

                handler13.removeCallbacksAndMessages(null);
                light_time13 = 0;
                //Toast.makeText(getContext(),"同时关白灯、黄灯",Toast.LENGTH_SHORT).show();
            } else {
                handler13.postDelayed(this, 2000);
            }
            light_time13++;
            Log.w("time","次数="+light_time13);

            if(light_time13 == 20){
                Log.w("time","异常");
                handler13.removeCallbacksAndMessages(null);
            }

        }
    };


    //获取状态
    private void State() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Getshuju getshuju = new Getshuju();
                    getshuju.deviceId = deviceId1;
                    getshuju.gatewayId = gatewayId1;
                    getshuju.GetaccessToken();
                    //获取状态
                    getshuju.GetDataandTime();
                }
                catch (Exception e)
                {
                    Log.w("rrr", "++++++++++++++++++"+e.getMessage());
                }
            }
        }).start();
    }

    //获取状态
    private void State2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Getshuju2 getshuju2 = new Getshuju2();
                    getshuju2.deviceId = deviceId2;
                    getshuju2.gatewayId = gatewayId2;
                    getshuju2.GetaccessToken();
                    //获取状态
                    getshuju2.GetDataandTime();
                }
                catch (Exception e)
                {
                    Log.w("rrr", "++++++++++++++++++"+e.getMessage());
                }
            }
        }).start();
    }






    @Override
    protected View initView() {

        return null;
    }

    @Override
    protected void initData() {
    }

    public void getusermessage() {
        User user = BmobUser.getCurrentUser(User.class);
        current_user = user.getUsername();
        Log.w("qqq","当前用户："+user.getUsername());
    }


    //删除一行数据
    private void  delete() {
        final Limit p2 = new Limit();
        p2.setObjectId(objectId1);
        p2.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.w("qqq","删除成功:" + p2.getUpdatedAt());
//                    can.setEnabled(true);
//                    can.setText("有没有被限制");
                    //Toast.makeText(MainActivity.this,"删除成功:" + p2.getUpdatedAt(),Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("qqq","删除失败:"+e.getMessage());
                    //Toast.makeText(MainActivity.this,"删除失败:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void send(){
        Limit p2 = new Limit();
        p2.setName(current_user);
        p2.setAddress("限制模式");
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    objectId1 = objectId;
                    Log.w("qqq","添加数据成功，返回objectId为："+objectId);
                    //Toast.makeText(MainActivity.this, "添加数据成功，返回objectId为："+objectId, Toast.LENGTH_SHORT).show();
                }else{
                    Log.w("qqq","创建数据失败：" + e.getMessage());
                    //Toast.makeText(MainActivity.this, "创建数据失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        equal();

    }




    //只返回Limit表的name这列的值
    private void equal() {
        BmobQuery<Limit> bmobQuery = new BmobQuery<Limit>();
        bmobQuery.addQueryKeys("name");
        bmobQuery.findObjects(new FindListener<Limit>() {
            String s0 = null;
            String s1 = null;
            @Override
            public void done(List<Limit> object, BmobException e) {
                //查找总共有多少条数据
                int n = object.size();
                Log.w("zzz","总共有多少条数据："+n);
                //如果只有一条，aaa或bbb发了限制指令
                if(n==1 ){
                    try {
                        s0 = object.get(0).getName();
                        Log.w("zzz", "s0==" + s0);
                    }catch (Exception e2){
                        s0 = null;
                    }


                    //如果是当前用户自己发的限制指令，可以控制开关
                    if(s0 != null && s0.equals(current_user)){
                        //open2.setEnabled(true);

//                        the_other_side_water.setEnabled(true);
//                        the_other_side_light_white.setEnabled(true);
//                        the_other_side_light_yellow.setEnabled(true);

                        //喷雾按钮
                        the_other_side_sprayon.setEnabled(true);
                        //喷雾文本编辑
                        edit_the_other_side_water.setVisibility(View.VISIBLE);
                        //灯光
                        the_other_side_light.setEnabled(true);
                        //风扇
                        //the_other_side_wind.setEnabled(true);

                        the_other_side_wind_on.setEnabled(true);
                        the_other_side_wind_off.setEnabled(true);

                        //背景
                        duifang.setBackgroundColor(Color.WHITE);

                    }
                    //如果不是当前用户自己发的限制指令，不可以控制开关
                    else if(s0 != null && !s0.equals(current_user)){
                        //open2.setEnabled(false);

//                        the_other_side_water.setEnabled(false);
//                        the_other_side_light_white.setEnabled(false);
//                        the_other_side_light_yellow.setEnabled(false);

                        //喷雾按钮
                        the_other_side_sprayon.setEnabled(false);
                        //喷雾文本编辑
                        edit_the_other_side_water.setVisibility(View.INVISIBLE);
                        //灯光
                        the_other_side_light.setEnabled(false);
                        //the_other_side_wind.setEnabled(false);
                        the_other_side_wind_on.setEnabled(false);
                        the_other_side_wind_off.setEnabled(false);
                        duifang.setBackgroundColor(Color.GRAY);
                    }
                    else if(s0 == null){
                        //open2.setEnabled(true);

//                        the_other_side_water.setEnabled(true);
//                        the_other_side_light_white.setEnabled(true);
//                        the_other_side_light_yellow.setEnabled(true);

                        //喷雾按钮
                        the_other_side_sprayon.setEnabled(true);
                        //喷雾文本编辑
                        edit_the_other_side_water.setVisibility(View.VISIBLE);
                        //灯光
                        the_other_side_light.setEnabled(true);
                        //the_other_side_wind.setEnabled(true);
                        the_other_side_wind_on.setEnabled(true);
                        the_other_side_wind_off.setEnabled(true);
                        duifang.setBackgroundColor(Color.WHITE);
                    }


                }
                //两条，aaa和bbb都发了限制指令
                else if(n==2){
                    try{
                        s0 = object.get(0).getName();
                        s1 = object.get(1).getName();
                        Log.w("zzz","s0=="+s0);
                        Log.w("zzz","s1=="+s1);
                    }catch (Exception e1){
                        s0 = null;
                        s1 = null;
                        Log.w("zzz","s0=="+s0);
                        Log.w("zzz","s1=="+s1);
                    }
                    //两个用户都发了限制指令，都不可以控制开关
                    if(s0!= null && s1 !=null){
                        //open2.setEnabled(false);

//                        the_other_side_water.setEnabled(false);
//                        the_other_side_light_white.setEnabled(false);
//                        the_other_side_light_yellow.setEnabled(false);

                        //喷雾按钮
                        the_other_side_sprayon.setEnabled(false);
                        //喷雾文本编辑
                        edit_the_other_side_water.setVisibility(View.INVISIBLE);
                        //灯光
                        the_other_side_light.setEnabled(false);
                        //the_other_side_wind.setEnabled(false);
                        the_other_side_wind_on.setEnabled(false);
                        the_other_side_wind_off.setEnabled(false);
                        duifang.setBackgroundColor(Color.GRAY);

                    }

                }
                //0条，aaa和bbb都删了限制指令
                else if(n==0){
                    //open2.setEnabled(true);

//                    the_other_side_water.setEnabled(true);
//                    the_other_side_light_white.setEnabled(true);
//                    the_other_side_light_yellow.setEnabled(true);
                    //喷雾按钮
                    the_other_side_sprayon.setEnabled(true);
                    //喷雾文本编辑
                    edit_the_other_side_water.setVisibility(View.VISIBLE);
                    //灯光
                    the_other_side_light.setEnabled(true);
                    //the_other_side_wind.setEnabled(true);
                    the_other_side_wind_on.setEnabled(true);
                    the_other_side_wind_off.setEnabled(true);
                    duifang.setBackgroundColor(Color.WHITE);
                }


            }
        });

    }

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
                    time = 0;
                    time2 = 0;
                    //更新数据
                    //每次进去APP的时候获取状态，获取5次
                    handler2.postDelayed(runnable,1000);
                    handler22.postDelayed(runnable2,1000);

                    //查找有没有被对方限制
                    equal();

                    isRefresh = false;
                }
            }, 2000);

        }

        if (!isRefresh2) {
            isRefresh2 = true;
            //模拟加载网络数据，这里设置4秒，正好能看到4色进度条
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //显示或隐藏刷新进度条
                    mSwipeLayout2.setRefreshing(false);

                    //更新数据
                    //Start_State3();
                    handler23.postDelayed(runnable3,1000);

                    isRefresh2 = false;
                }
            }, 2000);

        }
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
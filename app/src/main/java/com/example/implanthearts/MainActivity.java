package com.example.implanthearts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends FragmentActivity {
    private RadioGroup rg;
    private List<BaseFragment> baseFragments;
    //选中的fragment的对应的位置
    private int position;
    //上次切换的fragment
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化View
        initView();
        //初始化fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
        ActivityManager.addActivity(this);

    }


    private void setListener() {
        rg.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //默认选中第一个
        rg.check(R.id.rb_common_frame);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_common_frame://常用框架
                    position = 0;
                    break;
                case R.id.rb_thirdparty://第三方
                    position = 1;
                    break;
                case R.id.rb_custom://自定义
                    position = 2;
                    break;
                case R.id.rb_other://其他
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }

            //根据位置得到对应的fragment
            BaseFragment to = getFragment();
            //替换
            switchFragment(mFragment, to);
        }
    }

    /**
     * 解决重复加载fragment
     *
     * @param from 刚显示的fragment，即将被隐藏
     * @param to   即将要显示的fragment
     */
    private void switchFragment(Fragment from, Fragment to) {
        //如果两个fragment不相等才切换，相等就不用切换了
        if (from != to) {
            //1.得到fragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            //2.开启事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            mFragment = to;
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //掩藏from
                if (from != null) {
                    transaction.hide(from);
                }
                //添加to
                if (to != null) {
                    transaction.add(R.id.fl, to).commit();
                }
            } else {
                //to已经被添加
                //隐藏from
                if (from != null) {
                    transaction.hide(from);
                }
                //显示to
                if (to != null) {
                    transaction.show(to).commit();
                }
            }
        }
    }

    //会导致重复加载fragment
//    private void switchFragment(BaseFragment fragment) {
//        //1.得到fragmentManager
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        //2.开启事务
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        //3.替换
//        transaction.replace(R.id.fl,fragment);
//        //4.提交事务
//        transaction.commit();
//    }


    public BaseFragment getFragment() {
        BaseFragment fragment = baseFragments.get(position);
        return fragment;
    }


    private void initFragment() {
        baseFragments = new ArrayList<>();
        baseFragments.add(new HomeFragment());//首页fragment
        baseFragments.add(new OperationFragment());//远程操作fragment
        baseFragments.add(new CommunityFragment());//社区fragment
        baseFragments.add(new PersonFragment());//个人中心fragment
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        rg = (RadioGroup) findViewById(R.id.rg);

//        //默认选中第一个
//        rg.check(R.id.rb_common_frame);
    }

}
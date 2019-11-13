package com.example.implanthearts;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.Collections;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.functions.Action1;


/**
 * Created by Administrator on 2019/5/8.
 */

public class CommunityFragment extends BaseFragment  {

    private static final String TAG =CommunityFragment.class.getSimpleName();
    private ListView listview;
    private ListViewAdapter adapter;
    private ImageButton publish;

    private SwipeRefreshLayout swipeRefreshLayout;


    //保存所有图片的地址，方便查看图片大图的时候调用
    public static String a[][] = new String[1][];
    public static int size;
    public static  int n [] = new int[1];


    private BitmapToRound_Util round_Util = new BitmapToRound_Util();
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private File tempFile;




    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==199){
                //添加数据
                loadDate();
                //adapter.notifyDataSetChanged();
                //设置组件的刷洗状态；false代表关闭
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    private List<Publish> publishlist;

    @Override
    protected View initView() {
        return null;
    }

    @Override
    protected void initData() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_community, null);

        listview = (ListView)view.findViewById(R.id.listview);
        publish = (ImageButton)view.findViewById(R.id.publish);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.srfl);
        //设置进度圈的大小;(这里面只有两个值SwipeRefreshLayout.LARGE和DEFAULT，后者是默认效果)
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        //设置进度圈的背景色。这里随便给他设置了一个颜色：浅绿色
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.CYAN);
        //设置进度动画的颜色。这里面最多可以指定四个颜色，我这也是随机设置的，大家知道怎么用就可以了
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark
                ,android.R.color.holo_blue_dark
                ,android.R.color.holo_red_dark
                ,android.R.color.widget_edittext_dark);

        //query();

        //获取数据
        loadDate();


        //时刻刷新数据
        //adapter.notifyDataSetChanged();

        //设置手势滑动监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                //发送一个延时1秒的handler信息
                handler.sendEmptyMessageDelayed(199,1000);
            }
        });

        //给listview设置一个滑动的监听
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当滑动状态发生改变的时候执行
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    //当不滚动的时候
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        //判断是否是最底部
                        if(view.getLastVisiblePosition()==(view.getCount())-1){
//                            for(int x=0;x<5;x++){
//                                stringList.add(stringList.size(),"魔兽世界"+x);
//                            }
//                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
            //正在滑动的时候执行
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


        //发表帖子
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                    //当所有权限都允许之后，返回true
                                    Intent intent=new Intent(getContext(),PublishActivity.class);
                                    startActivity(intent);

            }
        });

        //item点击事件
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),"点击了第"+position+"个",Toast.LENGTH_SHORT).show();
//            }
//        });




        return view;
    }


    private void loadDate() {

        BmobQuery<Publish> bmobQuery = new BmobQuery<Publish>();
        bmobQuery.findObjects(new FindListener<Publish>() {  //按行查询，查到的数据放到List<Goods>的集合
            @Override
            public void done(List<Publish> list, BmobException e) {
                if (e == null){
                    //数据倒序显示,最新的数据在最上面
                    Collections.reverse(list);
                     Log.w("ccc","查询成功"+list.get(0).getName()+list.get(0).getMessage()+list.get(0).getPicture()+list.get(0).getTime());

//                    List<BmobFile> ll = list.get(0).getPicture();

//                     Log.w("ccc",ll.get(0).getFileUrl());
//                    Log.w("ccc",ll.get(1).getFileUrl());
//                    Log.w("ccc",ll.get(2).getFileUrl());

                    //总共有多少条朋友圈
                    size = list.size();
                    Log.w("nnn","总共有多少条朋友圈="+size);
                    a=new String [size][];
                     n  = new int[size];

                    //每条朋友圈的图片数量
                    for(int i = 0;i<size;i++){
                        Log.w("nnn","每条朋友圈的图片数量="+list.get(i).getN());
                        n[i] = list.get(i).getN();
                        a[i] = new String[list.get(i).getN()];
                        for(int j =0;j<list.get(i).getN();j++){
                            a[i][j] = list.get(i).getPicture().get(j).getFileUrl();
                            Log.w("nnn","图片地址"+a[i][j]);
                        }
                    }

                    //测试
                    for(int i = 0;i<size;i++){
                        for(int j = 0;j<n[i];j++){
                            Log.w("nnn","地址"+a[i][j]);
                        }
                    }

                    adapter = new ListViewAdapter(getContext(),list);
                    listview.setAdapter(adapter);


                }

            }
        });
    }




}



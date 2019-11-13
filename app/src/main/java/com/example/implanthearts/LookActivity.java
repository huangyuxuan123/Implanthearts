package com.example.implanthearts;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class LookActivity extends AppCompatActivity {
    private ListView look_listview;
    private Look_ListViewAdapter adapter;

    //保存所有图片的地址，方便查看图片大图的时候调用
    public static String a2[][] = new String[1][];
    public static int size2;
    public static  int n2 [] = new int[1];

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 1:
                    /**
                     获取数据，更新UI
                     */
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        look_listview = (ListView)findViewById(R.id.look_listview);
        ActivityManager.addActivity(this);
//        adapter = new ListViewAdapter(LookActivity.this,List);
//        look_listview.setAdapter(adapter);

        //根据用户名获取数据，查找用户自己发过的帖子，显示出来
        loadDate();


    }

    private void loadDate() {

        BmobQuery<Publish> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("name", HomeFragment.current_user);
        categoryBmobQuery.findObjects(new FindListener<Publish>() {
            @Override
            public void done(final List<Publish> list, BmobException e) {
                if (e == null) {
                    //数据倒序显示,最新的数据在最上面
                    Collections.reverse(list);
                    Log.w("iii","查询成功"+list.get(0).getName()+list.get(0).getMessage()+list.get(0).getPicture()+list.get(0).getTime());

//                    List<BmobFile> ll = list.get(0).getPicture();

//                     Log.w("ccc",ll.get(0).getFileUrl());
//                    Log.w("ccc",ll.get(1).getFileUrl());
//                    Log.w("ccc",ll.get(2).getFileUrl());

                    //总共有多少条朋友圈
                    size2 = list.size();
                    Log.w("iii","总共有多少条朋友圈="+size2);
                    a2=new String [size2][];
                    n2  = new int[size2];

                    //每条朋友圈的图片数量
                    for(int i = 0;i<size2;i++){
                        Log.w("iii","每条朋友圈的图片数量="+list.get(i).getN());
                        n2[i] = list.get(i).getN();
                        a2[i] = new String[list.get(i).getN()];
                        for(int j =0;j<list.get(i).getN();j++){
                            a2[i][j] = list.get(i).getPicture().get(j).getFileUrl();
                            Log.w("iii","图片地址"+a2[i][j]);
                        }
                    }

                    //测试
                    for(int i = 0;i<size2;i++){
                        for(int j = 0;j<n2[i];j++){
                            Log.w("iii","地址"+a2[i][j]);
                        }
                    }

                    adapter = new Look_ListViewAdapter(LookActivity.this,list);
                    look_listview.setAdapter(adapter);

                    look_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                            //先记录要删除的位置，对应哪条帖子
                            final String id2 = list.get(position).getObjectId();

                            //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                            AlertDialog.Builder builder=new AlertDialog.Builder(LookActivity.this);
                            builder.setMessage("确定删除?");
                            builder.setTitle("提示");

                            //添加AlertDialog.Builder对象的setPositiveButton()方法
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //在APP中除帖子
                                    if(list.remove(position)!=null){
                                        System.out.println("success");
                                    }else {
                                        System.out.println("failed");
                                    }

                                    //删除bmob上的数据
                                    delete(id2);
                                    //刷新
                                    adapter.notifyDataSetChanged();

                                    Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                }
                            });

                            //添加AlertDialog.Builder对象的setNegativeButton()方法
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            builder.create().show();
                            return false;
                        }
                    });


                } else {
                    Log.e("iii", e.toString());

                }
            }
        });

    }


    //删除bmob上的数据
    private void delete(String id) {
        //先根据position找到id，再根据id进行删除
        Log.w("ggg","id==="+id);
        Publish p2 = new Publish();
        p2.setObjectId(id);
        p2.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.w("ggg","删除成功");
                }else{
                    Log.w("ggg","删除失败"+e.getMessage());
                }
            }

        });

    }
}

package com.example.implanthearts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2019/6/18.
 */

public class Look_ListViewAdapter  extends BaseAdapter{
    private Context context;
    private List<Publish> list;
    List <String> imageurl;
    GrideViewAdapter adapter;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.mipmap.ic_launcher)                      //设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.ic_launcher)               //设置图片uri为空或者是错位的时候显示的图片
            .showImageOnFail(R.mipmap.ic_launcher)                    //设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)                                          //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)                                            //设置下载的图片是否缓存在SD中
            .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
            .build();                                                     //创建配置或的DisplayImageOption对象

    ImageLoader imageLoader = ImageLoader.getInstance();

    public Look_ListViewAdapter(Context context, List<Publish> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.listview_content, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.gridview = (MyGridView) convertView.findViewById(R.id.gridview);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Publish publish = list.get(position);
        viewHolder.name.setText(publish.getName());//用户名
        viewHolder.text.setText(publish.getMessage());//发表内容
        viewHolder.time.setText("发布时间："+publish.getTime());//发表时间

        //显示发表消息的用户的头像
        BmobQuery<advertisement> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("name", publish.getName());//根据用户名查找对应的图片头像
        categoryBmobQuery.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> object, BmobException e) {
                if (e == null) {
                    String touxiang = object.get(0).getPicture().getFileUrl();
                    Log.w("BMOB",touxiang);

                    imageLoader.displayImage(touxiang, viewHolder.image, options);

                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });



        //给当前的GridView设置一个位置标记
        viewHolder.gridview.setTag(position);

        //给GridView设置条目点击监听
        viewHolder.gridview.setOnItemClickListener(new Look_ListViewAdapter.GridViewItemOnClick());   //添加GridView的点击事件


        imageurl = new ArrayList<>();

        List<BmobFile> ll = publish.getPicture();
        int n = publish.getN();
        Log.w("ccc","多少张图片="+n);
        for(int i = 0;i<n;i++){
            String url = ll.get(i).getFileUrl();
            Log.w("ccc",ll.get(i).getFileUrl());
            imageurl.add(url);
        }



        if (imageurl == null || imageurl.size() == 0) { // 没有图片资源就隐藏GridView
            viewHolder.gridview.setVisibility(View.GONE);
        } else {
            adapter = new GrideViewAdapter(context,imageurl);
            viewHolder.gridview.setAdapter(adapter);

        }


        return convertView;
    }

    public  class ViewHolder {
        ImageView image;
        TextView name;
        TextView text;
        TextView time;
        MyGridView gridview;
    }

    class GridViewItemOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //GridView在ListView条目里的位置
            int lv_item_position= (Integer) parent.getTag();
            Toast.makeText(context,"位置="+lv_item_position,Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"gridview="+position,Toast.LENGTH_SHORT).show();

            /**
             * 启动展示图片的Activity
             * */
            Image image = new Image();
            image.setId(position);

            //获取点击的图片,查看对应消息的所有大图
            List<String> b = new ArrayList<>();

            for(int i = 0;i<LookActivity.size2;i++){
                if(i==lv_item_position){
                    for(int j = 0;j <LookActivity.n2[i];j++){
                        b.add( LookActivity.a2[i][j]);
                    }

                }
            }



            image.setImageurl(b);
            Intent intent = new Intent(context,ShowImageActivity.class);
            intent.putExtra("data",image);
//                Intent intent = new Intent(context, ShowImageActivity.class);
//                intent.putExtra("id", position);   //将当前点击的位置传递过去
//                intent.putStringArrayListExtra("image",imageurl);
            context.startActivity(intent);     //启动Activity
        }
    }
}

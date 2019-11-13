package com.example.implanthearts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/24.
 */

public class ShowImageActivity extends AppCompatActivity {
    private ViewPager viewPager;  //声明viewpage
    private List<View> listViews = null;  //用于获取图片资源
    private int index = 0;   //获取当前点击的图片位置
    private MyPagerAdapter adapter;   //ViewPager的适配器

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去除标题栏
        setContentView(R.layout.show_image_layout);    //绑定布局
        ActivityManager.addActivity(this);
        inint();   //初始化

        //状态栏透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

    }

    private void inint() {
        final Intent intent = getIntent();   //获取intent传递的信息
        viewPager = (ViewPager) findViewById(R.id.show_view_pager);  //绑定viewpager的id
        listViews = new ArrayList<View>();   //初始化list


        Image image  = (Image)getIntent().getParcelableExtra("data");
        int id = image.getId();
        List<String> imageurl = image.getImageurl();

        Log.w("ooo","id==="+id);
        for(int i = 0;i<imageurl.size();i++){
            Log.w("ooo","imageurl==="+imageurl.get(i));
        }


        for (int i = 0; i < imageurl.size() ; i++) {  //for循环将试图添加到list中
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.view_pager_item, null);   //绑定viewpager的item布局文件
            ImageView iv = (ImageView) view.findViewById(R.id.view_image);   //绑定布局中的id

//            Log.w("BBB",image.getId(i));
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.ic_launcher)                      //设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.ic_launcher)               //设置图片uri为空或者是错位的时候显示的图片
                    .showImageOnFail(R.mipmap.ic_launcher)                    //设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                                          //设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)                                            //设置下载的图片是否缓存在SD中
                    .build();                                                     //创建配置或的DisplayImageOption对象

            //设置当前点击的图片
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageurl.get(i), iv, options);


            listViews.add(view);
            /**
             * 图片的长按监听
             * */
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //弹出提示，提示内容为当前的图片位置
                    Toast.makeText(ShowImageActivity.this, "这是第" + (index + 1) + "图片", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
        adapter = new MyPagerAdapter(listViews);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new PageChangeListener()); //设置viewpager的改变监听
                         //截取intent获取传递的值
        viewPager.setCurrentItem(id);    //viewpager显示指定的位置

    }

    /**
     * pager的滑动监听
     * */
    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            index = arg0;
        }

    }


}

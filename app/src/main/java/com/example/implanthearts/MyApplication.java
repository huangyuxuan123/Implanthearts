package com.example.implanthearts;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Administrator on 2019/5/15.
 */

public class MyApplication extends Application {
    private static MyApplication baseApplication;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ImageLoader
        InitImageloader(MyApplication.this);
        baseApplication = this;
        mContext = getApplicationContext();
    }
    private void InitImageloader(Context context) {
        //初始化参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)               //线程优先级
                .denyCacheImageMultipleSizesInMemory()                   //当同一个url获取不用大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())  //将保存的时候的URI名称用MD5
                .tasksProcessingOrder(QueueProcessingType.FIFO)          //设置图片下载和显示的工作队列排序
                .writeDebugLogs()                                        //打印debug log
                .build();

        //全局初始化此配置
        ImageLoader.getInstance().init(configuration);
    }

    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }
    public static Context getCustomApplicationContext() {
        return mContext;
    }
}

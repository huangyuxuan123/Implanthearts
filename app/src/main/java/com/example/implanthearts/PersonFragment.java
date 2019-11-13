package com.example.implanthearts;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.implanthearts.HomeFragment.current_user;

/**
 * Created by Administrator on 2019/5/8.
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {

    //    private Button can;
    Intent intent;
    public static TextView user_name;
    public static TextView user_bangname;
    private ImageView h_back;
    private ImageView h_head;

    private RelativeLayout binding_account;
    private RelativeLayout look;
    private RelativeLayout setting;
    private RelativeLayout dingshi;

    public static String id;

    /* 请求识别码 */


    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private File tempFile;

    private BitmapToRound_Util round_Util = new BitmapToRound_Util();

    private static final String TAG =OperationFragment.class.getSimpleName();

    //是否绑定了账号
//    public static boolean binding = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_person, null);


        user_name = (TextView)view.findViewById(R.id.user_name);
        user_bangname = (TextView)view.findViewById(R.id.user_bang);
        h_back = (ImageView)view.findViewById(R.id.h_back);
        h_head = (ImageView)view.findViewById(R.id.h_head);
//        can = (Button)findViewById(R.id.can);

//        //设置图像背景是磨砂效果
//        Glide.with(this).load(R.drawable.a)
//                .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
//                .into(h_back);
//
//        //设置圆形头像
//        Glide.with(this).load(R.drawable.a)
//                .bitmapTransform(new CropCircleTransformation(getContext()))
//                .into(h_head);

        //显示用户名
        user_name.setText(current_user);


        //绑定账号
        binding_account = (RelativeLayout)view.findViewById(R.id.binding_account);
        binding_account.setOnClickListener(this);

        //查看帖子
        look = (RelativeLayout)view.findViewById(R.id.look);
        look.setOnClickListener(this);

        //设置
        setting = (RelativeLayout)view.findViewById(R.id.setting);
        setting.setOnClickListener(this);


        //更改头像
        h_head.setOnClickListener(this);

        //定时设置
        dingshi = (RelativeLayout)view.findViewById(R.id.dingshi);
        dingshi.setOnClickListener(this);

        //查找有没有被绑定账号
        //query();


        //下载图片
        BmobQuery<advertisement> query=new BmobQuery<advertisement>();
        query.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> list, BmobException e) {
                if(e == null){
                    show_ad(list);
                }else{
                    Toast.makeText(getContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        user_bangname.setText(LandingActivity.user_bang);



        return view;
    }


    @Override
    protected View initView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                intent = new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.binding_account:
                intent = new Intent(getContext(),BindingAccountActivity.class);
                startActivity(intent);

                break;

            case R.id.look:
                intent = new Intent(getContext(),LookActivity.class);
                startActivity(intent);

                break;

            case R.id.h_head:
//                Toast.makeText(getContext(),"点击了头像",Toast.LENGTH_SHORT).show();

                String[] actionItems = {"拍照", "从手机相册选择"};
                final ActionSheetDialog dialog = new ActionSheetDialog(getContext(), actionItems, null);
                dialog.isTitleShow(false).show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            camera();
                        } else if (position == 1) {
                            gallery();
                        }
                        dialog.dismiss();
                    }
                });

                break;

            case R.id.dingshi:
                intent = new Intent(getContext(),DingshiActivity.class);
                startActivity(intent);
                break;

        }

    }



//    //查找有没有被绑定账号
//    private void query() {
//        BmobQuery<Binding> bmobQuery = new BmobQuery<Binding>();
//        bmobQuery.addQueryKeys("initiative_username,passive_username");
//        bmobQuery.findObjects(new FindListener<Binding>() {
//            String initiative_username = null;
//            String passive_username = null;
//            @Override
//            public void done(List<Binding> object, BmobException e) {
//                    Toast.makeText(getContext(),"查询成功：共" + object.size() + "条数据。",Toast.LENGTH_SHORT).show();
//                    binding = true;
//                    try {
//                        initiative_username = object.get(0).getInitiative_username();
//                        passive_username = object.get(0).getPassive_username();
//                        Log.w("zzz", " try : initiative_username==" + initiative_username + " passive_username==" + passive_username);
//                    } catch (Exception e2) {
//                        initiative_username = null;
//                        passive_username = null;
//                        Log.w("zzz", e2.getMessage());
//                        Log.w("zzz", " catch : initiative_username==" + initiative_username + " passive_username==" + passive_username);
//                    }
//                    if (passive_username != null && passive_username.equals(current_user)) {
//                        PersonFragment.user_bang.setText("绑定账号：" + initiative_username);
//                    } else if (passive_username == null && initiative_username == null) {
//                        PersonFragment.user_bang.setText("未绑定账号");
//                    } else if (initiative_username != null && initiative_username.equals(current_user)) {
//                        PersonFragment.user_bang.setText("绑定账号：" + passive_username);
//                    }
//
//            }
//
//
//        });
//
//    }

    /*
 * 从相册获取
 */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 从相机获取
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED) {

            return;
        }


        if (requestCode == PHOTO_REQUEST_GALLERY) {

            if (data != null) {

                Uri uri = data.getData();
                try {

                    crop(uri);

                    //将获取的uri转为String类型
                    String []imgs={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor1=getActivity().managedQuery(uri, imgs, null, null, null);
                    int index1=cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor1.moveToFirst();
                    String img_url1=cursor1.getString(index1);
                    upload(img_url1);

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {

            if (hasSdcard()) {

                tempFile = new File(Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME);
                crop(Uri.fromFile(tempFile));

//                //将获取的uri转为String类型
//                String []imgs={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
//                Cursor cursor1=getActivity().managedQuery(Uri.fromFile(tempFile), imgs, null, null, null);
//                int index1=cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor1.moveToFirst();
//                String img_url1=cursor1.getString(index1);
//                upload(img_url1);

            } else {

                Toast.makeText(getContext(), "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {

            if (data != null) {

                Bitmap bitmap = data.getParcelableExtra("data");



                // 设置图像背景是磨砂效果
//                Glide.with(this).load(Bitmap2Bytes(bitmap))
//                        .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
//                        .into(h_back);

                //设置圆形头像
                 bitmap = round_Util.toRoundBitmap(bitmap);

                //显示头像
                h_head.setImageBitmap(bitmap);

                tempFile = new File(Environment.getExternalStorageDirectory(),
                        IMAGE_FILE_NAME);
                if (tempFile.exists()) {

                    tempFile.delete();
                }

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 设置图像背景是磨砂效果
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }


      //上传图片到素材的应用文件上
//    private void upload2(String imgpath){
//        final BmobFile bmobFile = new BmobFile(new File(imgpath));
//        bmobFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    Log.w("bbb",bmobFile.getFileUrl());
//                    Toast.makeText(getContext(),"上传文件成功:" + bmobFile.getFileUrl(),Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getContext(),"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//    }

        //上传图片到表中
//        private void upload(String imgpath){
//        final BmobFile bmobFile = new BmobFile(new File(imgpath));
//        bmobFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    advertisement ad = new advertisement();
//                    ad.setName(HomeFragment.current_user);//用户名
//                    ad.setPicture(bmobFile);//该用户的头像图片
//                    ad.save();
//                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    Log.w("bbb",bmobFile.getFileUrl());
//                    Toast.makeText(getContext(),"上传文件成功:" + bmobFile.getFileUrl(),Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(getContext(),"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//    }



    //更新表中对应用户的头像图片
    private void upload(String imgpath){
        Log.w("imgpath",imgpath);
        final BmobFile bmobFile = new BmobFile(new File(imgpath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    BmobQuery<advertisement> query = new BmobQuery<advertisement>();
                    query.addWhereEqualTo("name", HomeFragment.current_user);
                    final String[] objectId = new String[1];

                    query.findObjects(new FindListener<advertisement>() {
                        @Override
                        public void done(List<advertisement> list, BmobException e) {
                            if (e == null){
                                for (advertisement ad : list){
                                    objectId[0] = ad.getObjectId();
                                    Log.w(TAG, "获取id成功"+ objectId[0]);
                                }
                                advertisement ad2 = new advertisement();
                                ad2.setPicture(bmobFile);
                                ad2.update(objectId[0], new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            Toast.makeText(getContext(), "信息更新成功", Toast.LENGTH_SHORT).show();
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

                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.w("bbb",bmobFile.getFileUrl());
                    Toast.makeText(getContext(),"上传文件成功:" + bmobFile.getFileUrl(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    /*
* 例子中取得其中一个图片展示，所以选择的是list.get(0)
* 获取到BmobFile对象，然后调用下载方法。
* onProgress方法表示下载进程，目前用不到。
* done表明下载完成，得到的String s表明下载之后的储存的默认地址(当然可以自定义地址，方法请自行查询Bmob开发文档)
* */
    public void show_ad(List<advertisement> list){
        int num = list.size();
        Log.w("nnn","多少条数据："+num);
//        int i = 0;
        for (advertisement ad : list){
            if(ad.getName() != null && HomeFragment.current_user.equals(ad.getName())){
                Log.w("nnn","ad.getName()==="+ad.getName()+"HomeFragment.current_user==="+HomeFragment.current_user);
                BmobFile icon= ad.getPicture();
                Log.w("icon",icon.getFileUrl());
                icon.download(new DownloadFileListener() {
                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            // 设置图像背景是磨砂效果
//                            Glide.with(getContext()).load(R.drawable.a)
//                                    .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
//                                    .into(h_back);

                            //设置圆形头像并显示
                            h_head.setImageBitmap(round_Util.toRoundBitmap(BitmapFactory.decodeFile(s))); //根据地址解码并显示图片
                        }
                    }
                });
//                i++;
                break;
            }
        }


//        Log.w("nnn","i==="+i);
//        if( i == 0){
//            //设置圆形头像并显示
//            h_head.setBackgroundResource(R.drawable.a);
//
//        }
//        i = 0;
    }


}

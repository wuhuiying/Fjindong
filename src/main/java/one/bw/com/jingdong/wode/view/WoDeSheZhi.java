package one.bw.com.jingdong.wode.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.denglu.bean.MyUserBean;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;

public class WoDeSheZhi extends AppCompatActivity {

    private ImageView shezhiFan;
    private RelativeLayout shezhihuantouxiang;
    private ImageView shezhiTouXiang;
    private TextView shezhiname;
    private TextView shezhitoken;
    private SharedPreferences sp;
    private MyPresenter myPresenter;
    private LinearLayout shezhiTananniu;
    private Button shezhiquxiao;
    private Button shezhipaizhao;
    private Button shezhixuanze;
    private String path;
    private TextView shezhiwancheng;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_de_she_zhi);
        shezhiFan = (ImageView) findViewById(R.id.shezhiFan);
        shezhihuantouxiang = (RelativeLayout) findViewById(R.id.shezhihuantouxiang);
        shezhiTouXiang = (ImageView) findViewById(R.id.shezhiTouXiang);
        shezhiname = (TextView) findViewById(R.id.shezhiname);
        shezhitoken = (TextView) findViewById(R.id.shezhitoken);
        shezhiTananniu = (LinearLayout) findViewById(R.id.shezhiTananniu);
        shezhixuanze = (Button) findViewById(R.id.shezhixuanze);
        shezhipaizhao = (Button) findViewById(R.id.shezhipaizhao);
        shezhiquxiao = (Button) findViewById(R.id.shezhiquxiao);
        shezhiwancheng = (TextView) findViewById(R.id.shezhiwancheng);
        sp = getSharedPreferences("wc", MODE_PRIVATE);
        edit = sp.edit();
        myPresenter = new MyPresenter();

        //为头像，用户名，昵称，，开始赋值
        getUser();

        //点击”换头像“弹出底部的弹出框
        shezhihuantouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shezhiTananniu.setVisibility(View.VISIBLE);
            }
        });
        //“选择相册”点击
        shezhixuanze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, 100);
            }
        });
        //“拍照点击”
        shezhipaizhao.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //启动相机
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                File file1 = new File("/storage/emulated/0/DCIM/Camera/" + getPhotoFileName());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
                //记录当前路径
                path = file1.getPath();
                startActivityForResult(intent,200);
            }
        });
        //取消按钮
        shezhiquxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shezhiTananniu.setVisibility(View.INVISIBLE);
            }
        });
        //点击返回图片的事件
        shezhiFan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //完成点击事件
        shezhiwancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }
    //相片取名
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&resultCode==RESULT_OK){
            Uri uri = data.getData();
            //将uri路径转成绝对路径
            String[] proj = { MediaStore.Images.Media.DATA };

            Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);

            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            actualimagecursor.moveToFirst();

            String img_path = actualimagecursor.getString(actual_image_column_index);

            File file = new File(img_path);

            //A.检查用户是否已经允许了权限....PackageManager.PERMISSION_GRANTED代表的是用户已经允许
            //B.不允许...的时候,,,请求用户允许这个权限
            // Activity arg0代表当前的activity, @NonNull String[] arg1请求的权限的数组,也就是需要请求允许哪些权限, int arg2请求码
            if (ContextCompat.checkSelfPermission(WoDeSheZhi.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WoDeSheZhi.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }else {
                //允许...上传文件
                postFile(file);
                shezhiTananniu.setVisibility(View.INVISIBLE);
            }
        }else if(requestCode==200&resultCode==RESULT_OK){
            File file = new File(path);//拿到存储时所记录的路径
            if (ContextCompat.checkSelfPermission(WoDeSheZhi.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WoDeSheZhi.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }else {
                //允许...上传文件
                postFile(file);
                shezhiTananniu.setVisibility(View.INVISIBLE);
            }
        }
    }

    //退出按钮的点击事件
    public void tui(View view) {

        finish();
    }

    //请求个人信息接口，，为头像赋值
    private void getUser() {
        String uid = sp.getString("uid", "");
        String ren = APIuil.getRen(uid);
        myPresenter.getContent(ren, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyUserBean myUserBean = gson.fromJson(json, MyUserBean.class);
                        if(myUserBean.getCode().equals("0")){
                            edit.putString("icon", (String) myUserBean.getData().getIcon()).commit();
                            if(myUserBean.getData().getIcon().equals("null")){
                                shezhiTouXiang.setImageResource(R.mipmap.ic_launcher);
                            }else{
                                ImageLoader.getInstance().displayImage(((String)myUserBean.getData().getIcon()),shezhiTouXiang, ImageLoaderUtil.showImagYuan());
                            }
                            shezhiname.setText(myUserBean.getData().getUsername());
                            shezhitoken.setText(myUserBean.getData().getToken());
                        }else{
                            Toast.makeText(WoDeSheZhi.this,myUserBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onShiBai(final String ss) {
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WoDeSheZhi.this,ss,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //上传文件的方法，，上传成功后，重新请求用户接口，从新为头像等赋值
    private void postFile(File file) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //3.指定要上传的文件对象
//        File file = new File(Environment.getExternalStorageDirectory(),"fruit8.jpg");
//        Log.i("jiaaa","===="+file.getPath());
        //参数名，，，参数，，，参数类型，，，路径
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));

        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)//请求体
                .url(APIuil.chuanFile(file.getPath()))//指定路径
                .build();//启动
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WoDeSheZhi.this,string,Toast.LENGTH_SHORT).show();
                        getUser();
                    }
                });
            }
        });
    }

    //退出当前登录
    public void tuichu(View view) {
        edit.clear();
        edit.putBoolean("kai",false);
        edit.commit();

        finish();
    }
}

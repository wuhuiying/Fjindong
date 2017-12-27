package one.bw.com.jingdong.denglu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.denglu.bean.MyUserBean;
import one.bw.com.jingdong.shouye.ShouYe;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;

public class DengLu extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deng_lu);
        ImageView dengluQQ = (ImageView) findViewById(R.id.dengluQQ);
        ImageView dengluWEiXin = (ImageView) findViewById(R.id.dengluWEiXin);
        sp = getSharedPreferences("wc", MODE_PRIVATE);
        edit = sp.edit();
        boolean kai = sp.getBoolean("kai", false);
        if(kai){
            Intent intent = new Intent(DengLu.this, ShouYe.class);
            startActivity(intent);
            finish();
            Toast.makeText(DengLu.this,"您以登录成功！",Toast.LENGTH_SHORT).show();
        }
        dengluQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DengLu.this,"第三方QQ登录正在启动...",Toast.LENGTH_SHORT).show();
            }
        });
        dengluWEiXin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DengLu.this,"第三方”微信“登录正在启动...",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deng(View view) {
        EditText tel = (EditText) findViewById(R.id.tel);
        EditText pass = (EditText) findViewById(R.id.pass);
        String telzhi = tel.getText().toString();
        String passzhi = pass.getText().toString();
        if(telzhi.equals("")||passzhi.equals("")){
            Toast.makeText(DengLu.this,"用户和密码不能为空！",Toast.LENGTH_SHORT).show();
        }else {

            String url = APIuil.dengluJieKou(telzhi,passzhi);
            MyPresenter myPresenter = new MyPresenter();
            myPresenter.getContent(url, new MyJieKou() {
                @Override
                public void onChengGong(final String json) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            MyUserBean myUserBean = gson.fromJson(json, MyUserBean.class);
                            if (myUserBean.getCode().equals("0")) {
                                Toast.makeText(DengLu.this, myUserBean.getMsg(), Toast.LENGTH_SHORT).show();
                                edit.putBoolean("kai", true);
                                edit.putString("uid", myUserBean.getData().getUid() + "");
                                edit.putString("token",myUserBean.getData().getToken());
                                edit.putString("userName",myUserBean.getData().getUsername());
                                edit.putString("icon", (String) myUserBean.getData().getIcon());
                                edit.commit();
                                Intent intent = new Intent(DengLu.this, ShouYe.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(DengLu.this, myUserBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onShiBai(final String ss) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DengLu.this, ss, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void zhu(View view) {
        Intent intent = new Intent(DengLu.this, ZhuCe.class);
        startActivity(intent);
    }
}

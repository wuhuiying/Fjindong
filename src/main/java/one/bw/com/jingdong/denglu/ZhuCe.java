package one.bw.com.jingdong.denglu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.denglu.bean.MyUserZhuCe;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;

public class ZhuCe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ce);
        TextView fan = (TextView) findViewById(R.id.zhucefan);
        fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void zhu(View view) {
        EditText ztel = (EditText) findViewById(R.id.ztel);
        EditText zpass = (EditText) findViewById(R.id.zpass);
        String ztelzhi = ztel.getText().toString();
        String zpasszhi = zpass.getText().toString();
        if(ztelzhi.equals("")||zpasszhi.equals("")){
            Toast.makeText(ZhuCe.this,"用户和密码不能为空！",Toast.LENGTH_SHORT).show();
        }else{
            String url= APIuil.zhuceJieKou(ztelzhi,zpasszhi);
            MyPresenter myPresenter = new MyPresenter();
            myPresenter.getContent(url, new MyJieKou() {
                @Override
                public void onChengGong(final String json) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            MyUserZhuCe myBean1 = gson.fromJson(json, MyUserZhuCe.class);
                            if(myBean1.getCode().equals("0")){
                                Toast.makeText(ZhuCe.this,myBean1.getMsg(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ZhuCe.this, DengLu.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ZhuCe.this,myBean1.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onShiBai(final String ss) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ZhuCe.this,ss,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }
}

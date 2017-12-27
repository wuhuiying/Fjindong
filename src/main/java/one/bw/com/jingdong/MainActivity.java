package one.bw.com.jingdong;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import one.bw.com.jingdong.shouye.ShouYe;

public class MainActivity extends AppCompatActivity {
    int i=0;
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            i++;
            if(i==3){
                Intent intent = new Intent(MainActivity.this, ShouYe.class);
                startActivity(intent);
                finish();
            }else{
                h.sendEmptyMessageDelayed(0,1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h.sendEmptyMessageDelayed(0,1000);
    }

}

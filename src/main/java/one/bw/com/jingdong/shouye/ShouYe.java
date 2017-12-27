package one.bw.com.jingdong.shouye;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.shouye.frame.FraFa;
import one.bw.com.jingdong.shouye.frame.FraFen;
import one.bw.com.jingdong.shouye.frame.FraGou;
import one.bw.com.jingdong.shouye.frame.FraSou;
import one.bw.com.jingdong.wode.view.FraWode;

public class ShouYe extends AppCompatActivity {

    private FrameLayout frameLayout;
    private RadioButton shou;
    private RadioButton fen;
    private RadioButton fa;
    private RadioButton gou;
    private RadioButton my;
    private LinearLayout sao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shou_ye);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg);
        shou = (RadioButton) findViewById(R.id.shou);
        fen = (RadioButton) findViewById(R.id.fen);
        fa = (RadioButton) findViewById(R.id.fa);
        gou = (RadioButton) findViewById(R.id.gou);
        my = (RadioButton) findViewById(R.id.my);

        //初始化二维码工具类
        ZXingLibrary.initDisplayOpinion(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraSou()).commit();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i==R.id.shou){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraSou()).commit();
                }
                if(i==R.id.fen){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraFen()).commit();
                }
                if(i==R.id.fa){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraFa()).commit();
                }
                if(i==R.id.gou){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraGou()).commit();
                }
                if(i==R.id.my){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraWode()).commit();
                }


            }
        });

//        sao = (LinearLayout) findViewById(R.id.sao);
//        sao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(ShouYe.this,"进行扫描",Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }
}

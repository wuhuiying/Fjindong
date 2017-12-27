package one.bw.com.jingdong.gouwu.gouwuche.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyShangPingBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyaddCart;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;


public class ShangpingXiangQing extends AppCompatActivity {
    private TextView shangjianame;
    private ImageView shangjiaimag;
    private ImageView shangpinimag;
    private TextView shangpinprice;
    private TextView shangpintitle;
    private Button addCart;
    private SharedPreferences sp;
    private Button getCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangping_xiang_qing);
        shangjianame = (TextView) findViewById(R.id.shangjianame);
        shangjiaimag = (ImageView) findViewById(R.id.shangjiaimag);
        shangpinimag = (ImageView) findViewById(R.id.shangpinimag);
        shangpinprice = (TextView) findViewById(R.id.shangpinprice);
        shangpintitle = (TextView) findViewById(R.id.shangpintitle);
        addCart = (Button) findViewById(R.id.addCart);
        getCart = (Button) findViewById(R.id.getCart);

        sp = getSharedPreferences("wc", Context.MODE_PRIVATE);
        final boolean kai = sp.getBoolean("kai", false);

        Bundle bundle = getIntent().getExtras();
        MyGouWuBean.DataBean dataBean = (MyGouWuBean.DataBean) bundle.getSerializable("dataBean");

        final int pid = dataBean.getPid();
        String shangping = APIuil.shangping(pid + "");
        MyPresenter myPresenter = new MyPresenter();
        myPresenter.getContent(shangping, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyShangPingBean myShangPingBean = gson.fromJson(json, MyShangPingBean.class);
                        ImageLoader.getInstance().displayImage(myShangPingBean.getSeller().getIcon(),shangjiaimag, ImageLoaderUtil.showImag());
                        shangjianame.setText(myShangPingBean.getSeller().getName());
                        String[] split = myShangPingBean.getData().getImages().split("\\|");
                        ImageLoader.getInstance().displayImage(split[0],shangpinimag, ImageLoaderUtil.showImag());
                        shangpintitle.setText(myShangPingBean.getData().getTitle());
                        shangpinprice.setText("￥"+myShangPingBean.getData().getPrice());

                    }
                });
            }

            @Override
            public void onShiBai(String ss) {
                Toast.makeText(ShangpingXiangQing.this,ss,Toast.LENGTH_SHORT).show();
            }
        });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kai){
                    String uid = sp.getString("uid", "");
                    String s = APIuil.addCar(uid, pid);
                    Log.i("jiss","======addCart:"+s);
                    MyPresenter myPresenter1 = new MyPresenter();
                    myPresenter1.getContent(s, new MyJieKou() {
                        @Override
                        public void onChengGong(final String json) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Gson gson = new Gson();
                                    MyaddCart myaddCart = gson.fromJson(json, MyaddCart.class);
                                    if(myaddCart.getCode().equals("0")){
                                        Toast.makeText(ShangpingXiangQing.this,myaddCart.getMsg(),Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ShangpingXiangQing.this,myaddCart.getMsg(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                        @Override
                        public void onShiBai(String ss) {
                            Toast.makeText(ShangpingXiangQing.this,ss,Toast.LENGTH_SHORT).show();
                        }
                    });


                }else{
                    Toast.makeText(ShangpingXiangQing.this,"请先登录！",Toast.LENGTH_SHORT).show();

                }
            }
        });

        getCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ShangpingXiangQing.this, ShouYe.class);
//                startActivity(intent);
                finish();
            }
        });

    }
}

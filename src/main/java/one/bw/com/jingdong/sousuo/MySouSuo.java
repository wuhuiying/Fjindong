package one.bw.com.jingdong.sousuo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.gouwu.gouwuche.inters.DianjiJieKou;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.sousuo.adapter.MyReSouAdapter;
import one.bw.com.jingdong.sousuo.adapter.MySouSuoLvAdapter;
import one.bw.com.jingdong.sousuo.bean.MySousuoBean;
import one.bw.com.jingdong.sousuo.dao.MySousuoDao;
import one.bw.com.jingdong.sousuo.view.MySousuoFlowLayout;

public class MySouSuo extends AppCompatActivity {

    private String mNames[] = {
            "连衣裙","德龙咖啡机","镜头手机",
            "记忆棉垫","小米手环","笔记本",
            "三只松鼠","晾衣架","手机",
            "月饼","padding","text",
            "name","type","search","logcat"
    };
    private MySousuoFlowLayout mySousuoFlowLayout;
    private ImageView sousuofan;
    private TextView sousuobut;
    private EditText sousuozhi;
    private MyPresenter myPresenter;
    private LinearLayout flowReSou;
    private LinearLayout lishiSouSuo;
    private RecyclerView resouRecyclerView;
    private ListView sousuolv;
    private MySousuoDao mySousuoDao;
    ArrayList<String> list2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sou_suo);
        mySousuoFlowLayout = (MySousuoFlowLayout) findViewById(R.id.mySousuoFlowLayout);
        sousuofan = (ImageView) findViewById(R.id.sousuofan);
        sousuobut = (TextView) findViewById(R.id.sousuobut);
        sousuozhi = (EditText) findViewById(R.id.sousuozhi);
        flowReSou = (LinearLayout) findViewById(R.id.flowReSou);
        lishiSouSuo = (LinearLayout) findViewById(R.id.lishiSouSuo);
        resouRecyclerView = (RecyclerView) findViewById(R.id.resouRecyclerView);
        sousuolv = (ListView) findViewById(R.id.sousuolv);

        SQLiteDatabase db = openOrCreateDatabase("wc.db", MODE_PRIVATE, null);
        mySousuoDao = new MySousuoDao(db);
        myPresenter = new MyPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取数据库，，，判断是否有值，，有值就隐藏流式
        ArrayList<String> list2 = mySousuoDao.chaList();

        if(list2.size()>0){
            setLayout();
            initChildViews();
        }else{
            initChildViews();//流式布局，，，展示数据，，，流式布局的点击事件
        }
        //返回按钮
        sousuofan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //搜索按钮
        sousuobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = sousuozhi.getText().toString();
                if(s.equals("")){
                    Toast.makeText(MySouSuo.this,"搜索内容不能为空！",Toast.LENGTH_SHORT).show();
                }else{
                    boolean b = mySousuoDao.chaLiShi(s);
                    if(!b){
                        mySousuoDao.addShuJu(s);
                    }
                    //线布局，，，后接口
                    setLayout();
                    String sousuo = APIuil.sousuo(s);
                    getSouSuoContent(s,sousuo);
                }
            }
        });
    }

    //流式布局，，，展示数据，，，流式布局的点击事件
    private void initChildViews() {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for( int i = 0; i < mNames.length; i++){
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.WHITE);
            view.setTextSize(12);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.flowtextview));
            mySousuoFlowLayout.addView(view,lp);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //判断钙元素是否存在数据库中
                    boolean b = mySousuoDao.chaLiShi(mNames[finalI]);
                    if(!b){
                        mySousuoDao.addShuJu(mNames[finalI]);
                    }
                    //先改布局，，后请求接口
                    setLayout();
                    String sousuo = APIuil.sousuo(mNames[finalI]);
                    getSouSuoContent(mNames[finalI],sousuo);

                }
            });

        }
    }

    //更改布局
    private void setLayout() {
        //将历史布局显示，，，并改变热搜布局
        flowReSou.setVisibility(View.GONE);
        lishiSouSuo.setVisibility(View.VISIBLE);
        //流式横向布局
        resouRecyclerView.setLayoutManager(new LinearLayoutManager(MySouSuo.this, OrientationHelper.HORIZONTAL,false));
        MyReSouAdapter myReSouAdapter = new MyReSouAdapter(MySouSuo.this, mNames);
        resouRecyclerView.setAdapter(myReSouAdapter);
        //历史展示
        ArrayList<String> list = mySousuoDao.chaList();
        list2.clear();
        list2.addAll(list);
        sousuolv.setAdapter(new MySouSuoLvAdapter(MySouSuo.this,list2));
        //横向流式点击
        myReSouAdapter.setDianjie(new DianjiJieKou() {
            @Override
            public void onItemClike(int i) {
                boolean b = mySousuoDao.chaLiShi(mNames[i]);
                if(!b){
                    mySousuoDao.addShuJu(mNames[i]);
                }
                ArrayList<String>  list1 = mySousuoDao.chaList();
                list2.clear();
                list2.addAll(list1);
                sousuolv.setAdapter(new MySouSuoLvAdapter(MySouSuo.this,list2));

                String sousuo = APIuil.sousuo(mNames[i]);
                getSouSuoContent(mNames[i],sousuo);
            }
        });

        sousuolv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = list2.get(i);
                String sousuo = APIuil.sousuo(s);
                getSouSuoContent(s,sousuo);
            }
        });
    }

    //拼接参数，，，访问接口，，获取数据
    private void getSouSuoContent(final String s, String sousuo) {

        myPresenter.getContent(sousuo, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MySousuoBean mySousuoBean = gson.fromJson(json, MySousuoBean.class);
                        if(mySousuoBean.getCode().equals("0")){
                            if(mySousuoBean.getData().toString().equals("[]")){
                                Toast.makeText(MySouSuo.this,"没有该类商品！",Toast.LENGTH_SHORT).show();
                            }else{
                                //跳转，，将解析出来的对象穿走
                                Intent intent = new Intent(MySouSuo.this, MySouSuoShow.class);
                                intent.putExtra("json",json);
                                intent.putExtra("keywords",s);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(MySouSuo.this,mySousuoBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onShiBai(final String ss) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MySouSuo.this,ss,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }


    public void sousuoqingkong(View view) {
        mySousuoDao.qingLishi();
        //将历史布局隐藏，，，热搜显示
        flowReSou.setVisibility(View.VISIBLE);
        lishiSouSuo.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

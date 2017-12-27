package one.bw.com.jingdong.sousuo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.view.ShangpingXiangQing;
import one.bw.com.jingdong.shouye.ShouYe;
import one.bw.com.jingdong.sousuo.adapter.MySouxiangBiaoAdapter;
import one.bw.com.jingdong.sousuo.adapter.MySouxiangLieAdapter;
import one.bw.com.jingdong.sousuo.inerts.ScrollViewListener;
import one.bw.com.jingdong.sousuo.view.MyGridView;
import one.bw.com.jingdong.sousuo.view.MyScrollView;

public class MySouSuoShow extends AppCompatActivity {

    ArrayList<MyGouWuBean.DataBean> list = new ArrayList<>();
    private ListView souxiangListView;
    private MySouxiangLieAdapter mySouxiangLieAdapter;
    private MyScrollView souxiangScrollView;
    private DrawerLayout souxiangDrawerLayout;
    private RadioGroup cebianrg;
    private RelativeLayout souxiangcebian;
    private TextView souxiangzong;
    int i=0;
    int j=0;
    private TextView souxiangjiageUp;
    private TextView souxiangjiageDown;
    private TextView souxiangzongDown;
    private ImageView souxiangGeShi;
    int z=0;
    private MySouxiangBiaoAdapter mySouxiangBiaoAdapter;
    private MyGridView souxiangGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sou_suo_show);
        souxiangScrollView = (MyScrollView) findViewById(R.id.souxiangScrollView);
        souxiangListView = (ListView) findViewById(R.id.souxiangListView);
        TextView souxiangkeywords = (TextView) findViewById(R.id.souxiangkeywords);//搜索值
        ImageView souxiangfan = (ImageView) findViewById(R.id.souxiangfan);//返回键
        LinearLayout souxiangsousuo = (LinearLayout) findViewById(R.id.souxiangsousuo);
        souxiangzong = (TextView) findViewById(R.id.souxiangzong);
        final TextView souxiangxiao = (TextView) findViewById(R.id.souxiangxiao);
        final TextView souxiangjiage = (TextView) findViewById(R.id.souxiangjiage);
        final TextView souxiangshuai = (TextView) findViewById(R.id.souxiangshuai);
        souxiangDrawerLayout = (DrawerLayout) findViewById(R.id.souxiangDrawerLayout);
        cebianrg = (RadioGroup) findViewById(R.id.cebianrg);
        souxiangcebian = (RelativeLayout) findViewById(R.id.souxiangcebian);
        souxiangjiageUp = (TextView) findViewById(R.id.souxiangjiageUp);
        souxiangjiageDown = (TextView) findViewById(R.id.souxiangjiageDown);
        souxiangzongDown = (TextView) findViewById(R.id.souxiangzongDown);
        souxiangGeShi = (ImageView) findViewById(R.id.souxiangGeShi);
        souxiangGridView = (MyGridView) findViewById(R.id.souxiangGridView);

        String json = getIntent().getStringExtra("json");//获取值
        String keywords = getIntent().getStringExtra("keywords");
        souxiangkeywords.setText(keywords);//给搜索框添加值
        //数据的解析，，，，显示
        Gson gson = new Gson();
        final MyGouWuBean myGouWuBean = gson.fromJson(json, MyGouWuBean.class);
        List<MyGouWuBean.DataBean> data = myGouWuBean.getData();
        list.addAll(data);

        //默认以listview格式展示商品数据
        setAdapter();

        View view = View.inflate(MySouSuoShow.this, R.layout.souxiangshanglajiazai, null);
        souxiangListView.addFooterView(view);
        //让listview与gridview失去焦点，从而达到scrollview默认展示顶部的效果
        souxiangListView.setFocusable(false);
        souxiangGridView.setFocusable(false);
        //上拉加载数据
        souxiangScrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView context, int x, int y, int oldx, int oldy) {
//                Toast.makeText(MySouSuoShow.this,"正在加载。。。",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<MyGouWuBean.DataBean> data = myGouWuBean.getData();
                list.addAll(data);
                if(z%2==1){
                    setBiaoGeAdapter();
                }else{
                    setAdapter();
                }
            }
        });

        //点击综合排序，，，弹出popu框，，，并选择传值,,,加载popu视图是不能写在点击事件里面
        final View view1 = View.inflate(MySouSuoShow.this, R.layout.souxiangzonghe, null);
        final PopupWindow popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        souxiangzong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                souxiangzong.setTextColor(Color.RED);
                souxiangzongDown.setTextColor(Color.RED);
                souxiangxiao.setTextColor(Color.BLACK);
                souxiangjiage.setTextColor(Color.BLACK);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(souxiangzong, 0, 0);
            }
        });
        //点击其他地方也可以关闭popu框
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        setPopuWindowClick(view1,popupWindow);//点击popu框传值


        //点击销量排序，，奇数倒序，，偶数正序
        souxiangxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                souxiangzong.setTextColor(Color.BLACK);
                souxiangzongDown.setTextColor(Color.BLACK);
                souxiangxiao.setTextColor(Color.RED);
                souxiangjiage.setTextColor(Color.BLACK);
                if(i%2==1){
                    Collections.sort(list, new Comparator<MyGouWuBean.DataBean>() {
                        @Override
                        public int compare(MyGouWuBean.DataBean dataBean, MyGouWuBean.DataBean t1) {
                            //sort判断，，根据x轴定律，，小数在左，大数在右
                            if(dataBean.getSalenum()>t1.getSalenum()){
                                return -1;
                            }else if(dataBean.getSalenum()<t1.getSalenum()){
                                return 1;
                            }else{
                                return 0;
                            }
                        }
                    });
                }else{
                    Collections.sort(list, new Comparator<MyGouWuBean.DataBean>() {
                        @Override
                        public int compare(MyGouWuBean.DataBean dataBean, MyGouWuBean.DataBean t1) {
                            if(dataBean.getSalenum()>t1.getSalenum()){
                                return 1;
                            }else if(dataBean.getSalenum()<t1.getSalenum()){
                                return -1;
                            }else{
                                return 0;
                            }
                        }
                    });
                }
                if(z%2==1){
                    setBiaoGeAdapter();
                }else{
                    setAdapter();
                }
            }
        });

        //点击价格排序,,,默认正序，，奇数倒序，，偶数正序
        souxiangjiage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j++;
                souxiangzong.setTextColor(Color.BLACK);
                souxiangzongDown.setTextColor(Color.BLACK);
                souxiangxiao.setTextColor(Color.BLACK);
                souxiangjiage.setTextColor(Color.RED);
                if(j%2==1){
                    souxiangjiageDown.setTextColor(Color.RED);
                    souxiangjiageUp.setTextColor(Color.BLACK);
                    Collections.sort(list, new Comparator<MyGouWuBean.DataBean>() {
                        @Override
                        public int compare(MyGouWuBean.DataBean dataBean, MyGouWuBean.DataBean t1) {
                            if(dataBean.getPrice()>t1.getPrice()){
                                return -1;
                            }else if(dataBean.getPrice()<t1.getPrice()){
                                return 1;
                            }else{
                                return 0;
                            }
                        }
                    });
                }else{
                    souxiangjiageDown.setTextColor(Color.BLACK);
                    souxiangjiageUp.setTextColor(Color.RED);
                    Collections.sort(list, new Comparator<MyGouWuBean.DataBean>() {
                        @Override
                        public int compare(MyGouWuBean.DataBean dataBean, MyGouWuBean.DataBean t1) {
                            if(dataBean.getPrice()>t1.getPrice()){
                                return 1;
                            }else if(dataBean.getPrice()<t1.getPrice()){
                                return -1;
                            }else{
                                return 0;
                            }
                        }
                    });
                }
                if(z%2==1){
                    setBiaoGeAdapter();
                }else{
                    setAdapter();
                }

            }
        });
        //筛选点击，，，打开侧拉
        souxiangshuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                souxiangDrawerLayout.openDrawer(souxiangcebian);
            }
        });
        //关闭侧拉
        cebianrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton cebianshuai = (RadioButton) findViewById(R.id.cebianshuai);
                if(i==R.id.cebianshuai){
                    souxiangDrawerLayout.closeDrawer(souxiangcebian);
                    cebianshuai.setChecked(false);
                }
            }
        });



        //搜索框的点击事件
        souxiangsousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySouSuoShow.this, MySouSuo.class);
                startActivity(intent);
            }
        });

        //"<"按钮的点击事件
        souxiangfan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySouSuoShow.this, ShouYe.class);
               startActivity(intent);
            }
        });


        //切换布局格式，，奇数表格，，偶数列表
        souxiangGeShi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                z++;
                if(z%2==1){
                    souxiangGeShi.setImageResource(R.drawable.liebiao);
                    souxiangGridView.setVisibility(View.VISIBLE);
                    souxiangListView.setVisibility(View.GONE);
                    setBiaoGeAdapter();

                }else{
                    souxiangGeShi.setImageResource(R.drawable.biaoge);
                    souxiangGridView.setVisibility(View.GONE);
                    souxiangListView.setVisibility(View.VISIBLE);
                    setAdapter();
                }
            }
        });

        souxiangListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyGouWuBean.DataBean dataBean = list.get(i);
                Intent intent = new Intent(MySouSuoShow.this, ShangpingXiangQing.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean",dataBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        souxiangGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyGouWuBean.DataBean dataBean = list.get(i);
                Intent intent = new Intent(MySouSuoShow.this, ShangpingXiangQing.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean",dataBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    //popuwindow框内部的操作
    private void setPopuWindowClick(final View view1, final PopupWindow popupWindow) {
        RelativeLayout souxiangpopu1 = view1.findViewById(R.id.souxiangpopu1);
        RelativeLayout souxiangpopu2 = view1.findViewById(R.id.souxiangpopu2);
        RelativeLayout souxiangpopu3 = view1.findViewById(R.id.souxiangpopu3);
        final TextView zongpai = view1.findViewById(R.id.zongpai);
        final TextView zonggou = view1.findViewById(R.id.zonggou);
        final TextView xinpinpai = view1.findViewById(R.id.xinpinpai);
        final TextView xinpingou = view1.findViewById(R.id.xinpingou);
        final TextView pingpai = view1.findViewById(R.id.pingpai);
        final TextView pinggou = view1.findViewById(R.id.pinggou);
        souxiangpopu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                souxiangzong.setText("综合");
                zongpai.setTextColor(Color.RED);
                zonggou.setVisibility(View.VISIBLE);
                xinpinpai.setTextColor(Color.BLACK);
                xinpingou.setVisibility(View.INVISIBLE);
                pingpai.setTextColor(Color.BLACK);
                pinggou.setVisibility(View.INVISIBLE);
                popupWindow.dismiss();
                Toast.makeText(MySouSuoShow.this,"按照综合排序！",Toast.LENGTH_SHORT).show();
            }
        });
        souxiangpopu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                souxiangzong.setText("新品");
                zongpai.setTextColor(Color.BLACK);
                zonggou.setVisibility(View.INVISIBLE);
                xinpinpai.setTextColor(Color.RED);
                xinpingou.setVisibility(View.VISIBLE);
                pingpai.setTextColor(Color.BLACK);
                pinggou.setVisibility(View.INVISIBLE);
                popupWindow.dismiss();
                Toast.makeText(MySouSuoShow.this,"按照新品排序！",Toast.LENGTH_SHORT).show();
            }
        });
        souxiangpopu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                souxiangzong.setText("评论");
                zongpai.setTextColor(Color.BLACK);
                zonggou.setVisibility(View.INVISIBLE);
                xinpinpai.setTextColor(Color.BLACK);
                xinpingou.setVisibility(View.INVISIBLE);
                pingpai.setTextColor(Color.RED);
                pinggou.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
                Toast.makeText(MySouSuoShow.this,"按照评论排序！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //刷新适配器
    public void setAdapter(){
        if(mySouxiangLieAdapter==null){
            mySouxiangLieAdapter = new MySouxiangLieAdapter(MySouSuoShow.this, list);
            souxiangListView.setAdapter(mySouxiangLieAdapter);
        }else{
            mySouxiangLieAdapter.notifyDataSetChanged();
        }

    }
    public void setBiaoGeAdapter(){
        if(mySouxiangBiaoAdapter==null){
            mySouxiangBiaoAdapter = new MySouxiangBiaoAdapter(MySouSuoShow.this, list);
            souxiangGridView.setAdapter(mySouxiangBiaoAdapter);
        }else{
            mySouxiangBiaoAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}

package one.bw.com.jingdong.shouye.frame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.denglu.DengLu;
import one.bw.com.jingdong.gouwu.gouwuche.adapter.MyGouWuAdapter;
import one.bw.com.jingdong.gouwu.gouwuche.adapter.MyGouWuLvAdapter;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyaddCart;
import one.bw.com.jingdong.gouwu.gouwuche.view.MyBianJiView;
import one.bw.com.jingdong.gouwu.gouwuche.view.MyExpandableListView;
import one.bw.com.jingdong.gouwu.gouwuche.view.ShangpingXiangQing;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.sousuo.inerts.ScrollViewListener;
import one.bw.com.jingdong.sousuo.view.MyGridView;
import one.bw.com.jingdong.sousuo.view.MyScrollView;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class FraGou extends Fragment {

    private MyGridView gouwuMyGridView;
    ArrayList<MyGouWuBean.DataBean> list = new ArrayList<>();
    private MyExpandableListView gouwuExlv;
    private LinearLayout gouwukong;
    private TextView bianji;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    public  MyGouWuBean.DataBean dataBean;
    private TextView userName;

    int page=1;
    private MyPresenter myPresenter;
    private MyGouWuAdapter myGouWuAdapter;
    private Button denglu;
    private LinearLayout denglutiao;
    private LinearLayout huanyingtiao;
    public static TextView gouwusum;
    public CheckBox gouwuQuXuan;
    String getShangping="";
    MyGouBean myGet;
    boolean gouwuquanxuan=true;
    private MyScrollView gouwuMyScrollView;
    private ImageView gouwudingwei;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gouwuche, container, false);
        gouwuMyGridView = view.findViewById(R.id.gouwuMyGridView1);
        gouwuExlv = view.findViewById(R.id.gouwuExlv);
        gouwukong = view.findViewById(R.id.gouwukong);
        bianji = view.findViewById(R.id.bianji);
        denglu = view.findViewById(R.id.denglu);
        denglutiao = view.findViewById(R.id.denglutiao);
        huanyingtiao = view.findViewById(R.id.huanyingtiao);
        userName = view.findViewById(R.id.userName);
        gouwusum = view.findViewById(R.id.gouwusum);
        gouwuQuXuan = view.findViewById(R.id.gouwuQuXuan);
        gouwuMyScrollView = view.findViewById(R.id.gouwuMyScrollView);
        gouwudingwei = view.findViewById(R.id.gouwudingwei);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //”为你推荐“的默认展示条目展示
        gouwuTuiJian();
        //判断是否登录
        sp = getActivity().getSharedPreferences("wc", Context.MODE_PRIVATE);
        edit = sp.edit();
        final boolean kai = sp.getBoolean("kai", false);
        //如果登录，，，查看购物车，，，
        if(kai){
            denglutiao.setVisibility(View.GONE);
            huanyingtiao.setVisibility(View.VISIBLE);
            String uid = sp.getString("uid", "");
//            String token = sp.getString("token", "");
            String userName = sp.getString("userName", "");
            this.userName.setText(userName);

            String car = APIuil.getCar(uid);
            Log.i("jibasd",car);
            MyPresenter myPresenter1 = new MyPresenter();
            myPresenter1.getContent(car, new MyJieKou() {
                @Override
                public void onChengGong(final String json) {
                    getShangping=json;
                    if(json.equals("null")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"购物车为空!",Toast.LENGTH_SHORT).show();
                                gouwuQuXuan.setChecked(false);
                            }
                        });

                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                MyGouBean myGouBean = gson.fromJson(json, MyGouBean.class);
                                myGet=myGouBean;
                                //有商品就显示
                                if(myGouBean.getCode().equals("0")){
                                    bianji.setVisibility(View.VISIBLE);
                                    gouwukong.setVisibility(View.GONE);
                                    gouwuExlv.setVisibility(View.VISIBLE);
                                    final List<MyGouBean.DataBean> data = myGouBean.getData();
                                    gouwuExlv.setAdapter(new MyGouWuLvAdapter(getActivity(),data,gouwusum,gouwuQuXuan));
//                                gouwuExlv.expandGroup(0);
                                    for (int i=0;i<data.size();i++){
                                        gouwuExlv.expandGroup(i);
                                    }
                                    bianji.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new MyBianJiView()).commit();
                                        }
                                    });

                                    gouwuExlv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                        @Override
                                        public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                                                Log.i("jiansd","二级点击");

                                            return true;
                                        }
                                    });
                                    //判断二级列表中是否全部勾选上了，，，false=全选框赋值
                                    for (MyGouBean.DataBean d:data){
                                        for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                            if(l.getSelected()==0){
                                                gouwuquanxuan=false;
                                                break;
                                            }
                                        }
                                        if(gouwuquanxuan==false){
                                            break;
                                        }
                                    }

                                    gouwuQuXuan.setChecked(gouwuquanxuan);

                                }else{
                                    Toast.makeText(getActivity(),myGouBean.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onShiBai(String ss) {
                    Toast.makeText(getActivity(),ss,Toast.LENGTH_SHORT).show();
                }
            });


        }else{
            denglutiao.setVisibility(View.VISIBLE);
            huanyingtiao.setVisibility(View.GONE);
            denglu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DengLu.class);
                    getActivity().startActivity(intent);
                }
            });
        }

        //点击定位的时间
        gouwudingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"正在打开高德地图....",Toast.LENGTH_SHORT).show();
            }
        });

        //上拉加载更多推荐数据
        gouwuMyScrollView.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView context, int x, int y, int oldx, int oldy) {
                Toast.makeText(getActivity(),"正在加载更多数据....",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                page++;
                if(page<4){
                    gouwuMianTuiJian();
                    Toast.makeText(getActivity(),"加载完成！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"后面已经没有数据啦！",Toast.LENGTH_SHORT).show();
                }

            }
        });


        //条目点击事件。。。跳转到商品详情
        gouwuMyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dataBean = list.get(i);
                Intent intent = new Intent(getActivity(), ShangpingXiangQing.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean",dataBean);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        //购物车的全选
        gouwuQuXuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kai){
                    if(getShangping.equals("null")){
                        Toast.makeText(getActivity(),"购物车为空！",Toast.LENGTH_SHORT).show();
                        gouwuQuXuan.setChecked(false);
                    }else{
                        List<MyGouBean.DataBean> data = myGet.getData();
                        if(gouwuQuXuan.isChecked()){
                            for (MyGouBean.DataBean d:data){
                                d.setFlag(gouwuQuXuan.isChecked());
                                for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                    l.setSelected(1);
                                }
                            }
                        }else{
                            for (MyGouBean.DataBean d:data){
                                d.setFlag(gouwuQuXuan.isChecked());
                                for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                    l.setSelected(0);
                                }
                            }
                        }

//                        Log.i("jibbasd","gouwuQuXuan==="+data.get(0).getFlag());//检测bean类数据是否已修改
                        Toast.makeText(getActivity(),"数据正在更新，请稍等....",Toast.LENGTH_SHORT).show();
                        // 进行更改购物车,,,借助容器将二级列表中的数据全部取出遍历，，再将数据传入接口进行修改数据源，，最后刷新适配器
                        String uid = sp.getString("uid", "");
                        ArrayList<MyGouBean.DataBean.ListBean> list = new ArrayList<>();
                        for (MyGouBean.DataBean d:data){
                            for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                list.add(l);
                            }
                        }
                        updataGouWu(list,uid,data);//修改数据源的方法
                    }
                }else{
                    Toast.makeText(getActivity(),"请先登录用户！",Toast.LENGTH_SHORT).show();
                    gouwuQuXuan.setChecked(false);
                }
            }
        });


    }
    //使用递归，根据下标获取listBean类中的数据，再传入接口，进行修改数据源，最后将二级列表展示，并刷新适配器
    int i=0;
    private void updataGouWu(final List<MyGouBean.DataBean.ListBean> data, final String uid, final List<MyGouBean.DataBean> list) {
        if(i<data.size()) {
            MyGouBean.DataBean.ListBean listBean = data.get(i);
            String s = APIuil.updataCart(uid, listBean);
            myPresenter.getContent(s, new MyJieKou() {
                @Override
                public void onChengGong(final String json) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            final MyaddCart myaddCart = gson.fromJson(json, MyaddCart.class);
                            if (myaddCart.getCode().equals("0")) {
                                Log.i("jibass",  "====updataGouWu===" +true);
                                i++;
                                updataGouWu(data, uid,list);
                            } else {
                                Toast.makeText(getActivity(), myaddCart.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                @Override
                public void onShiBai(final String ss) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), ss, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else{
            i=0;
            Toast.makeText(getActivity(), "购物车更新完毕！", Toast.LENGTH_SHORT).show();
            gouwuExlv.setAdapter(new MyGouWuLvAdapter(getActivity(),list,gouwusum,gouwuQuXuan));
            for (int i=0;i<list.size();i++){
                gouwuExlv.expandGroup(i);
            }
        }
    }

    //推荐视图的适配器，，与条目点击事件
    public void setGouAdapter(){
        if(myGouWuAdapter==null){
            myGouWuAdapter = new MyGouWuAdapter(getActivity(), list);
            gouwuMyGridView.setAdapter(myGouWuAdapter);
        }else{
            myGouWuAdapter.notifyDataSetChanged();
        }
    }

    //第一次，购物车界面底部的推荐视图数据
    private void gouwuTuiJian() {

        myPresenter = new MyPresenter();
        myPresenter.getContent(APIuil.gouwu(page), new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyGouWuBean myGouWuBean = gson.fromJson(json, MyGouWuBean.class);
                        if(myGouWuBean.getCode().equals("0")){
                             final List<MyGouWuBean.DataBean> data = myGouWuBean.getData();
                            list.clear();
                            list.addAll(data);
                            setGouAdapter();
                        }else{
                            Toast.makeText(getActivity(),myGouWuBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onShiBai(final String ss) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),ss,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    //第N次，购物车界面底部的推荐视图数据
    private void gouwuMianTuiJian() {
        myPresenter = new MyPresenter();
        myPresenter.getContent(APIuil.gouwu(page), new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyGouWuBean myGouWuBean = gson.fromJson(json, MyGouWuBean.class);
                        if(myGouWuBean.getCode().equals("0")){
                            final List<MyGouWuBean.DataBean> data = myGouWuBean.getData();
                            list.addAll(data);
                            setGouAdapter();
                        }else{
                            Toast.makeText(getActivity(),myGouWuBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onShiBai(final String ss) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),ss,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


}

package one.bw.com.jingdong.wode.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.denglu.DengLu;
import one.bw.com.jingdong.denglu.bean.MyUserBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.view.ShangpingXiangQing;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;
import one.bw.com.jingdong.sousuo.adapter.MySouxiangBiaoAdapter;
import one.bw.com.jingdong.sousuo.view.MyGridView;

/**
 * Created by Administrator on 2017/12/12/012.
 */

public class FraWode extends Fragment {

    ArrayList<MyGouWuBean.DataBean> listdata = new ArrayList<>();
    int page=1;
    private MyPresenter myPresenter;
    private SharedPreferences sp;
    private boolean kai;
    public RelativeLayout wodeRelativeLayout;
    public ImageView wodetouxiang;
    public TextView wodeusername;
    public TextView wodedengjian;
    public LinearLayout wodehuianniu;
    public ImageView wodexiaoxi;
    public ImageView wodeshezhi;
    public LinearLayout wodetuijian;
    private MyGridView wodeMyGridView;
    MySouxiangBiaoAdapter mySouxiangBiaoAdapter=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frawode, container, false);
        //头部登录布局
         wodeRelativeLayout = view.findViewById(R.id.wodeRelativeLayout);
        //头像
        wodetouxiang = view.findViewById(R.id.wodetouxiang);
        //用户名
        wodeusername = view.findViewById(R.id.wodeusername);
        //小箭头
        wodedengjian = view.findViewById(R.id.wodedengjian);
        //用户名下方的按钮
        wodehuianniu = view.findViewById(R.id.wodehuianniu);
        //消息按钮
        wodexiaoxi = view.findViewById(R.id.wodexiaoxi);
        //设置按钮
        wodeshezhi = view.findViewById(R.id.wodeshezhi);
        //推荐布局视图
        wodetuijian = view.findViewById(R.id.wodetuijian);
        //数据管理
        wodeMyGridView = view.findViewById(R.id.wodeMyGridView);
        wodeMyGridView.setFocusable(false);
        sp = getActivity().getSharedPreferences("wc", Context.MODE_PRIVATE);

        myPresenter = new MyPresenter();
        //点击消息图标的事件
        wodexiaoxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"跳到消息页面！",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        kai = sp.getBoolean("kai", false);
//        Log.i("jiabb","onResume===="+kai);
        /**
        *如果登录了
         * 1，就显示推荐布局的数据，，，否则隐藏,,,且点击头部登录布局会跳转到登录页面
        * 2，获取uid查看用户信息，得到用户名，头像图，
         */
        if(kai){
            getFirstContent();//首次默认展示数据
            wodetuijian.setVisibility(View.VISIBLE);//推荐视图的显示
            String uid = sp.getString("uid", "");
            String userName = sp.getString("userName", "");
            String icon = sp.getString("icon", "");
//            String ren = APIuil.getRen(uid);//进行拼接查看用户接口，并访问
//            getUser(ren);//请求个人信息接口，，为头像赋值

            wodeusername.setText("jd_"+userName);
            ImageLoader.getInstance().displayImage(icon,wodetouxiang, ImageLoaderUtil.showImagYuan());
            wodedengjian.setVisibility(View.GONE);//小箭头隐藏
            wodehuianniu.setVisibility(View.VISIBLE);//会员按钮显示
            //未推荐条目添加点击事件
            wodeMyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MyGouWuBean.DataBean dataBean = listdata.get(i);
                    Intent intent = new Intent(getActivity(), ShangpingXiangQing.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dataBean",dataBean);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });

        }else{
            wodetuijian.setVisibility(View.GONE);
            wodetuijian.setVisibility(View.GONE);//推荐视图的显示
            wodedengjian.setVisibility(View.VISIBLE);//小箭头隐藏
            wodehuianniu.setVisibility(View.INVISIBLE);//会员按钮显示
            wodeusername.setText("登录/注册");
            wodetouxiang.setImageResource(R.drawable.user);

            wodeRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), DengLu.class);
                    getActivity().startActivity(intent);
                }
            });
        }

        //点击“设置”按钮的事件，，先判断是否已经登录，false==跳到登录页面，，true==跳到设置页面
        wodeshezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kai){
                    Intent intent = new Intent(getActivity(), WoDeSheZhi.class);
                    getActivity().startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), DengLu.class);
                    getActivity().startActivity(intent);
                }

            }
        });

    }
    //请求个人信息接口，，为头像赋值
    private void getUser(String ren) {
        myPresenter.getContent(ren, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MyUserBean myUserBean = gson.fromJson(json, MyUserBean.class);
                        if(myUserBean.getCode().equals("0")){
                            if(myUserBean.getData().getIcon().equals("null")){
                                wodetouxiang.setImageResource(R.mipmap.ic_launcher);
                            }else{
                                ImageLoader.getInstance().displayImage(((String)myUserBean.getData().getIcon()),wodetouxiang, ImageLoaderUtil.showImagYuan());
                            }
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
    //第一次请求“为你推荐”数据
    private void getFirstContent() {
        page=1;
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
                            listdata.clear();//清除商品数据源
                            listdata.addAll(data);
                            setAdapter();
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
    //适配器的应用
    private void setAdapter() {
        if(mySouxiangBiaoAdapter==null){
            mySouxiangBiaoAdapter = new MySouxiangBiaoAdapter(getActivity(), listdata);
            wodeMyGridView.setAdapter(mySouxiangBiaoAdapter);
        }else{
            mySouxiangBiaoAdapter.notifyDataSetChanged();
        }
    }

    private void getMinContent() {
        page++;
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
                            listdata.addAll(data);

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

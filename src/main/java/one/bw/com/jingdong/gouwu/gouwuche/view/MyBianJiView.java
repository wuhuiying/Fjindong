package one.bw.com.jingdong.gouwu.gouwuche.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.gouwu.gouwuche.adapter.MyGouWuLvAdapter;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyaddCart;
import one.bw.com.jingdong.shouye.frame.FraGou;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;

/**
 * Created by Administrator on 2017/12/7/007.
 */

public class MyBianJiView extends Fragment {

    private ExpandableListView bianjiExlv;
    private TextView bianjiwancheng;
    private TextView bianjiuserName;
    private Button bianjishanchu;
    private SharedPreferences sp;
    private MyPresenter myPresenter;
    ArrayList<Integer> pidlist = new ArrayList<>();
    private String uid;
    private String car;
    private List<MyGouBean.DataBean> data;
    private LinearLayout bianjikong;
    public static CheckBox bianjiQuanXuan;
    private String getShuJu;
    private MyGouBean myGet;
    boolean bianjiquanxuan=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bianjiview, container, false);
        bianjiExlv = view.findViewById(R.id.bianjiExlv);
        bianjiwancheng = view.findViewById(R.id.bianjiwancheng);
        bianjiuserName = view.findViewById(R.id.bianjiuserName);
        bianjishanchu = view.findViewById(R.id.bianjishanchu);
        bianjikong = view.findViewById(R.id.bianjikong);
        bianjiQuanXuan = view.findViewById(R.id.bianjiQuanXuan);

        //首先获取，，登录值，，uid,,uname,,kai,,
        sp = getActivity().getSharedPreferences("wc", Context.MODE_PRIVATE);
        boolean kai = sp.getBoolean("kai", false);
        if(kai){
            uid = sp.getString("uid", "");
            String userName = sp.getString("userName", "");
            bianjiuserName.setText(userName);

            car = APIuil.getCar(uid);//查询购物车中的数据，，并显示
            myPresenter = new MyPresenter();
            gouwucheshuju();
        }
        //点击“完成”按钮，，返回购物车界面
        bianjiwancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FraGou()).commit();
            }
        });
        //点击“删除”按钮，，首先取出所有selected为1的pid,将其存入集合，，最后使用递归的思想进行删除，，因为是与接口交互，有访问的时间存在，不能用for循环
        bianjishanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pidlist.clear();
                for (int i=0;i<data.size();i++){
                    List<MyGouBean.DataBean.ListBean> list = data.get(i).getList();
                    for (int j=0;j<list.size();j++){
                        int selected = list.get(j).getSelected();
                        if(selected==1){
                            int pid = list.get(j).getPid();
                            pidlist.add(pid);
                        }
                    }
                }
                if(pidlist.size()>0){
                    Log.i("jiabs","=====bianjishanchu======"+pidlist.size());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("确定删除商品吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteShuju();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                }else{
                    Toast.makeText(getActivity(),"没有选中的内容！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //编辑中全选的点击事件，，首先判断车里是否有数据
        bianjiQuanXuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getShuJu.equals("null")){
                    Toast.makeText(getActivity(),"购物车中没有商品！",Toast.LENGTH_SHORT).show();
                    bianjiQuanXuan.setChecked(false);
                }else{
                    List<MyGouBean.DataBean> data = myGet.getData();
                    if(bianjiQuanXuan.isChecked()){
                        for (MyGouBean.DataBean d:data){
                            d.setFlag(bianjiQuanXuan.isChecked());
                            for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                l.setSelected(1);
                            }
                        }
                    }else{
                        for (MyGouBean.DataBean d:data){
                            d.setFlag(bianjiQuanXuan.isChecked());
                            for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                l.setSelected(0);
                            }
                        }
                    }
                    Toast.makeText(getActivity(),"数据正在更新，请稍等....",Toast.LENGTH_SHORT).show();
                    // 进行更改购物车,,,借助容器将二级列表中的数据全部取出遍历，，再将数据传入接口进行修改数据源，，最后刷新适配器
                    ArrayList<MyGouBean.DataBean.ListBean> list = new ArrayList<>();
                    for (MyGouBean.DataBean d:data){
                        for (MyGouBean.DataBean.ListBean l:d.getList()) {
                            list.add(l);
                        }
                    }
                    updataGouWu(list,uid,data);//修改数据源的方法


                }
            }
        });
        return view;
    }

    //使用递归，根据下标获取listBean类中的数据，再传入接口，进行修改数据源，最后将二级列表展示，并刷新适配器
    int z=0;
    private void updataGouWu(final ArrayList<MyGouBean.DataBean.ListBean> list, final String uid, final List<MyGouBean.DataBean> data) {
        if(z<list.size()){
            MyGouBean.DataBean.ListBean listBean = list.get(z);
            String s = APIuil.updataCart(uid, listBean);
            myPresenter.getContent(s, new MyJieKou() {
                @Override
                public void onChengGong(final String json) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            final MyaddCart myaddCart = gson.fromJson(json, MyaddCart.class);
                            if(myaddCart.getCode().equals("0")){
                                z++;
                                updataGouWu(list,uid,data);
                            }else{
                                Toast.makeText(getActivity(),myaddCart.getMsg(),Toast.LENGTH_SHORT).show();
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
        }else{
            z=0;
            Toast.makeText(getActivity(),"购物车更新完成！",Toast.LENGTH_SHORT).show();
            bianjiExlv.setAdapter(new MyGouWuLvAdapter(getActivity(), data,FraGou.gouwusum,bianjiQuanXuan));
            //二级列表的全部展开
            for (int i = 0; i< data.size(); i++){
                bianjiExlv.expandGroup(i);
            }
        }
    }

    //购物车中如果为空，就隐藏二级列表，显示空图片，，，有数据就展示
    private void gouwucheshuju() {
        myPresenter.getContent(car, new MyJieKou() {
            @Override
            public void onChengGong(final String json) {
                getShuJu = json;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(json.equals("null")){
                            bianjiExlv.setVisibility(View.INVISIBLE);
                            bianjikong.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(),"购物车已经清空啦！",Toast.LENGTH_SHORT).show();
                            bianjiQuanXuan.setChecked(false);
                        }else{
                            Gson gson = new Gson();
                            final MyGouBean myGouBean = gson.fromJson(json, MyGouBean.class);
                            myGet = myGouBean;
                            //有商品就显示
                            if(myGouBean.getCode().equals("0")){

                                data = myGouBean.getData();
                                bianjiExlv.setAdapter(new MyGouWuLvAdapter(getActivity(), data,FraGou.gouwusum,bianjiQuanXuan));
//                                gouwuExlv.expandGroup(0);
                                for (int i = 0; i< data.size(); i++){
                                    bianjiExlv.expandGroup(i);
                                }
                                for (MyGouBean.DataBean d:data){
                                    for (MyGouBean.DataBean.ListBean l:d.getList()) {
                                        if(l.getSelected()==0){
                                            bianjiquanxuan=false;
                                            break;
                                        }
                                    }
                                    if(bianjiquanxuan==false){
                                        break;
                                    }
                                }
                                bianjiQuanXuan.setChecked(bianjiquanxuan);

                            }else{
                                Toast.makeText(getActivity(),myGouBean.getMsg(),Toast.LENGTH_SHORT).show();
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

    //删除购物车的递归
    private void deleteShuju() {
        String s = APIuil.deleteCar(uid, pidlist.get(0) + "");
            myPresenter.getContent(s, new MyJieKou() {
                @Override
                public void onChengGong(final String json) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.i("jiabab","====="+json);
                            Gson gson = new Gson();
                            MyaddCart myaddCart = gson.fromJson(json, MyaddCart.class);
                            if(myaddCart.getCode().equals("0")){
                                Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT).show();
                                pidlist.remove(0);
                                if(pidlist.size()>0){
                                    Log.i("jibadfg","======"+pidlist.size());
                                    deleteShuju();
                                }else{
                                    gouwucheshuju();
                                }

                            }else{
                                Toast.makeText(getActivity(),"删除失败！",Toast.LENGTH_SHORT).show();
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

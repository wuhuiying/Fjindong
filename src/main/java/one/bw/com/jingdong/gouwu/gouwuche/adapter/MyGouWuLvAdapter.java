package one.bw.com.jingdong.gouwu.gouwuche.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.apiUIL.APIuil;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyaddCart;
import one.bw.com.jingdong.gouwu.gouwuche.view.ShangpingXiangQing;
import one.bw.com.jingdong.shouye.inters.MyJieKou;
import one.bw.com.jingdong.shouye.presenter.MyPresenter;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;


/**
 * Created by Administrator on 2017/12/6/006.
 */

public class MyGouWuLvAdapter extends BaseExpandableListAdapter {
    Context context;
    List<MyGouBean.DataBean> list;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private final String uid;
    private final MyPresenter myPresenter;
    TextView gouwusum;
    double sum=0.0;
    CheckBox QuanXuan;

    public MyGouWuLvAdapter(Context context, List<MyGouBean.DataBean> list,TextView gouwusum,CheckBox QuanXuan) {
        this.context = context;
        this.list = list;
        this.gouwusum=gouwusum;
        this.QuanXuan=QuanXuan;
        sp = context.getSharedPreferences("wc", Context.MODE_PRIVATE);
        edit = sp.edit();
        uid = sp.getString("uid", "");
        myPresenter = new MyPresenter();
        for (MyGouBean.DataBean d:list) {
            for (MyGouBean.DataBean.ListBean l:d.getList()) {
                if(l.getSelected()==1){
                    sum=sum+l.getNum()*l.getPrice();
                }
            }
        }
        gouwusum.setText("合计：￥"+sum);

    }




    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        if(view==null){
            view = View.inflate(context, R.layout.gouwucheyiji, null);
        }

       final CheckBox gouwuyijikuang = view.findViewById(R.id.gouwuyijikuang);
        TextView gouwuyijititle = view.findViewById(R.id.gouwuyijititle);
        //为一级列表的title赋值
        gouwuyijititle.setText(list.get(i).getSellerName());
        //为一级的复选框赋值，，，根据数据源中的selected值进行赋值，，，0==false,,1==true
        gouwuyijikuang.setChecked(panChlidType(i));
        //一级复选框的点击事件，，一级下的所有二级列表全部赋值，，父布局的全选框也要赋值==false
        gouwuyijikuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,""+ gouwuyijikuang.isChecked(),Toast.LENGTH_SHORT).show();
                //根据一级复选框的值来更改所有二级列表的复选框
                chengeChlidType(i, gouwuyijikuang.isChecked());
                //同时记录下一级复选框的值，以便于刷新适配器时的联动
                list.get(i).setFlag(gouwuyijikuang.isChecked());
                //为全选框复制，，判断所有商品是否都已选中，，
                QuanXuan.setChecked(false);
//                MyBianJiView.bianjiQuanXuan.setChecked(false);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    //判断当前条目下的所有之条目是否都是...1
    //指定条目下的所有子条目
    private boolean panChlidType(int i) {
        boolean flag=true;
        List<MyGouBean.DataBean.ListBean> list = MyGouWuLvAdapter.this.list.get(i).getList();
        for (MyGouBean.DataBean.ListBean d:list) {
            if(d.getSelected()==0){
                flag=false;
            }
        }
        return flag;
    }



    //指定条目下的所有子条目
    private void chengeChlidType(int i, final boolean checked) {
        List<MyGouBean.DataBean.ListBean> list = MyGouWuLvAdapter.this.list.get(i).getList();
        for (MyGouBean.DataBean.ListBean d:list) {
            if(checked){
                d.setSelected(1);
                sum=sum+d.getPrice()*d.getNum();
                gouwusum.setText("合计：￥"+sum);
                String s = APIuil.updataCart(uid, d);
                xiugaijiekou(checked, s);

            }else{
                d.setSelected(0);
                sum=sum-d.getPrice()*d.getNum();
                gouwusum.setText("合计：￥"+sum);
                String s = APIuil.updataCart(uid, d);
                xiugaijiekou(checked, s);
            }

        }
    }

    private void xiugaijiekou(final boolean checked, String s) {
        myPresenter.getContent(s, new MyJieKou() {
            @Override
            public void onChengGong(String json) {

                Gson gson = new Gson();
                MyaddCart myaddCart = gson.fromJson(json, MyaddCart.class);
                if(myaddCart.getCode().equals("0")){
                    Log.i("jibass",myaddCart.getCode()+"===="+myaddCart.getMsg()+"==="+checked);
                }else{
                    Log.i("jibass",myaddCart.getCode()+"====="+myaddCart.getMsg());
                }
            }
            @Override
            public void onShiBai(String ss) {
                Toast.makeText(context,ss,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view==null){
            view=View.inflate(context,R.layout.gouwuchebuju,null);
        }
        LinearLayout gouwucheerjiid = view.findViewById(R.id.gouwucheerjiid);
        final CheckBox gouwucheerjikuang = view.findViewById(R.id.gouwucheerjikuang);
        ImageView img = view.findViewById(R.id.gouwuimg);
        TextView title = view.findViewById(R.id.gouwutitle);
        TextView price = view.findViewById(R.id.gouwuprice);
        final TextView gouwunum = view.findViewById(R.id.gouwunum);
        Button numjian = view.findViewById(R.id.numjian);
        Button numjia = view.findViewById(R.id.numjia);

        //获取selected
        final MyGouBean.DataBean.ListBean listBean = list.get(i).getList().get(i1);
        int selected = listBean.getSelected();
        if(selected==0){
            gouwucheerjikuang.setChecked(false);
        }else{
            gouwucheerjikuang.setChecked(true);
        }

        ImageLoader.getInstance().displayImage(listBean.getImages().split("\\|")[0],img,ImageLoaderUtil.showImag());
        title.setText(listBean.getTitle());
        price.setText("￥"+listBean.getPrice());
        gouwunum.setText(""+listBean.getNum());
        //二级复选框的点击事件
        gouwucheerjikuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //为当前下标的一级框赋值
                list.get(i).setFlag(panChlidType(i));
                //判断，，修改数据，，，修改总价===false,总价减去点击条目的总价
                if(gouwucheerjikuang.isChecked()){
                    listBean.setSelected(1);
                    String s = APIuil.updataCart(uid, listBean);
                    xiugaijiekou(gouwucheerjikuang.isChecked(),s);//修改数据源
                    sum=sum+listBean.getNum()*listBean.getPrice();//修改总价
                    gouwusum.setText("合计：￥"+sum);
                }else{
                    listBean.setSelected(0);
                    String s = APIuil.updataCart(uid, listBean);
                    xiugaijiekou(gouwucheerjikuang.isChecked(),s);//修改数据源
                    sum=sum-listBean.getNum()*listBean.getPrice();//修改总价
                    gouwusum.setText("合计：￥"+sum);
                }
                //为全选框复制，，判断所有商品是否都已选中，，
                QuanXuan.setChecked(false);
//                MyBianJiView.bianjiQuanXuan.setChecked(false);

                notifyDataSetChanged();
            }
        });

        numjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                list.get(i).setFlag(panChlidType(i));//更改一级框
//                listBean.setSelected(1);//更改二级框
                int num = listBean.getNum();
                int n=num;
                num++;
                listBean.setNum(num);
                String s = APIuil.updataCart(uid, listBean);
                xiugaijiekou(true,s);//修改数据源
                if(listBean.getSelected()==1){
                    sum=sum-listBean.getPrice()*n+listBean.getPrice()*num;//修改总价，，避免重复加价，，先减去原有的num，在加上现有的num
                    gouwusum.setText("合计：￥"+sum);
                }
                notifyDataSetChanged();
            }
        });

        numjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                list.get(i).setFlag(panChlidType(i));//更改一级框
//                listBean.setSelected(1);//更改二级框
                int num = listBean.getNum();
                int n=num;
                num--;
                if(num<0){
                    Toast.makeText(context,"再减，就亏本了！",Toast.LENGTH_SHORT).show();
                }else{
                    listBean.setNum(num);
                    String s = APIuil.updataCart(uid, listBean);
                    xiugaijiekou(true,s);//修改数据源
                    if(listBean.getSelected()==1){
                        sum=sum-listBean.getPrice()*n+listBean.getPrice()*num;//修改总价，，避免重复加价，，先减去原有的num，在加上现有的num
                         gouwusum.setText("合计：￥"+sum);
                    }

                    notifyDataSetChanged();
                }
            }
        });
        //二级列表的点击事件
        gouwucheerjiid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("jiab","gouwucheerjiid====="+2);
                MyGouBean.DataBean.ListBean li = list.get(i).getList().get(i1);
                MyGouWuBean.DataBean d=new MyGouWuBean.DataBean();
                d.setDetailUrl(li.getDetailUrl());
                d.setCreatetime(li.getCreatetime());
                d.setBargainPrice(li.getBargainPrice());
                d.setImages(li.getImages());
                d.setItemtype(1);
                d.setPid(li.getPid());
                d.setPrice(li.getPrice());
                d.setPscid(li.getPscid());
                d.setSellerid(li.getSellerid());
                d.setSalenum(li.getNum());
                d.setSubhead(li.getSubhead());
                d.setTitle(li.getTitle());
                Intent intent = new Intent(context, ShangpingXiangQing.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean",d);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

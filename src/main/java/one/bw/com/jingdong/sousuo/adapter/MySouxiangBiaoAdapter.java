package one.bw.com.jingdong.sousuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2017/12/13/013.
 */

public class MySouxiangBiaoAdapter extends BaseAdapter{
    Context context;
    ArrayList<MyGouWuBean.DataBean> list;

    public MySouxiangBiaoAdapter(Context context, ArrayList<MyGouWuBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
           view=View.inflate(context, R.layout.souxiangbiaogebuju,null);
        }
        ImageView souxiangBiaoimg = view.findViewById(R.id.souxiangBiaoimg);
        TextView souxiangBiaoTitle = view.findViewById(R.id.souxiangBiaoTitle);
        TextView souxiangBiaoSelNum = view.findViewById(R.id.souxiangBiaoSelNum);
        TextView souxiangBiaoprice = view.findViewById(R.id.souxiangBiaoprice);
        String[] split = list.get(i).getImages().split("\\|");
        ImageLoader.getInstance().displayImage(split[0],souxiangBiaoimg, ImageLoaderUtil.showImag());
        souxiangBiaoTitle.setText(list.get(i).getTitle().substring(0,20)+"....");
        souxiangBiaoSelNum.setText("销量:"+list.get(i).getSalenum());
        souxiangBiaoprice.setText("￥"+list.get(i).getPrice());
        return view;
    }
}

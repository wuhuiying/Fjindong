package one.bw.com.jingdong.gouwu.gouwuche.adapter;

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
 * Created by Administrator on 2017/12/6/006.
 */

public class MyGouWuAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyGouWuBean.DataBean> list;

    public MyGouWuAdapter(Context context, ArrayList<MyGouWuBean.DataBean> list) {
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
            view = View.inflate(context,R.layout.gouwuchetuijianbuju, null);
        }
        ImageView gouwutuijianimg = view.findViewById(R.id.gouwutuijianimg);
        TextView  goueutuijiantitle = view.findViewById(R.id.goueutuijiantitle);
        TextView gouwutuijianprice = view.findViewById(R.id.gouwutuijianprice);
        String[] split = list.get(i).getImages().split("\\|");
        ImageLoader.getInstance().displayImage(split[0],gouwutuijianimg, ImageLoaderUtil.showImag());
        goueutuijiantitle.setText(list.get(i).getTitle());
        gouwutuijianprice.setText("￥"+list.get(i).getPrice());
        return view;
    }
}


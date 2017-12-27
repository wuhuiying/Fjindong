package one.bw.com.jingdong.sousuo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import one.bw.com.jingdong.R;
import one.bw.com.jingdong.gouwu.gouwuche.bean.MyGouWuBean;
import one.bw.com.jingdong.shouye.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2017/12/12/012.
 */

public class MySouxiangLieAdapter extends BaseAdapter {
    Context context;
    List<MyGouWuBean.DataBean> list;

    public MySouxiangLieAdapter(Context context, List<MyGouWuBean.DataBean> list) {
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
            view = View.inflate(context, R.layout.souxiangliebuju, null);
        }
        ImageView souxianglieimg = view.findViewById(R.id.souxianglieimg);
        TextView souxianglietitle = view.findViewById(R.id.souxianglietitle);
        TextView souxianglieSelNum = view.findViewById(R.id.souxianglieSelNum);
        TextView souxianglieprice = view.findViewById(R.id.souxianglieprice);
        String[] split = list.get(i).getImages().split("\\|");
        ImageLoader.getInstance().displayImage(split[0],souxianglieimg, ImageLoaderUtil.showImag());
        souxianglietitle.setText(list.get(i).getTitle().substring(0,20)+"....");
        souxianglieSelNum.setText("销量:"+list.get(i).getSalenum());
        souxianglieprice.setText("￥"+list.get(i).getPrice());
        return view;
    }
}
